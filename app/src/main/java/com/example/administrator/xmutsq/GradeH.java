package com.example.administrator.xmutsq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GradeH extends Fragment {
    private View view;
    private Context context;

    public GradeH(){}
    @SuppressLint("ValidFragment")
    public GradeH(Context context){
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
