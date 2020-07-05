package com.eddy.rpc.configuration;

import com.alibaba.fastjson.JSONObject;
import com.eddy.rpc.client.task.HiveSqlRequestTask;
import com.eddy.rpc.client.task.HiveSqlResponseTask;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class HiveSqlResponseTaskDecoder extends MessageToMessageDecoder<ByteBuf> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) {
    // 读到客户端的数据 json格式
    byte[] bytes = new byte[buf.readableBytes()];
    buf.readBytes(bytes);

    String taskString = new String(bytes, CharsetUtil.UTF_8);

    // 转成RPC对象
    HiveSqlResponseTask hiveSqlTask = JSONObject.parseObject(taskString, HiveSqlResponseTask.class);

    // 写入到下一个任务
    out.add(hiveSqlTask);
  }
}