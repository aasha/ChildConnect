package com.acubeapps.childconnect.task;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.Injectors;
import com.acubeapps.childconnect.R;
import com.acubeapps.childconnect.events.CourseClearedEvent;
import com.acubeapps.childconnect.helpers.AppPolicyManager;
import com.acubeapps.childconnect.model.BaseResponse;
import com.acubeapps.childconnect.model.LocalCourse;
import com.acubeapps.childconnect.model.QuestionDetails;
import com.acubeapps.childconnect.model.QuestionType;
import com.acubeapps.childconnect.network.NetworkInterface;
import com.acubeapps.childconnect.network.NetworkResponse;
import com.acubeapps.childconnect.utils.Device;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ProblemActivity extends AppCompatActivity implements McqFragment.OnMcqFragmentInteractionListener ,
    SubjectiveFragment.OnSubjectiveFragmentInteractionListener{
    private int currentQuestionId = 0;
    private int maxQuestionId = 2;

    String packageName;
    List<QuestionDetails> questionDetailsList;

    List<QuestionDetails> solutionList;

    McqFragment mcqFragment;
    SubjectiveFragment subjectiveFragment;

    @Inject
    SharedPreferences preferences;

    @Inject
    AppPolicyManager appPolicyManager;

    @Inject
    EventBus eventBus;

    @Inject
    NetworkInterface networkInterface;

    @BindView(R.id.btn_done)
    Button btnDone;

    @BindView(R.id.txt_intro)
    TextView txtIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);
        Injectors.appComponent().injectProblemActivity(this);
        ButterKnife.bind(this);
        String courseId = preferences.getString(Constants.COURSE_ID, null);
        packageName = getIntent().getStringExtra(Constants.PACKAGE_NAME);
        currentQuestionId = preferences.getInt(Constants.QUESTION_ID, 0);
        LocalCourse courseDetails = appPolicyManager.getCourse();
        questionDetailsList = courseDetails.getQuestionDetailsList();
        solutionList = new ArrayList<>();
        solutionList.addAll(questionDetailsList);
        if (currentQuestionId >= questionDetailsList.size()) {
            currentQuestionId = 0;
        }
        maxQuestionId = currentQuestionId + 2;
        if (maxQuestionId >= questionDetailsList.size()) {
            maxQuestionId = questionDetailsList.size();
        }
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDone.setVisibility(View.GONE);
                txtIntro.setVisibility(View.GONE);
                QuestionDetails questionDetails = questionDetailsList.get(currentQuestionId);
                showQuestion(questionDetails);
            }
        });
    }

    @Override
    public void onSuccessfulAttempt(String optionSelected) {
        solutionList.get(currentQuestionId).solution = optionSelected;
        currentQuestionId++;
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        if (currentQuestionId < maxQuestionId) {
            showQuestion(questionDetailsList.get(currentQuestionId));
        } else {
            sessionDone();
        }
    }

    @Override
    public void onFailedAttempt(String optionSelected) {
        solutionList.get(currentQuestionId).solution = optionSelected;
        currentQuestionId++;
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        if (currentQuestionId < maxQuestionId) {
            showQuestion(questionDetailsList.get(currentQuestionId));
        } else {
            sessionDone();
        }
    }

    private void sessionDone(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (null != mcqFragment) {
            ft.remove(mcqFragment);
        }
        if (null != subjectiveFragment) {
            ft.remove(subjectiveFragment);
        }
        ft.commit();
        btnDone.setVisibility(View.VISIBLE);
        txtIntro.setVisibility(View.VISIBLE);
        txtIntro.setText("Good job !! \n You are good to go back to your app ");
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventBus.post(new CourseClearedEvent(packageName, System.currentTimeMillis()));
                String childId = preferences.getString(Constants.CHILD_ID, null);
                String courseId = preferences.getString(Constants.COURSE_ID, null);
                networkInterface.sendCompleteResult(childId, courseId, solutionList, new NetworkResponse<BaseResponse>() {
                    @Override
                    public void success(BaseResponse baseResponse, Response response) {
                        Log.d(Constants.LOG_TAG, "results submitted to server");
                    }

                    @Override
                    public void failure(BaseResponse baseResponse) {

                    }

                    @Override
                    public void networkFailure(Throwable error) {

                    }
                });
                finish();
            }
        });
    }

    private void showQuestion(QuestionDetails questionDetails) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (null != mcqFragment) {
            ft.remove(mcqFragment);
        }
        if (null != subjectiveFragment) {
            ft.remove(subjectiveFragment);
        }
        if (questionDetails.questionType == QuestionType.mcq) {
            mcqFragment = McqFragment.newInstance(questionDetails);
            ft.add(R.id.activity_problem, mcqFragment, "mcq");
        } else {
            subjectiveFragment = SubjectiveFragment.newInstance(questionDetails);
            ft.add(R.id.activity_problem, subjectiveFragment, "Subjective");
        }
        ft.commit();
    }

    @Override
    public void onAttempt(File pictureFile) {
        String path = Device.uploadImageToServer(this, pictureFile);
        solutionList.get(currentQuestionId).solution = path;
        currentQuestionId++;
        preferences.edit().putInt(Constants.QUESTION_ID, currentQuestionId).apply();
        if (currentQuestionId < maxQuestionId) {
            showQuestion(questionDetailsList.get(currentQuestionId));
        } else {
            sessionDone();
        }
    }
}
