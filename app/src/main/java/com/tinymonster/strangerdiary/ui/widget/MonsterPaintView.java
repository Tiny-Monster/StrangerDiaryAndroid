package com.tinymonster.strangerdiary.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.utils.UserInfoManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by TinyMonster on 16/01/2019.
 */

public class MonsterPaintView extends View {

    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;
    private Paint mPaint;
    private ArrayList<DrawPath> savePath;
    private ArrayList<DrawPath> deletePath;
    private DrawPath dp;
    private float mX, mY;  //当前x坐标  y坐标
    private static final float TOUCH_TOLERANCE = 4;  //移动的时候最小分辨率
    private int bitmapWidth;
    private int bitmapHeight;
    private boolean isMoving = false;
    //画笔颜色
    private int[] paintColor = {
            Color.RED,
            Color.BLUE,
            Color.BLACK,
            Color.GREEN,
            Color.YELLOW,
            Color.CYAN,
            Color.LTGRAY
    };

    private int currentColor = Color.RED;
    private int currentSize = 5;
    private int currentStyle = 1;  //橡皮擦或者是画笔

    public MonsterPaintView(Context c) {
        super(c);
        //得到屏幕的分辨率
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);
        bitmapWidth = dm.widthPixels;
        bitmapHeight = dm.heightPixels - 2 * 45;
        initCanvas();//初始化画布与画笔
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }

    public MonsterPaintView(Context c, AttributeSet attrs) {
        super(c,attrs);
        //得到屏幕的分辨率
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) c).getWindowManager().getDefaultDisplay().getMetrics(dm);
        bitmapWidth = dm.widthPixels;
        bitmapHeight = dm.heightPixels - 2 * 45;
        initCanvas();
        savePath = new ArrayList<DrawPath>();  //DrawPath  画笔 path
        deletePath = new ArrayList<DrawPath>();
    }

    /**
     * 初始化画布
     * 画笔
     */
    public void initCanvas(){
        setPaintStyle();//设置画笔粗细  颜色
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        //画布大小
        mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
                Bitmap.Config.RGB_565);
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中

        mCanvas.drawColor(Color.WHITE);   //画布背景色  白色
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }
    //设置画笔样式
    public void setPaintStyle(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(currentSize);  //粗细
        if(currentStyle == 1)  //橡皮擦或者是画笔
            mPaint.setColor(currentColor);  //颜色
        else{
            mPaint.setColor(Color.WHITE);
        }
    }

    /**
     * 每次调用invalidate()就会重新绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);     //将之前的bitmap绘制出来
        if(mPath != null){
            // 绘制图案
            canvas.drawPath(mPath, mPaint);
            //移动时，显示画笔图标
            if(this.isMoving && currentColor != Color.WHITE){  //绘制橡皮擦？？
                //绘制画笔
                Bitmap pen = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.pen);
                canvas.drawBitmap(pen, this.mX, this.mY - pen.getHeight(),
                        new Paint(Paint.DITHER_FLAG));  //绘制画笔样式
            }
        }
    }
    //路径对象
    class DrawPath{
        Path path;
        Paint paint;
    }

    //设置画笔样式
    public void selectPaintStyle(int which){
        if(which == 0){
            currentStyle = 1;
            setPaintStyle();
        }
        //当选择的是橡皮擦时，设置颜色为白色
        if(which == 1){
            currentStyle = 2;
            setPaintStyle();
            mPaint.setStrokeWidth(20);
        }
    }

    //选择画笔大小
    public void selectPaintSize(int which){
        int size =Integer.parseInt(this.getResources().getStringArray(R.array.paintsize)[which]);
        currentSize = size;
        setPaintStyle();
    }
    //设置画笔颜色
    public void selectPaintColor(int which){
        currentColor = paintColor[which];
        setPaintStyle();
    }

    /**
     * 撤销的核心思想就是将画布清空，
     * 删除List中保存的最后一个path,然后重新绘制画布
     */
    public void undo(){
        System.out.println(savePath.size()+"--------------");
        if(savePath != null && savePath.size() > 0){
            //调用初始化画布函数以清空画布
            initCanvas();//清空画布
            //将路径保存列表中的最后一个元素删除 ,并将其保存在路径删除列表中
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            //将路径保存列表中的路径重绘在画布上
            Iterator<DrawPath> iter = savePath.iterator();		//重复保存
            while (iter.hasNext()) {
                DrawPath dp = iter.next();
                mCanvas.drawPath(dp.path, dp.paint);
            }
            invalidate();// 刷新
        }
    }
    /**
     * 恢复的核心思想就是将撤销的路径保存到另外一个列表里面(栈)，
     * 然后从redo的列表里面取出最顶端对象，
     * 画在画布上面即可
     */
    public void redo(){
        if(deletePath.size() > 0){
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            mCanvas.drawPath(dp.path, dp.paint);
            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }

    /**
     * 将画布清空（初始化）
     * 清空两个list
     */
    public void removeAllPaint(){
        //调用初始化画布函数以清空画布
        initCanvas();
        invalidate();//刷新
        savePath.clear();
        deletePath.clear();
    }

    /**
     * 保存图片  返回图片保存的路径
     * @return
     */
    public String saveBitmap(){
        //获得系统当前时间，并以该时间作为文件名
        String   str   =  "DIARY_IMG_"+ UserInfoManager.getUserId()+"_"+System.currentTimeMillis();
        String paintPath = "";
        str = str + "paint.png";
        String dataPath= AppContext.getContext().getFilesDir().getPath()+"/photos";
        File dir = new File(dataPath);
        File file = new File(dataPath,str);
        if (!dir.exists()) {
            dir.mkdir();
        }
        else{
            if(file.exists()){
                file.delete();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            paintPath = file.getPath();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paintPath;
    }

    /**
     * 开始绘制调用
     * @param x
     * @param y
     */
    private void touch_start(float x, float y) {
        mPath.reset();//清空path
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        this.isMoving = false;
    }

    /**
     * 绘制过程中调用
     * @param x
     * @param y
     */
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//            mPath.quadTo(mX, mY, x, y);
            mPath.quadTo((x + mX)/2, (y + mY)/2, x, y);  //绘制圆滑曲线
            mX = x;
            mY = y;
            this.isMoving = true;
        }
        mX = x;
        mY = y;
        this.isMoving = true;
    }

    /**
     * 结束绘制调用
     * @param x
     * @param y
     */
    private void touch_up(float x,float y) {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        savePath.add(dp);
        mPath = null;
        this.isMoving = false;
    }

    /**
     * 触摸点击事件  绘制  连线
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = mPaint;
                touch_start(x, y);
                invalidate(); //刷新
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);  //绘制曲线  刷新
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x,y);  //结束绘制
                invalidate();
                break;

        }
        return true;
    }
}
