package com.example.administrator.xmutsq;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.entity.Notice;
import com.example.administrator.ut.AdapterUT;

import java.util.ArrayList;
import java.util.List;

public class NoticeH extends Fragment {
    private View view;
    private Context context;

    private ListView listV;
    private LinearLayout visible;
    private AdapterUT<Notice> adapter;
    private Notice notice;
    private ArrayList<Notice> noticeList;
    //广播通知主线程更新
    private static boolean state=false;
    private PersonalCenter.MyBroad broad;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    listV.setVisibility(View.VISIBLE);
                    visible.setVisibility(View.GONE);
                    break;
                case 0x002:
                    listV.setVisibility(View.GONE);
                    visible.setVisibility(View.VISIBLE);
                    break;
                case 0x003:
                    getData();
                    break;
            }
        }
    };

    public NoticeH(){}
    @SuppressLint("ValidFragment")
    public NoticeH(Context context){
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.home_notice,container, false);
            init();

        }
        getData();
        return view;
    }

    private void init() {
        listV =view.findViewById(R.id.list);
        visible=view.findViewById(R.id.visible);

        //广播更新
        broad=new PersonalCenter.MyBroad();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.administrator.MYBROAD3");
        getActivity().registerReceiver(broad,filter);

        new Thread(){
            @Override
            public void run() {
                while (true)
                {
                    if(state) {
                        state=false;
                        handler.sendEmptyMessage(0x003);
                    }
                }
            }
        }.start();
    }

    public void  getData(){
        //从服务器中获取数据
        AVQuery<AVObject> query = new AVQuery<>("Notice");
        query.orderByDescending("Date");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()>0) {
                    handler.sendEmptyMessage(0x001);
                    noticeList=new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        notice = new Notice();
                        notice.setNoticeID(list.get(i).getObjectId().toLowerCase());
                        notice.setTitle(list.get(i).get("Title").toString());
                        notice.setContent(list.get(i).get("Content").toString());
                        notice.setDate(list.get(i).get("Date").toString());

                        noticeList.add(notice);
                    }
                    setAdapter();
                }else
                {
                    handler.sendEmptyMessage(0x002);
                }

            }
        });
    }

    private void setAdapter() {
        adapter=new AdapterUT<Notice>(noticeList, R.layout.home_notice_item) {
            @Override
            public void bindView(ViewHolder holder, Notice obj) {
                holder.setText(R.id.title,obj.getTitle());
                holder.setText(R.id.content,obj.getContent());
                holder.setText(R.id.date,obj.getDate());

            }
        };

        listV.setAdapter(adapter);

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,NoticeInfo.class);
                Bundle bd=new Bundle();
                Notice notice1=noticeList.get(position);
                ArrayList<Notice> bookInfos=new ArrayList<>();
                bookInfos.add(notice1);
                bd.putSerializable("notice",bookInfos);
                intent.putExtras(bd);
                startActivity(intent);
            }
        });

    }

    public static class MyBroad extends BroadcastReceiver {
        public final String board="com.example.administrator.MYBROAD3";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(board)){
                state=true;

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broad != null) {
            getActivity().unregisterReceiver(broad);
        }
    }
}
