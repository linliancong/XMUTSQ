package com.example.administrator.xmutsq;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.util.Arrays;
import java.util.List;

public class Login extends StatusBarUT implements View.OnClickListener,TextWatcher{

    private EditText user;
    private EditText pwd;

    private Button login;
    private Button register;
    private Button find;

    private Context context;
    private SharedPUT sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login_user);

        context=getApplicationContext();
        sp=new SharedPUT(context);

        user=findViewById(R.id.login_username);
        pwd=findViewById(R.id.login_pwd);

        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        find=findViewById(R.id.find);

        //文本变化的监听
        user.addTextChangedListener(this);
        pwd.addTextChangedListener(this);

        //按钮点击的监听
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        find.setOnClickListener(this);

        user.setText(sp.getUserName());
        pwd.setText(sp.getPWD());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.login_user;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:

                AVQuery<AVObject> query1 = new AVQuery<>("UserInfo");
                query1.whereEqualTo("UserName",user.getText().toString());
                AVQuery<AVObject> query2 = new AVQuery<>("UserInfo");
                query2.whereEqualTo("Password",pwd.getText().toString());
                AVQuery<AVObject> query = AVQuery.and(Arrays.asList(query1, query2));
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if(list.size()>0) {
                            sp.setIsFirst(false);
                            sp.setID(list.get(0).getObjectId().toString());
                            sp.setUserName(list.get(0).get("UserName").toString());
                            sp.setPWD(list.get(0).get("Password").toString());
                            sp.setName(list.get(0).get("Name").toString());
                            sp.setRole(list.get(0).get("Role").toString());
                            if(list.get(0).get("ImageUrl")!=null) {
                                sp.setImage(list.get(0).get("ImageUrl").toString());
                            }

                            //设置登录标记
                            sp.setIsLogin(true);
                            //登录成功通知更新
                            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
                                sendBroadcast(new Intent("com.example.administrator.MYBROAD"));
                                sendBroadcast(new Intent("com.example.administrator.MYBROAD2"));
                            }
                            else {
                                sendBroadcast(new Intent("com.example.administrator.MYBROAD").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.PersonalCenter$MyBroad")));
                                sendBroadcast(new Intent("com.example.administrator.MYBROAD2").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.MainActivity$MyBroad")));
                            }
                            finish();
                        }else {
                            //登录失败
                            Toast.makeText(context, "用户名或密码有误，请重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.register:
                //注册
                Intent it3=new Intent(context,Register.class);
                startActivity(it3);
                break;
            case R.id.find:
                //找回密码
                Intent it4=new Intent(context,ResetPasswd.class);
                startActivity(it4);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        login.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(user.getText().length()>0&& pwd.getText().length()>0){
            login.setEnabled(true);
        }
    }
}
