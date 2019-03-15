package cn.feihutv.zhibofeihu.data.network.http.model.me;


/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/7 14:35
 *      desc   : 我守护的
 *      version: 1.0
 * </pre>
 */

public class GetUserGuardListRequest {

    private String userId;

    private String offset;

    private String count;

    public GetUserGuardListRequest(String userId, String offset, String count) {
        this.userId = userId;
        this.offset = offset;
        this.count = count;
    }
}
