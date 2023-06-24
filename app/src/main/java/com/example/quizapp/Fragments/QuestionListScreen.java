package com.example.quizapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.Adapter.QuestionAdapter;
import com.example.quizapp.Alert.Alerts;
import com.example.quizapp.QuestionModel.QuestionModal;
import com.example.quizapp.R;
import com.example.quizapp.ViewModels.QuestionListViewModel;
import com.example.quizapp.ViewModels.TimerViewModel;

import java.util.List;

public class QuestionListScreen extends Fragment implements QuestionAdapter.OnItemClickListner {
    //    question list view model
    private QuestionListViewModel mQuestionListViewModel;
    private Button submitButton;
    private ProgressDialog progressDialog;

    public QuestionListScreen() {
        // Required empty public constructor
    }

    @Override
    public void itemClicked(int location) {
//        on clicking particular question show that question on details page along with its options
        mQuestionListViewModel.updateIndexLiveData(location);
        QuestionDetailScreen questionDetailScreen = new QuestionDetailScreen();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.main_activity, questionDetailScreen).commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question_list_screen, container, false);
        createTimerViewModel();
        createQuestionList();
        submitButton = (Button) v.findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alerts.alertDialogShow(getActivity(), getActivity().getSupportFragmentManager());
            }
        });
        return v;
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

    //    create QuestionList view model and observe the live data
    private void createQuestionList() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        mQuestionListViewModel = viewModelProvider.get(QuestionListViewModel.class);
//        defining observer for questions list
        final Observer<List<QuestionModal>> questionsObserver = new Observer<List<QuestionModal>>() {
            @Override
            public void onChanged(List<QuestionModal> questionModalList) {
                handleQuestionChange(questionModalList);
            }
        };
//        defining observer for request status
        final Observer<QuestionListViewModel.RequestStatus> requestStatusObserver = new Observer<QuestionListViewModel.RequestStatus>() {
            @Override
            public void onChanged(QuestionListViewModel.RequestStatus requestStatus) {
                handleRequestStatus(requestStatus);
            }
        };
//        setting up the observers
        mQuestionListViewModel.getQuestionsLiveData().observe(getViewLifecycleOwner(), questionsObserver);
        mQuestionListViewModel.getRequestStatusLiveData().observe(getViewLifecycleOwner(), requestStatusObserver);
    }


    //    code to execute when some changes happen in given data
    private void handleQuestionChange(List<QuestionModal> questionModals) {
//        creating adapter instance
        QuestionAdapter questionAdapter = new QuestionAdapter(questionModals, this);
        RecyclerView recyclerView = getView().findViewById(R.id.questionRecyclerView);
//        setting up recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        setting up adapter
        recyclerView.setAdapter(questionAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    //    code to execute when some changes happen in request status
    private void handleRequestStatus(QuestionListViewModel.RequestStatus requestStatus) {
        switch (requestStatus) {
            case IN_PROGRESS:
                showSpinner();
                break;
            case SUCCEEDED:
                hideSpinner();
                break;
            case FAILED:
                failureDialog();
                break;
        }
    }

    private void showSpinner() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Fetching Questions");
            progressDialog.setMessage("Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void hideSpinner() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void failureDialog() {
        hideSpinner();
        Toast.makeText(getActivity(), "Questions Unavailable", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}