package com.eddy.rpc.client.task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.client.task.ThreadPoolConfig
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 6:39 PM
 * ***********************************************
 */
public class ThreadPoolConfiguration {
    // 客户端接收用户提交的任务承载线程池 每个线程对应一个服务节点， 此线程处理一个优先级队列
    public static final ThreadPoolExecutor clientTaskReceiverExecutor = new ThreadPoolExecutor(5, 5, 30, TimeUnit.MINUTES, new LinkedBlockingQueue(5));

    // 客户端发送用户提交的任务
    public static final ThreadPoolExecutor clientTaskSendExecutor = new ThreadPoolExecutor(5, 5, 30, TimeUnit.MINUTES, TaskConfiguration.clientSendBufferQueue);

    // 服务端处理客户端的请求任务 最高5个线程同时处理
    public static final ThreadPoolExecutor taskExecutor = new ThreadPoolExecutor(5, 5, 30, TimeUnit.MINUTES, TaskConfiguration.taskWaitingQueue);
}
