package cn.feihutv.zhibofeihu.data.network.http.model.me;

/**
 * <pre>
 *      @author : liwen.chen
 *      time   : 2017/11/28 10:15
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class LoadAllFeedListRequest {

    private int offset;

    private int count;

    public LoadAllFeedListRequest(int offset, int count) {
        this.offset = offset;
        this.count = count;
    }
}
