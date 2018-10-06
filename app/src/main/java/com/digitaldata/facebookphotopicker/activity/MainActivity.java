package com.digitaldata.facebookphotopicker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digitaldata.facebookphotopicker.FaceBookManager;
import com.digitaldata.facebookphotopicker.IDownloadListener;
import com.digitaldata.facebookphotopicker.PhotoDownloadManager;
import com.digitaldata.facebookphotopicker.R;
import com.digitaldata.facebookphotopicker.adapter.FBPhotoAdapter;
import com.digitaldata.facebookphotopicker.entity.FacebookPhoto;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IDownloadListener, FacebookCallback<LoginResult> {

    private CallbackManager callbackManager;
    private RecyclerView lstPhotos;
    private ProgressBar progressBar;
    private ProgressBar progressDownload;
    private FBPhotoAdapter adapter;
    private TextView txtCount;
    private AccessToken token = AccessToken.getCurrentAccessToken();
    private PhotoDownloadManager downloadManager;
    private static  final  String PHOTO_REQUEST_STRING="/photos?type=uploaded&fields=id,images,picture";
    private boolean keepFiles= false;
    private int maxCount=Integer.MAX_VALUE;
    private int minCount=1;
    private String buttonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar()!=null)
        getSupportActionBar().hide();
        attachViews();
        attachEvents();
        init();
    }
    private void attachViews() {
        lstPhotos = findViewById(R.id.lstPhotos);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        txtCount = findViewById(R.id.txtCount);
        progressDownload = findViewById(R.id.progressDownload);
    }

    private void attachEvents(){
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter!=null) {
                    ArrayList<String> urls = new ArrayList<>();
                    for (FacebookPhoto photo: adapter.getPhotos()){
                        if (photo.getUri()!=null)
                            urls.add(photo.getUri().getPath());
                    }
                    keepFiles = true;
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("photos",urls);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }


    private void getIntentData(){
        maxCount = getIntent().getIntExtra("max",Integer.MAX_VALUE);
        minCount = getIntent().getIntExtra("min",0);
        String text = getIntent().getStringExtra("button-text");
        if (text!=null&&text.length()>0)
            buttonText = text;
    }
    private void init(){
        getIntentData();
        txtCount.setText(buttonText);
        progressDownload.setVisibility(View.INVISIBLE);
        downloadManager = new PhotoDownloadManager(this);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        lstPhotos.setLayoutManager(manager);
        callbackManager = CallbackManager.Factory.create();
        FaceBookManager.registerLoginCallback(callbackManager, this);
        if (FaceBookManager.isLogged()) {
            getUserPhotos("/" + token.getUserId() + PHOTO_REQUEST_STRING);
        } else {
            FaceBookManager.login(this);
        }
    }

    private void getUserPhotos(String url) {

        AccessToken token = AccessToken.getCurrentAccessToken();
        Log.e("user_id", token.getUserId());
        progressBar.setVisibility(View.VISIBLE);
        new GraphRequest(token, url,
                null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(final GraphResponse response) {
                if (response.getError() == null) {
                    try {
                        Log.e("response", response.getJSONObject().toString());
                        ///  paging information ///
                        JSONObject paging = response.getJSONObject().getJSONObject("paging");
                        String data = response.getJSONObject().getString("data");
                        Type type = new TypeToken<List<FacebookPhoto>>() {
                        }.getType();
                        Gson gson = new Gson();
                        final List<FacebookPhoto> photos = gson.fromJson(data, type);
                        GraphRequest nextRequest =response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                        if (nextRequest!=null){
                            nextRequest.setCallback(this);
                            nextRequest.executeAsync();
                        }
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter == null) {
                                    adapter = new FBPhotoAdapter(MainActivity.this, photos,downloadManager);
                                    lstPhotos.setAdapter(adapter);
                                } else {
                                    adapter.addPhotos(photos);
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }).executeAsync();

    }

    @Override
    protected void onDestroy() {
        if (!keepFiles&&adapter!=null){
            for (FacebookPhoto photo : adapter.getPhotos()){
                if (photo.getUri()!=null){
                    File file = new File(photo.getUri().getPath());
                    file.delete();
                    Log.e("delete","file deleted");
                }
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDownloadStart(FacebookPhoto photo) {
        progressDownload.setVisibility(View.VISIBLE);
        txtCount.setEnabled(false);
        txtCount.setBackgroundColor(getResources().getColor(R.color.com_facebook_button_background_color_disabled));
    }

    @Override
    public void onDownloadComplete(FacebookPhoto photo) {
        if (downloadManager.getActiveDownloads()==0) {
            progressDownload.setVisibility(View.INVISIBLE);
            txtCount.setEnabled(true);
            txtCount.setBackgroundColor(getResources().getColor(R.color.com_facebook_button_background_color));
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        if (token==null)
            token = AccessToken.getCurrentAccessToken();
        getUserPhotos("/" + token.getUserId() + PHOTO_REQUEST_STRING);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }
}
