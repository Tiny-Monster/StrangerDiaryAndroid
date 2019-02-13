package com.tinymonster.strangerdiary.ui.paint;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.ui.base.BasePresenterActivity;
import com.tinymonster.strangerdiary.ui.widget.MonsterPaintView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TinyMonster on 16/01/2019.
 */

public class PaintActivity extends BasePresenterActivity<PaintPresenter, PaintContract.IPaintView> implements PaintContract.IPaintView, AdapterView.OnItemClickListener {
    private MonsterPaintView paint_paint_view;
    private GridView paint_BottomMenu;

    private int select_paint_size_index = 0;
    private int select_paint_style_index = 0;
    private int select_paint_color_index = 0;

    //菜单资源
    private int[] paintItems = {
            R.drawable.paint_pen_707070,
            R.drawable.paint_eraser_707070,
            R.drawable.paint_size_707070,
            R.drawable.paint_color_707070,
            R.drawable.paint_undo_707070,
            R.drawable.paint_forword_707070,
            R.drawable.paint_delete_707070
    };

    @Override
    protected boolean initToolbar() {
        mToolbar.setTitle(R.string.paint_title);
        return true;
    }

    @Override
    protected void initViews() {
        paint_paint_view = (MonsterPaintView) findViewById(R.id.paint_paint_view);
        paint_BottomMenu = (GridView) findViewById(R.id.paint_BottomMenu);
        InitPaintMenu();
        paint_BottomMenu.setOnItemClickListener(this);
    }

    @Override
    protected PaintPresenter createPresenter() {
        return new PaintPresenter();
    }

    @Override
    protected void dealIntent(Intent intent) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_paint;
    }

    /**
     * 配置绘图菜单
     */
    public void InitPaintMenu() {
        ArrayList<Map<String, Object>> menus = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < paintItems.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", paintItems[i]);
            menus.add(item);
        }
        paint_BottomMenu.setNumColumns(paintItems.length);
        SimpleAdapter mAdapter = new SimpleAdapter(PaintActivity.this, menus, R.layout.paint_menuitem_layout, new String[]{"image"}, new int[]{R.id.item_image});
        paint_BottomMenu.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                paint_paint_view.selectPaintStyle(0);
                break;
            case 1:
                paint_paint_view.selectPaintStyle(1);
                break;
            case 2:
                showPaintSizeDialog(view);
                break;
            case 3:
                showPaintColorDialog(view);
                break;
            case 4:
                paint_paint_view.undo();
                break;
            case 5:
                paint_paint_view.redo();
                break;
            case 6:
                showDeleteDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 设置画笔粗细
     * @param parent
     */
    public void showPaintSizeDialog(View parent) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.custom_dialog);
        alertDialogBuilder.setTitle("选择画笔大小：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintsize, select_paint_size_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_size_index = which;
                paint_paint_view.selectPaintSize(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    /**
     * 设置画笔颜色
     *
     * @param parent
     */
    public void showPaintColorDialog(View parent) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.custom_dialog);
        alertDialogBuilder.setTitle("选择画笔颜色：");

        alertDialogBuilder.setSingleChoiceItems(R.array.paintcolor, select_paint_color_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_color_index = which;
                paint_paint_view.selectPaintColor(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaintActivity.this, R.style.custom_dialog);
        builder.setTitle("清空提示");
        builder.setMessage("您确定要清空所有吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                paint_paint_view.removeAllPaint();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 为toolbar添加menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.paint_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.paint_save:
                //得到调用该Activity的Intent对象
                Intent intent = getIntent();
                Bundle b = new Bundle();
                String path = paint_paint_view.saveBitmap();
                b.putString("paintPath", path);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                PaintActivity.this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
