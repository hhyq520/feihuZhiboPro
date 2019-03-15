package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SysMountNewBean {
    @Id
    private String id;
    private String name;
    private String isAnimation;
    private String level;
    private String icon;
    private String animName;

    @Generated(hash = 1414931939)
    public SysMountNewBean(String id, String name, String isAnimation, String level,
                           String icon, String animName) {
        this.id = id;
        this.name = name;
        this.isAnimation = isAnimation;
        this.level = level;
        this.icon = icon;
        this.animName = animName;
    }

    @Generated(hash = 418810209)
    public SysMountNewBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAnimName() {
        return animName;
    }

    public void setAnimName(String animName) {
        this.animName = animName;
    }

    public String getIsAnimation() {
        return isAnimation;
    }

    public void setIsAnimation(String isAnimation) {
        this.isAnimation = isAnimation;
    }
}