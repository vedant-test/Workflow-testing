package com.example.quizapp.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizapp.QuestionModel.QuestionModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class QuestionListViewModel extends AndroidViewModel implements Response.Listener<String>, Response.ErrorListener {
    //    api
    private final String API = "https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/db.json";
    private final RequestQueue queue;
    private final MutableLiveData<List<QuestionModal>> questionLiveData = new MutableLiveData<List<QuestionModal>>();
    private final MutableLiveData<RequestStatus> requestStatusLiveData = new MutableLiveData<RequestStatus>();
    private final MutableLiveData<Integer> indexLiveData = new MutableLiveData<Integer>();

    public QuestionListViewModel(Application application) {
        super(application);
        queue = Volley.newRequestQueue(application);
        requestStatusLiveData.postValue(RequestStatus.IN_PROGRESS);
        fetchQuestions();
    }

    //    fetching questions
    private void fetchQuestions() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API, this, this);
        queue.add(stringRequest);
    }

    //    updating questions live data
    public void updateQuestionsLiveData(List<QuestionModal> questionModals) {
        questionLiveData.postValue(questionModals);
    }

    //    updating index live data
    public void updateIndexLiveData(Integer updatedIndex) {
        indexLiveData.postValue(updatedIndex);
    }

    //    getting questions live data
    public MutableLiveData<List<QuestionModal>> getQuestionsLiveData() {
        return questionLiveData;
    }

    //    getting request status
    public MutableLiveData<RequestStatus> getRequestStatusLiveData() {
        return requestStatusLiveData;
    }

    //    getting index live data
    public MutableLiveData<Integer> getIndexLiveData() {
        return indexLiveData;
    }

    @Override
    public void onResponse(String response) {
        try {
            List<QuestionModal> questionModals = parseResponse(response);
            questionLiveData.postValue(questionModals);
            requestStatusLiveData.postValue(RequestStatus.SUCCEEDED);
        } catch (JSONException e) {
            requestStatusLiveData.postValue(RequestStatus.FAILED);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        requestStatusLiveData.postValue(RequestStatus.FAILED);
    }

    //    parsing response in required data types
    public List<QuestionModal> parseResponse(String response) throws JSONException {
        List<QuestionModal> questions = new ArrayList<QuestionModal>();
        JSONObject jsonObject = new JSONObject(response);
        JSONArray jsonArray = null;
//        getting all questions along with answers and options
        jsonArray = jsonObject.getJSONArray("questions");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject newObject = (JSONObject) jsonArray.get(i);
            String question = newObject.optString("question");
//            getting options
            JSONArray jsonArray1 = (JSONArray) newObject.opt("options");
            List<String> options = new ArrayList<String>();
            for (int j = 0; j < jsonArray1.length(); j++) {
                options.add(String.valueOf(jsonArray1.get(j)));
            }
            int correctAnswer = Integer.parseInt(newObject.optString("correct_option"));
            QuestionModal questionModal = new QuestionModal(question, correctAnswer, options.get(0), options.get(1), options.get(2), options.get(3));
            questions.add(questionModal);
        }
        return questions;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public enum RequestStatus {
        IN_PROGRESS, FAILED, SUCCEEDED
    }
}
