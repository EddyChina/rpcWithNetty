package com.eddy.rpc.client.constant;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  com.eddy.rpc.client.constant.RpcConst
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 6:00 PM
 * ***********************************************
 */
public class RpcConst {

    public static final class Netty {

        public static final String SERVER_HOST = "127.0.0.1";
        public static final int SERVER_PORT = 10099;
        public static final int BOSS_EVENT_LOOPS = 2;
        public static final int WORKER_EVENT_LOOPS = 4;
    }

    public static final int CLUSTER_SIZE = 5;

}
