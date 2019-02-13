package com.tinymonster.strangerdiary.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tinymonster.strangerdiary.R;
import com.tinymonster.strangerdiary.application.AppContext;
import com.tinymonster.strangerdiary.core.presenter.BasePresenter;
import com.tinymonster.strangerdiary.ui.base.BasePresenterActivity;
import com.tinymonster.strangerdiary.ui.widget.NoEmojEditText;
import com.tinymonster.strangerdiary.utils.LightStatusBarUtils;
import com.tinymonster.strangerdiary.utils.TimeCount;
import com.tinymonster.strangerdiary.utils.ToastUtils;

/**
 * Created by TinyMonster on 11/01/2019.
 */

public class RegisterActivity extends BasePresenterActivity<RegisterPresenter,RegisterContract.IRegisterView> implements RegisterContract.IRegisterView ,
        View.OnClickListener{
    private EditText account;
    private EditText smsCode;
    private NoEmojEditText password;
    private NoEmojEditText pswVerify;
    private TextView register;
    private Button get_verify_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LightStatusBarUtils.setLightStatusBar(this,true);//设置透明状态栏
    }

    @Override
    protected void initViews() {
        account=(EditText)findViewById(R.id.register_account);
        smsCode=(EditText)findViewById(R.id.register_smscode);
        password=(NoEmojEditText)findViewById(R.id.register_password);
        pswVerify=(NoEmojEditText)findViewById(R.id.register_verify_password);
        register=(TextView)findViewById(R.id.register);
        get_verify_code=(Button)findViewById(R.id.register_getcode);
        register.setOnClickListener(this);
        get_verify_code.setOnClickListener(this);
    }

    @Override
    public String getAccount() {
        return account.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public String getPswConfirm() {
        return pswVerify.getText().toString();
    }

    @Override
    public String getVerifyCode() {
        return smsCode.getText().toString();
    }

    @Override
    public void showResult(String msg) {
        ToastUtils.showToast(AppContext.getContext(),msg);
    }

    @Override
    public void onRegisterSuccess() {
        //ToDo 跳转到主页面
    }

    @Override
    public void onRegisterFail(String msg) {
        ToastUtils.showToast(AppContext.getContext(),msg);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    protected void dealIntent(Intent intent) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_getcode:
                mPresenter.getVerifyCode();
                new TimeCount(get_verify_code,60*1000,1000).start();
                break;
            case R.id.register:
                mPresenter.register();
                break;
        }
    }
}
