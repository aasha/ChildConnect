package com.acubeapps.childconnect.task;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.R;
import com.acubeapps.childconnect.model.McqOptions;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.model.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class ProblemActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        Injectors.appComponent().injectProblemActivity(this);
        String courseId = getIntent().getStringExtra(Constants.COURSE_ID);
        QuestionType courseType = getCourseType(courseId);
        if (courseType == QuestionType.MCQ) {
            Fragment newFragment = new McqFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
        } else {
            Fragment newFragment = new SubjectiveFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
        }
    }

    private List<QuestionDetails> getAllQuestions(String courseId){
        List<QuestionDetails> questionDetailsList = new ArrayList<>();
        QuestionDetails questionDetails = new QuestionDetails();
        questionDetails.questionId = "1";
        questionDetails.questionText = "How are you";
        questionDetails.questionType = QuestionType.MCQ.name();
        questionDetails.options = new ArrayList<McqOptions>();
        McqOptions options = new McqOptions();
        options.optionSeq = 1;
        options.optionText = "Good";
        questionDetails.options.add(options);
        options = new McqOptions();
        options.optionSeq = 2;
        options.optionText = "Bad";
        questionDetails.options.add(options);
        questionDetailsList.add(questionDetails);
        return questionDetailsList;
    }

}
