package com.tinymonster.strangerdiary.ui.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.utils.GlideLoaderUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TinyMonster on 03/01/2019.
 */

public class RichBoard extends ScrollView {
    private DiaryBean diaryBean;
    private Context context;
    LayoutInflater inflater;
    private LinearLayout allLayout;
    private LayoutTransition transition;  //图片动画
    private int viewTagIndex=0;
    private BoardImageClickListener boardImageClickListener;

    public RichBoard(Context context) {
        this(context,null);
    }

    public RichBoard(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RichBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater= LayoutInflater.from(context);
        allLayout=new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        allLayout.setBackgroundColor(Color.WHITE);
        ViewGroup.LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(allLayout,layoutParams);//添加最外层LinearLayout
        Log.e("RichBoard","getWidth():"+getWidth());
    }

    public void setOnImageClickListener(BoardImageClickListener boardImageClickListener){
        this.boardImageClickListener=boardImageClickListener;
    }

    /**
     *
     * @param diaryBean
     */
    public void setData(DiaryBean diaryBean){
        this.diaryBean=diaryBean;
        String content=diaryBean.getContent();
        Pattern patten= Pattern.compile("img.+?pic");
        Matcher matcher = patten.matcher(content);
        int startIndex=0;
        while(matcher.find()){
            if(matcher.start()>startIndex){
                TextView editText=(TextView)inflater.inflate(R.layout.detail_text_layout,null);
                editText.setText(content.substring(startIndex,matcher.start()));
                allLayout.addView(editText,viewTagIndex);
                viewTagIndex++;
            }
            String path=matcher.group().toString();
            Log.e("path",path);
            path=path.substring(3,path.length()-3);
            addImageViewAtIndex(viewTagIndex,path);
            viewTagIndex++;
            startIndex = matcher.end();
        }
        if(startIndex<content.length()){
            TextView editText=(TextView)inflater.inflate(R.layout.detail_text_layout,null);
            editText.setText(content.substring(startIndex));
            allLayout.addView(editText,viewTagIndex);
            viewTagIndex++;
        }
    }
    /**
     * 创建ImageLayout
     * @return
     */
    private RelativeLayout createImageLayout(){
        RelativeLayout layout=(RelativeLayout)inflater.inflate(R.layout.detail_image_layout,null);
        layout.setTag(viewTagIndex);
        return layout;
    }

    /**
     * 将照片添加到指定位置
     * @param index
     * @param imagePath
     */
    private void addImageViewAtIndex(final int index,String imagePath){
        final RelativeLayout imageLayout =createImageLayout();
        final MonsterImageView imageView=(MonsterImageView)imageLayout.findViewById(R.id.detail_imageView);
        imageView.setUrl(imagePath);
        if(imagePath.startsWith("/monster")){
            String subPath=imagePath.substring(15);
            String absolutePath="http://39.105.104.164/getimage"+subPath;
            GlideLoaderUtils.loadImageFromNet(absolutePath,imageView,200,200);
        }else {
            GlideLoaderUtils.loadImageFromDisk(imagePath,imageView,200,200);
        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                boardImageClickListener.onImageClick(index);
            }
        });
        allLayout.addView(imageLayout, index);
    }

    public interface BoardImageClickListener{
        void onImageClick(int position);
    }
}
