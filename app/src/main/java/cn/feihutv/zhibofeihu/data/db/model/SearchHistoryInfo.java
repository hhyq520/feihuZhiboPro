package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/23 09:26
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */
@Entity
public class SearchHistoryInfo {

    @Id
    private String content;

    private long time;

    @Generated(hash = 813628143)
    public SearchHistoryInfo(String content, long time) {
        this.content = content;
        this.time = time;
    }

    @Generated(hash = 1071675110)
    public SearchHistoryInfo() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
