package com.eddy.rpc.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.eddy.rpc.client.task.HiveSqlRequestTask;
import com.eddy.rpc.client.task.TaskConfiguration;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.client.handler.HiveSqlRequestRunnable
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 10:22 PM
 * ***********************************************
 */
public class HiveSqlRequestSendExecutor implements Runnable {

    private HiveSqlRequestTask task;

    private ChannelHandlerContext ctx;

    public HiveSqlRequestSendExecutor(HiveSqlRequestTask task, ChannelHandlerContext ctx) {
        this.task = task;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        String msgToSend = JSONObject.toJSONString(task);

        ByteBuf encoded = ctx.alloc().buffer(2 * msgToSend.length());

        encoded.writeBytes(msgToSend.getBytes());
        ctx.write(encoded);

        ctx.flush();

        TaskConfiguration.sentCounter.incrementAndGet();
    }
}
