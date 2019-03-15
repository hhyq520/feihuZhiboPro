package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public  class SysTipsBean {
        /**
         * tid : 1
         * cond : 注册-没有输入手机号就点击注册按钮
         * text : 请输入手机号
         */
        @Id
        private String tid;
        private String cond;
        private String text;

        @Generated(hash = 223588750)
        public SysTipsBean(String tid, String cond, String text) {
            this.tid = tid;
            this.cond = cond;
            this.text = text;
        }

        @Generated(hash = 1074618777)
        public SysTipsBean() {
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getCond() {
            return cond;
        }

        public void setCond(String cond) {
            this.cond = cond;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }