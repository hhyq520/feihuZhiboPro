package cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.manager;


import java.util.HashSet;
import java.util.Set;

import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.XTcpClient;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TargetInfo;

/**
 * tcpclient的管理者
 */
public class TcpClientManager {
    private static Set<XTcpClient> sMXTcpClients = new HashSet<>();

    public static void putTcpClient(XTcpClient XTcpClient) {
        sMXTcpClients.add(XTcpClient);
    }

    public static XTcpClient getTcpClient(TargetInfo targetInfo) {
        for (XTcpClient tc : sMXTcpClients) {
            if (tc.getTargetInfo().equals(targetInfo)) {
                return tc;
            }
        }
        return null;
    }
}
