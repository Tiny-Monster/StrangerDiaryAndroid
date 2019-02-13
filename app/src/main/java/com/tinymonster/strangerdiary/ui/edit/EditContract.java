package com.tinymonster.strangerdiary.ui.edit;

import android.content.Intent;
import android.net.Uri;

import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.core.view.IView;
import com.tinymonster.strangerdiary.ui.widget.RichEditor;

import java.io.File;
import java.util.List;

/**
 * Created by TinyMonster on 15/01/2019.
 */

public interface EditContract {

    interface IEditPresenter{

        void saveDiary();

        String getPhotoFileName();

        Intent getTakePickIntent(File file);

        void insertBitmap(String path);

        List<String> getPicPathList(String content);
    }

    interface IEditView extends IView{


        void onSaveDiarySuccess();

        RichEditor getEditor();

        void postEvent(DiaryBean diaryBean);
    }
}
