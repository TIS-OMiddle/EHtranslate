package com.ljh.dragoon.ehtranslate;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.loopj.android.image.SmartImageView;

/**
 * Created by dragoon on 2018/7/27.
 */

public class MyAdapter extends BaseAdapter {
    public List<MyTranslateBean> list;
    private Context context;
    private boolean visiable;

    public MyAdapter(List<MyTranslateBean> list, Context context) {
        this(list,context,true);
    }

    public MyAdapter(List<MyTranslateBean> list, Context context, boolean visiable) {
        this.list = list;
        this.context = context;
        this.visiable = visiable;
    }

    public boolean isVisiable() {
        return visiable;
    }

    public void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Helper helper;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item, parent, false);
            helper = new Helper();
            helper.t1 = convertView.findViewById(R.id.lv_item_tv);
            helper.s1=convertView.findViewById(R.id.lv_item_iv);
            convertView.setTag(helper);
        }
        helper = (Helper) convertView.getTag();
        MyTranslateBean bean=list.get(position);
        helper.t1.setText(bean.getCn());
        if(visiable){
            helper.s1.setVisibility(View.VISIBLE);
            helper.s1.setImageUrl(bean.getUrl(),R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        }
        else helper.s1.setVisibility(View.GONE);
        return convertView;
    }

    class Helper {
        public TextView t1;
        public SmartImageView s1;
    }
}
