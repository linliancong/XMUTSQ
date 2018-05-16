package com.example.administrator.xmutsq;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;
import com.example.administrator.ut.TimeUT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/5/11.
 */

public class AddLost extends StatusBarUT implements View.OnClickListener,TextWatcher{

    private Context context;
    private SharedPUT sp;
    private LinearLayout back;
    private Button send;
    private EditText title;
    private EditText content;
    private EditText phone;
    private ImageView image;

    private static final int REQUEST_CODE_PICK_IMAGE=1;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA=2;
    private static final int CODE_RESULT_REQUEST=3;

    public final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断

    public final int CODE_TAKE_PHOTO = 1;//相机RequestCode

    private int state=1;
    private String path="";
    private Uri path2;
    private String imgurl="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=AddLost.this;
        sp=new SharedPUT(context);

        back=findViewById(R.id.back);
        send=findViewById(R.id.send);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        phone=findViewById(R.id.phone);
        image=findViewById(R.id.image);

        title.addTextChangedListener(this);
        content.addTextChangedListener(this);
        phone.addTextChangedListener(this);

        back.setOnClickListener(this);
        send.setOnClickListener(this);
        image.setOnClickListener(this);


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_lost_release;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send:
                setData();
                upLoadBitmap(path);
                break;
            case R.id.image:
                /*Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = getMediaFileUri(TYPE_TAKE_PHOTO);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takeIntent, CODE_TAKE_PHOTO);*/

                if (Build.VERSION.SDK_INT >= 24) {
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = get24MediaFileUri(TYPE_TAKE_PHOTO);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
                } else {
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = getMediaFileUri(TYPE_TAKE_PHOTO);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
                }
                break;
        }

    }

    public Uri getMediaFileUri(int type){
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = context.getExternalFilesDir("");
        }else{//手机内部存储
            //路径2：data/data/包名/files
            filesDir = context.getFilesDir();
        }
        File file;
        if (type == TYPE_TAKE_PHOTO) {
            file = new File(filesDir,TimeUT.getCurrentDate2()+".png");
            path=file.getAbsolutePath().toString();
        } else {
            return null;
        }
        return Uri.fromFile(file);
    }

    /**
     * Android 7.0以后调用拍张的处理
     * */
    public Uri get24MediaFileUri(int type) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = context.getExternalFilesDir("");
        }else{//手机内部存储
            //路径2：data/data/包名/files
            filesDir = context.getFilesDir();
        }
        File file;
        if (type == TYPE_TAKE_PHOTO) {
            file = new File(filesDir,TimeUT.getCurrentDate2()+".png");
            //path=file.getAbsolutePath().toString();
        } else {
            return null;
        }
        path2=FileProvider.getUriForFile(this, getPackageName(), file);
        return path2;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_TAKE_PHOTO:
               // if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.hasExtra("data")) {
                            Bitmap bitmap = data.getParcelableExtra("data");
                            image.setImageBitmap(bitmap);//imageView即为当前页面需要展示照片的控件，可替换
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 24){
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(path2));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            image.setImageBitmap(bitmap);
                        }else {
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            image.setImageBitmap(bitmap);//imageView即为当前页面需要展示照片的控件，可替换
                        }
                    }
                //}
                break;
        }
    }

    public void setData(){
        AVObject testObject = new AVObject("Lost");
        testObject.put("LostName",title.getText().toString());
        testObject.put("Content", content.getText().toString());
        testObject.put("Time",TimeUT.getCurrentDate());
        testObject.put("Image", imgurl);
        testObject.put("UserID",sp.getID());
        testObject.put("UserName",sp.getUserName());
        testObject.put("Name",sp.getName());
        testObject.put("UserImageURL",sp.getImage());
        testObject.put("Phone",phone.getText().toString());
        testObject.put("State","1");
        testObject.put("ToUserID","");
        testObject.put("ToUserName","");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD5"));
                    }
                    else {
                        sendBroadcast(new Intent("com.example.administrator.MYBROAD5").setComponent(new ComponentName("com.example.administrator.xmutsq", "com.example.administrator.xmutsq.LostAndFoundH$MyBroad")));
                    }
                    finish();
                }else{
                    Toast.makeText(context, "发布失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 保存图片
     */
    private void saveBitmap(Bitmap bitmap) {
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
            File file = new File(filesDir,TimeUT.getCurrentDate2()+".png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            path=file.getAbsolutePath().toString();
            upLoadBitmap(path);
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
     * 上传图片
     * */
    public void upLoadBitmap(String path){
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(TimeUT.getCurrentDate2()+".png", path);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    //把图片的路径保存下来
                    imgurl=file.getUrl();
                    setData();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

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
        if(title.getText().toString().length()>0&&content.getText().toString().length()>0&&phone.getText().toString().length()>0){
            send.setEnabled(true);
        }

    }
}
