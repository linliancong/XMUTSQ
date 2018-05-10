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


/**
 * Created by Administrator on 2017/11/29.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT=4;
    private final int PAGER_1=0;
    private final int PAGER_2=1;
    private final int PAGER_3=2;
    private final int PAGER_4=3;

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
            case PAGER_1:
                fragment=lostAndFoundH;
                break;
            case PAGER_2:
                fragment= bookH;
                break;
            case PAGER_3:
                fragment= gradeH;
                break;
            case PAGER_4:
                fragment= noticeH;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
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
