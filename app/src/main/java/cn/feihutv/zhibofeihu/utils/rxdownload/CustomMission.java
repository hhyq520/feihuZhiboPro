package cn.feihutv.zhibofeihu.utils.rxdownload;


import org.jetbrains.annotations.NotNull;

import zlc.season.rxdownload3.core.Mission;

public class CustomMission extends Mission {
    private String img = "";
    private String introduce = "";
    private String title= "";
    private String zbName= "";
    private String zbIcon= "";
    private int downState=-1;//下载状态,-1 未下载，0：已下载；1：正在下载;2:已暂停；3：下载失败；4：连接已过期

//    public CustomMission(@NotNull String url, String introduce, String img,String title) {
//        super(url);
//        setUrl(url);
//        this.introduce = introduce;
//        this.img = img;
//        this.title=title;
//    }

    public CustomMission(@NotNull Mission mission, String img,String title,
                         String zbName,String zbIcon,int downState) {
        super(mission);
        this.img = img;
        this.title=title;
        this.zbIcon=zbIcon;
        this.zbName=zbName;
        this.downState=downState;

    }


    public int getDownState() {
        return downState;
    }

    public void setDownState(int downState) {
        this.downState = downState;
    }

    public String getZbName() {
        return zbName;
    }

    public void setZbName(String zbName) {
        this.zbName = zbName;
    }

    public String getZbIcon() {
        return zbIcon;
    }

    public void setZbIcon(String zbIcon) {
        this.zbIcon = zbIcon;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
