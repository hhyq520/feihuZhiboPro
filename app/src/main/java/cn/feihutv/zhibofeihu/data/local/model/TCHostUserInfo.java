package cn.feihutv.zhibofeihu.data.local.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17.
 */

public class TCHostUserInfo implements Serializable {

    public String nickname;  // 昵称

    public String headpic; // 头像

    public String grade; // 性别

    public int type; // 是否在直播

    public int contribution; // 贡献榜

    public String sign;  // 个性签名

    public int visitorCount; // 观看人数

    public boolean isConcern; // 是否被关注

    public TCHostUserInfo(String nickname, String headpic, String grade, int type, int contribution, String sign, int visitorCount, boolean isConcern) {
        this.nickname = nickname;
        this.headpic = headpic;
        this.grade = grade;
        this.type = type;
        this.contribution = contribution;
        this.sign = sign;
        this.visitorCount = visitorCount;
        this.isConcern = isConcern;
    }

    public TCHostUserInfo() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(int visitorCount) {
        this.visitorCount = visitorCount;
    }

    public boolean isConcern() {
        return isConcern;
    }

    public void setConcern(boolean concern) {
        isConcern = concern;
    }

    @Override
    public String toString() {
        return "TCHostUserInfo{" +
            "nickname='" + nickname + '\'' +
            ", headpic='" + headpic + '\'' +
            ", grade='" + grade + '\'' +
            ", type=" + type +
            ", contribution=" + contribution +
            ", sign='" + sign + '\'' +
            ", visitorCount=" + visitorCount +
            ", isConcern=" + isConcern +
            '}';
    }
}
