package cn.feihutv.zhibofeihu.data.network.http.model.common;

import org.json.JSONObject;

import cn.feihutv.zhibofeihu.data.network.http.model.BaseResponse;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/08
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class WebHttpResponse extends BaseResponse{


    private JSONObject response;

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }
}
