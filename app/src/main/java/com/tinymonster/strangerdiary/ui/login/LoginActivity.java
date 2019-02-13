package com.tinymonster.strangerdiary.ui.login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.ui.base.BasePresenterActivity;
import com.tinymonster.strangerdiary.ui.main.MainActivity;
import com.tinymonster.strangerdiary.ui.register.RegisterActivity;
import com.tinymonster.strangerdiary.ui.widget.JellyInterpolator;
import com.tinymonster.strangerdiary.utils.Const;
import com.tinymonster.strangerdiary.utils.LightStatusBarUtils;
import com.tinymonster.strangerdiary.utils.LogUtils;
import com.tinymonster.strangerdiary.utils.ToastUtils;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public class LoginActivity extends BasePresenterActivity<LoginPresenter,LoginContract.ILoginView> implements LoginContract.ILoginView ,
        View.OnClickListener{
    private EditText account;
    private EditText password;
    private TextView login;
    private CheckBox showPassword;
    private ImageView clearAccount;
    private ImageView clearPassword;
    private LinearLayout inputLayout;
    private LinearLayout progressLayout;
    private float mWidth, mHeight;
    private LinearLayout login_input_layout_account;
    private LinearLayout login_input_layout_psw;
    private View login_input_gap;
    private int mFrom=0;
    private ImageView login_back;
    private TextView login_register;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LightStatusBarUtils.setLightStatusBar(this,true);//设置透明状态栏
    }

    @Override
    protected void initViews() {
        super.initViews();
        account=(EditText)findViewById(R.id.login_account);
        password=(EditText)findViewById(R.id.login_password);
        login=(TextView)findViewById(R.id.login_login);
        showPassword=(CheckBox)findViewById(R.id.login_showPwd);
        clearPassword=(ImageView)findViewById(R.id.login_password_clear);
        clearAccount=(ImageView)findViewById(R.id.login_account_clear);
        inputLayout=(LinearLayout)findViewById(R.id.login_input_layout);
        progressLayout=(LinearLayout)findViewById(R.id.login_progress);
        login_input_layout_account=(LinearLayout)findViewById(R.id.login_input_layout_account);
        login_input_layout_psw=(LinearLayout)findViewById(R.id.login_input_layout_psw);
        login_input_gap=(View)findViewById(R.id.login_input_gap);
        login.setOnClickListener(this);
        login_back=(ImageView)findViewById(R.id.login_back);
        login_register=(TextView)findViewById(R.id.login_register);
        login_back.setOnClickListener(this);
        login_register.setOnClickListener(this);
        clearPassword.setOnClickListener(this);
        clearAccount.setOnClickListener(this);
        progressLayout.setVisibility(View.GONE);
        login.setEnabled(false);
        //监听事件  账号不为空，才可点击清空账号。账号密码都不为空，才可以点击登陆
        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(account.getText().toString().length()>0){
                    clearAccount.setVisibility(View.VISIBLE);
                    if(password.getText().toString().length()>0){
                        login.setEnabled(true);
                    }else {
                        login.setEnabled(false);
                    }
                }else {
                    clearAccount.setVisibility(View.INVISIBLE);
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //监听事件  账号框获得焦点，隐藏密码框的清除文本按钮
        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    clearPassword.setVisibility(View.INVISIBLE);
                    if(account.getText().toString().length()>0){
                        clearAccount.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //密码框逻辑同账号框
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.getText().toString().length()>0){
                    clearPassword.setVisibility(View.VISIBLE);
                    if(account.getText().toString().length()>0){
                        login.setEnabled(true);
                    }else {
                        login.setEnabled(false);
                    }
                }else {
                    clearPassword.setVisibility(View.INVISIBLE);
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    clearAccount.setVisibility(View.INVISIBLE);
                    if(account.getText().toString().length()>0){
                        clearPassword.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //隐藏或展示密码
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setBackgroundResource(R.drawable.showpwd);
                }else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setBackgroundResource(R.drawable.hidepwd);
                }
                password.setSelection(password.getText().toString().length());
            }
        });
    }

    @Override
    public String getUserName() {
        return account.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void showResult(String msg) {
        LogUtils.d(Thread.currentThread().getName());
        Log.e("LoginActivity","返回数据："+msg);
//        Toast.makeText(AppContext.getContext(),msg,Toast.LENGTH_LONG).show();
        ToastUtils.showToast(AppContext.getContext(),msg);
    }

    @Override
    public void onComplete() {
        recovery();
    }

    @Override
    public void dealLoginSuccess() {
        if(mFrom!=1){
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        LoginActivity.this.finish();
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void dealIntent(Intent intent) {
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            mFrom=bundle.getInt(Const.INTENT_TAG.getFrom);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_login:
                mWidth=progressLayout.getHeight();
                mHeight=progressLayout.getHeight();
                login_input_gap.setVisibility(View.INVISIBLE);
                login_input_layout_account.setVisibility(View.INVISIBLE);
                login_input_layout_psw.setVisibility(View.INVISIBLE);
                inputLayout.setVisibility(View.INVISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                inputAnimator(inputLayout,mWidth,mHeight);
                break;
            case R.id.login_account_clear:
                clearText(account);
                break;
            case R.id.login_password_clear:
                clearText(password);
                break;
            case R.id.login_back:
                LoginActivity.this.finish();
                break;
            case R.id.login_register:
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(Const.INTENT_TAG.setFrom,1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    /**
     * 清空控件文本
     */
    private void clearText(EditText edit) {
        edit.setText("");
    }

    /**
     *
     * @param view
     * @param w
     * @param h
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view,
                "scaleX", 1f, 0.5f);
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
//        set.playTogether(animator, animator2);
        set.play(animator2);
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                inputLayout.clearAnimation();
                inputLayout.setVisibility(View.INVISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                //TODO 登陆
                mPresenter.login();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        set.start();
    }


    public void recovery() {
        progressLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.VISIBLE);
        login_input_gap.setVisibility(View.VISIBLE);
        login_input_layout_account.setVisibility(View.VISIBLE);
        login_input_layout_psw.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inputLayout.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        inputLayout.setLayoutParams(params);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(inputLayout, "scaleX", 0.5f,1f );
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());
        animator2.start();
    }
}
