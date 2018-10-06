package com.digitaldata.facebookphotopicker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.digitaldata.facebookphotopicker.PhotoDownloadManager;
import com.digitaldata.facebookphotopicker.R;
import com.digitaldata.facebookphotopicker.entity.FacebookPhoto;

import java.util.List;

/**
 * Created by kameloov on 10/3/2018.
 */

public class FBPhotoAdapter extends RecyclerView.Adapter<FBPhotoAdapter.PhotoHolder> {

    private Context context;
    private List<FacebookPhoto> photoList;
    private PhotoDownloadManager downloadManager;
    public FBPhotoAdapter(Context context, List<FacebookPhoto> photos, PhotoDownloadManager downloadManager) {
        this.context = context;
        this.photoList = photos;
        this.downloadManager = downloadManager;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v  = inflater.inflate(R.layout.list_item_photo,parent,false);
        return new PhotoHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.bind(photoList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void addPhotos(List<FacebookPhoto> photos){
        if (photos!=null) {
            photoList.addAll(photos);
           // notifyDataSetChanged();
        }
    }

    public List<FacebookPhoto> getPhotos(){
        return  photoList;
    }

    class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private FacebookPhoto photo;
        private ImageView imgItem;
        private CardView crdItem;
        private ConstraintLayout lytItem;
        private ImageView imgSelected;

        public PhotoHolder(View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgItem);
            crdItem = itemView.findViewById(R.id.crdItem);
            lytItem = itemView.findViewById(R.id.lytItem);
            imgSelected = itemView.findViewById(R.id.imgSelected);
            itemView.setOnClickListener(this);
        }

        public void bind(final FacebookPhoto photo){
            this.photo = photo;
                Glide.with(context).asBitmap()
                        .load(photo.getPicture())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                imgItem.setImageBitmap(resource);
                            }
                        });
            crdItem.setBackgroundColor(context.getResources().getColor(photo.isSelected()?R.color.selected_item:R.color.white));
            crdItem.setCardElevation(photo.isSelected()?0:4);

        }

        @Override
        public void onClick(View view) {
            photo.setSelected(!photo.isSelected());
            /// download the photo if not downloaded yet
            if (photo.isSelected()){
                if (!photo.isDownloading()&& !photo.isDownloaded())
                    downloadManager.downloadPhoto(photo);
            }
            notifyItemChanged(photoList.indexOf(photo));
        }
    }
}
