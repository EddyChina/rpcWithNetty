package com.eddy.rpc.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.eddy.rpc.client.task.HiveSqlRequestTask;
import com.eddy.rpc.client.task.HiveSqlResponseTask;
import com.eddy.rpc.client.task.ThreadPoolConfiguration;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutionException;

public class HiveSqlTaskDispatcherHandler extends ChannelHandlerAdapter {
    /**
     * 本方法用于读取客户端发送的信息
     * @param ctx hanlder的上下文数据
     * @param msg 接收到的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException, ExecutionException {
        HiveSqlRequestTask task = (HiveSqlRequestTask) msg;

        HiveSqlResponseTask responseTask = ThreadPoolConfiguration.taskExecutor.submit(new HiveSqlExecutor(task)).get();

        String jsonResponse = JSONObject.toJSONString(responseTask);
        // 在当前场景下，发送的数据必须转换成ByteBuf数组
        ByteBuf encoded = ctx.alloc().buffer(2 * jsonResponse.length());
        encoded.writeBytes(jsonResponse.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }

    /**
     * 本方法用作处理异常
     * @param ctx hanlder的上下文数据
     * @param cause 具体失败的异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
 
    /**
     * 信息获取完毕后操作
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
 
}
