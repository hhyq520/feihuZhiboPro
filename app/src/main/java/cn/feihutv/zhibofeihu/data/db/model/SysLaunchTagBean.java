package cn.feihutv.zhibofeihu.data.db.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/17 11:33
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */
@Entity
public class SysLaunchTagBean {

    /**
     * id : 3
     * name : 才艺
     * type : 2
     * sort : 98
     */

    @Id
    private String id;
    private String name;
    private String type;
    private String tag;
    private int defaultTag;

    @Expose
    @SerializedName("sort")
    private int launchSort;

    @Generated(hash = 902670358)
    public SysLaunchTagBean(String id, String name, String type, String tag,
            int defaultTag, int launchSort) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.tag = tag;
        this.defaultTag = defaultTag;
        this.launchSort = launchSort;
    }

    @Generated(hash = 655454302)
    public SysLaunchTagBean() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getDefaultTag() {
        return this.defaultTag;
    }

    public void setDefaultTag(int defaultTag) {
        this.defaultTag = defaultTag;
    }

    public int getLaunchSort() {
        return this.launchSort;
    }

    public void setLaunchSort(int launchSort) {
        this.launchSort = launchSort;
    }

}
