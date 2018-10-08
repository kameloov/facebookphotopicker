package com.dandan.facebookphotopicker;

import android.net.Uri;
import android.os.AsyncTask;

import com.dandan.facebookphotopicker.entity.FacebookPhoto;
import com.dandan.facebookphotopicker.entity.ImageData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kameloov on 10/4/2018.
 */

public class LoadPhotoTask extends AsyncTask<Void,Void,File> {
    private FacebookPhoto photo;
    private IDownloadListener downloadListener;

     LoadPhotoTask(FacebookPhoto photo, IDownloadListener downloadListener) {
        this.photo = photo;
        this.downloadListener = downloadListener;
    }

    @Override
    protected void onPreExecute() {
        photo.setDownloading(true);
        if (downloadListener!=null)
            downloadListener.onDownloadStart(photo);
    }

    @Override
    protected void onPostExecute(File file) {
        photo.setDownloading(false);
        if (file!=null){
            photo.setUri(Uri.fromFile(file));
            photo.setDownloaded(true);
        }
        if (downloadListener!=null)
        downloadListener.onDownloadComplete(photo);
    }

    private String getBestLink(){
        String url ="";
        int maxHeight = 0 ;
        for (ImageData imageData : photo.getImages()){
            if (imageData.getHeight() > maxHeight) {
                maxHeight = imageData.getHeight();
                url = imageData.getSource();
            }
        }
        return url;
    }

    @Override
    protected File doInBackground(Void... voids) {
        File file = null;
        try {
            URL url = new URL(getBestLink());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int code = connection.getResponseCode();
            if (code == HttpsURLConnection.HTTP_OK){
                InputStream is = connection.getInputStream();
                file = File.createTempFile("fb_img",".img");
                FileOutputStream os = new FileOutputStream(file);
                int bytesRead = -1;
                byte [] buffer = new byte[1024];
                while ((bytesRead =is.read(buffer))!=-1){
                    os.write(buffer,0,bytesRead);
                }
                os.close();
                is.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
