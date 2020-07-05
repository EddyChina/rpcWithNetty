package com.eddy.rpc.configuration;

import com.alibaba.fastjson.JSONObject;
import com.eddy.rpc.client.task.HiveSqlRequestTask;
import com.eddy.rpc.client.task.HiveSqlResponseTask;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HiveSqlResponseTaskEncoder extends MessageToByteEncoder<HiveSqlResponseTask> {

  @Override
  protected void encode(ChannelHandlerContext ctx, HiveSqlResponseTask hiveSqlTask, ByteBuf buf) {
    String json = JSONObject.toJSONString(hiveSqlTask);
    ctx.writeAndFlush(Unpooled.wrappedBuffer(json.getBytes()));
  }
}