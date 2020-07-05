package com.eddy.rpc.configuration;

import com.alibaba.fastjson.JSONObject;
import com.eddy.rpc.client.task.HiveSqlRequestTask;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HiveSqlRequestTaskEncoder extends MessageToByteEncoder<HiveSqlRequestTask> {

  @Override
  protected void encode(ChannelHandlerContext ctx, HiveSqlRequestTask hiveSqlTask, ByteBuf buf) {
    String json = JSONObject.toJSONString(hiveSqlTask);
    ctx.writeAndFlush(Unpooled.wrappedBuffer(json.getBytes()));
  }
}