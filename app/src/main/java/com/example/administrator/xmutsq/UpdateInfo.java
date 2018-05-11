package com.example.administrator.xmutsq;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.util.Arrays;
import java.util.List;

public class UpdateInfo extends StatusBarUT implements TextWatcher,View.OnClickListener {

    private LinearLayout back;
    private TextView title;
    private Button save;
    private EditText input;

    private String content;
    private String value;

    private Intent it;
    private SharedPUT sp;
    private Context context;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD"));
                    }
                    else {
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.PersonalCenter$MyBroad")));
                    }
                    break;
                case 0x002:
                    Toast.makeText(context,"修改失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.info_update);
        init();


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.info_update;
    }


    public void init() {
        context=UpdateInfo.this;
        sp=new SharedPUT(context);
        back = findViewById(R.id.cp_info_back);
        title = findViewById(R.id.cp_info_title);
        save = findViewById(R.id.cp_btn_update);
        input = findViewById(R.id.cp_txt_update);

        it = getIntent();
        Bundle bd = it.getExtras();
        content = bd.getString("STR","");
        value = bd.getString("VALUE","");
        input.setText(content);
        input.setSelection(content.length());

        input.addTextChangedListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);

        switch (value) {
            case "Name":
                title.setText("姓名");
                break;
            case "Sex":
                title.setText("性别");
                break;
            case "Weight":
                title.setText("体重");
                break;
            case "UserName":
                title.setText("账号");
                break;
            case "Email":
                title.setText("邮箱");
                break;
            case "Phone":
                title.setText("手机");
                break;
            case "QQ":
                title.setText("QQ");
                break;
            default:
                break;

        }
    }

    @Override
    public void onClick(View v) {
        update(value);
        finish();
    }

    private void update(String sql) {

        // 第一参数是 className,第二个参数是 objectId
        AVObject testObject1 = AVObject.createWithoutData("UserInfo", sp.getID());

        testObject1.put(sql,input.getText().toString());
        // 保存到云端
        testObject1.saveInBackground();
        //查询是否更新成功
        AVQuery<AVObject> query1 = new AVQuery<>("UserInfo");
        query1.whereEqualTo(sql,input.getText().toString());
        AVQuery<AVObject> query2 = new AVQuery<>("UserInfo");
        query2.whereEqualTo("UserName",sp.getUserName());
        AVQuery<AVObject> query = AVQuery.and(Arrays.asList(query1, query2));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()>0){
                    handler.sendEmptyMessage(0x001);
                }else {
                    handler.sendEmptyMessage(0x002);
                }
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        save.setEnabled(false);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (input.getText().toString().length() != 0) {
            save.setEnabled(true);
        }

    }

}
