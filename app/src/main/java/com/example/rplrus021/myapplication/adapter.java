package com.example.rplrus021.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.Holder> {
    Context context;
    ArrayList<question>questionArrayList;

    public adapter(Context context,ArrayList<question>questionArrayList){
        this.context = context;
        this.questionArrayList = questionArrayList;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        final question question2=questionArrayList.get(i);
        holder.textView_question_id.setText(""+question2.getId());
        holder.textView_question.setText(question2.getQuestion());
    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textView_question_id,textView_question;
        public Holder(View itemView) {
            super(itemView);
            textView_question_id = (TextView)itemView.findViewById(R.id.text_view_question_id);
            textView_question = (TextView)itemView.findViewById(R.id.text_view_question);
        }
    }
}
