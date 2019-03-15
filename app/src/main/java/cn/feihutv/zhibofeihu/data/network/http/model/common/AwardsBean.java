package cn.feihutv.zhibofeihu.data.network.http.model.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author : liwen.chen
 *      time   : 2017/10/20 11:49
 *      desc   : 界面描述
 *      version: 1.0
 * </pre>
 */

public class AwardsBean {


    private boolean isSignDay = false;
    private int day;
    private List<AwardsDatas> mAwardsDatases;


    public boolean isSignDay() {
        return isSignDay;
    }

    public void setSignDay(boolean signDay) {
        isSignDay = signDay;
    }

    @Override
    public String toString() {
        return "AwardsBean{" +
                "day=" + day +
                ", mAwardsDatases=" + mAwardsDatases +
                '}';
    }

    public List<AwardsDatas> getAwardsDatases() {
        return mAwardsDatases;
    }

    public void setAwardsDatases(List<AwardsDatas> awardsDatases) {
        mAwardsDatases = awardsDatases;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public static class AwardsDatas {
        /**
         * type : 4
         * id : 0
         * cnt : 10
         */

        private int type;
        private int id;
        private int cnt;

        public static List<AwardsDatas> arrayAwardsBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<AwardsDatas>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }

        @Override
        public String toString() {
            return "AwardsDatas{" +
                    "type=" + type +
                    ", id=" + id +
                    ", cnt=" + cnt +
                    '}';
        }
    }

}
