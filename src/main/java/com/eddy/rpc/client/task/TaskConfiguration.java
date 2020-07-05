package com.eddy.rpc.client.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.client.task.TaskQueue
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 6:34 PM
 * ***********************************************
 */
public class TaskConfiguration {

    // 客户端处理用户提交任务的缓冲队列
    public static final LinkedBlockingQueue clientSendBufferQueue = new LinkedBlockingQueue(100000);

    // 服务端处理任务队列
    public static final LinkedBlockingQueue taskWaitingQueue = new LinkedBlockingQueue(1000);

    // sentCounter和canceledCounter 只是用于计数
    public static final AtomicInteger sentCounter = new AtomicInteger(0);
    public static final AtomicInteger canceledCounter = new AtomicInteger(0);

    /**
     * 用户保存任务放在了哪个服务的队列里面，作用是在提交取消类任务时， 可以知道去哪个队列里找原始任务
     * 实际可以用redis存储这个信息
     */
    public static final Map<String, Integer> taskQueueMap = new ConcurrentHashMap<>(10000);
}
