package com.example.administrator.xmutsq;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.example.administrator.entity.Lost;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Administrator on 2018/5/15.
 */

public class LostAndFoundInfo extends StatusBarUT implements View.OnClickListener{
    private Intent intent;
    private ArrayList<Lost> lost;
    private Context context;
    private SharedPUT sp;

    private LinearLayout back;
    private Button get;
    private TextView title;
    private TextView time;
    private TextView username;
    private ImageView image;
    private TextView content;
    private ImageView image_info;
    private TextView state;
    private TextView tousername;

    private String lostID="";
    private String phone="";
    private String name="";

    //弹窗所需的控件
    private AlertDialog alert;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=LostAndFoundInfo.this;
        sp=new SharedPUT(context);

        back=findViewById(R.id.back);
        get=findViewById(R.id.get);
        title=findViewById(R.id.title);
        time=findViewById(R.id.time);
        username=findViewById(R.id.username);
        content=findViewById(R.id.content);
        state=findViewById(R.id.state);
        tousername=findViewById(R.id.tousername);
        image=findViewById(R.id.image);
        image_info=findViewById(R.id.image_info);

        back.setOnClickListener(this);
        get.setOnClickListener(this);

        getData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_lost_info;
    }

    private void getData() {
        intent=getIntent();
        Bundle bd=intent.getExtras();
        lost =new ArrayList<>();
        lost = (ArrayList<Lost>) bd.getSerializable("home_lost");
        lostID=lost.get(0).getLostID();
        phone=lost.get(0).getPhone();
        name=lost.get(0).getName();
        image.setImageBitmap(readBitmap(lost.get(0).getUserID()));
        image_info.setImageBitmap(readBitmap(lost.get(0).getLostID()));
        title.setText(lost.get(0).getLostName());
        time.setText(lost.get(0).getTime());
        username.setText(lost.get(0).getUserName());
        content.setText(lost.get(0).getContent());
        if(lost.get(0).getState().equals("1")){
            state.setText("待认领");
        }else {
            state.setText("已认领");
        }
        tousername.setText(lost.get(0).getToUserName());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.get:
                if(state.getText().toString().equals("待认领")){
                    setData();
                }else {
                    View ad_view2 = getAlert(R.layout.ad_hint);
                    final TextView hint =  ad_view2.findViewById(R.id.txt_hint2);
                    hint.setText("该物品已被认领！如有疑问请联系:"+name+"\n"+"联系电话："+phone);
                    ad_view2.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //handler.sendEmptyMessage(0x0001);
                            alert.dismiss();
                            finish();
                        }
                    });
                }

                break;
        }

    }

    private void setData() {
        // 第一参数是 className,第二个参数是 objectId
        AVObject testObject = AVObject.createWithoutData("Lost", lostID);
        //1、待认领，2、已认领
        testObject.put("State", "2");
        testObject.put("ToUserID",sp.getID());
        testObject.put("ToUserName",sp.getUserName());
        // 保存到云端
        testObject.saveInBackground();
        state.setText("已认领");
        tousername.setText(sp.getUserName());
        Toast.makeText(context,"认领成功",Toast.LENGTH_SHORT).show();

        View ad_view2 = getAlert(R.layout.ad_hint);
        final TextView hint =  ad_view2.findViewById(R.id.txt_hint2);
        hint.setText("认领成功！请联系:"+name+"\n"+"联系电话："+phone);
        ad_view2.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handler.sendEmptyMessage(0x0001);
                alert.dismiss();
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O) {
                    sendBroadcast(new Intent("com.example.administrator.MYBROAD5"));
                }else {
                    sendBroadcast(new Intent("com.example.administrator.MYBROAD5").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.LostAndFoundS$MyBroad")));
                }
                finish();
            }
        });
    }

    /**
     * 读取图片
     * */
    private Bitmap readBitmap(String id) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = context.getExternalFilesDir("");
        }else{//手机内部存储
            //路径2：data/data/包名/files
            filesDir = context.getFilesDir();
        }
        File file = new File(filesDir,id+".png");
        if(file.exists()){
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());


            return bitmap;
        }
        return null;
    }


    //定义弹窗方法
    public View getAlert(int mLayout){
        View ad_view;
        //初始化Builder
        builder=new AlertDialog.Builder(context);
        //完成相关设置
        //inflater=getLayoutInflater();
        inflater= LayoutInflater.from(context);
        ad_view=inflater.inflate(mLayout,null,false);
        builder.setView(ad_view);
        builder.setCancelable(true);
        alert=builder.create();
        alert.show();
        return ad_view;
    }
}
