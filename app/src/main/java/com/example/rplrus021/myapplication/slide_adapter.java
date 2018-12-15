package com.example.rplrus021.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class slide_adapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<question>questionArrayList;

    public slide_adapter (Context context,ArrayList<question>questionArrayList){
        this.context = context;
        this.questionArrayList = questionArrayList;
    }

    @Override
    public int getCount() {
        return this.questionArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        question question = questionArrayList.get(position);
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_item,container,false);
        TextView textView_question_id = (TextView)view.findViewById(R.id.text_view_question_id);
        TextView textView_question = (TextView)view.findViewById(R.id.text_view_question);
        textView_question_id.setText(""+question.getId());
        textView_question.setText(question.getQuestion());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
