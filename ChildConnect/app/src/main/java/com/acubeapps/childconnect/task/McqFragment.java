package com.acubeapps.childconnect.task;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.acubeapps.childconnect.R;
import com.acubeapps.childconnect.model.QuestionDetails;

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
public class McqFragment extends Fragment {
    private static final String QUESTION_DETAILS = "QUESTION_DETAILS";

    private QuestionDetails questionDetails;

    private OnMcqFragmentInteractionListener mListener;

    @BindView(R.id.txt_question)
    TextView txtQuestion;

    @BindView(R.id.btn_done)
    Button btnDone;

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
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_mcq, container, false);
        ButterKnife.bind(this, fragmentView);
        txtQuestion.setText(questionDetails.questionText);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSuccessfulAttempt();
            }
        });
        return fragmentView;
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
        mListener = null;
    }

    public interface OnMcqFragmentInteractionListener {
        void onSuccessfulAttempt();
        void onFailedAttempt();
    }
}
