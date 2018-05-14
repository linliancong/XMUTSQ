package com.example.administrator.xmutsq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.entity.Message;
import com.example.administrator.entity.Notice;
import com.example.administrator.ut.AdapterUT;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;
import com.example.administrator.ut.TimeUT;

import java.util.ArrayList;
import java.util.List;

public class NoticeInfo extends StatusBarUT implements View.OnClickListener,TextWatcher{

    private Context context;
    private SharedPUT sp;
    private Intent intent;
    private ArrayList<Notice> notices;

    private TextView title;
    private TextView content;
    private TextView date;

    private LinearLayout message;
    private ListView list;
    private AdapterUT<Message> adapter;
    private Message msg;
    private ArrayList<Message> msgs;

    private RelativeLayout visable;
    private Button send;
    private EditText contents;
    private TextView txt;

    private String noticeID="";
    private String messageID ="";
    private int state=0;
    private String userName ="";




    private LinearLayout back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=NoticeInfo.this;
        sp=new SharedPUT(context);

        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        date=findViewById(R.id.date);
        back=findViewById(R.id.back);

        message=findViewById(R.id.message);
        list=findViewById(R.id.list);

        visable=findViewById(R.id.visible);
        send=findViewById(R.id.send);
        contents=findViewById(R.id.contents);
        txt=findViewById(R.id.txt);

        getData();
        getServiceData();


        back.setOnClickListener(this);
        message.setOnClickListener(this);
        send.setOnClickListener(this);
        visable.setOnClickListener(this);

        contents.addTextChangedListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_notice_info;
    }

    private void getData() {
        intent=getIntent();
        Bundle bd=intent.getExtras();
        notices=new ArrayList<>();
        notices= (ArrayList<Notice>) bd.getSerializable("notice");
        title.setText(notices.get(0).getTitle());
        content.setText(notices.get(0).getContent());
        date.setText(notices.get(0).getDate());
        noticeID=notices.get(0).getNoticeID();

    }

    private void getServiceData() {
        //从服务器中获取数据
        AVQuery<AVObject> query = new AVQuery<>("Message");
        query.whereEqualTo("NoticeID",noticeID);
        query.orderByAscending("Date");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()>0) {
                    msgs=new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        msg = new Message();
                        msg.setUsername(list.get(i).get("UserName").toString());
                        msg.setReplyname(list.get(i).get("ReplyName").toString());
                        msg.setContent(list.get(i).get("Content").toString());
                        msg.setMessageID(list.get(i).getObjectId().toString());
                        msgs.add(msg);
                    }
                    setAdapter();
                }

            }
        });

    }

    private void setAdapter() {
        adapter=new AdapterUT<Message>(msgs, R.layout.home_notice_info_item) {
            @Override
            public void bindView(ViewHolder holder, Message obj) {
                if(!obj.getReplyname().equals("")) {
                    holder.setText(R.id.username2, " 回复 ");
                    holder.setText(R.id.username3, obj.getReplyname());
                }
                holder.setText(R.id.username, obj.getUsername());
                holder.setText(R.id.content, obj.getContent());
            }
        };

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //管理员才可以回复留言
                if(sp.getRole().equals("0")) {
                    state = 1;
                    txt.setText("回复");
                    contents.setHint("请在此输入回复的内容");
                    contents.setText("");
                    visable.setVisibility(View.VISIBLE);
                    messageID = msgs.get(position).getMessageID();
                    userName = msgs.get(position).getUsername();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.message:
                txt.setText("留言");
                contents.setText("");
                contents.setHint("请在此输入留言的内容");
                state=0;
                visable.setVisibility(View.VISIBLE);
                break;
            case R.id.visible:
                visable.setVisibility(View.GONE);
                break;
            case R.id.send:
                if (state==0) {
                    setData();
                }else {
                    setData2();
                }
                break;
        }
    }

    private void setData() {
        //保存数据
        AVObject testObject = new AVObject("Message");
        testObject.put("NoticeID",noticeID);
        testObject.put("MessageID","");
        testObject.put("UserID", sp.getID());
        testObject.put("UserName",sp.getUserName());
        testObject.put("ReplyName","");
        testObject.put("Date", TimeUT.getCurrentDate());
        testObject.put("Content", contents.getText().toString());
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Toast.makeText(context, "留言成功", Toast.LENGTH_SHORT).show();
                    getServiceData();
                }else{
                    Toast.makeText(context, "留言失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                visable.setVisibility(View.GONE);
            }
        });

    }

    private void setData2() {
        //保存数据
        AVObject testObject = new AVObject("Message");
        testObject.put("NoticeID",noticeID);
        testObject.put("MessageID",messageID);
        testObject.put("UserID", sp.getID());
        testObject.put("UserName",sp.getUserName());
        testObject.put("ReplyName",userName);
        testObject.put("Date", TimeUT.getCurrentDate());
        testObject.put("Content", contents.getText().toString());
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Toast.makeText(context, "回复成功", Toast.LENGTH_SHORT).show();
                    getServiceData();
                }else{
                    Toast.makeText(context, "回复失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                visable.setVisibility(View.GONE);
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
        if(contents.getText().toString().length()>0){
            send.setEnabled(true);
        }

    }
}
