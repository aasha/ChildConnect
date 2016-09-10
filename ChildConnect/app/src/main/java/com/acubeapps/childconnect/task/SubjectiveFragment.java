package com.acubeapps.childconnect.task;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.R;
import com.acubeapps.childconnect.model.QuestionDetails;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectiveFragment extends Fragment implements View.OnClickListener{
    private static final String QUESTION_DETAILS = "QUESTION_DETAILS";

    private QuestionDetails questionDetails;

    private OnSubjectiveFragmentInteractionListener mListener;

    @BindView(R.id.txt_question)
    TextView txtQuestion;

    @BindView(R.id.btn_done)
    Button btnDone;

    @BindView(R.id.camera_preview)
    FrameLayout layoutCameraPreview;

    @BindView(R.id.img_capture)
    ImageView imgCapture;

    File pictureFile;
    private Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;

    public SubjectiveFragment() {
        // Required empty public constructor
    }

    public static SubjectiveFragment newInstance(QuestionDetails questionDetails) {
        SubjectiveFragment fragment = new SubjectiveFragment();
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
        View fragmentView =  inflater.inflate(R.layout.fragment_subjective, container, false);
        ButterKnife.bind(this, fragmentView);
        bindData();
        return fragmentView;
    }

    private void bindData() {
        try {
            txtQuestion.setText(questionDetails.questionText);
            mCamera = getCameraInstance();
            //mCamera.setDisplayOrientation(90);
            mCamera.enableShutterSound(true);
            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(getActivity(), mCamera);
            layoutCameraPreview.addView(mPreview);
            btnDone.setOnClickListener(this);
            imgCapture.setOnClickListener(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubjectiveFragmentInteractionListener) {
            mListener = (OnSubjectiveFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMcqFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnSubjectiveFragmentInteractionListener) {
            mListener = (OnSubjectiveFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMcqFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            mCamera.stopPreview();
            mCamera.release();
        } catch (Exception e){
            e.printStackTrace();
        }
        mCamera = null;
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_done:
                mListener.onAttempt(pictureFile);
                break;
            case R.id.img_capture:
                mCamera.takePicture(null, null, mPicture);
                break;
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(Constants.LOG_TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(Constants.LOG_TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(Constants.LOG_TAG, "Error accessing file: " + e.getMessage());
            }
            mCamera.stopPreview();
            btnDone.setBackgroundResource(R.drawable.rounded_corner_blue);
            btnDone.setEnabled(true);
        }
    };

    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "ChildConnect");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(Constants.LOG_TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public interface OnSubjectiveFragmentInteractionListener {
        void onAttempt(File pictureFile);
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            e.printStackTrace();
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}
