package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public  class SysLevelBean {
        /**
         * level : 1
         * needExp : 0
         */
        @Id
        private String level;
        private String needExp;

        @Generated(hash = 1421975157)
        public SysLevelBean(String level, String needExp) {
            this.level = level;
            this.needExp = needExp;
        }

        @Generated(hash = 909381536)
        public SysLevelBean() {
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getNeedExp() {
            return needExp;
        }

        public void setNeedExp(String needExp) {
            this.needExp = needExp;
        }
    }