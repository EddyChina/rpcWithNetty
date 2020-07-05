package com.eddy.rpc.client.handler;

import com.eddy.rpc.client.task.HiveSqlRequestTask;
import com.eddy.rpc.client.task.HiveSqlResponseTask;
import com.eddy.rpc.client.task.TaskConfiguration;
import com.eddy.rpc.client.task.ThreadPoolConfiguration;
import com.eddy.rpc.configuration.cluster.ClusterNode;
import com.eddy.rpc.configuration.cluster.ServerRegisterCenter;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.PriorityBlockingQueue;

public class HiveSqlTaskRequestHandler extends ChannelHandlerAdapter {
    /**
     * 和指定服务端口绑定的handler
     */
    private int serverNode;

    public HiveSqlTaskRequestHandler(int serverNode) {
        this.serverNode = serverNode;
    }

    /**
     * 本方法用于接收服务端发送过来的消息
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        HiveSqlResponseTask responseTask = (HiveSqlResponseTask) msg;
        System.out.println("Server responses: " + responseTask + ", costs: " + (System.currentTimeMillis() - responseTask.getRequestTask().getTaskTime()));
    }
 
    /**
     * 本方法用于处理异常
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
 
 
    /**
     * 本方法用于向服务端发送信息
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ThreadPoolConfiguration.clientTaskReceiverExecutor.execute(() -> {
            System.out.println(String.format("<<<<<<<<<<任务接收队列[%s]启动>>>>>>>>>", serverNode));

            // 模拟队列拥堵 便于测试 任务取消情况
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 这个是为了测试
//            new Thread(() -> {
//                try {
//                    Thread.sleep(10 * 1000);
//
//                    System.out.println("sent count is " + TaskConfiguration.sentCounter.get());
//                    System.out.println("cancel count is " + TaskConfiguration.canceledCounter.get());
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();

            // 随机选择一个服务
            ClusterNode availableServerNode = ServerRegisterCenter.getServerNode(serverNode);

            PriorityBlockingQueue<HiveSqlRequestTask> taskInputQueue = availableServerNode.getTaskInputQueue();
            System.out.println("size of waiting to be sent out is " + taskInputQueue.size());

            while (true) {
                try {
                    // 取出任务 json序列化2
                    HiveSqlRequestTask hiveSqlTask = taskInputQueue.take();

                    // 取消类任务优先先出队列 如果此时 原始任务还在队列里 就去删除（重写了equals和hashcode）
                    if (hiveSqlTask.isCancel()) {
                        // 如果taskId一样就可以定位到原始任务

                        HiveSqlRequestTask cancelTask = new HiveSqlRequestTask();

                        cancelTask.setTaskId(hiveSqlTask.getTaskId());
                        cancelTask.setCancel(false);

                        boolean remove = taskInputQueue.remove(cancelTask);
//                        System.err.println("delete task result is " + remove + ", task id is " + cancelTask.getTaskId());

                        TaskConfiguration.canceledCounter.incrementAndGet();

                        continue;
                    }

                    ThreadPoolConfiguration.clientTaskSendExecutor.submit(new HiveSqlRequestSendExecutor(hiveSqlTask, ctx));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}