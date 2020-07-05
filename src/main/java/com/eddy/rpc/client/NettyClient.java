package com.eddy.rpc.client;

import com.eddy.rpc.client.handler.HiveSqlTaskRequestHandler;
import com.eddy.rpc.configuration.HiveSqlResponseTaskDecoder;
import com.eddy.rpc.configuration.HiveSqlResponseTaskEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.concurrent.CountDownLatch;

public class NettyClient {
    /**
     * 连接服务器
     * @param host 服务ip
     * @param port 服务端口
     * @param countDownLatch 当全部服务连接之后，模拟用户发起请求
     * @throws Exception
     */
    public void connect(String host, int port, CountDownLatch countDownLatch) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            // 设置客户端连接后的事件处理器
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(102400, 0, 4, 0, 4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new HiveSqlResponseTaskDecoder());
                    ch.pipeline().addLast(new HiveSqlTaskRequestHandler(port)); // 这个handler和端口绑定
                    ch.pipeline().addLast(new HiveSqlResponseTaskEncoder());
                }
            });
            // 开启客户端监听
            ChannelFuture f = bootstrap.connect(host, port).sync();

            System.out.println(String.format("*** client has connected to rpc server[%s].", port));

            // 连接到服务器之后 计数-1， 计数完成之后启动模拟任务
            countDownLatch.countDown();

            // 等待数据直到客户端关闭
            f.channel().closeFuture().sync();

            System.out.println("*** client has disconnected from rpc server.");
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
