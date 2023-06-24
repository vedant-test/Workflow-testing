package com.example.quizapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizapp.Alert.Alerts;
import com.example.quizapp.QuestionModel.QuestionModal;
import com.example.quizapp.R;
import com.example.quizapp.ViewModels.QuestionListViewModel;
import com.example.quizapp.ViewModels.TimerViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionDetailScreen extends Fragment {

    private int location;
    private QuestionListViewModel mQuestionListViewModel;
    private List<QuestionModal> questionModals;
    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;

    //    handling clicks on the options
    CompoundButton.OnCheckedChangeListener optionSelected = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.option1:
                    option1.setChecked(true);
                    questionModals.get(location).setSelectedAnswer(0);
                    mQuestionListViewModel.updateQuestionsLiveData(questionModals);
                    break;

                case R.id.option2:
                    option2.setChecked(true);
                    questionModals.get(location).setSelectedAnswer(1);
                    mQuestionListViewModel.updateQuestionsLiveData(questionModals);
                    break;
                case R.id.option3:
                    option3.setChecked(true);
                    questionModals.get(location).setSelectedAnswer(2);
                    mQuestionListViewModel.updateQuestionsLiveData(questionModals);
                    break;
                case R.id.option4:
                    option4.setChecked(true);
                    questionModals.get(location).setSelectedAnswer(3);
                    mQuestionListViewModel.updateQuestionsLiveData(questionModals);
                    break;
            }
        }
    };
    private Button previousButton;
    private Button nextButton;
    private ImageButton bookMarkButton;
    private TextView question;
    private Button submitButton;

    public QuestionDetailScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_detail_screen, container, false);
        createTimerViewModel();
        createQuestionViewModel();
        bindingAllItems(v);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                option1.setOnCheckedChangeListener(optionSelected);
                option2.setOnCheckedChangeListener(optionSelected);
                option3.setOnCheckedChangeListener(optionSelected);
                option4.setOnCheckedChangeListener(optionSelected);
                bookMarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        questionModals.get(location).setIsBookMarked();
                        mQuestionListViewModel.updateQuestionsLiveData(questionModals);
                    }
                });
                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (location > 0 && location < (questionModals.size() - 1)) {
                            mQuestionListViewModel.updateIndexLiveData(location - 1);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "First Question", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (location < (questionModals.size() - 1)) {
                            mQuestionListViewModel.updateIndexLiveData(location + 1);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Last Question", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Alerts.alertDialogShow(getActivity(),getActivity().getSupportFragmentManager());
                    }
                });
            }
        }, 2000);
        return v;
    }

    //    binding all items with viw
    public void bindingAllItems(View v) {
        option1 = (RadioButton) v.findViewById(R.id.option1);
        option2 = (RadioButton) v.findViewById(R.id.option2);
        option3 = (RadioButton) v.findViewById(R.id.option3);
        option4 = (RadioButton) v.findViewById(R.id.option4);
        question = (TextView) v.findViewById(R.id.question);
        bookMarkButton = (ImageButton) v.findViewById(R.id.bookmark);
        previousButton = (Button) v.findViewById(R.id.previous_button);
        nextButton = (Button) v.findViewById(R.id.next_button);
        submitButton=(Button) v.findViewById(R.id.submit);
    }

    //    updating which question to be showed on the screen
    private void bind() {
        QuestionModal item = questionModals.get(location);
        question.setText(item.getQuestion());
        option1.setText(item.getOption1());
        option2.setText(item.getOption2());
        option3.setText(item.getOption3());
        option4.setText(item.getOption4());
        option4.setChecked(false);
        option3.setChecked(false);
        option2.setChecked(false);
        option1.setChecked(false);
        if (item.getSelectedAnswer() == 0) {
            option1.setChecked(true);
        } else if (item.getSelectedAnswer() == 1) {
            option2.setChecked(true);
        } else if (item.getSelectedAnswer() == 2) {
            option3.setChecked(true);
        } else if (item.getSelectedAnswer() == 3) {
            option4.setChecked(true);
        }
        if (item.getIsBookMarked()) {
            bookMarkButton.setBackgroundResource(R.drawable.bookmarked);
        } else {
            bookMarkButton.setBackgroundResource(R.drawable.bookmark_it);
        }
    }

    //creating timer
    private void createTimerViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(getActivity());
        TimerViewModel mTimerViewModel = viewModelProvider.get(TimerViewModel.class);
//        defining observer
        final Observer<Long> elapsedTimeObserver = new Observer<Long>() {
            @Override
            public void onChanged(@Nullable final Long newVal) {
                String newText = getActivity().getResources().getString(
                        R.string.seconds, (newVal / 60), (newVal % 60));
                ((TextView) getView().findViewById(R.id.timer)).setText(newText);
            }
        };
//        setting up the observer
        mTimerViewModel.getElapsedTime().observe(getViewLifecycleOwner(), elapsedTimeObserver);
    }

    private void createQuestionViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        mQuestionListViewModel = viewModelProvider.get(QuestionListViewModel.class);
        final Observer<List<QuestionModal>> questionsObserver = new Observer<List<QuestionModal>>() {
            @Override
            public void onChanged(List<QuestionModal> questionModalList) {
                handleQuestionModalChange(questionModalList);
            }
        };
        final Observer<Integer> indexObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                handleIndexChange(integer);
            }
        };
        mQuestionListViewModel.getQuestionsLiveData().observe(getViewLifecycleOwner(), questionsObserver);
        mQuestionListViewModel.getIndexLiveData().observe(getViewLifecycleOwner(), indexObserver);
    }


    private void handleQuestionModalChange(List<QuestionModal> questionModals) {
        this.questionModals = questionModals;
        bind();
    }

    private void handleIndexChange(Integer integer) {
        this.location = integer;
        bind();
    }
}