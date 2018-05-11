package com.example.administrator.xmutsq;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVSMS;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.util.List;

public class Register extends StatusBarUT implements View.OnClickListener,TextWatcher{

    private EditText phone;
    private EditText user;
    private EditText pwd1;
    private EditText pwd2;
    private EditText code;
    private Button regi;
    private LinearLayout back;
    private Button sendCode;

    private SharedPUT sp;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.register_user);

        init();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.register_user;
    }

    private void init() {
        context=Register.this;
        sp=new SharedPUT(context);
        phone =findViewById(R.id.regi_phone);
        user=findViewById(R.id.regi_username);
        pwd1=findViewById(R.id.regi_pwd1);
        pwd2=findViewById(R.id.regi_pwd2);
        code=findViewById(R.id.regi_code);
        regi=findViewById(R.id.regi_btn);
        back=findViewById(R.id.regi_back);
        sendCode =findViewById(R.id.regi_imgcode);

        regi.setOnClickListener(this);
        sendCode.setOnClickListener(this);
        back.setOnClickListener(this);
        phone.addTextChangedListener(this);
        user.addTextChangedListener(this);
        pwd1.addTextChangedListener(this);
        pwd2.addTextChangedListener(this);
        code.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regi_btn:
                //判断验证码
                    //判断密码
                    if (pwd1.getText().toString().equals(pwd2.getText().toString())) {
                        //判断手机
                        AVQuery<AVObject> query = new AVQuery<>("UserInfo");
                        query.whereEqualTo("Phone", phone.getText().toString());
                        query.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if(list.size()>0){
                                    Toast.makeText(context, "手机已存在，重新输入", Toast.LENGTH_SHORT).show();
                                }else {
                                    AVSMS.verifySMSCodeInBackground(code.getText().toString(), phone.getText().toString(), new AVMobilePhoneVerifyCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (null == e) {
                                                AVObject testObject = new AVObject("UserInfo");
                                                testObject.put("UserName",phone.getText().toString());
                                                testObject.put("Password",pwd1.getText().toString());
                                                testObject.put("Phone", phone.getText().toString());
                                                //设置用户的默认角色为1
                                                testObject.put("Role","1");
                                                testObject.put("Name","");
                                                testObject.put("Sex", "");
                                                testObject.put("Email","");
                                                testObject.put("QQ", "");
                                                testObject.put("ImageUrl","");
                                                testObject.put("Remark","");
                                                testObject.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(AVException e) {
                                                        if(e == null){
                                                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                                                            sp.setUserName(phone.getText().toString());
                                                            /*Intent intent=new Intent(context,RegisterInfo.class);
                                                            startActivity(intent);*/
                                                            finish();
                                                        }else{
                                                            Toast.makeText(context, "注册失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(context, "验证码输入有误，请重新输入", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }
                        });
                    }else {
                            Toast.makeText(context, "两次输入的密码不匹配，请重新输入", Toast.LENGTH_SHORT).show();

                        }

                break;
            case R.id.regi_imgcode:
                AVSMSOption option = new AVSMSOption();
                option.setTtl(10);                     // 验证码有效时间为 10 分钟
                option.setApplicationName("XMUTSQ");
                option.setOperation("注册");
                AVSMS.requestSMSCodeInBackground(phone.getText().toString(), option, new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null == e) {
                            Toast.makeText(context,"验证码发送成功，请注意查收",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context,"验证码发送失败，请检查你的手机号是否正确",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.regi_back:
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        regi.setEnabled(false);
        sendCode.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(pwd1.getText().length()>0&&pwd2.getText().length()>0&& phone.getText().length()>0&&code.getText().length()>0){
            regi.setEnabled(true);
        }
        if(phone.getText().length()>0){
            sendCode.setEnabled(true);
        }

    }
}
