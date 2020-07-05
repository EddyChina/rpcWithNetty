package com.eddy.rpc.server.handler;

import com.eddy.rpc.client.task.HiveSqlRequestTask;
import com.eddy.rpc.client.task.HiveSqlResponseTask;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.server.handler.HiveSqlRunnable
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 10:02 PM
 * ***********************************************
 */
public class HiveSqlExecutor implements Callable<HiveSqlResponseTask> {
    private HiveSqlRequestTask task;

    public HiveSqlExecutor(HiveSqlRequestTask task) {
        this.task = task;
    }

    @Override
    public HiveSqlResponseTask call() {
        System.out.println("Received: priority = " + task.getPriority() + ", taskId = " + task.getTaskId());

        // 模拟任务耗时
        try {
            Thread.sleep(new Random().nextInt(500) + 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HiveSqlResponseTask response = new HiveSqlResponseTask();

        response.setRequestTask(task);
        response.setResponseData("I am data from Response");
        response.setResponseId(task.getTaskId());
        response.setResponseTime(System.currentTimeMillis());

        return response;
    }
}
