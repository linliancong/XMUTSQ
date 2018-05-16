package com.example.administrator.xmutsq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.entity.Grade;
import com.example.administrator.entity.Notice;
import com.example.administrator.ut.AdapterUT;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class GradeInfo extends StatusBarUT {

    private ListView list;
    private LinearLayout back;
    private Context context;
    private SharedPUT sp;

    private AdapterUT<Grade> adapter;
    private Grade grade;
    private ArrayList<Grade> grades;

    private Bundle bd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=GradeInfo.this;
        sp=new SharedPUT(context);

        back=findViewById(R.id.back);
        list=findViewById(R.id.list);

        bd=getIntent().getExtras();
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
        return R.layout.home_grade_info;
    }

    public void  getData(){
        //从服务器中获取数据
        AVQuery<AVObject> query = new AVQuery<>("Grade");
        query.whereEqualTo("UserID",bd.getString("number"));
        query.whereEqualTo("Year",bd.getString("year"));
        query.whereEqualTo("Term",bd.getString("term"));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()>0) {
                    grades=new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        grade = new Grade();
                        grade.setCourseName(list.get(i).get("CourseName").toString());
                        grade.setCredits(list.get(i).get("Credits").toString());
                        grade.setGrade(list.get(i).get("Grade").toString());
                        grade.setGradePoint(list.get(i).get("GradePoint").toString());
                        grade.setTime(list.get(i).get("Time").toString());

                        grades.add(grade);
                    }
                    setAdapter();
                }

            }
        });
    }

    private void setAdapter() {
        adapter=new AdapterUT<Grade>(grades, R.layout.home_grade_info_item) {
            @Override
            public void bindView(ViewHolder holder, Grade obj) {
                holder.setText(R.id.coursename,obj.getCourseName());
                holder.setText(R.id.credits,obj.getCredits());
                holder.setText(R.id.grade,obj.getGrade());
                holder.setText(R.id.gradepoint,obj.getGradePoint());
                holder.setText(R.id.time,obj.getTime());

            }
        };

        list.setAdapter(adapter);

    }
}
