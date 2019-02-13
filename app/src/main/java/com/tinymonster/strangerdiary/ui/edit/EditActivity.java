package com.tinymonster.strangerdiary.ui.edit;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.event.Event;
import com.tinymonster.strangerdiary.event.EventBus;
import com.tinymonster.strangerdiary.ui.base.BasePresenterActivity;
import com.tinymonster.strangerdiary.ui.paint.PaintActivity;
import com.tinymonster.strangerdiary.ui.widget.RichEditor;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.File2PartsUtils;
import com.tinymonster.strangerdiary.utils.FilePathUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.TimeUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TinyMonster on 15/01/2019.
 */

public class EditActivity extends BasePresenterActivity<EditPresenter, EditContract.IEditView> implements EditContract.IEditView,AdapterView.OnItemClickListener {
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 1022;
    private File mCurrentPhotoFile;// 照相机拍照得到的图片
    private RichEditor edit_rich_edit;
    private GridView edit_BottomMenu;
    private DiaryBean saveDiary;
    //菜单资源
    private int[] editItems = {
            R.drawable.edit_camera_707070,
            R.drawable.edit_pic_707070,
            R.drawable.edit_pen_707070
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    @Override
    protected void initViews() {
        edit_BottomMenu=(GridView)findViewById(R.id.edit_BottomMenu);
        edit_rich_edit = (RichEditor) findViewById(R.id.edit_rich_edit);
        InitPaintMenu();
        edit_BottomMenu.setOnItemClickListener(this);
        if(saveDiary!=null){
            edit_rich_edit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    edit_rich_edit.init(saveDiary);
                }
            },200);
        }
        //默认的初始化
    }

    public void InitPaintMenu() {
        ArrayList<Map<String, Object>> menus = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < editItems.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", editItems[i]);
            menus.add(item);
        }
        edit_BottomMenu.setNumColumns(editItems.length);
        SimpleAdapter mAdapter = new SimpleAdapter(EditActivity.this, menus, R.layout.paint_menuitem_layout, new String[]{"image"}, new int[]{R.id.item_image});
        edit_BottomMenu.setAdapter(mAdapter);
    }

    @Override
    public void onSaveDiarySuccess() {
        EditActivity.this.finish();
    }

    @Override
    public RichEditor getEditor() {
        return edit_rich_edit;
    }

    @Override
    public void postEvent(DiaryBean diaryBean) {
        Event event=new Event(Event.Type.INSERT,diaryBean);
        EventBus.getInstance().postEvent(Const.EVENT_ACTION.REFRESH_DATA,event);
    }

    @Override
    protected EditPresenter createPresenter() {
        return new EditPresenter();
    }

    @Override
    protected void dealIntent(Intent intent) {
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            saveDiary=(DiaryBean) bundle.getSerializable(Const.BUNDLE_KEY.OBJ);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit;
    }

    @Override
    protected boolean initToolbar() {
        mToolbar.setTitle(TimeUtils.getFormatTime("yyyy-MM-dd HH:mm"));
        return true;
    }

    /**
     * 打开相机拍照
     */
    protected void openCamera() {
        try {
            // Launch camera to take photo for selected contact
            Const.FILE_PATH.PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(Const.FILE_PATH.PHOTO_DIR, mPresenter.getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = mPresenter.getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, Const.EDIT_ACTIVITY.CALL_FOR_TAKE_PIC);
        } catch (ActivityNotFoundException e) {
            ToastUtils.showToast(AppContext.getContext(), R.string.ActivityNotFoundException);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Const.EDIT_ACTIVITY.CALL_FOR_SELECT_PIC) {
                Uri uri = data.getData();  //根据返回的URL获取绝对路径
                FilePathUtils filePathUtils=new FilePathUtils();
                mPresenter.insertBitmap(filePathUtils.getRealFilePath(uri,EditActivity.this));  //根据绝对路径插入图片
            } else if (requestCode == Const.EDIT_ACTIVITY.CALL_FOR_TAKE_PIC) {
                mPresenter.insertBitmap(mCurrentPhotoFile.getAbsolutePath());
            } else if (requestCode == Const.EDIT_ACTIVITY.CALL_FOR_PAINT) {
                //接收返回信息
                Bundle extras = null;
                extras = data.getExtras();
                //接收返回的路径
                String path = extras.getString("paintPath");
                mPresenter.insertBitmap(path);
            }
        }
    }

    /**
     * 初始化toolbar菜单栏
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_save:
                mPresenter.saveDiary();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                openCamera();
                break;
            case 1:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, Const.EDIT_ACTIVITY.CALL_FOR_SELECT_PIC);
                break;
            case 2:
                Intent intent1 = new Intent(EditActivity.this, PaintActivity.class);
                startActivityForResult(intent1, Const.EDIT_ACTIVITY.CALL_FOR_PAINT);
                break;
        }
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtils.showToast(AppContext.getContext(),msg);
    }

}
