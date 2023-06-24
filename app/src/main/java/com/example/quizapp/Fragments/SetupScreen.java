package com.example.quizapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.quizapp.Fragments.QuestionListScreen;
import com.example.quizapp.R;

public class SetupScreen extends Fragment {

    private final String[] instructions = {"Number of Questions:10", "Duration:30 minutes", "Do not leave the window before completing the test","All Questions are compulsory"};

    public SetupScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setup_screen, container, false);
        ListView instructionList = v.findViewById(R.id.instructions);
        ArrayAdapter<String> arr;
//        adding instructions into ArrayAdapter
        arr = new ArrayAdapter<String>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, instructions);
        instructionList.setAdapter(arr);
        Button startTestButton = (Button) v.findViewById(R.id.start_test);
        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionListScreen questionListScreen = new QuestionListScreen();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, questionListScreen).commit();
            }
        });
        return v;
    }
}