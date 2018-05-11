package com.example.administrator.xmutsq;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/3/13.
 */

public class AddBook extends StatusBarUT implements View.OnClickListener{

    private LinearLayout back;
    private ImageView img;
    private TextView bookname;
    private TextView author;
    private TextView isbn;
    private TextView publisher;
    private TextView num;
    private TextView states;
    private TextView summary;

    private Button image_btn;

    private SharedPUT sp;
    private Context context;

    private static final int REQUEST_CODE_PICK_IMAGE=1;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA=2;
    private static final int CODE_RESULT_REQUEST=3;

    private int state=1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.home_book_add);

        init();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_book_add;
    }

    public void init(){
        context=AddBook.this;
        sp=new SharedPUT(context);

        back=findViewById(R.id.back);
        img=findViewById(R.id.image);
        bookname =findViewById(R.id.bookname);
        author=findViewById(R.id.author);
        isbn=findViewById(R.id.isbn);
        publisher=findViewById(R.id.publisher);
        num =findViewById(R.id.num);
        states =findViewById(R.id.state);
        summary=findViewById(R.id.summary);

        image_btn=findViewById(R.id.image_btn);

        back.setOnClickListener(this);
        image_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.image_btn:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode==RESULT_OK &&data!=null) {
            if (data != null) {
                //cropPhoto(data.getData());
                setImageToHeadView(data);
            }
        }
        else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            Uri uri = data.getData();
            //to do find the path of pic
        }else if(requestCode==CODE_RESULT_REQUEST)
        {
            if (data!=null) {
                setImageToHeadView(data);
            }
        }
    }

    public void setImageToHeadView(Intent intent){
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        try {
            Bitmap photo= MediaStore.Images.Media.getBitmap(resolver, intent.getData());
            img.setImageBitmap(photo);
            saveBitmap(photo,true);
        } catch (IOException e) {
            e.printStackTrace();
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
            File file = new File(filesDir,isbn.getText().toString()+".png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
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
     * 上传图片
     * */
    public void upLoadBitmap(String path){
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(isbn.getText().toString()+".png", path);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    //sp.setImage(file.getUrl());
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

}
