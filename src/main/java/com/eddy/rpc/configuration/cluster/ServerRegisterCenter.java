package com.eddy.rpc.configuration.cluster;

import com.eddy.rpc.client.constant.RpcConst;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.configuration.cluster.ConnectionPool
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/5/20 7:55 PM
 * ***********************************************
 */
public class ServerRegisterCenter {

    private static Map<Integer, ClusterNode> serverNodeMap = new ConcurrentHashMap<>(RpcConst.CLUSTER_SIZE * 2 + 1);

    // 模拟注册中心初始化（实际应该是服务启动时向注册中心发起注册请求）
    static {
        for (int i = RpcConst.Netty.SERVER_PORT; i < RpcConst.Netty.SERVER_PORT + RpcConst.CLUSTER_SIZE; i++) {
            serverNodeMap.put(i, new ClusterNode());
        }
    }

    public static int getAvailableServerNode() {
        int randomServerKey = new Random().nextInt(RpcConst.CLUSTER_SIZE) + RpcConst.Netty.SERVER_PORT;
        return randomServerKey;
    }

    public static ClusterNode getServerNode(int port) {
        return serverNodeMap.get(port);
    }
}
