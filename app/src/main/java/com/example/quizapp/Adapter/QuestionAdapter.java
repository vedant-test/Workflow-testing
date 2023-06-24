package com.example.quizapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.QuestionModel.QuestionModal;
import com.example.quizapp.R;

import java.util.List;

//adapter class
public class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {
    //    questions list
    private final List<QuestionModal> questions;
    private final OnItemClickListner mOnItemClickListener;

    public QuestionAdapter(List<QuestionModal> list, QuestionAdapter.OnItemClickListner listner) {
        questions = list;
        mOnItemClickListener = listner;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        creating view for an item
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question, parent, false);
//        passing that view to the view holder to create view holder
        return new QuestionViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
//        binding view holder with the data
        holder.bind(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    //    interface for listning on click events
    public interface OnItemClickListner {
        void itemClicked(int location);
    }
}

//view holder
class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView question;
    public QuestionAdapter.OnItemClickListner listner;

    public QuestionViewHolder(View itemView, QuestionAdapter.OnItemClickListner listner) {
        super(itemView);
        this.listner = listner;
        question = itemView.findViewById(R.id.question);
        itemView.setOnClickListener(this);

    }

    public void bind(QuestionModal questionModal) {
        question.setText(questionModal.getQuestion());
    }

    @Override
    public void onClick(View view) {
        listner.itemClicked(getAdapterPosition());
    }
}