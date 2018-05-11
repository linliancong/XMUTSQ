package com.example.administrator.ut;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class AdapterUT<T> extends BaseAdapter {

    private ArrayList<T> mData;
    private int mLayoutRes;         //布局id

    private static onItemClickListener mOnItemClickListener;

    public AdapterUT(String[] data, int opc_list_item){}
    public AdapterUT(ArrayList<T> mData, int mLayoutRes)
    {
        this.mData=mData;
        this.mLayoutRes=mLayoutRes;
    }

    public AdapterUT(T data, int opc_list_item) {

    }

    @Override
    public int getCount() {
        return mData!=null?mData.size():0;
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder= ViewHolder.bind(parent.getContext(),convertView,parent,mLayoutRes,position);
        bindView(holder,getItem(position));
        return holder.getItemView();
    }

    public abstract void bindView(ViewHolder holder,T obj);

    public static class ViewHolder
    {
        private SparseArray<View> mViews;    //存储ListView 的 item中的View
        private View item;                  //存放convertView
        private Context context;            //Context上下文
        private int position;               //游标

        /**
         *  构造方法，完成相关初始化
         */
        private ViewHolder(Context context,ViewGroup parent,int layoutRes)
        {
            mViews=new SparseArray<>();
            this.context=context;
            View convertView= LayoutInflater.from(context).inflate(layoutRes,parent,false);
            convertView.setTag(this);
            item=convertView;
        }

        /**
         * 绑定ViewHolder与item
        * */
        public static ViewHolder bind(Context context,View convertView,ViewGroup parent,int layoutRes,int position)
        {
            ViewHolder holder;
            if(convertView==null)
            {
                holder=new ViewHolder(context,parent,layoutRes);
            }
            else
            {
                holder= (ViewHolder) convertView.getTag();
                holder.item=convertView;
            }
            holder.position=position;
            return holder;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id)
        {
            T t=(T)mViews.get(id);
            if(t==null)
            {
                t= (T) item.findViewById(id);
                mViews.put(id,t);
            }
            return t;
        }

        /**
         * 获取当前条目
         */
        public View getItemView()
        {
            return item;
        }

        /**
         * 获取条目位置
         */
        public int getItemPosition()
        {
            return position;
        }

        /**
         * 设置文字
         */
        public ViewHolder setText(int id,CharSequence text)
        {
            View view=getView(id);
            if(view instanceof TextView)
            {
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置图片
         */
        public ViewHolder setImageResource(int id,int drwableRes)
        {
            View view=getView(id);
            if(view instanceof ImageView)
            {
                ((ImageView) view).setImageResource(drwableRes);
            }
            else
            {
                view.setBackgroundResource(drwableRes);
            }
            return this;
        }

        /**
         * 设置图片(Bitmap)
         */
        public ViewHolder setImageBitmap(int id,Bitmap drwableRes)
        {
            View view=getView(id);
            if(view instanceof ImageView)
            {
                ((ImageView) view).setImageBitmap(drwableRes);
            }
            return this;
        }

        /**
         * 设置点击监听
         */
        public ViewHolder setOnClickListener(int id)
        {
            getView(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(getItemPosition());
                }
            });
            return this;
        }

        /**
         * 设置可见
         */
        public ViewHolder setVisibility(int id,int visible)
        {
            getView(id).setVisibility(visible);
            return this;
        }

        /**
         * 设置标签
         */
        public ViewHolder setTag(int id,Object obj)
        {
            getView(id).setTag(obj);
            return this;
        }

    }

    /**
     * 按钮的监听接口
     */
    public interface onItemClickListener {
        void onItemClick(int i);
    }

    public void setOnItemClickListener(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
