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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;
import com.example.administrator.ut.TimeUT;

/**
 * Created by Administrator on 2018/5/11.
 */

public class AddNotice extends StatusBarUT implements View.OnClickListener,TextWatcher{

    private Context context;
    private SharedPUT sp;
    private LinearLayout back;
    private Button send;
    private EditText title;
    private EditText content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=AddNotice.this;
        sp=new SharedPUT(context);

        back=findViewById(R.id.back);
        send=findViewById(R.id.send);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);

        title.addTextChangedListener(this);
        content.addTextChangedListener(this);

        back.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_notice_release;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send:
                setData();
                break;
        }

    }

    public void setData(){
        AVObject testObject = new AVObject("Notice");
        testObject.put("UserID",sp.getID());
        testObject.put("UserName",sp.getUserName());
        testObject.put("Title",title.getText().toString());
        testObject.put("Content",content.getText().toString());
        testObject.put("Date", TimeUT.getCurrentDate());
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD3"));
                    }
                    else {
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD3").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.NoticeH$MyBroad")));
                    }
                    finish();
                }else{
                    Toast.makeText(context, "发布失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        send.setEnabled(false);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(title.getText().toString().length()>0&&content.getText().toString().length()>0){
            send.setEnabled(true);
        }

    }
}
