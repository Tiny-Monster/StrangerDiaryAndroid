package com.tinymonster.strangerdiary.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.ui.diary.OnDiaryItemClickListener;
import com.tinymonster.strangerdiary.ui.holder.ListDataHolder;
import com.tinymonster.strangerdiary.utils.GlideLoaderUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.http.PATCH;

/**
 * Created by TinyMonster on 06/01/2019.
 */

public class DiaryListAdapter extends BaseListAdapter<DiaryBean>{
    Pattern patten= Pattern.compile("img.+?pic");
    private OnDiaryItemClickListener listener;
    private dialog_item_pic_click picClick;

    public DiaryListAdapter(OnDiaryItemClickListener listener){
        this.listener=listener;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_my_article;
    }

    @Override
    public void bindData(ListDataHolder holder, final DiaryBean bean, int itemType, int position) {
        //加载视图
        TextView my_article_item_date=holder.getView(R.id.my_article_item_date);
        ImageView my_article_item_isSyn=holder.getView(R.id.my_article_item_isSyn);
        TextView my_article_item_time=holder.getView(R.id.my_article_item_time);
        TextView my_article_item_content=holder.getView(R.id.my_article_item_content);
        ImageView my_article_item_img_1 = holder.getView(R.id.my_article_item_img_1);
        ImageView my_article_item_img_2 = holder.getView(R.id.my_article_item_img_2);
        ImageView my_article_item_img_3 = holder.getView(R.id.my_article_item_img_3);
        LinearLayout my_article_item_layout=(LinearLayout) holder.getParentView();
        List<ImageView> imageViews=new ArrayList<>();
        imageViews.add(my_article_item_img_1);
        imageViews.add(my_article_item_img_2);
        imageViews.add(my_article_item_img_3);
        //设置同步图片
        if(bean.getIsSyn().equals("1")){
            my_article_item_isSyn.setVisibility(View.GONE);
        }else {
            my_article_item_isSyn.setVisibility(View.VISIBLE);
            my_article_item_isSyn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemSynClick(bean);
                }
            });
        }
        final List<String> imagePath=new ArrayList<>();
        //正则匹配  获取图片 设置图片预览
        int pic_index=0;
        String content=bean.getContent();
        if(content!=null){
            if(bean.getPicNum()>0){
                Matcher matcher = patten.matcher(content);
                while(matcher.find()){
                    if(pic_index>=3)
                        break;
                    String path= matcher.group();
                    path=path.substring(3,path.length()-3);
                    if(path.startsWith("/monster")){
                        String subPath=path.substring(15);
                        String absolutePath="http://39.105.104.164/getimage"+subPath;
                        imagePath.add(absolutePath);
                        GlideLoaderUtils.loadImageFromNetThumbnail(absolutePath,imageViews.get(pic_index),0.2f);
                        pic_index++;
                    }else {
                        imagePath.add(path);
                        GlideLoaderUtils.loadImageFromDiskThumbnail(path,imageViews.get(pic_index),0.2f);
                        pic_index++;
                    }
                }
                content=content.replaceAll("img.+?pic"," [图片] ");
            }
            bean.setimagePaths(imagePath);
            for(;pic_index<3;pic_index++){
                imageViews.get(pic_index).setVisibility(View.GONE);
            }
            //设置content
            if(content.length()>30){
                content=content.substring(0,30);
                content=content+"...";
            }
        }
        my_article_item_content.setText(content);
        //设置日期
        my_article_item_date.setText(getDateSpanText(bean.getDate()));
        //设置时间
        my_article_item_time.setText(getTimeSpanText(bean.getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null)
                    listener.onItemClick(bean);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener!=null){
                    listener.onItemLongClick(bean);
                }
                return true;
            }
        });
        /**
         * 图片点击  浏览大图
         */
        my_article_item_img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picClick.onImageClick(0,imagePath);
            }
        });
        my_article_item_img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picClick.onImageClick(1,imagePath);
            }
        });
        my_article_item_img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picClick.onImageClick(2,imagePath);
            }
        });
        my_article_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(bean);
            }
        });
    }

    /**
     * DateSpan效果
     * @param source
     * @return
     */
    private Spannable getDateSpanText(Long source) {
        if(source!=null){
            Date date=new Date(source);
            String day_format="MM/dd";
            String year_format="yyyy";
            StringBuilder dateString=new StringBuilder();
            dateString.append(new SimpleDateFormat(day_format).format(date));
            dateString.append("\n");
            dateString.append(new SimpleDateFormat(year_format).format(date));
            Spannable mSpan = new SpannableString(dateString);
            mSpan.setSpan(new RelativeSizeSpan(2.0f),6,dateString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(AppContext.getContext(), R.color.colorPrimary)), 0, dateString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            return mSpan;
        }else {
            Spannable mSpan = new SpannableString("未知");
            return mSpan;
        }
    }

    /**
     * timeSpan效果
     * @param source
     * @return
     */
    private Spannable getTimeSpanText(Long source) {
        if(source!=null){
            Date date=new Date(source);
            String format="HH:mm";
            String dateString= new SimpleDateFormat(format).format(date);
            Spannable mSpan = new SpannableString(dateString);
            mSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(AppContext.getContext(), R.color._a5a9aa)), 0, dateString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            return mSpan;
        }else {
            Spannable mSpan = new SpannableString("未知");
            return mSpan;
        }
    }

    public interface dialog_item_pic_click{
        void onImageClick(int startPosition,List<String> urls);
    }

    public void setImageClickListener(dialog_item_pic_click pic_click){
        this.picClick=pic_click;
    }
}
