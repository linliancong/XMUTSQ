package com.example.administrator.xmutsq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class LostAndFoundH extends Fragment implements View.OnClickListener{
    private View view;
    private Context context;

    private RelativeLayout yes;
    private RelativeLayout no;

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

            yes=view.findViewById(R.id.cp_ly_yes);
            no=view.findViewById(R.id.cp_ly_no);

            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cp_ly_yes:
                Intent intent=new Intent(context,LostAndFoundS.class);
                intent.putExtra("state","2");
                startActivity(intent);
                break;
            case R.id.cp_ly_no:
                Intent intent2=new Intent(context,LostAndFoundS.class);
                intent2.putExtra("state","1");
                startActivity(intent2);
                break;
        }
    }
}