package com.example.administrator.xmutsq;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalCenter extends StatusBarUT implements View.OnClickListener{


    private Context context;
    private SharedPUT sp;

    private List<Map<String, String>> data = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    private RelativeLayout tx;
    private RelativeLayout xm;
    private RelativeLayout xb;
    private RelativeLayout yx;
    private RelativeLayout sj;
    private RelativeLayout qq;
    private RelativeLayout tc;
    private RelativeLayout pwd;

    private ImageView img;
    private TextView tx_txt;
    private TextView tx2_txt;

    private TextView xm_txt;
    private TextView xb_txt;
    private TextView yx_txt;
    private TextView sj_txt;
    private TextView qq_txt;

    private Button back;

    //广播通知主线程更新
    private static boolean state=false;
    private MyBroad broad;

    private static final int REQUEST_CODE_PICK_IMAGE=1;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA=2;
    private static final int CODE_RESULT_REQUEST=3;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    getUserInfo();
                    sp.setIsUpdate(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        //广播更新
        broad=new MyBroad();
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

        getUserInfo();


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.center_personal;
    }


    private void init() {

        context=PersonalCenter.this;
        sp=new SharedPUT(context);

        tx=findViewById(R.id.cp_ly_tx);
        xm=findViewById(R.id.cp_ly_xm);
        xb=findViewById(R.id.cp_ly_xb);
        yx=findViewById(R.id.cp_ly_yx);
        sj=findViewById(R.id.cp_ly_sj);
        qq=findViewById(R.id.cp_ly_qq);
        tc=findViewById(R.id.cp_ly_tc);
        pwd=findViewById(R.id.cp_ly_pwd);

        img=findViewById(R.id.cp_img_tx);
        tx_txt=findViewById(R.id.cp_txt_tx);
        tx2_txt=findViewById(R.id.cp_txt_tx2);
        xm_txt=findViewById(R.id.cp_txt_xm);
        xb_txt=findViewById(R.id.cp_txt_xb);
        yx_txt=findViewById(R.id.cp_txt_yx);
        sj_txt=findViewById(R.id.cp_txt_sj);
        qq_txt=findViewById(R.id.cp_txt_qq);
        back=findViewById(R.id.cp_btn_back);

        tx.setOnClickListener(this);
        xm.setOnClickListener(this);
        xb.setOnClickListener(this);
        yx.setOnClickListener(this);
        sj.setOnClickListener(this);
        qq.setOnClickListener(this);
        tc.setOnClickListener(this);
        pwd.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cp_ly_tx:
                if(sp.getIsLogin()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                }else {
                    Intent it_tx=new Intent(context,Login.class);
                    startActivity(it_tx);
                }
                break;
            case R.id.cp_ly_xm:
                if(sp.getIsLogin()) {
                    Intent it_xm = new Intent();
                    it_xm.setClass(context, UpdateInfo.class);
                    Bundle bd_xm = new Bundle();
                    bd_xm.putString("STR", map.get("Name"));
                    bd_xm.putString("VALUE", "Name");
                    it_xm.putExtras(bd_xm);
                    startActivity(it_xm);
                }else {
                    Toast.makeText(context,"请先登录~",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cp_ly_xb:
                if(sp.getIsLogin()) {
                    Intent it_xb = new Intent();
                    it_xb.setClass(context, UpdateInfo.class);
                    Bundle bd_xb = new Bundle();
                    bd_xb.putString("STR", map.get("Sex"));
                    bd_xb.putString("VALUE", "Sex");
                    it_xb.putExtras(bd_xb);
                    startActivity(it_xb);
                }else {
                    Toast.makeText(context,"请先登录~",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cp_ly_yx:
                if(sp.getIsLogin()) {
                    Intent it_yx = new Intent();
                    it_yx.setClass(context, UpdateInfo.class);
                    Bundle bd_yx = new Bundle();
                    bd_yx.putString("STR", map.get("Email"));
                    bd_yx.putString("VALUE", "Email");
                    it_yx.putExtras(bd_yx);
                    startActivity(it_yx);
                }else {
                    Toast.makeText(context,"请先登录~",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cp_ly_sj:
                if(sp.getIsLogin()) {
                    Intent it_sj = new Intent();
                    it_sj.setClass(context, UpdateInfo.class);
                    Bundle bd_sj = new Bundle();
                    bd_sj.putString("STR", map.get("Phone"));
                    bd_sj.putString("VALUE", "Phone");
                    it_sj.putExtras(bd_sj);
                    startActivity(it_sj);
                }else {
                    Toast.makeText(context,"请先登录~",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cp_ly_qq:
                if(sp.getIsLogin()) {
                    Intent it_qq = new Intent();
                    it_qq.setClass(context, UpdateInfo.class);
                    Bundle bd_qq = new Bundle();
                    bd_qq.putString("STR", map.get("QQ"));
                    bd_qq.putString("VALUE", "QQ");
                    it_qq.putExtras(bd_qq);
                    startActivity(it_qq);
                }else {
                    Toast.makeText(context,"请先登录~",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cp_ly_tc:
                if(sp.getIsLogin()) {
                    sp.setIsLogin(false);
                    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD"));
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD2"));
                    }
                    else {
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.PersonalCenter$MyBroad")));
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD2").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.MainActivity$MyBroad")));
                    }
                    finish();
                }else {
                    Toast.makeText(context,"请先登录~",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cp_btn_back:
                finish();
                break;
            case R.id.cp_ly_pwd:
                Intent intent=new Intent(context,AlterPWD.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode==RESULT_OK &&data!=null) {
            if (data != null) {
                cropBitmap(data.getData());
            }
        }
        else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
        }else if(requestCode==CODE_RESULT_REQUEST)
        {
            if (data!=null) {
                setImageVar(data);

            }
        }
    }

    /**
     *获取用户信息
     * */
    public void getUserInfo(){
        if(!sp.getIsLogin()){
            tx_txt.setText("点我登录~");
            tx2_txt.setText("");
            xm_txt.setText("");
            xb_txt.setText("");
            yx_txt.setText("");
            sj_txt.setText("");
            qq_txt.setText("");
            img.setImageResource(R.mipmap.ic_launcher_round);
        }else {
            if(!readBitmap()){
                downLoadBitmap();
            }
            AVQuery<AVObject> avQuery = new AVQuery<>("UserInfo");
            avQuery.getInBackground(sp.getID(), new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject != null) {
                        tx_txt.setText(avObject.get("Name").toString());
                        tx2_txt.setText(avObject.get("UserName").toString());
                        xm_txt.setText(avObject.get("Name").toString());
                        xb_txt.setText(avObject.get("Sex").toString());
                        yx_txt.setText(avObject.get("Email").toString());
                        sj_txt.setText(avObject.get("Phone").toString());
                        qq_txt.setText(avObject.get("QQ").toString());
                    }
                }
            });

        }

    }

    /**
     *裁剪图片
     * */
    public void cropBitmap(Uri uri){
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");

        //设置裁剪
        intent.putExtra("crop","true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", img.getWidth());
        intent.putExtra("outputY", img.getHeight());
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    public void setImageVar(Intent intent){
        Bundle bd=intent.getExtras();
        if(bd!=null){
            Bitmap photo=bd.getParcelable("data");
            img.setImageBitmap(toOvalBitmap(photo,200));
            saveBitmap(toOvalBitmap(photo,200),true);

        }
    }

    /**
     * 保存图片
     */
    private void saveBitmap(Bitmap bitmap, boolean isUP) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = context.getExternalFilesDir("");
        }else{//手机内部存储
            //路径2：data/data/包名/files
            filesDir = context.getFilesDir();
        }
        FileOutputStream fos = null;
        try {
            File file = new File(filesDir,sp.getID()+".png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            //图片保存成功通知主线程更新
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
                sendBroadcast(new Intent("com.example.administrator.MYBROAD2"));
            }
            else {
                sendBroadcast(new Intent("com.example.administrator.MYBROAD2").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.MainActivity$MyBroad")));
            }
            if(isUP) {
                upLoadBitmap(file.getAbsolutePath().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取图片
     * */
    private boolean readBitmap() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = context.getExternalFilesDir("");
        }else{//手机内部存储
            //路径2：data/data/包名/files
            filesDir = context.getFilesDir();
        }
        File file = new File(filesDir,sp.getID()+".png");
        if(file.exists()){
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            img.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    /**
     * 设置图片圆形显示
     * */
    public static Bitmap toOvalBitmap(Bitmap bitmap, float pix) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getHeight(),
                    bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
            RectF rectF = new RectF(rect);
            float roundPx = pix;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            int color = 0xff424242;
            paint.setColor(color);
            canvas.drawOval(rectF, paint);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    /**
     * 上传图片
     * */
    public void upLoadBitmap(String path){
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(sp.getID()+".png", path);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    sp.setImage(file.getUrl());
                    // 第一参数是 className,第二个参数是 objectId
                    AVObject testObject1 = AVObject.createWithoutData("UserInfo", sp.getID());
                    testObject1.put("ImageUrl", file.getUrl());
                    // 保存到云端
                    testObject1.saveInBackground();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 下载图片
     * */
    public void downLoadBitmap(){
        final Bitmap[] bitmap = new Bitmap[1];
        final AVFile file=new AVFile(sp.getID()+".png",sp.getImage(),new HashMap<String, Object>());
        file.getThumbnailUrl(true, 100, 100);
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, AVException e) {
                // bytes 就是文件的数据流
                if(bytes!=null) {
                    bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    saveBitmap(bitmap[0], false);
                    img.setImageBitmap(bitmap[0]);
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                // 下载进度数据，integer 介于 0 和 100。
            }
        });
    }


    public static class MyBroad extends BroadcastReceiver {
        public final String board="com.example.administrator.MYBROAD";
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
        if(broad!=null){
            unregisterReceiver(broad);
        }
    }
}
