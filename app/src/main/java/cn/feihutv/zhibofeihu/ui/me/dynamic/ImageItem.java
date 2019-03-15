package cn.feihutv.zhibofeihu.ui.me.dynamic;


import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;

import cn.feihutv.zhibofeihu.ui.widget.Bimp;

public class ImageItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    private Bitmap bitmap;
    private boolean isCapture = false;
    public boolean isSelected = false;
    public String newPath;

    public String getnewPath() {
        return newPath;
    }

    public void setnewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            try {
                bitmap = Bimp.revitionImageSize(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    @Override
    public String toString() {
        return "imageId:" + imageId + "\n"
                + "thumbnailPath:" + thumbnailPath + "\n"
                + "imagePath:" + imagePath + "";
    }

    public boolean isCapture() {
        return isCapture;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }
}
