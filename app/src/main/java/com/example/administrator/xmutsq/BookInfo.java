package com.example.administrator.xmutsq;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.administrator.entity.Book;
import com.example.administrator.ut.SharedPUT;
import com.example.administrator.ut.StatusBarUT;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class BookInfo extends StatusBarUT implements View.OnClickListener {

    private LinearLayout back;
    private ImageView img;
    private TextView bookname;
    private TextView author;
    private TextView publisher;
    private TextView num;
    private TextView states;
    private TextView summary;
    private Intent intent;

    private Context context;
    private ArrayList<Book> book;
    private SharedPUT sp;
    private int state=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        getData();

    }

    private void init() {
        context=BookInfo.this;
        sp=new SharedPUT(context);

        back=findViewById(R.id.back);
        img=findViewById(R.id.image);
        bookname =findViewById(R.id.bookname);
        author=findViewById(R.id.author);
        publisher=findViewById(R.id.publisher);
        num =findViewById(R.id.num);
        states =findViewById(R.id.state);
        summary=findViewById(R.id.summary);

        back.setOnClickListener(this);
    }

    private void getData() {
        intent=getIntent();
        Bundle bd=intent.getExtras();
        book=new ArrayList<>();
        book= (ArrayList<Book>) bd.getSerializable("book");
        img.setImageBitmap(readBitmap(book.get(0).getIsbn()));
        bookname.setText(book.get(0).getBookname());
        author.setText(book.get(0).getAuthor());
        publisher.setText(book.get(0).getPublisher());
        num.setText(book.get(0).getPrice());
        summary.setText(book.get(0).getSummary());

        //从服务器中获取书本借阅数据
        AVQuery<AVObject> query1 = new AVQuery<>("BookBorrow");
        query1.whereEqualTo("UserID",sp.getID());
        AVQuery<AVObject> query2 = new AVQuery<>("BookBorrow");
        query2.whereEqualTo("ISBN",book.get(0).getIsbn());
        AVQuery<AVObject> query = AVQuery.and(Arrays.asList(query1, query2));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()>0) {
                    if (list.get(0).get("State").toString().equals("1")) {
                        states.setText("预约中");
                        state=1;
                    }else if (list.get(0).get("State").toString().equals("2")) {
                        states.setText("借阅中");
                        state=2;
                    }else if (list.get(0).get("State").toString().equals("3")) {
                        states.setText("已归还");
                        state=3;
                    }
                }else {
                    states.setText("没有借阅过本书");
                    state=0;
                }

            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_book_info;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
