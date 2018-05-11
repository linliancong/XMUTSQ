package com.example.administrator.ut;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.administrator.xmutsq.BookH;
import com.example.administrator.xmutsq.GradeH;
import com.example.administrator.xmutsq.LostAndFoundH;
import com.example.administrator.xmutsq.NoticeH;

public class FragmentAdapter extends FragmentPagerAdapter {

    private final int PC =4;
    private final int P1 =0;
    private final int P2 =1;
    private final int P3 =2;
    private final int P4 =3;

    private Context context;

    private LostAndFoundH lostAndFoundH=null;
    private BookH bookH =null;
    private GradeH gradeH =null;
    private NoticeH noticeH =null;


    public FragmentAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context=context;

        lostAndFoundH=new LostAndFoundH(context);
        bookH =new BookH(context);
        gradeH =new GradeH(context);
        noticeH =new NoticeH(context);

    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position)
        {
            case P1:
                fragment= noticeH;
                break;
            case P2:
                fragment= bookH;
                break;
            case P3:
                fragment= gradeH;
                break;
            case P4:
                fragment=lostAndFoundH;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PC;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
