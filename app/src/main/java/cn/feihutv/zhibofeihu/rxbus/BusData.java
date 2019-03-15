package cn.feihutv.zhibofeihu.rxbus;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/09/25
 *     desc   : Rx bus data
 *     version: 1.0
 * </pre>
 */
public class BusData {
    String id;
    String status;
    public BusData() {}
    public BusData(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
