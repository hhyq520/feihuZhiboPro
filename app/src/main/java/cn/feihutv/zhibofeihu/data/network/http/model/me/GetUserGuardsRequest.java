package cn.feihutv.zhibofeihu.data.network.http.model.me;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/7 14:27
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class GetUserGuardsRequest {

    private String userId;

    private String offset;

    private String count;

    public GetUserGuardsRequest(String userId, String offset, String count) {
        this.userId = userId;
        this.offset = offset;
        this.count = count;
    }
}
