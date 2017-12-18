package com.gholl.pinyin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gholl.PinyinTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gholl on 2016/6/12.
 * Email:slgogo@foxmail.com
 */
public class LessonDeatilAdapter extends BaseAdapter {
    Context context;
    List<Example> list = new ArrayList<>();
    public LessonDeatilAdapter(Context context,List<Example> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView != null){
            holder = (ViewHolder)convertView.getTag();
        }else {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.f_lesson_detail_item,null);
            convertView.setTag(holder);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_attribute = (TextView)convertView.findViewById(R.id.tv_attribute);
            holder.pinyinTextView = (PinyinTextView) convertView.findViewById(R.id.pinyinTextView);
        }
        Example example = list.get(position);
        holder.tv_name.setText(example.getName());
        holder.tv_attribute.setText(example.getAttribute());
        holder.pinyinTextView.setText(example.getExampleText());
        holder.pinyinTextView.setShowPinyin(example.isShowPy());
        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_attribute;
        PinyinTextView pinyinTextView;
    }
}
