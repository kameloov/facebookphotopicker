package com.dandan.facebookphotopicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kameloov on 10/8/2018.
 */

public class FacebookPhotoPicker {
    public static void startPicker(Activity activity,String okButtonText ,int requestCode){
        Intent intent = new Intent(activity,FbPickerActivity.class);
        intent.putExtra("button-text",okButtonText);
        activity.startActivityForResult(intent,requestCode);
    }

    public static void startPicker(Fragment fragment, String okButtonText , int requestCode){
        Intent intent = new Intent(fragment.getActivity(),FbPickerActivity.class);
        intent.putExtra("button-text",okButtonText);
        fragment.startActivityForResult(intent,requestCode);
    }

    public static void startPicker(android.support.v4.app.Fragment fragment, String okButtonText , int requestCode){
        Intent intent = new Intent(fragment.getActivity(),FbPickerActivity.class);
        intent.putExtra("button-text",okButtonText);
        fragment.startActivityForResult(intent,requestCode);
    }

    public static List<File> getFiles(Intent resultIntent){
        ArrayList<File> files  = new ArrayList<>();
        ArrayList<String> paths = resultIntent.getStringArrayListExtra("photos");
        if (paths!=null) {
            for (String s : paths) {
                File f = new File(s);
                files.add(f);
            }
        }
        return files;
    }
}
