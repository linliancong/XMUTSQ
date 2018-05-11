package com.example.administrator.xmutsq;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.example.administrator.entity.Book;
import com.example.administrator.ut.AdapterUT;
import com.example.administrator.ut.SharedPUT;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookH extends Fragment {

    private SharedPUT sp;
    private Context context;
    private View view;

    private AdapterUT adapter;
    private Book book;
    private ArrayList<Book> books;
    private ListView list;

    //广播通知主线程更新
    private static boolean state=false;
    private PersonalCenter.MyBroad broad;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    getData();
                    break;
            }
        }
    };

    public BookH(){}
    @SuppressLint("ValidFragment")
    public BookH(Context context){
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            sp=new SharedPUT(context);
            view=inflater.inflate(R.layout.home_book,container,false);
            list=view.findViewById(R.id.list);

            //广播更新
            broad=new PersonalCenter.MyBroad();
            IntentFilter filter=new IntentFilter();
            filter.addAction("com.example.administrator.MYBROAD4");
            getActivity().registerReceiver(broad,filter);

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


        }
        getData();
        return view;
    }

    public void  getData(){
        //从服务器中获取数据
        AVQuery<AVObject> query = new AVQuery<>("Book");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()>0) {
                    books =new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        book = new Book();
                        book.setBookname(list.get(i).get("Bookname").toString());
                        book.setAuthor(list.get(i).get("Author").toString());
                        book.setIsbn(list.get(i).get("ISBN").toString());
                        book.setSummary(list.get(i).get("Summary").toString());
                        book.setImagepath(list.get(i).get("Imgpath").toString());
                        book.setPublisher(list.get(i).get("Publisher").toString());
                        book.setPrice(list.get(i).get("Price").toString());
                        book.setTag(list.get(i).get("Tag").toString());
                        book.setNum(list.get(i).get("Num").toString());

                        books.add(book);
                    }
                    setAdapter();
                }

            }
        });
    }

    private void setAdapter() {
        adapter=new AdapterUT<Book>(books, R.layout.home_notice_item) {
            @Override
            public void bindView(ViewHolder holder, Book obj) {
                holder.setText(R.id.bookname,obj.getBookname());
                holder.setText(R.id.author,obj.getAuthor());
                holder.setText(R.id.summary,obj.getSummary());
                if(readBitmap(obj.getIsbn())!=null) {
                    holder.setImageBitmap(R.id.image, readBitmap(obj.getIsbn()));
                }else {
                    downLoad(obj.getIsbn(), obj.getImagepath());
                }
            }

        };

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,BookInfo.class);
                Bundle bd=new Bundle();
                Book book1= books.get(position);
                ArrayList<Book> bookInfos=new ArrayList<>();
                bookInfos.add(book1);
                bd.putSerializable("book",bookInfos);
                intent.putExtras(bd);
                startActivity(intent);
            }
        });

    }

    public static class MyBroad extends BroadcastReceiver {
        public final String board="com.example.administrator.MYBROAD4";
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

    /**
     * 下载图片
     * */
    public void downLoad(final String name, String url){
        final Bitmap[] bitmap = new Bitmap[1];
        final AVFile file=new AVFile(name+".png",url,new HashMap<String, Object>());
        file.getThumbnailUrl(true, 100, 100);
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, AVException e) {
                // bytes 就是文件的数据流
                if(bytes!=null) {
                    bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    saveBitmap(bitmap[0], name);
                }
                //img.setImageBitmap(bitmap[0]);
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                // 下载进度数据，integer 介于 0 和 100。
            }
        });
    }

    /**
     * 保存图片
     */
    private void saveBitmap(Bitmap bitmap, String name) {
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
            File file = new File(filesDir,name+".png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            adapter.notifyDataSetChanged();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broad != null) {
            getActivity().unregisterReceiver(broad);
        }
    }
}
