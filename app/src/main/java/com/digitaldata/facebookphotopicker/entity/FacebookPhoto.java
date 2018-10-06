package com.digitaldata.facebookphotopicker.entity;

import android.net.Uri;

import java.util.List;

/**
 * Created by kameloov on 9/23/2018.
 */

public class FacebookPhoto {
    private double id;
    private List<ImageData> images;
    private int width ;
    private int height;
    private boolean downloaded;
    private boolean downloading;
    private Uri uri;
    private boolean selected;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public List<ImageData> getImages() {
        return images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
