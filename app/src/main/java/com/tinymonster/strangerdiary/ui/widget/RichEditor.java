package com.tinymonster.strangerdiary.ui.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.utils.GlideLoaderUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.PxUtils;
import com.tinymonster.strangerdiary.utils.UserInfoManager;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by TinyMonster on 31/12/2018.
 */

public class RichEditor extends ScrollView{
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp
    private static final int EDIT_FIRST_PADDING_TOP = 10; // 第一个EditText的paddingTop值
    private int viewTagIndex=1;
    private LinearLayout allLayout;
    private LayoutInflater inflater;  //资源加载器
    private OnKeyListener keyListener; //EditText的软键盘监听器
    private OnClickListener btnListener;//图片的点击监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private MonsterEditText lastFocusEdit;  //最近被聚焦的EditText
    private LayoutTransition transition;  //图片动画
    private int editNormalPadding = 0;
    private int disappearingImageIndex = 0;  //上一个删除的ImageView

    public RichEditor(Context context) {
        this(context,null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater= LayoutInflater.from(context);
        //初始化外层LinearLayout
        allLayout=new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        allLayout.setBackgroundColor(Color.WHITE);
        setupLayoutTransitions();
        ViewGroup.LayoutParams layoutParams=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(allLayout,layoutParams);//添加最外层LinearLayout
        //软键盘退格事件
        keyListener=new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN&&keyEvent.getKeyCode()==KeyEvent.KEYCODE_DEL){
                    MonsterEditText editText=(MonsterEditText) view;
                    onBackspacePress(editText);
                }
                return false;
            }
        };

        //删除事件
        btnListener=new OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout parentView = (RelativeLayout) view.getParent();
                onImageCloseClick(parentView);
            }
        };

        //焦点监听事件
        focusListener=new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(hasFocus()){
                    lastFocusEdit=(MonsterEditText)view;
                }
            }
        };
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        editNormalPadding= PxUtils.Dip2Px(EDIT_PADDING);
        MonsterEditText editText=createEditText("请输入。。。",PxUtils.Dip2Px(EDIT_FIRST_PADDING_TOP));
        allLayout.addView(editText,firstEditParam);
        lastFocusEdit=editText;
    }

    public void init(DiaryBean diaryBean){
        String content=diaryBean.getContent();
        Pattern patten= Pattern.compile("img.+?pic");
        Matcher matcher = patten.matcher(content);
        allLayout.removeView(lastFocusEdit);
        int startIndex=0;
        while(matcher.find()){
            if(matcher.start()>startIndex){
                String subString=content.substring(startIndex,matcher.start());
                EditText editText2 = createEditText("", getResources()
                        .getDimensionPixelSize(R.dimen.edit_padding_top));
                editText2.setText(subString);

                // 请注意此处，EditText添加、或删除不触动Transition动画
                allLayout.setLayoutTransition(null);
                allLayout.addView(editText2);
                allLayout.setLayoutTransition(transition); // remove之后恢复transition动画
            }
            String path=matcher.group().toString();
            path=path.substring(3,path.length()-3);
            addImageViewAtIndexNoDelay(path);
            startIndex = matcher.end();
        }
        allLayout.addView(lastFocusEdit);
        if(startIndex<content.length()){
            lastFocusEdit.setText(content.substring(startIndex));
            lastFocusEdit.setSelection(content.substring(startIndex).length());
        }
        LogUtils.d("finish,allLayout.getcount:"+allLayout.getChildCount());
    }

    /**
     * 初始化动画
     */
    private void setupLayoutTransitions(){
        transition = new LayoutTransition();
        allLayout.setLayoutTransition(transition);
        transition.addTransitionListener(new LayoutTransition.TransitionListener() {
            @Override
            public void startTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {

            }

            @Override
            public void endTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
                if(!transition.isRunning()&&i==LayoutTransition.CHANGE_DISAPPEARING){

                }
            }
        });
        transition.setDuration(300);
    }

    /**
     * 当删除EditText中的文字删除完了的时候
     * 需要判断当前EditText的前一个View是什么类型
     * 如果是EditText，合并两个EditText
     * 如果是ImageView 删除前面的ImageView
     * @param editText
     */
    private void onBackspacePress(MonsterEditText editText){
        int startSelection = editText.getSelectionStart();
        if(startSelection==0){
            int editIndex = allLayout.indexOfChild(editText);//找到当前editText在allLayout中的位置
            View preView = allLayout.getChildAt(editIndex-1);//找到当前editText前一个View
            if(preView!=null){
                if(preView instanceof RelativeLayout){  //光标前面是图片
                    onImageCloseClick(preView);//删除图片
                }else if(preView instanceof MonsterEditText){
                    String str1=editText.getText().toString();
                    MonsterEditText preEditText = (MonsterEditText) preView;
                    String str2 =preEditText .getText().toString();

                    allLayout.setLayoutTransition(null);
                    allLayout.removeView(editText);
                    allLayout.setLayoutTransition(transition);
                    // 文本合并
                    preEditText.setText(str2 + str1);
                    preEditText.requestFocus();
                    preEditText.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEditText;
                }
            }
        }
    }

    /**
     * 插入一张图片
     */
    public void insertImage(String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString(); //当前的聚焦的文字
        LogUtils.d("当前聚焦的文字："+lastEditStr);
        int cursorIndex = lastFocusEdit.getSelectionStart();  //当前聚焦的index
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim(); //根据聚焦的index裁剪文字
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);  //获得聚焦的编号
        LogUtils.d("allLayout.indexOfChild(lastFocusEdit):"+lastEditIndex);
        if (lastEditStr.length() == 0 || editStr1.length() == 0) {//在文字前面插入图片
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            LogUtils.d("在文字前面插入一个图片");
            addImageViewAtIndex(lastEditIndex,imagePath);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            if (allLayout.getChildCount() - 1 == lastEditIndex
                    || editStr2.length() > 0) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }

            addImageViewAtIndex(lastEditIndex + 1,imagePath);
            lastFocusEdit.requestFocus();
            lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
        }
        hideKeyBoard();
    }

    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在指定位置添加text
     * @param index
     * @param editStr
     */
    private void addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText("", getResources()
                .getDimensionPixelSize(R.dimen.edit_padding_top));
        editText2.setText(editStr);

        // 请注意此处，EditText添加、或删除不触动Transition动画
        allLayout.setLayoutTransition(null);
        allLayout.addView(editText2, index);
        allLayout.setLayoutTransition(transition); // remove之后恢复transition动画
    }

    /**
     * 删除图片
     * @param view
     */
    private void onImageCloseClick(View view){
        if(!transition.isRunning()){
            disappearingImageIndex=allLayout.indexOfChild(view);
            allLayout.removeView(view);
        }
    }

    /**
     * 根据hint和padding创建EditText
     * 为EditText添加各种事件监听
     * @param hint
     * @param paddingTop
     * @return
     */
    private MonsterEditText createEditText(String hint,int paddingTop){
        MonsterEditText editText=(MonsterEditText)inflater.inflate(R.layout.edit_text_layout,null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setPadding(editNormalPadding,paddingTop,editNormalPadding,0);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        return editText;
    }

    /**
     * 创建ImageLayout
     * @return
     */
    private RelativeLayout createImageLayout(){
        RelativeLayout layout=(RelativeLayout)inflater.inflate(R.layout.edit_image_layout,null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        return layout;
    }

    /**
     * 将照片添加到指定位置
     * @param index
     * @param imagePath
     */
    private void addImageViewAtIndex(final int index,String imagePath){
        final RelativeLayout imageLayout =createImageLayout();
        final MonsterImageView imageView=(MonsterImageView)imageLayout.findViewById(R.id.edit_imageView);
        imageView.setUrl(imagePath);
//        Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
//        // 调整imageView的高度
//        int imageHeight = getWidth() * bitmap.getHeight() / bitmap.getWidth();
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, imageHeight);
//        imageView.setLayoutParams(lp);
        if(imagePath.startsWith("/monster")){
            String subPath=imagePath.substring(15);
            String absolutePath="http://39.105.104.164/getimage"+subPath;
            GlideLoaderUtils.loadImageFromNet(absolutePath,imageView,200,200);
        }else {
            GlideLoaderUtils.loadImageFromDisk(imagePath,imageView,200,200);
        }
        // onActivityResult无法触发动画，此处post处理
        allLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                allLayout.addView(imageLayout, index);
            }
        }, 200);
    }
    /**
     * 将照片添加到指定位置
     * @param imagePath
     */
    private void addImageViewAtIndexNoDelay(String imagePath){
        final RelativeLayout imageLayout =createImageLayout();
        final MonsterImageView imageView=(MonsterImageView)imageLayout.findViewById(R.id.edit_imageView);
        imageView.setUrl(imagePath);
        if(imagePath.startsWith("/monster")){
            String subPath=imagePath.substring(15);
            String absolutePath="http://39.105.104.164/getimage"+subPath;
            GlideLoaderUtils.loadImageFromNet(absolutePath,imageView,200,200);
        }else {
            GlideLoaderUtils.loadImageFromDisk(imagePath,imageView,200,200);
        }
        // onActivityResult无法触发动画，此处post处理
        allLayout.addView(imageLayout);
    }
    /**
     * 导出实体类
     * @return
     */
    public DiaryBean BuildEditData(){
        DiaryBean diaryBean=new DiaryBean();
        int num=allLayout.getChildCount();
        StringBuilder content=new StringBuilder();
        int picCount=0;
        for(int index=0;index<num;index++){
            View itemView = allLayout.getChildAt(index);
            if(itemView instanceof MonsterEditText){
                MonsterEditText editText = (MonsterEditText)itemView;
                content.append(editText.getText().toString());
            }else if(itemView instanceof RelativeLayout){
                MonsterImageView imageView = (MonsterImageView) itemView.findViewById(R.id.edit_imageView);
                content.append("img"+imageView.getUrl()+"pic");
                picCount++;
            }
        }
        diaryBean.setPicNum(picCount);
        diaryBean.setIsSyn("0");
        diaryBean.setContent(content.toString());
        Date date=new Date();
        diaryBean.setDate(date.getTime());
        diaryBean.setUserId(UserInfoManager.getUserId());
        return diaryBean;
    }
}
