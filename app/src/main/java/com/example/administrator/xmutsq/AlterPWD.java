package com.example.administrator.xmutsq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.ut.SharedPUT;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class AlterPWD extends AppCompatActivity implements TextWatcher,View.OnClickListener{

    private LinearLayout back;
    private Button alter;
    private EditText oldpwd;
    private EditText newpwd;
    private EditText newpwd2;

    private SharedPUT sp;
    private Context context;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    private View view;

    private TextView txt;

    private long state=0;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    //密码更改成功
                    dialog.show();
                    txt= view.findViewById(R.id.txt_hint2);
                    txt.setText("密码修改成功，请返回重新登录。");
                    break;
                case 0x002:
                    //更改密码失败
                    dialog.show();
                    txt=view.findViewById(R.id.txt_hint2);
                    txt.setText("密码修改失败，请稍后再试。");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_passwd);


        init();

        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==1){
                    Intent intent=new Intent(context,Login.class);
                    startActivity(intent);
                    finish();
                }
                dialog.dismiss();
            }
        });


    }

    private void init() {
        context =AlterPWD.this;
        sp=new SharedPUT(context);

        //提示信息相关设置
        builder=new AlertDialog.Builder(context);
        inflater=getLayoutInflater();
        view=inflater.inflate(R.layout.ad_hint,null,false);
        builder.setView(view);
        dialog=builder.create();

        back =  findViewById(R.id.back);
        alter =  findViewById(R.id.alter);
        oldpwd =findViewById(R.id.oldpwd);
        newpwd =findViewById(R.id.newpwd);
        newpwd2 =findViewById(R.id.newpwd2);

        oldpwd.addTextChangedListener(this);
        newpwd.addTextChangedListener(this);
        newpwd2.addTextChangedListener(this);


        alter.setOnClickListener(this);

        back.setOnClickListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        alter.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(newpwd.getText().toString().length()!=0 && newpwd2.getText().toString().length()!=0&& oldpwd.getText().toString().length()!=0){
            alter.setEnabled(true);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.alter:
                alterPWD();
                break;
        }
    }

    private void alterPWD() {
        if(newpwd.getText().toString().equals(newpwd2.getText().toString()))
        {
            if(newpwd.getText().toString().equals(sp.getPWD()))
            {
                dialog.show();
                txt= view.findViewById(R.id.txt_hint2);
                txt.setText("新密码和原密码相同，请重新输入。");
            }
            //在这里做修改密码的操作
            else{

                // 第一参数是 className,第二个参数是 objectId
                AVObject testObject = AVObject.createWithoutData("UserInfo", sp.getID());

                testObject.put("Password", newpwd.getText().toString());
                // 保存到云端
                testObject.saveInBackground();
                //查询是否更新成功
                AVQuery<AVObject> query1 = new AVQuery<>("UserInfo");
                query1.whereEqualTo("Password", newpwd.getText().toString());
                AVQuery<AVObject> query2 = new AVQuery<>("UserInfo");
                query2.whereEqualTo("UserName",sp.getUserName());
                AVQuery<AVObject> query = AVQuery.and(Arrays.asList(query1, query2));
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if(list.size()>0){
                            state=1;
                            sp.setPWD("");
                            handler.sendEmptyMessage(0x001);
                        }else {
                            state=0;
                            handler.sendEmptyMessage(0x002);
                        }
                    }
                });
                handler.sendEmptyMessage(0x001);
            }
        }
        else{
            dialog.show();
            txt=view.findViewById(R.id.txt_hint2);
            txt.setText("两次输入的密码不相同，请重新输入。");
        }
    }
}
