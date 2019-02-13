package com.tinymonster.strangerdiary.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;

import java.io.File;

/**
 * Created by TinyMonster on 06/01/2019.
 */

public class GlideLoaderUtils {
    private static RequestOptions nomal_image_options = RequestOptions.placeholderOf(R.drawable.iv_img_default)
            .centerCrop();

    public static void loadImageFromDisk(File file, ImageView imageView){
        Glide.with(AppContext.getContext()).load(file).apply(nomal_image_options).into(imageView);
    }

    public static void loadImageFromDisk(String filePath, ImageView imageView){
        File file=new File(filePath);
        Glide.with(AppContext.getContext()).load(file).apply(nomal_image_options).into(imageView);
    }

    public static void loadImageFromDiskThumbnail(String filePath, ImageView imageView,float value){
        File file=new File(filePath);
        Glide.with(AppContext.getContext()).load(file).apply(nomal_image_options).thumbnail(value).into(imageView);
    }

    public static void loadImageFromDisk(String filePath, ImageView imageView,int width,int height){
        File file=new File(filePath);
        Glide.with(AppContext.getContext()).load(file).apply(nomal_image_options.override(PxUtils.Dip2Px(width),PxUtils.Dip2Px(height))).into(imageView);
    }

    public static void loadImageFromNet(String url, ImageView imageView){
        Glide.with(AppContext.getContext()).load(url).apply(nomal_image_options).into(imageView);
    }

    public static void loadImageFromNetThumbnail(String url, ImageView imageView,float value){
        Glide.with(AppContext.getContext()).load(url).apply(nomal_image_options).thumbnail(value).into(imageView);
    }

    public static void loadImageFromNet(String url, ImageView imageView,int width,int height){
        Glide.with(AppContext.getContext()).load(url).apply(nomal_image_options.override(PxUtils.Dip2Px(width),PxUtils.Dip2Px(height))).into(imageView);
    }
}
