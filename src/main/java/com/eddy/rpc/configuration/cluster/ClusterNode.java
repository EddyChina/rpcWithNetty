package com.eddy.rpc.configuration.cluster;

import com.eddy.rpc.client.task.HiveSqlRequestTask;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.configuration.cluster.ClusterNode
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/5/20 12:41 PM
 * ***********************************************
 */
public class ClusterNode {
    // 客户端接收用户提交的查询任务队列, 优先级队列
    private PriorityBlockingQueue<HiveSqlRequestTask> taskInputQueue = new PriorityBlockingQueue<>(1000000, Comparator.comparingInt(HiveSqlRequestTask::getPriority));

    public PriorityBlockingQueue<HiveSqlRequestTask> getTaskInputQueue() {
        return taskInputQueue;
    }
}
