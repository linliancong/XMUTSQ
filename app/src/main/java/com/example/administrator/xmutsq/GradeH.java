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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class GradeH extends Fragment implements View.OnClickListener{
    private View view;
    private Context context;

    private EditText number;
    private Spinner year;
    private Spinner term;
    private Button search;
    private ArrayAdapter<String> adapter=null;
    private ArrayAdapter<String> adapter2=null;
    private List<String> mYear =new ArrayList<>();
    private List<String> mTerm =new ArrayList<>();

    private int position1=-1;
    private int position2=-1;


    public GradeH(){}
    @SuppressLint("ValidFragment")
    public GradeH(Context context){
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.home_grade,container,false);
            init();

            setAdapter();

        }
        return view;
    }

    private void init() {
        number=view.findViewById(R.id.number);
        year=view.findViewById(R.id.year);
        term=view.findViewById(R.id.term);
        search=view.findViewById(R.id.search);

        search.setOnClickListener(this);

        mTerm.add("1");
        mTerm.add("2");
        mTerm.add("3");

        mYear.add("2017-2018");
        mYear.add("2016-2017");
        mYear.add("2015-2016");
        mYear.add("2014-2015");

    }

    private void setAdapter() {
        adapter=new ArrayAdapter<String>(context, R.layout.simple_spinner_item,R.id.tv_spinner,mYear);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        year.setAdapter(adapter);

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position1=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter2=new ArrayAdapter<String>(context, R.layout.simple_spinner_item,R.id.tv_spinner,mTerm);
        adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        term.setAdapter(adapter2);

        term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position2=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.search){
            if(position1==-1){
                Toast.makeText(context,"学年不能为空",Toast.LENGTH_SHORT).show();
            }else if(position2==-1){
                Toast.makeText(context,"学期不能为空",Toast.LENGTH_SHORT).show();
            }else if(number.getText().toString().length()==0){
                Toast.makeText(context,"学号不能为空",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent=new Intent(context,GradeInfo.class);
                Bundle bd=new Bundle();
                bd.putString("number",number.getText().toString());
                bd.putString("year",mYear.get(position1));
                bd.putString("term",mTerm.get(position2));
                intent.putExtras(bd);
                startActivity(intent);
            }

        }
    }
}
