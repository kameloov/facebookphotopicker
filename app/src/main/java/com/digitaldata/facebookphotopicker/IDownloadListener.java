package com.digitaldata.facebookphotopicker;

import com.digitaldata.facebookphotopicker.entity.FacebookPhoto;

public interface IDownloadListener {
    public void onDownloadStart(FacebookPhoto photo);
    public void onDownloadComplete(FacebookPhoto photo);

}