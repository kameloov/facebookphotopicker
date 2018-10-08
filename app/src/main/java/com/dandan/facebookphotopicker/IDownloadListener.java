package com.dandan.facebookphotopicker;

import com.dandan.facebookphotopicker.entity.FacebookPhoto;

 interface IDownloadListener {
    public void onDownloadStart(FacebookPhoto photo);
    public void onDownloadComplete(FacebookPhoto photo);

}