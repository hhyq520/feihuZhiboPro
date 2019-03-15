package cn.feihutv.zhibofeihu.data.network.http.model.me;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <pre>
 *     author : sichu.chen
 *     time   : 2017/11/01
 *     desc   : 发送动态
 *     version: 1.0
 * </pre>
 */
public class PostFeedRequest {


    @Expose
    @SerializedName("content")
    public String content; //


    @Expose
    @SerializedName("location")
    public String location; //





    @Expose
    @SerializedName("img1")
    public String img1; //

    @Expose
    @SerializedName("img2")
    public String img2; //

    @Expose
    @SerializedName("img3")
    public String img3; //

    @Expose
    @SerializedName("img4")
    public String img4; //

    @Expose
    @SerializedName("img5")
    public String img5; //

    @Expose
    @SerializedName("img6")
    public String img6; //

    @Expose
    @SerializedName("img7")
    public String img7; //

    @Expose
    @SerializedName("img8")
    public String img8; //

    @Expose
    @SerializedName("img9")
    public String img9; //

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    public String getImg6() {
        return img6;
    }

    public void setImg6(String img6) {
        this.img6 = img6;
    }

    public String getImg7() {
        return img7;
    }

    public void setImg7(String img7) {
        this.img7 = img7;
    }

    public String getImg8() {
        return img8;
    }

    public void setImg8(String img8) {
        this.img8 = img8;
    }

    public String getImg9() {
        return img9;
    }

    public void setImg9(String img9) {
        this.img9 = img9;
    }

    public PostFeedRequest(String content, String location) {
        this.content = content;
        this.location = location;
    }


    public PostFeedRequest() {
    }

}
