package com.tinymonster.strangerdiary.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.haha.perflib.Main;
import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.bean.BaseBean;
import com.tinymonster.strangerdiary.bean.DiaryBean;
import com.tinymonster.strangerdiary.event.Event;
import com.tinymonster.strangerdiary.event.EventBus;
import com.tinymonster.strangerdiary.net.RxRetrofit;
import com.tinymonster.strangerdiary.permission.PermissionHelper;
import com.tinymonster.strangerdiary.permission.PermissionInterface;
import com.tinymonster.strangerdiary.ui.about.AboutActivity;
import com.tinymonster.strangerdiary.ui.base.BaseActivity;
import com.tinymonster.strangerdiary.ui.diary.DiaryFragment;
import com.tinymonster.strangerdiary.ui.edit.EditActivity;
import com.tinymonster.strangerdiary.ui.feedback.FeedbackActivity;
import com.tinymonster.strangerdiary.ui.login.LoginActivity;
import com.tinymonster.strangerdiary.ui.set.SetActivity;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.DaoUtils;
import com.tinymonster.strangerdiary.utils.LightStatusBarUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;
import com.tinymonster.strangerdiary.utils.UserInfoManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener,PermissionInterface{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView mNameView;
    private ImageView mAvatarView;
    private Button[] buttons;
    private Fragment[] fragments;
    private int currentPosition;
    private int index;
    private long mExitTime;
    private FloatingActionButton main_add_diary;
    private PermissionHelper permissionHelper;
    private static final int PermissionCode=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mToggle.syncState();
        mDrawerLayout.addDrawerListener(mToggle);
        mNavigationView.setItemIconTintList(null);
        //侧滑菜单点击
        mNavigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        //初始化NavigationHeader
        initNavigationHeaderView();
        LightStatusBarUtils.setLightStatusBar(this,true);//设置透明状态栏
        main_add_diary=(FloatingActionButton)findViewById(R.id.main_add_diary);
        main_add_diary.setOnClickListener(this);
        permissionHelper=new PermissionHelper(this,this);
        //初始化fragment
        initFragments();
    }

    private void initFragments() {
        fragments=new Fragment[]{new DiaryFragment()};
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_container,fragments[0]).show(fragments[0]).commitAllowingStateLoss();
    }

    private void initNavigationHeaderView() {
        View mHeaderView = mNavigationView.getHeaderView(0);
        mAvatarView = (ImageView) mHeaderView.findViewById(R.id.img_avatar);
        mNameView = (TextView) mHeaderView.findViewById(R.id.tv_name);
    }

    @Override
    protected void dealIntent(Intent intent) {

    }

    @Override
    protected boolean initToolbar() {
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        return true;
    }

    @Override
    protected void initViews() {
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        mNavigationView=(NavigationView)findViewById(R.id.navigation_view);
        buttons=new Button[2];
        buttons[0] = (Button)findViewById(R.id.btn_diary);
        buttons[1] = (Button)findViewById(R.id.btn_stranger);
        buttons[0].setOnClickListener(this);
        buttons[1].setOnClickListener(this);
        buttons[0].setSelected(true);

        for(int i=0;i<buttons.length;i++){
            if(i!=currentPosition){
                buttons[i].setScaleX(0.9f);
                buttons[i].setScaleY(0.9f);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String registerEvent() {
        return null;
    }

    @Override
    protected void receiveEvent(Object object) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_diary:
                index=0;
                break;
            case R.id.btn_stranger:
                index=1;
                break;
            case R.id.main_add_diary:
                permissionHelper.requestPermissions();
                break;
        }
        showCurrentFragment(index);
    }

    private void showCurrentFragment(int index){
        if (currentPosition != index) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragments[currentPosition]);
            if (!fragments[index].isAdded()) {
                ft.add(R.id.container, fragments[index]);
            }
            ft.show(fragments[index]).commitAllowingStateLoss();
            buttons[currentPosition].setSelected(false);
            buttons[index].setSelected(true);
            scaleView();//按钮动画
            currentPosition = index;
            setCurrentTitle();//设置title文字
        }
    }

    private void scaleView(){
        buttons[currentPosition].animate().scaleX(0.9f).scaleY(0.9f)
                .setDuration(150).start();
        buttons[index].animate().scaleY(1.0f).scaleY(1.0f)
                .setDuration(150).start();
    }

    private void setCurrentTitle(){
        if(currentPosition==0){
            mToolbar.setTitle(R.string.diary);
        }else if(currentPosition==1){
            mToolbar.setTitle(R.string.stranger);
        }
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_set:
                    Intent set_intent=new Intent(MainActivity.this, SetActivity.class);
                    startActivity(set_intent);
                    break;
                case R.id.menu_recovery_data:
                    if(UserInfoManager.isLogin()){
                        RxRetrofit.Api().downloadAllDiary(UserInfoManager.getUserId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ResourceObserver<BaseBean<List<DiaryBean>>>() {
                                    @Override
                                    public void onNext(BaseBean<List<DiaryBean>> listBaseBean) {
                                        List<DiaryBean> diaryBeans=listBaseBean.data;
                                        DaoUtils.DiaryDaoUtils.clearAllDiary();
                                        DaoUtils.DiaryDaoUtils.insertDiarys(diaryBeans);
                                        Event event=new Event(Event.Type.LIST, new Object());
                                        EventBus.getInstance().postEvent(Const.EVENT_ACTION.REFRESH_DATA,event);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }else {
                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.menu_feedback:
                    Intent feedback_intent=new Intent(MainActivity.this, FeedbackActivity.class);
                    startActivity(feedback_intent);
                    break;
                case R.id.menu_sponsor:
                    break;
                case R.id.menu_about:
                    Intent aboutIntent=new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(aboutIntent);
                    break;
                case R.id.menu_login_logout:
                    if(UserInfoManager.isLogin()){
                        UserInfoManager.saveIsLogin(false);
                        UserInfoManager.removeUserInfo();
                        Intent loginIntent=new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }else {
                        Intent feedbackIntent=new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(feedbackIntent);
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            //如果drawLayout是展开状态，关闭drawLayout
            if(mDrawerLayout.isDrawerOpen(Gravity.START)){
                mDrawerLayout.closeDrawer(Gravity.START);
                return true;
            }
            if(System.currentTimeMillis()-mExitTime<2000){
                finish();
            }else {
                mExitTime=System.currentTimeMillis();
                ToastUtils.showToast(AppContext.getContext(),"请再点击一次退出程序");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        if(UserInfoManager.isLogin()){
            mNavigationView.getMenu().getItem(5).setTitle(AppContext.getContext().getString(R.string.main_logout));
        }else {
            mNavigationView.getMenu().getItem(5).setTitle(AppContext.getContext().getString(R.string.main_login));
        }
        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissionHelper.requestPermissionsResult(requestCode,permissions,grantResults)){
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getPermissionsRequestCode() {
        return PermissionCode;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }

    @Override
    public void requestPermissionsSuccess() {
        if (!UserInfoManager.isLogin()){
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else {
            Intent intent=new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void requestPermissionsFail() {
        ToastUtils.showToast(MainActivity.this,R.string.permission_deny);
    }
}
