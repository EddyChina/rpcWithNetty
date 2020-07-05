package com.eddy.rpc.server.impl;

import com.eddy.rpc.client.constant.RpcConst;
import com.eddy.rpc.configuration.HiveSqlRequestTaskDecoder;
import com.eddy.rpc.configuration.HiveSqlRequestTaskEncoder;
import com.eddy.rpc.server.RpcServer;
import com.eddy.rpc.server.handler.HiveSqlTaskDispatcherHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.NettpRpcServer
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 5:32 PM
 * ***********************************************
 */
public class NettyRpcServer implements RpcServer {

    @Override
    public void start() {
        EventLoopGroup acceptorGroup = new NioEventLoopGroup(RpcConst.Netty.BOSS_EVENT_LOOPS);
        EventLoopGroup workerGroup = new NioEventLoopGroup(RpcConst.Netty.WORKER_EVENT_LOOPS);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(acceptorGroup, workerGroup);

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.option(ChannelOption.TCP_NODELAY, true);

            serverBootstrap.channel(NioServerSocketChannel.class);

//            serverBootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    // 注册handler
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(102400, 0, 4, 0, 4));
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new HiveSqlRequestTaskDecoder());
                    ch.pipeline().addLast(new HiveSqlTaskDispatcherHandler());
                    ch.pipeline().addLast(new HiveSqlRequestTaskEncoder());
                }
            });

            List<ChannelFuture> channelFutureList = new ArrayList<>();

            for (int port = RpcConst.Netty.SERVER_PORT; port < RpcConst.Netty.SERVER_PORT + RpcConst.CLUSTER_SIZE; port++) {
                // 绑定端口，开始接收进来的连接
                ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
                final int currentPort = port;
                channelFuture.addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println(String.format("********** Netty Server has successfully started on port %s **********", currentPort));
                    }
                });

                channelFutureList.add(channelFuture);
            }

            channelFutureList.get(0).channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acceptorGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
