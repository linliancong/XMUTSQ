package com.example.administrator.ut;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPUT {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharedPUT(Context context){
        sp=context.getSharedPreferences("MYSP",context.MODE_PRIVATE);
        editor=sp.edit();
    }


    //用户ID
    public void setID(String ID){
        editor.putString("ID",ID);
        editor.commit();
    }

    public String getID()
    {
        return sp.getString("ID","");
    }

    //用户账户
    public void setUserName(String UserName){
        editor.putString("UserName",UserName);
        editor.commit();
    }

    public String getUserName()
    {
        return sp.getString("UserName","");
    }

    //用户名
    public void setName(String Name){
        editor.putString("Name",Name);
        editor.commit();
    }

    public String getName()
    {
        return sp.getString("Name","");
    }

    //用户密码
    public void setPWD(String pwd){
        editor.putString("PWD",pwd);
        editor.commit();
    }

    public String getPWD()
    {
        return sp.getString("PWD","");
    }


    //用户角色
    public void setRole(String Role){
        editor.putString("Role",Role);
        editor.commit();
    }
    public String getRole()
    {
        return sp.getString("Role","");
    }

    //用户头像
    public void setImage(String Image){
        editor.putString("Image",Image);
        editor.commit();
    }

    public String getImage()
    {
        return sp.getString("Image","");
    }


    // 是否第一次运行
    public void setIsFirst(boolean isFirst) {
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
    }

    public boolean getIsFirst() {
        return sp.getBoolean("isFirst", true);
    }

    // 是否登录
    public void setIsLogin(boolean isLogin) {
        editor.putBoolean("isLogin", isLogin);
        editor.commit();
    }

    public boolean getIsLogin() {
        return sp.getBoolean("isLogin", false);
    }


    // 是否更新用户资料
    public void setIsUpdate(boolean isUpdate) {
        editor.putBoolean("isUpdate", isUpdate);
        editor.commit();
    }

    public boolean getIsUpdate() {
        return sp.getBoolean("isUpdate", false);
    }



}
