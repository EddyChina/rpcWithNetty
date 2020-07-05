package server;

import com.eddy.rpc.server.RpcServer;
import com.eddy.rpc.server.impl.NettyRpcServer;

/**
 * ******************  Instructions  *********************
 *
 * @author :  Xiangchi Fan
 * @version :  1.0
 * @class :  server.ServerTest
 * @email : xiangchi.fan@hotmail.com
 * @since :  7/4/20 6:03 PM
 * ***********************************************
 */
public class ServerTest {

    public static void main(String[] args) {
        RpcServer rpcServer = new NettyRpcServer();

        rpcServer.start();
    }
}
