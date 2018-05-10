package com.example.administrator.xmutsq;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.ut.FragmentAdapter;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;


/**
 * Created by Administrator on 2017/3/16.
 */

public class MainActivity extends StatusBarUT implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{

    public static final int PAG_ONE=0;
    public static final int PAG_TWO=1;
    public static final int PAG_THREE=2;
    public static final int PAG_FOUR=3;

    private RadioGroup rg_tab_bar;
    private RadioButton rb_home;
    private RadioButton rb_plan;
    private RadioButton rb_interact;
    private RadioButton rb_me;
    private ImageView image;


    private View view_home;
    private View view_plan;
    private View view_interact;
    private View view_me;

    private ViewPager vpager;
    private FragmentAdapter mAdapter=null;


    private long mTime=0;
    private Context context;
    private SharedPUT sp;
    //private SQLOperator op;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        context=getApplicationContext();
        sp=new SharedPUT(context);

        //以下为初始化各个表
        /**
         * 表名：UserInfo （用户信息表）
         * 以下为各个字段
         * UserName：用户名
         * Password：密码
         * Name：姓名
         * Sex：性别
         * Phone：手机
         * Email：邮箱
         * QQ：qq号
         * ImageUrl：头像
         * Remark：备注
         * */
        /*AVObject testObject = new AVObject("UserInfo");
        testObject.put("UserName","");
        testObject.put("Password", "");
        testObject.put("Name","");
        testObject.put("Sex", "");
        testObject.put("Phone","");
        testObject.put("Email","");
        testObject.put("QQ", "");
        testObject.put("ImageUrl","");
        testObject.put("Remark","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/


        /**
         * 表名：Book（图书信息表）
         * 以下为各个字段
         * ISBN: ISBN
         * BookName：图书名
         * Author：作者
         * Publisher：出版社
         * Tag：类别
         * ImgPath：图书封面
         * Price：价格
         * Num：库存数量
         * Summary：简介
         * Remark：备注
         * */
        /*testObject = new AVObject("Book");
        testObject.put("ISBN","");
        testObject.put("BookName", "");
        testObject.put("Author","");
        testObject.put("Publisher", "");
        testObject.put("Tag","");
        testObject.put("ImgPath","");
        testObject.put("Price", "");
        testObject.put("Num","");
        testObject.put("Summary","");
        testObject.put("Remark","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/



        /**
         * 表名：BookBorrow（借阅信息表）
         * 以下为各个字段
         * ISBN：ISBN
         * UserID：用户编号
         * State：借阅状态（1、预约中，2、借阅中，3、已归还）
         * Date：借阅时间
         * Remark：备注
         * */
        /*testObject = new AVObject("BookBorrow");
        testObject.put("ISBN","");
        testObject.put("UserID", "");
        testObject.put("State","");
        testObject.put("Date", "");
        testObject.put("Remark","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/

        /**
         * 表名：Notice（公告表）
         * 以下为各个字段
         * Title：标题
         * Content：内容
         * Date：发布时间
         * Num：留言数量
         * Remark：备注
         * */
        /*testObject = new AVObject("Notice");
        testObject.put("Title","");
        testObject.put("Content", "");
        testObject.put("Date","");
        testObject.put("Num", "");
        testObject.put("Remark","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/

        /**
         * 表名：Message（留言表）
         * 以下为各个字段
         * NoticeID：公告ID
         * UserID：用户ID
         * Content：内容
         * Date：留言时间
         * Remark：备注
         * */
        /*testObject = new AVObject("Message");
        testObject.put("NoticeID","");
        testObject.put("UserID", "");
        testObject.put("Date","");
        testObject.put("Content", "");
        testObject.put("Remark","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/

        /**
         * 表名：Course（课程表）
         * 以下为各个字段
         * CourseName：课程名称
         * Credits：学分
         * Time：学时
         * Remark：备注
         * */
        /*testObject = new AVObject("Course");
        testObject.put("CourseName","");
        testObject.put("Credits","");
        testObject.put("Time", "");
        testObject.put("Remark","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/

        /**
         * 表名：Grade（成绩表）
         * 以下为各个字段
         * UserID：用户ID
         * CourseID：课程ID
         * GradePoint：绩点
         * Grade：成绩
         * Remark：备注
         * */
        /*testObject = new AVObject("Grade");
        testObject.put("CourseID","");
        testObject.put("UserID", "");
        testObject.put("GradePoint","");
        testObject.put("Grade", "");
        testObject.put("Remark","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/

        /**
         * 表名：Lost（失物表）
         * 以下为各个字段
         * LostName：物品名称
         * Content：详细内容
         * Time：登记时间
         * Image：物品图片
         * UserID：发布的用户ID
         * UserName：发布用户名
         * State：物品状态（1、发布中，2、已认领）
         * ToUserID：认领的用户ID
         * ToUserName：认领的用户名
         * */
        /*testObject = new AVObject("Lost");
        testObject.put("LostName","");
        testObject.put("Content", "");
        testObject.put("Time","");
        testObject.put("Image", "");
        testObject.put("UserID","");
        testObject.put("UserName","");
        testObject.put("State","");
        testObject.put("ToUserID","");
        testObject.put("ToUserName","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
            }
        });*/


        image=findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
            }
        });
        mAdapter=new FragmentAdapter(getSupportFragmentManager(),MainActivity.this);
        bindView();
        rb_home.setChecked(true);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    public void bindView() {
        //设置菜单上方的区块
        view_home=findViewById(R.id.view_home);
        view_plan =findViewById(R.id.view_plan);
        view_interact =findViewById(R.id.view_interact);
        view_me=findViewById(R.id.view_me);

        //按钮
        rg_tab_bar=  findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个按钮，并设置其状态为选中
        rb_home= findViewById(R.id.rb_home);
        rb_plan = findViewById(R.id.rb_plan);
        rb_interact = findViewById(R.id.rb_interact);
        rb_me= findViewById(R.id.rb_me);

        //viewPager相关的设置
        vpager= findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_home:
                setSelected();
                view_home.setSelected(true);
                vpager.setCurrentItem(PAG_ONE);
                break;
            case R.id.rb_plan:
                setSelected();
                view_plan.setSelected(true);
                vpager.setCurrentItem(PAG_TWO);
                break;
            case R.id.rb_interact:
                setSelected();
                view_interact.setSelected(true);
                vpager.setCurrentItem(PAG_THREE);
                break;
            case R.id.rb_me:
                setSelected();
                view_me.setSelected(true);
                vpager.setCurrentItem(PAG_FOUR);
                break;
            default:break;
        }
    }

    public void setSelected(){
        view_home.setSelected(false);
        view_plan.setSelected(false);
        view_me.setSelected(false);
        view_interact.setSelected(false);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state 0：都没做，1：正在动，2：动完
        if (state==2)
        {
            switch (vpager.getCurrentItem()){
                case PAG_ONE:
                    rb_home.setChecked(true);
                    break;
                case PAG_TWO:
                    rb_plan.setChecked(true);
                    break;
                case PAG_THREE:
                    rb_interact.setChecked(true);
                    break;
                case PAG_FOUR:
                    rb_me.setChecked(true);
                    break;
            }

        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //拦截返回键
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            //判断触摸UP事件才会进行返回事件处理
            if (event.getAction() == KeyEvent.ACTION_UP) {
                onBackPressed();
            }
            //只要是返回事件，直接返回true，表示消费掉
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-mTime>2000)
        {
            Toast.makeText(getApplicationContext(),"再按一次退出",Toast.LENGTH_SHORT).show();
            mTime=System.currentTimeMillis();
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}



