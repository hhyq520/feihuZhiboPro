package cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.helper.decode;


import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.TcpConnConfig;
import cn.feihutv.zhibofeihu.data.network.socket.xsocket.tcp.client.bean.TargetInfo;

public class BaseDecodeHelper implements AbsDecodeHelper {
    @Override
    public byte[][] execute(byte[] data, TargetInfo targetInfo, TcpConnConfig tcpConnConfig) {
        return new byte[][]{data};
    }
}
