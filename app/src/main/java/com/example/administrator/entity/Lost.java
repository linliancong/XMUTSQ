package com.example.administrator.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/15.
 */

public class Lost implements Serializable{
    private String LostID;
    private String LostName;
    private String Content;
    private String Time;
    private String Image;
    private String UserID;
    private String UserName;
    private String UserImageURL;
    private String Phone;
    private String State;
    private String ToUserID;
    private String ToUserName;
    private String Name;


    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getLostID() {
        return LostID;
    }

    public String getTime() {
        return Time;
    }

    public String getContent() {
        return Content;
    }

    public String getImage() {
        return Image;
    }

    public String getLostName() {
        return LostName;
    }

    public String getState() {
        return State;
    }

    public String getToUserID() {
        return ToUserID;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public String getUserID() {
        return UserID;
    }

    public String getUserImageURL() {
        return UserImageURL;
    }

    public String getUserName() {
        return UserName;
    }

    public void setLostID(String lostID) {
        LostID = lostID;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setLostName(String lostName) {
        LostName = lostName;
    }

    public void setState(String state) {
        State = state;
    }

    public void setToUserID(String toUserID) {
        ToUserID = toUserID;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setUserImageURL(String userImageURL) {
        UserImageURL = userImageURL;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setName(String name) {
        Name = name;
    }
}
