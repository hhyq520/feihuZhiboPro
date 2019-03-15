package cn.feihutv.zhibofeihu.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public  class SysGiftNewBean {
        /**
         * id : 1
         * name : 小黄瓜
         * incomePercent : 400
         * goodsId : 1
         * isAnimation : 0
         * fileName :
         * animName :
         * localPath : giftimages/banana.png
         * cosPath : gift/1/v1.png
         * sortOrder : 1
         */
        private boolean is520=false;
        private boolean is1314=false;
        private boolean isMount=false;
        @Id
        private String id;
        private String name;
        private String incomePercent;
        private String isAnimation;
        private String tips;
        private String shelfBegin;
        private String shelfEnd;
        private String tagIcon;
        private int sortOrder;
        private String icon;
        private String animName;
        private int giftCount=0;
        private boolean isSelected;
        private int enableVip;
        @Transient
        private boolean isVip;


        @Generated(hash = 1241225313)
        public SysGiftNewBean(boolean is520, boolean is1314, boolean isMount, String id,
                String name, String incomePercent, String isAnimation, String tips,
                String shelfBegin, String shelfEnd, String tagIcon, int sortOrder,
                String icon, String animName, int giftCount, boolean isSelected,
                int enableVip) {
            this.is520 = is520;
            this.is1314 = is1314;
            this.isMount = isMount;
            this.id = id;
            this.name = name;
            this.incomePercent = incomePercent;
            this.isAnimation = isAnimation;
            this.tips = tips;
            this.shelfBegin = shelfBegin;
            this.shelfEnd = shelfEnd;
            this.tagIcon = tagIcon;
            this.sortOrder = sortOrder;
            this.icon = icon;
            this.animName = animName;
            this.giftCount = giftCount;
            this.isSelected = isSelected;
            this.enableVip = enableVip;
        }

        @Generated(hash = 564899866)
        public SysGiftNewBean() {
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

        public String getIncomePercent() {
            return incomePercent;
        }

        public void setIncomePercent(String incomePercent) {
            this.incomePercent = incomePercent;
        }

        public String getIsAnimation() {
            return isAnimation;
        }

        public void setIsAnimation(String isAnimation) {
            this.isAnimation = isAnimation;
        }

        public int getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
        }

        public int getGiftCount() {
            return giftCount;
        }

        public void setGiftCount(int giftCount) {
            this.giftCount = giftCount;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean is520() {
            return is520;
        }

        public void setIs520(boolean is520) {
            this.is520 = is520;
        }

        public boolean is1314() {
            return is1314;
        }

        public void setIs1314(boolean is1314) {
            this.is1314 = is1314;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getShelfBegin() {
            return shelfBegin;
        }

        public void setShelfBegin(String shelfBegin) {
            this.shelfBegin = shelfBegin;
        }

        public String getShelfEnd() {
            return shelfEnd;
        }

        public void setShelfEnd(String shelfEnd) {
            this.shelfEnd = shelfEnd;
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

        public String getTagIcon() {
            return tagIcon;
        }

        public void setTagIcon(String tagIcon) {
            this.tagIcon = tagIcon;
        }

        public boolean isMount() {
            return isMount;
        }

        public void setMount(boolean mount) {
            isMount = mount;
        }

        public boolean getIs520() {
            return this.is520;
        }

        public boolean getIs1314() {
            return this.is1314;
        }

        public boolean getIsMount() {
            return this.isMount;
        }

        public void setIsMount(boolean isMount) {
            this.isMount = isMount;
        }

        public boolean getIsSelected() {
            return this.isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

    public int getEnableVip() {
        return enableVip;
    }

    public void setEnableVip(int enableVip) {
        this.enableVip = enableVip;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}