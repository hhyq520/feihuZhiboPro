package cn.feihutv.zhibofeihu.data.network.socket;

import com.androidnetworking.utils.ParseUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.feihutv.zhibofeihu.data.network.socket.xsocket.FHSocket;
import cn.feihutv.zhibofeihu.utils.FHUtils;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/10/12
 *     desc   : socket 请求必填参数
 *     version: 1.0
 * </pre>
 */
@Singleton
public class SocketApiHeader {

    private SocketProtectedApiHeader mProtectedApiHeader;

    @Inject
    public SocketApiHeader(SocketProtectedApiHeader protectedApiHeader) {
        mProtectedApiHeader=protectedApiHeader;
    }

    public SocketProtectedApiHeader getProtectedApiHeader() {
        return mProtectedApiHeader;
    }

    //登录用户执行的请求头
    public static final class SocketProtectedApiHeader {


        @Expose
        @SerializedName("d")
        private String device;

        @Expose
        @SerializedName("c")
        private String market;

        @Expose
        @SerializedName("i")
        private String info;//设备信息


        @Inject
        public SocketProtectedApiHeader(String device, String market,String info) {
            this.device = device;
            this.market = market;
            this.info=info;
        }



        public String getSocketRequestData(String apiName,Object reqObj){

            HashMap<String, String> requestParams = ParseUtil.getParserFactory().getStringMap(this);
            requestParams.putAll(ParseUtil.getParserFactory().getStringMap(reqObj));

            return   apiName + "|" + apiName + (requestParams.size() == 0 ? "" : "?"
                    + FHUtils.formatUrlMap(requestParams, true, false, "&"));
        }



        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getMarket() {
            return market;
        }

        public void setMarket(String market) {
            this.market = market;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    //关闭socket
    public void closeSocket(){
        //关闭socket 连接服务
        FHSocket.getInstance().close();
        //关闭socket 心跳任务
        RxSocketUtil.getInstance().cancelHeartDisposable();

    }


}
