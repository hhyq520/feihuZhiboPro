package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public  class SysHBBean {
        /**
         * saleId : 1
         * goldCnt : 600
         * rmb : 6.00
         * giveGold : 0
         */
        @Id
        private String saleId;
        private String goldCnt;
        private String rmb;
        private String giveGold;

        @Generated(hash = 1648305430)
        public SysHBBean(String saleId, String goldCnt, String rmb, String giveGold) {
            this.saleId = saleId;
            this.goldCnt = goldCnt;
            this.rmb = rmb;
            this.giveGold = giveGold;
        }

        @Generated(hash = 1530678234)
        public SysHBBean() {
        }

        public String getSaleId() {
            return saleId;
        }

        public void setSaleId(String saleId) {
            this.saleId = saleId;
        }

        public String getGoldCnt() {
            return goldCnt;
        }

        public void setGoldCnt(String goldCnt) {
            this.goldCnt = goldCnt;
        }

        public String getRmb() {
            return rmb;
        }

        public void setRmb(String rmb) {
            this.rmb = rmb;
        }

        public String getGiveGold() {
            return giveGold;
        }

        public void setGiveGold(String giveGold) {
            this.giveGold = giveGold;
        }
    }