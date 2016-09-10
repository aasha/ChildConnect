package com.acubeapps.childconnect.task;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.R;
import com.acubeapps.childconnect.model.McqOptions;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.model.QuestionType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ProblemActivity extends AppCompatActivity implements McqFragment.OnMcqFragmentInteractionListener{
    private int currentQuestionId = 0;
    private int maxQuestionId = 5;
    List<QuestionDetails> questionDetailsList;

    McqFragment mcqFragment;
    SubjectiveFragment subjectiveFragment;

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        Injectors.appComponent().injectProblemActivity(this);
        String courseId = getIntent().getStringExtra(Constants.COURSE_ID);
        currentQuestionId = preferences.getInt(Constants.QUESTION_ID, 0);
        questionDetailsList = getAllQuestions(courseId);
        if (questionDetailsList.size() >= currentQuestionId) {
            currentQuestionId = 0;
        }
        maxQuestionId = currentQuestionId + 5;
        if (maxQuestionId >= questionDetailsList.size()) {
            maxQuestionId = questionDetailsList.size();
        }
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        QuestionDetails questionDetails = questionDetailsList.get(currentQuestionId);
        Log.e("AASHA", "create  Current q " + currentQuestionId);
        Log.e("AASHA", "create Current q "  + maxQuestionId);
        showQuestion(questionDetails);
    }

    private List<QuestionDetails> getAllQuestions(String courseId){
        List<QuestionDetails> questionDetailsList = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            QuestionDetails questionDetails = new QuestionDetails();
            questionDetails.questionId = index + "";
            questionDetails.questionText = "How are you " + index;
            questionDetails.questionType = QuestionType.MCQ.name();
            questionDetails.options = new ArrayList<McqOptions>();
            McqOptions options = new McqOptions(1, "Good");
            questionDetails.options.add(options);
            options = new McqOptions(2, "Bad");
            questionDetails.options.add(options);
            questionDetails.solution = 1;
            questionDetailsList.add(questionDetails);
        }
        return questionDetailsList;
    }

    @Override
    public void onSuccessfulAttempt() {
        currentQuestionId++;
        Log.e("AASHA", "suc  Current q " + currentQuestionId);
        Log.e("AASHA", "succ Current q "  + maxQuestionId);
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        if (currentQuestionId < maxQuestionId) {
            showQuestion(questionDetailsList.get(currentQuestionId));
        }
    }

    @Override
    public void onFailedAttempt() {
        currentQuestionId++;
        Log.e("AASHA", "failed  Current q " + currentQuestionId);
        Log.e("AASHA", "failed Current q "  + maxQuestionId);
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        if (currentQuestionId < maxQuestionId) {
            showQuestion(questionDetailsList.get(currentQuestionId));
        }
    }

    private void showQuestion(QuestionDetails questionDetails) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (null != mcqFragment) {
            ft.remove(mcqFragment).commit();
        }
        if (null != subjectiveFragment) {
            ft.remove(subjectiveFragment).commit();
        }
        if (questionDetails.questionType == QuestionType.MCQ.name()) {
            McqFragment mcqFragment = McqFragment.newInstance(questionDetails);
            ft.add(R.id.activity_problem, mcqFragment, "MCQ").commit();
        } else {
//            SubjectiveFragment subjectiveFragment = SubjectiveFragment.newInstance(questionDetails);
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
        }
    }
}
