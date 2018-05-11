package com.example.administrator.xmutsq;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.entity.Notice;
import com.example.administrator.ut.StatusBarUT;

import java.util.ArrayList;

public class NoticeInfo extends StatusBarUT {

    private Intent intent;
    private ArrayList<Notice> notices;

    private TextView title;
    private TextView content;
    private TextView date;

    private LinearLayout back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        date=findViewById(R.id.date);
        back=findViewById(R.id.back);

        getData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    }
}
