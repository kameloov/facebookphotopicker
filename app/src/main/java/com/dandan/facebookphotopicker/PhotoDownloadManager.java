package com.dandan.facebookphotopicker;

import com.dandan.facebookphotopicker.entity.FacebookPhoto;

/**
 * Created by kameloov on 10/6/2018.
 */

 class PhotoDownloadManager implements IDownloadListener {
    private int activeDownloads=0 ;
    private IDownloadListener listener;

    public PhotoDownloadManager() {
    }

    public int getActiveDownloads() {
        return activeDownloads;
    }

    public void setActiveDownloads(int activeDownloads) {
        this.activeDownloads = activeDownloads;
    }

    public PhotoDownloadManager(IDownloadListener listener) {
        this.listener = listener;
    }

    public void  downloadPhoto(FacebookPhoto photo){
        LoadPhotoTask task = new LoadPhotoTask(photo,PhotoDownloadManager.this);
        task.execute();
    }

    @Override
    public void onDownloadStart(FacebookPhoto photo) {
        activeDownloads++;
        if (listener!=null)
            listener.onDownloadStart(photo);
    }

    @Override
    public void onDownloadComplete(FacebookPhoto photo) {
        activeDownloads--;
        if (listener!=null)
            listener.onDownloadComplete(photo);
    }
}
