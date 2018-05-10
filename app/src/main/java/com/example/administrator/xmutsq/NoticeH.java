package com.example.administrator.xmutsq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/5/10.
 */

public class NoticeH extends Fragment {
    private View view;
    private Context context;

    public NoticeH(){}
    @SuppressLint("ValidFragment")
    public NoticeH(Context context){
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){

        }
        return view;
    }
}
