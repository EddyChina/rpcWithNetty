package client;

import com.eddy.rpc.client.NettyClient;
import com.eddy.rpc.client.constant.RpcConst;
import com.eddy.rpc.client.task.HiveSqlRequestTask;
import com.eddy.rpc.client.task.TaskConfiguration;
import com.eddy.rpc.configuration.cluster.ServerRegisterCenter;

import java.util.Random;
import java.util.concurrent.*;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  client.ClientTest
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 9:11 PM
 * ***********************************************
 */
public class ClientTest {

    public static void main(String[] args) throws Exception {
        // 利用多个端口模拟一个集群
        int startPort = RpcConst.Netty.SERVER_PORT;
        int clusterSize = 5;

        // 客户端连接之后 启动模拟任务
        CountDownLatch countDownLatch = new CountDownLatch(clusterSize);

        // 模拟客户端多线程连接RPC集群（实际可以使用ZK作为服务注册中心的实现方式）

        ThreadPoolExecutor connectionPool = new ThreadPoolExecutor(clusterSize, clusterSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(clusterSize));

        for (int i = startPort; i < startPort + clusterSize; i++) {
            final int port = i;
            connectionPool.execute(() -> {
                NettyClient client = new NettyClient();
                try {
                    client.connect(RpcConst.Netty.SERVER_HOST, port, countDownLatch);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }

        // 模拟用户提交任务
        TaskMocker taskMocker = new TaskMocker(countDownLatch);
        taskMocker.sendTask();

        connectionPool.shutdown();
    }

    private static class TaskMocker{
        private CountDownLatch countDownLatch;

        public TaskMocker(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public void sendTask() {
            // 等待连接集群完成
            try {
                System.out.println("############ waiting for connection finished ############");
                this.countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("############ All connections has been initialized ############");

            // 连接到服务端之后 mock用户的请求
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, TaskConfiguration.taskWaitingQueue);
            final int threadCount = 1000; // 用户数
            final int concurrency = 1; // 每个用户发起的查询请求

            final int priorityOfCancel = Integer.MIN_VALUE;

            // 模拟1000个并发
            for (int i = 0; i < threadCount * concurrency; i++) {
                final String taskId = String.valueOf(i);
                Random random = new Random();

                Thread request = new Thread(() -> {
                    HiveSqlRequestTask task = new HiveSqlRequestTask();

                    task.setTaskId(taskId);
                    task.setCommitter("范相池");
                    task.setHiveSql("select name from user where id=" + taskId);
                    task.setTaskTime(System.currentTimeMillis());

                    int priority = random.nextInt(5);

                    task.setPriority(priority); // 随机生成一个优先级

                    // 随机选择一个队列
                    int serverNode = ServerRegisterCenter.getAvailableServerNode();
                    PriorityBlockingQueue<HiveSqlRequestTask> taskInputQueue = ServerRegisterCenter.getServerNode(serverNode).getTaskInputQueue();

                    taskInputQueue.offer(task);

                    TaskConfiguration.taskQueueMap.put(taskId, serverNode);

                    // 模拟针对优先级比较小的数据 以50%几率去取消这个任务 取消类任务的优先级最高
                    if (random.nextInt(2) == 1) {
                        HiveSqlRequestTask cancel = new HiveSqlRequestTask();
                        cancel.setTaskId(task.getTaskId());
                        cancel.setPriority(priorityOfCancel);
                        cancel.setCancel(true);

                        // 取出原始任务所在服务节点 将取消类任务放入同一个队列 便于删除原始任务
                        Integer originNode = TaskConfiguration.taskQueueMap.get(cancel.getTaskId());

                        // 放入原始队列
                        ServerRegisterCenter.getServerNode(originNode).getTaskInputQueue().offer(cancel);
                    }
                });

                threadPoolExecutor.execute(request);
            }

            threadPoolExecutor.shutdown();

        }
    }

}
