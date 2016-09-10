package com.acubeapps.childconnect.task;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.acubeapps.childconnect.R;
import com.acubeapps.childconnect.model.McqOptions;
import com.acubeapps.childconnect.model.QuestionDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMcqFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link McqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class McqFragment extends Fragment implements View.OnClickListener{
    private static final String QUESTION_DETAILS = "QUESTION_DETAILS";

    private QuestionDetails questionDetails;

    private OnMcqFragmentInteractionListener mListener;

    @BindView(R.id.txt_question)
    TextView txtQuestion;

    @BindView(R.id.btn_option_1)
    Button btnOption1;

    @BindView(R.id.btn_option_2)
    Button btnOption2;

    @BindView(R.id.btn_option_3)
    Button btnOption3;

    @BindView(R.id.btn_option_4)
    Button btnOption4;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    CountDownTimer countDownTimer;

    static Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public McqFragment() {
        // Required empty public constructor
    }

    public static McqFragment newInstance(QuestionDetails questionDetails) {
        McqFragment fragment = new McqFragment();
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_DETAILS, questionDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionDetails = (QuestionDetails) (getArguments().getSerializable(QUESTION_DETAILS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView =  inflater.inflate(R.layout.fragment_mcq, container, false);
        ButterKnife.bind(this, fragmentView);
        bindData();
        return fragmentView;
    }

    private void bindData() {
        txtQuestion.setText(questionDetails.questionText);
        List<McqOptions> optionsList = questionDetails.getOptions();
        switch (optionsList.size()){
            case 2:
                btnOption1.setText(optionsList.get(0).optionText);
                btnOption2.setText(optionsList.get(1).optionText);
                btnOption3.setVisibility(View.GONE);
                btnOption4.setVisibility(View.GONE);
                break;
            case 3:
                btnOption1.setText(optionsList.get(0).optionText);
                btnOption2.setText(optionsList.get(1).optionText);
                btnOption3.setText(optionsList.get(2).optionText);
                btnOption4.setVisibility(View.GONE);
                break;
            case 4:
                btnOption1.setText(optionsList.get(0).optionText);
                btnOption2.setText(optionsList.get(1).optionText);
                btnOption3.setText(optionsList.get(2).optionText);
                btnOption4.setText(optionsList.get(3).optionText);
                break;
        }

        btnOption1.setOnClickListener(this);
        btnOption2.setOnClickListener(this);
        btnOption3.setOnClickListener(this);
        btnOption4.setOnClickListener(this);
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                progressBar.setProgress(10 - (int)(l/1000));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(10);
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progess_bar_complete));
                postFailure();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnMcqFragmentInteractionListener) {
            mListener = (OnMcqFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMcqFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMcqFragmentInteractionListener) {
            mListener = (OnMcqFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMcqFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        countDownTimer.cancel();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_option_1:
                if(questionDetails.solution == 1){
                    btnOption1.setBackgroundResource(R.drawable.rounded_corner_green);
                    postSuccess();
                } else {
                    btnOption1.setBackgroundResource(R.drawable.rounded_corner_red);
                    postFailure();
                }
                break;
            case R.id.btn_option_2:
                if(questionDetails.solution == 2){
                    btnOption2.setBackgroundResource(R.drawable.rounded_corner_green);
                    postSuccess();
                } else {
                    btnOption2.setBackgroundResource(R.drawable.rounded_corner_red);
                    postFailure();
                }
                break;
            case R.id.btn_option_3:
                if(questionDetails.solution == 3){
                    btnOption3.setBackgroundResource(R.drawable.rounded_corner_green);
                    postSuccess();
                } else {
                    btnOption3.setBackgroundResource(R.drawable.rounded_corner_red);
                    postFailure();
                }
                break;
            case R.id.btn_option_4:
                if(questionDetails.solution == 4){
                    btnOption4.setBackgroundResource(R.drawable.rounded_corner_green);
                    postSuccess();
                } else {
                    btnOption4.setBackgroundResource(R.drawable.rounded_corner_red);
                    postFailure();
                }
                break;
        }
    }

    public interface OnMcqFragmentInteractionListener {
        void onSuccessfulAttempt();
        void onFailedAttempt();
    }

    private void postSuccess(){
        MAIN_HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onSuccessfulAttempt();
            }
        }, 500);
    }

    private void postFailure(){
        MAIN_HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onFailedAttempt();
            }
        }, 500);
    }
}
