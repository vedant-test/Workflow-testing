package com.example.quizapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.quizapp.Fragments.SplashScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        showing splash screen
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SplashScreen splashScreen = SplashScreen.newInstance();
            fragmentManager.beginTransaction().replace(R.id.main_activity, splashScreen).commit();
        }
    }
}