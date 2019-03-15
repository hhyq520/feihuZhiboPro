package cn.feihutv.zhibofeihu.data.network.http.model.common;

import java.util.Map;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/08
 *     desc   : 基本描述
 *     version: 1.0
 * </pre>
 */
public class WebHttpRequest {


    public WebHttpRequest(String apiName, Map<String, String> req) {
        this.apiName = apiName;
        this.req = req;
    }

    private String apiName;

    private  Map<String, String> req;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }



    public Map<String, String> getReq() {
        return req;
    }

    public void setReq(Map<String, String> req) {
        this.req = req;
    }
}

