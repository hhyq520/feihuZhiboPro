package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public  class SysItemBean {
        /**
         * itemId : 1
         * itemName : 飞虎流星
         */
        @Id
        private String itemId;
        private String itemName;

        @Generated(hash = 223124932)
        public SysItemBean(String itemId, String itemName) {
            this.itemId = itemId;
            this.itemName = itemName;
        }

        @Generated(hash = 169706944)
        public SysItemBean() {
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }