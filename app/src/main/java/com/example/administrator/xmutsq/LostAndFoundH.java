package com.example.administrator.xmutsq;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.example.administrator.entity.Lost;
import com.example.administrator.entity.Notice;
import com.example.administrator.ut.AdapterUT;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LostAndFoundH extends Fragment {
    private View view;
    private Context context;

    private ListView listV;
    private LinearLayout visible;
    private AdapterUT<Lost> adapter;
    private Lost lost;
    private ArrayList<Lost> losts;
    //广播通知主线程更新
    private static boolean state=false;
    private PersonalCenter.MyBroad broad;

    private RelativeLayout sche_rl;
    private ImageView sche_img;
    private AnimationDrawable anima;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    listV.setVisibility(View.VISIBLE);
                    visible.setVisibility(View.GONE);
                    break;
                case 0x002:
                    listV.setVisibility(View.GONE);
                    visible.setVisibility(View.VISIBLE);
                    break;
                case 0x003:
                    getData();
                    break;
                case 0x004:
                    setAdapter();
                    break;
            }
        }
    };

    public LostAndFoundH(){}
    @SuppressLint("ValidFragment")
    public LostAndFoundH(Context context){
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.home_lost,container, false);
            init();

        }
        sche_rl.setVisibility(View.VISIBLE);
        listV.setVisibility(View.GONE);
        anima.start();
        getData();
        return view;
    }

    private void init() {
        listV =view.findViewById(R.id.list);
        visible=view.findViewById(R.id.visible);

        sche_rl=view.findViewById(R.id.sche_rl);
        sche_img=view.findViewById(R.id.sche_img);
        anima= (AnimationDrawable) sche_img.getDrawable();

        //广播更新
        broad=new PersonalCenter.MyBroad();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.administrator.MYBROAD5");
        getActivity().registerReceiver(broad,filter);

        new Thread(){
            @Override
            public void run() {
                while (true)
                {
                    if(state) {
                        state=false;
                        handler.sendEmptyMessage(0x003);
                    }
                }
            }
        }.start();

    }

    public void  getData(){
        //从服务器中获取数据
        AVQuery<AVObject> query = new AVQuery<>("Lost");
        query.orderByDescending("Time");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()>0) {
                    handler.sendEmptyMessage(0x001);
                    losts =new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        lost = new Lost();
                        lost.setLostID(list.get(i).getObjectId());
                        lost.setUserID(list.get(i).get("UserID").toString());
                        lost.setUserName(list.get(i).get("UserName").toString());
                        lost.setUserImageURL(list.get(i).get("UserImageURL").toString());
                        lost.setContent(list.get(i).get("Content").toString());
                        lost.setLostName(list.get(i).get("LostName").toString());
                        lost.setState(list.get(i).get("State").toString());
                        lost.setImage(list.get(i).get("Image").toString());
                        lost.setTime(list.get(i).get("Time").toString());
                        lost.setToUserID(list.get(i).get("ToUserID").toString());
                        lost.setToUserName(list.get(i).get("ToUserName").toString());
                        lost.setName(list.get(i).get("Name").toString());
                        lost.setPhone(list.get(i).get("Phone").toString());


                        losts.add(lost);
                    }
                    handler.sendEmptyMessage(0x004);
                }else
                {
                    handler.sendEmptyMessage(0x002);
                }

            }
        });
    }

    private void setAdapter() {
        adapter=new AdapterUT<Lost>(losts, R.layout.home_lost_item) {
            @Override
            public void bindView(ViewHolder holder, Lost obj) {
                holder.setText(R.id.username,obj.getUserName());
                holder.setText(R.id.title,obj.getLostName());
                holder.setText(R.id.time,obj.getTime());
                if(readBitmap(obj.getUserID())!=null) {
                    holder.setImageBitmap(R.id.image, readBitmap(obj.getUserID()));
                }else {
                    downLoad(obj.getUserID(), obj.getUserImageURL());
                }
                if(readBitmap(obj.getLostID())!=null) {
                    holder.setImageBitmap(R.id.image_info, readBitmap(obj.getLostID()));
                }else {
                    downLoad(obj.getLostID(), obj.getImage());
                }
            }
        };

        listV.setAdapter(adapter);

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,LostAndFoundInfo.class);
                Bundle bd=new Bundle();
                Lost lost1= losts.get(position);
                ArrayList<Lost> losts1=new ArrayList<>();
                losts1.add(lost1);
                bd.putSerializable("lost",losts1);
                intent.putExtras(bd);
                startActivity(intent);
            }
        });
        anima.stop();
        sche_rl.setVisibility(View.GONE);
        listV.setVisibility(View.VISIBLE);

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

    public static class MyBroad extends BroadcastReceiver {
        public final String board="com.example.administrator.MYBROAD5";
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
        if (broad != null) {
            getActivity().unregisterReceiver(broad);
        }
    }
}