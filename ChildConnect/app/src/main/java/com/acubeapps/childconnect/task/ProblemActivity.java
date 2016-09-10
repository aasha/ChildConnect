package com.acubeapps.childconnect.task;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.R;
import com.acubeapps.childconnect.events.CourseClearedEvent;
import com.acubeapps.childconnect.model.McqOptions;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.model.QuestionType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ProblemActivity extends AppCompatActivity implements McqFragment.OnMcqFragmentInteractionListener{
    private int currentQuestionId = 0;
    private int maxQuestionId = 5;

    String packageName;
    List<QuestionDetails> questionDetailsList;

    McqFragment mcqFragment;
    SubjectiveFragment subjectiveFragment;

    @Inject
    SharedPreferences preferences;

    @Inject
    EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        Injectors.appComponent().injectProblemActivity(this);
        String courseId = getIntent().getStringExtra(Constants.COURSE_ID);
        packageName = getIntent().getStringExtra(Constants.PACKAGE_NAME);
        currentQuestionId = preferences.getInt(Constants.QUESTION_ID, 0);
        questionDetailsList = getAllQuestions(courseId);
        if (currentQuestionId >= questionDetailsList.size()) {
            currentQuestionId = 0;
        }
        maxQuestionId = currentQuestionId + 5;
        if (maxQuestionId >= questionDetailsList.size()) {
            maxQuestionId = questionDetailsList.size();
        }
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        QuestionDetails questionDetails = questionDetailsList.get(currentQuestionId);
        showQuestion(questionDetails);
    }

    private List<QuestionDetails> getAllQuestions(String courseId){
        List<QuestionDetails> questionDetailsList = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            QuestionDetails questionDetails = new QuestionDetails();
            questionDetails.questionId = index + "";
            questionDetails.questionText = "How are you " + index;
            questionDetails.questionType = QuestionType.MCQ;
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
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        if (currentQuestionId < maxQuestionId) {
            showQuestion(questionDetailsList.get(currentQuestionId));
        } else {
            eventBus.post(new CourseClearedEvent(packageName, System.currentTimeMillis()));
            finish();
        }
    }

    @Override
    public void onFailedAttempt() {
        currentQuestionId++;
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        if (currentQuestionId < maxQuestionId) {
            showQuestion(questionDetailsList.get(currentQuestionId));
        } else {
            eventBus.post(new CourseClearedEvent(packageName, System.currentTimeMillis()));
            finish();
        }
    }

    private void showQuestion(QuestionDetails questionDetails) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (null != mcqFragment) {
            ft.remove(mcqFragment);
        }
        if (null != subjectiveFragment) {
            ft.remove(subjectiveFragment);
        }
        if (questionDetails.questionType == QuestionType.MCQ) {
            mcqFragment = McqFragment.newInstance(questionDetails);
            ft.add(R.id.activity_problem, mcqFragment, "MCQ");
        } else {
//            SubjectiveFragment subjectiveFragment = SubjectiveFragment.newInstance(questionDetails);
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
        }
        ft.commit();
    }
}
