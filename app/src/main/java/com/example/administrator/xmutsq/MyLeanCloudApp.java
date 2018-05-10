package com.example.administrator.xmutsq;

import android.app.Application;
import android.os.Build;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;

/**
 * Created by Administrator on 2018/3/28.
 */

public class MyLeanCloudApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
        AVOSCloud.setDebugLogEnabled(true);
        // 启用北美节点, 需要在 initialize 之前调用
        //AVOSCloud.useAVCloudUS();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            PushService.setDefaultChannelId(this, "public");
        }
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"fQ1FPXxdIQIVPPcjemAlXIm0-gzGzoHsz","h35fGeuM9N4yEH9LBJY9DXuY");
    }
}
