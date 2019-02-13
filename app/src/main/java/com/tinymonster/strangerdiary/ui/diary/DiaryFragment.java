package com.tinymonster.strangerdiary.ui.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.event.Event;
import com.tinymonster.strangerdiary.ui.diarydetail.DiaryDetailActivity;
import com.tinymonster.strangerdiary.ui.adapter.BaseListAdapter;
import com.tinymonster.strangerdiary.ui.adapter.DiaryListAdapter;
import com.tinymonster.strangerdiary.ui.base.BaseListFragment;
import com.tinymonster.strangerdiary.ui.widget.ScanImageView;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class DiaryFragment extends BaseListFragment<DiaryPresenter,DiaryContract.IDiaryView,DiaryBean> implements DiaryContract.IDiaryView,OnDiaryItemClickListener{
    private int id;//文章id
    private int position;
    private ScanImageView scanImageView;

    @Override
    public int getDiaryId() {
        return id;
    }


    @Override
    protected DiaryPresenter createPresenter() {
        return new DiaryPresenter();
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    protected boolean isCanLoadMore() {
        return true;
    }

    @Override
    protected View initHeaderView() {
       return null;
    }

    @Override
    protected void loadDatas() {
        mPresenter.getDataFromDisk();
    }

    @Override
    protected BaseListAdapter getListAdapter() {
        DiaryListAdapter diaryListAdapter=new DiaryListAdapter(this);
        diaryListAdapter.setImageClickListener(new DiaryListAdapter.dialog_item_pic_click() {
            @Override
            public void onImageClick(int startPosition, List<String> urls) {
                if(scanImageView==null){
                    scanImageView=new ScanImageView(DiaryFragment.this.getActivity());
                }
                scanImageView.setUrl(urls);
                try {
                    scanImageView.create(startPosition);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return diaryListAdapter;
    }

    @Override
    protected void synData() {
        mPresenter.synData();
    }

    /**
     * item点击
     */
    @Override
    public void onItemClick(DiaryBean bean) {
        Intent intent = new Intent(getActivity(), DiaryDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.BUNDLE_KEY.OBJ, bean);
        bundle.putString(Const.BUNDLE_KEY.SAVE_TYPE, bean.getIsSyn());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 设置日记数据
     * @param data
     */
    @Override
    public void setData(List<DiaryBean> data) {
        mListData.addAll(data);
    }

    /**
     * item长按
     */
    @Override
    public void onItemLongClick(DiaryBean bean) {
        ToastUtils.showToast(AppContext.getContext(),"长按"+bean.toString());
    }

    @Override
    public void onItemSynClick(DiaryBean bean) {
        //TODO 更新BEAN

    }

    /**
     * UI更新  item的局部刷新
     * 同步成功
     */
    @Override
    public void notifyItemData(DiaryBean diaryBean) {
        LogUtils.d("notifyItemData");
        for(int i=0;i<mListData.size();i++){
            if(mListData.get(i).getDisk_id()==diaryBean.getDisk_id()){
                mListData.get(i).setId(diaryBean.getId());
                mListData.get(i).setIsSyn(diaryBean.getIsSyn());
                mListAdapter.notifyItemDataChanged(i,mRecyclerView);
                break;
            }
        }
    }

    private void notifyItemInsert(){

    }

    /**
     * @param object
     */
    @Override
    protected void receiveEvent(Object object) {
        Event mEvent = (Event) object;
        LogUtils.d("收到事件");
        if (mEvent.type == Event.Type.INSERT) {
            refreshData();
        }else if(mEvent.type==Event.Type.ITEM){

        } else{
            refreshData();
        }
    }

    /**
     * 注册事件总线
     * @return
     */
    @Override
    protected String registerEvent() {
        return Const.EVENT_ACTION.REFRESH_DATA;
    }

}
