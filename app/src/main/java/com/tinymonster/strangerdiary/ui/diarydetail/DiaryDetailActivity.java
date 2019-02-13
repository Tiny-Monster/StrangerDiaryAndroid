package com.tinymonster.strangerdiary.ui.diarydetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.ui.base.BaseActivity;
import com.tinymonster.strangerdiary.ui.edit.EditActivity;
import com.tinymonster.strangerdiary.ui.widget.RichBoard;
import com.tinymonster.strangerdiary.ui.widget.ScanImageView;
import com.tinymonster.strangerdiary.utils.Const;

import java.util.concurrent.ExecutionException;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class DiaryDetailActivity extends BaseActivity{
    private RichBoard diary_detail_board;
    private FloatingActionButton diary_detail_edit;
    private DiaryBean diaryBean;
    private ScanImageView scanImageView;
    @Override
    protected void dealIntent(Intent intent) {
        diaryBean=(DiaryBean) intent.getSerializableExtra(Const.BUNDLE_KEY.OBJ);
    }

    @Override
    protected boolean initToolbar() {
        return true;
    }

    @Override
    protected void initViews() {
        diary_detail_board=(RichBoard)findViewById(R.id.diary_detail_board);
        diary_detail_edit=(FloatingActionButton)findViewById(R.id.diary_detail_edit);
        diary_detail_board.setData(diaryBean);
        scanImageView=new ScanImageView(DiaryDetailActivity.this);
        scanImageView.setUrl(diaryBean.getimagePaths());
        diary_detail_board.setOnImageClickListener(new RichBoard.BoardImageClickListener() {
            @Override
            public void onImageClick(int position) {
                try {
                    scanImageView.create(position);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        diary_detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DiaryDetailActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Const.BUNDLE_KEY.OBJ, diaryBean);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_diary_detail;
    }

    @Override
    protected String registerEvent() {
        return null;
    }

    @Override
    protected void receiveEvent(Object object) {

    }
}
