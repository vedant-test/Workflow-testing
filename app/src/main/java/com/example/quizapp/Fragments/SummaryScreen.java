package com.example.quizapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizapp.QuestionModel.QuestionModal;
import com.example.quizapp.R;
import com.example.quizapp.ViewModels.QuestionListViewModel;
import com.example.quizapp.ViewModels.TimerViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SummaryScreen extends Fragment {
    private TextView timeTaken;
    private Button restartButton;
    private Button exitButton;
    private int finalScore;
    private List<QuestionModal> questionModalList;

    public SummaryScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summary_screen, container, false);
        timeTaken = (TextView) v.findViewById(R.id.time_taken);
        restartButton = (Button) v.findViewById(R.id.restart);
        exitButton = (Button) v.findViewById(R.id.exit);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetupScreen setupScreen = new SetupScreen();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, setupScreen).commit();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        getTimeTaken();
        getScore(v);
        return v;
    }

    //    displaying time taken to complete the test
    private void getTimeTaken() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(getActivity());
        TimerViewModel timerViewModel = viewModelProvider.get(TimerViewModel.class);
        Long timeRemaining = timerViewModel.getElapsedTime().getValue();
        Long time = 1800 - timeRemaining;
        String newText = getActivity().getResources().getString(
                R.string.seconds, (time / 60), (time % 60));
        timeTaken.setText(newText);
    }

    //    displaying score
    private void getScore(View v) {
        ViewModelProvider viewModel = new ViewModelProvider(requireActivity());
        QuestionListViewModel questionListViewModel = viewModel.get(QuestionListViewModel.class);
        final TextView scoreShower = v.findViewById(R.id.score);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                questionModalList = questionListViewModel.getQuestionsLiveData().getValue();
                int score = 0;
                for (int i = 0; i < questionModalList.size(); i++) {
                    QuestionModal questionModal = questionModalList.get(i);
                    if (questionModal.getAnswer() == questionModal.getSelectedAnswer()) {
                        score++;
                    }
                }
                finalScore = score;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scoreShower.setText(String.valueOf(finalScore / 10));
                    }
                });
            }
        }, 2000);
    }
}