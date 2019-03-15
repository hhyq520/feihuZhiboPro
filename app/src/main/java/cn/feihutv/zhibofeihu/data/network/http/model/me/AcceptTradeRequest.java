package cn.feihutv.zhibofeihu.data.network.http.model.me;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/23 10:20
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class AcceptTradeRequest {

    private String id;

    private String from;

    public AcceptTradeRequest(String id, String from) {
        this.id = id;
        this.from = from;
    }
}
