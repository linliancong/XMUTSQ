package com.example.administrator.xmutsq;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.ut.FragmentAdapter;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.io.File;

public class MainActivity extends StatusBarUT implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener,View.OnClickListener{

    public static final int PAG_ONE=0;
    public static final int PAG_TWO=1;
    public static final int PAG_THREE=2;
    public static final int PAG_FOUR=3;
    private int page=0;

    private RadioGroup rg_tab_bar;
    private RadioButton rb_home;
    private RadioButton rb_plan;
    private RadioButton rb_interact;
    private RadioButton rb_me;
    private ImageView image;
    private ImageView edit;


    private View view_notice;
    private View view_book;
    private View view_grade;
    private View view_lost;

    private ViewPager vpager;
    private FragmentAdapter mAdapter=null;


    private long mTime=0;
    private Context context;
    private SharedPUT sp;

    //广播通知主线程更新
    private static boolean state=false;
    private PersonalCenter.MyBroad broad;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    if(sp.getIsLogin()){
                        readBitmap();
                        if(sp.getRole().equals("0")){
                            edit.setVisibility(View.VISIBLE);
                        }else {
                            edit.setVisibility(View.GONE);
                        }
                    }else {
                        image.setImageResource(R.mipmap.ic_launcher_round);
                        edit.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        context=getApplicationContext();
        sp=new SharedPUT(context);

        //初始化
        image=findViewById(R.id.image);
        edit=findViewById(R.id.edit);
        image.setOnClickListener(this);
        edit.setOnClickListener(this);
        mAdapter=new FragmentAdapter(getSupportFragmentManager(),MainActivity.this);
        init();
        rb_home.setChecked(true);

        if(sp.getIsLogin()){
            readBitmap();
            if(sp.getRole().equals("0")){
                edit.setVisibility(View.VISIBLE);
            }else {
                edit.setVisibility(View.GONE);
            }
        }

        //广播更新
        broad=new PersonalCenter.MyBroad();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.administrator.MYBROAD");
        registerReceiver(broad,filter);

        new Thread(){
            @Override
            public void run() {
                while (true)
                {
                    if(state) {
                        state=false;
                        handler.sendEmptyMessage(0x001);
                    }
                }
            }
        }.start();

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
         * Role：角色（0、管理员，1、普通用户）
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
        testObject.put("Role","");
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
         * BookName：图书名
         * UserID：用户编号
         * State：借阅状态（1、预约中，2、借阅中，3、已归还）
         * Date：借阅时间
         * Remark：备注
         * */
        /*testObject = new AVObject("BookBorrow");
        testObject.put("ISBN","");
        testObject.put("BookName", "");
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
         * UserName：用户名
         * Content：内容
         * Date：留言时间
         * Remark：备注
         * */
        /*testObject = new AVObject("Message");
        testObject.put("NoticeID","");
        testObject.put("UserID", "");
        testObject.put("UserName","");
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

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    public void init() {
        //设置菜单上方的区块
        view_notice =findViewById(R.id.view_home);
        view_book =findViewById(R.id.view_plan);
        view_grade =findViewById(R.id.view_interact);
        view_lost =findViewById(R.id.view_me);

        //按钮
        rg_tab_bar=  findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个按钮，并设置其状态为选中
        rb_home= findViewById(R.id.rb_notice);
        rb_plan = findViewById(R.id.rb_book);
        rb_interact = findViewById(R.id.rb_grade);
        rb_me= findViewById(R.id.rb_lost);

        //viewPager相关的设置
        vpager= findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_notice:
                setSelected();
                view_notice.setSelected(true);
                vpager.setCurrentItem(PAG_ONE);
                page=PAG_ONE;
                break;
            case R.id.rb_book:
                setSelected();
                view_book.setSelected(true);
                vpager.setCurrentItem(PAG_TWO);
                page=PAG_TWO;
                break;
            case R.id.rb_grade:
                setSelected();
                view_grade.setSelected(true);
                vpager.setCurrentItem(PAG_THREE);
                page=PAG_THREE;
                break;
            case R.id.rb_lost:
                setSelected();
                view_lost.setSelected(true);
                vpager.setCurrentItem(PAG_FOUR);
                page=PAG_FOUR;
                break;
            default:break;
        }
    }

    public void setSelected(){
        view_notice.setSelected(false);
        view_book.setSelected(false);
        view_lost.setSelected(false);
        view_grade.setSelected(false);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image:
                Intent intent=new Intent(context,PersonalCenter.class);
                startActivity(intent);
                break;
                //针对管理员的跳转界面
            case R.id.edit:
                switch (page){
                    //校园公告
                    case PAG_ONE:
                        Intent it1=new Intent(context,AddNotice.class);
                        startActivity(it1);
                        break;
                    //图书信息
                    case PAG_TWO:
                        Intent it2=new Intent(context,AddBook.class);
                        startActivity(it2);
                        break;
                    //成绩查询
                    case PAG_THREE:
                        Intent it3=new Intent(context,PersonalCenter.class);
                        startActivity(it3);
                        break;
                    //失物招领
                    case PAG_FOUR:
                        Intent it4=new Intent(context,PersonalCenter.class);
                        startActivity(it4);
                        break;
                }
                break;
        }
    }

    public static class MyBroad extends BroadcastReceiver {
        public final String board="com.example.administrator.MYBROAD2";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(board)){
                state=true;

            }
        }
    }


    /**
     * 读取图片
     * */
    private boolean readBitmap() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            filesDir = context.getExternalFilesDir("");
        }else{//手机内部存储
            filesDir = context.getFilesDir();
        }
        File file = new File(filesDir,sp.getID()+".png");
        if(file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            image.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broad != null) {
            unregisterReceiver(broad);
        }
    }

}



