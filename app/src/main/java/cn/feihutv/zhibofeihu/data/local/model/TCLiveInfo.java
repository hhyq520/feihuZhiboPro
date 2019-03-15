package cn.feihutv.zhibofeihu.data.local.model;

public class TCLiveInfo {
    public String userid;
    public String groupid;
    public int      timestamp;
    public int      type;//直播还是回访
    public int      viewercount;
    public int      likecount;
    public String title;  // 直播标题
    public String playurl;
    public String fileid;

    //TCLiveUserInfo
    public TCLiveUserInfo userinfo;
    public void setTCLiveUserInfo(String nickname,
             String headpic,
             String frontcover,
             String location){
        userinfo=new TCLiveUserInfo(nickname,headpic,frontcover,location);
    }

    public class TCLiveUserInfo {
        public String nickname;  // 直播昵称
        public String headpic;
        public String frontcover;
        public String location;
        public  TCLiveUserInfo(String nickname,
                                      String headpic,
                                      String frontcover,
                                      String location){
            this.nickname=nickname;
            this.headpic=headpic;
            this.frontcover=frontcover;
            this.location=location;
        }
    }
}
