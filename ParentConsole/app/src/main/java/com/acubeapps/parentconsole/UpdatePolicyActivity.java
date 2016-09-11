package com.acubeapps.parentconsole;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.acubeapps.parentconsole.model.AppConfig;
import com.acubeapps.parentconsole.model.AppSessionConfig;
import com.acubeapps.parentconsole.model.AppStatus;
import com.acubeapps.parentconsole.model.BaseResponse;
import com.acubeapps.parentconsole.model.ChildDetails;
import com.acubeapps.parentconsole.model.GetUsageConfigResponse;
import com.acubeapps.parentconsole.model.Policy;
import com.acubeapps.parentconsole.network.NetworkInterface;
import com.acubeapps.parentconsole.network.NetworkResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class UpdatePolicyActivity extends AppCompatActivity implements View.OnClickListener{

    ChildDetails childDetails;

    @BindView(R.id.spinner_appname)
    Spinner spinnerAppName;

    @BindView(R.id.txt_AppName)
    TextView txtAppName;

    @BindView(R.id.spinner_Status)
    Spinner spinnerStatus;

    @BindView(R.id.txt_StartTime)
    EditText txtStartTime;

    @BindView(R.id.txt_EndTime)
    EditText txtEndTime;

    @BindView(R.id.txt_Duration)
    EditText txtDuration;

    @BindView(R.id.txt_Task)
    Spinner spinnerTask;

    @BindView(R.id.btn_submit)
    Button btnSubmitPolicy;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    NetworkInterface networkInterface;

    String appPackageName;
    String displayName;
    Policy policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_policy);
        Injectors.appComponent().injectUpdatePolicyActivity(this);
        childDetails = getIntent().getParcelableExtra(Constants.CHILD_DETAILS);
        setTitle(childDetails.name);
        ButterKnife.bind(this);
        networkInterface.getChildUsagePolicy(childDetails.childId, new NetworkResponse<GetUsageConfigResponse>() {
            @Override
            public void success(GetUsageConfigResponse getUsageConfigResponse, Response response) {
                policy = getUsageConfigResponse.policy;
                if (policy == null) {
                    policy = getDummyPolicy();
                }
                bindData();
            }

            @Override
            public void failure(GetUsageConfigResponse getUsageConfigResponse) {
                policy = getDummyPolicy();
                bindData();
            }

            @Override
            public void networkFailure(Throwable error) {
                policy = getDummyPolicy();
                bindData();
            }
        });
        btnSubmitPolicy.setOnClickListener(this);

    }

    private Policy getDummyPolicy(){
        Policy policy = new Policy();
        policy.courseId = "SampleCourse";
        policy.appConfigList = new ArrayList<>();
        List<AppSessionConfig> appSessionConfigList = new ArrayList<>();
        AppSessionConfig appSessionConfig = new AppSessionConfig(0, 0, 0, AppStatus.ALLOWED);
        appSessionConfigList.add(appSessionConfig);
        AppConfig appConfig = new AppConfig("com.facebook.katana", "Facebook" ,appSessionConfigList);
        policy.appConfigList.add(appConfig);
        return policy;
    }

    private void bindData(){
        final List<AppConfig> appConfigList = policy.appConfigList;
        List<String> appNameList = new ArrayList<>();
        for (int index = 0; index < appConfigList.size(); index++) {
            appNameList.add(appConfigList.get(index).getDisplayName());
        }
        ArrayAdapter<String> spinnerAppAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, appNameList);
        spinnerAppName.setAdapter(spinnerAppAdapter);
        updateAllFields(policy.courseId, appConfigList.get(0));
        spinnerAppName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    AppConfig appConfig = appConfigList.get(position);
                    updateAllFields(policy.courseId, appConfig);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateAllFields(String courseId, AppConfig appConfig) {
        try {
            appPackageName = appConfig.getAppName();
            displayName = appConfig.getDisplayName();
            txtAppName.setText(appConfig.getDisplayName());
            AppSessionConfig appSessionConfig = appConfig.getAppSessionConfigList().get(0);
            int minutes = (int) (appSessionConfig.getSessionStartTime() % 3600000);
            int hours = (int) (appSessionConfig.getSessionStartTime() / 3600000);
            txtStartTime.setText(hours + ":" + minutes);
            minutes = (int) (appSessionConfig.getSessionEndTime() % 3600000);
            hours = (int) (appSessionConfig.getSessionEndTime() / 3600000);
            txtEndTime.setText(hours + ":" + minutes);
            txtDuration.setText("" + appSessionConfig.getSessionAllowedDuration() / 60000);
            List<AppStatus> appStatusList = new ArrayList<>();
            appStatusList.add(AppStatus.ALLOWED);
            appStatusList.add(AppStatus.BLOCKED);
            ArrayAdapter<AppStatus> spinnerStatusAdapter = new ArrayAdapter<AppStatus>(this, android.R.layout.simple_spinner_item, appStatusList);
            spinnerStatus.setAdapter(spinnerStatusAdapter);
            spinnerStatus.setSelection(spinnerStatusAdapter.getPosition(appSessionConfig.getStatus()));

            List<String> taskList = new ArrayList<>();
            taskList.add("NONE");
            if (courseId != null) {
                taskList.add(courseId);
            }
            ArrayAdapter<String> spinnerTaskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, taskList);
            spinnerTask.setAdapter(spinnerTaskAdapter);
            if (courseId != null) {
                spinnerTask.setSelection(1);
            } else {
                spinnerTask.setSelection(0);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                try {
                    boolean status;
                    status = validateFields(txtStartTime.getText().toString());
                    if (status == false) {
                        Toast.makeText(UpdatePolicyActivity.this, "Please insert valid start time", Toast.LENGTH_LONG).show();
                        return;
                    }
                    status = validateFields(txtEndTime.getText().toString());
                    if (status == false) {
                        Toast.makeText(UpdatePolicyActivity.this, "Please insert valid end time", Toast.LENGTH_LONG).show();
                        return;
                    }
                    status = validateFields(txtDuration.getText().toString());
                    if (status == false) {
                        Toast.makeText(UpdatePolicyActivity.this, "Please insert valid duration time", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String parentId = sharedPreferences.getString(Constants.PARENT_ID, null);
                    policy.courseId = spinnerTask.getSelectedItem().toString();
                    List<AppConfig> appConfigList = policy.appConfigList;
                    String startTimeStr =  txtStartTime.getText().toString();
                    String[] times = startTimeStr.split(":");
                    long hours = Integer.valueOf(times[0]);
                    long mins = Integer.valueOf(times[1]);
                    long startTime = hours * 3600000 + mins * 60000;

                    String endTimeStr =  txtEndTime.getText().toString();
                    times = endTimeStr.split(":");
                    hours = Integer.valueOf(times[0]);
                    mins = Integer.valueOf(times[1]);
                    long endTime = hours * 3600000 + mins * 60000;

                    String durationStr = txtDuration.getText().toString();
                    long duration = Integer.valueOf(durationStr) * 60000;
                    AppStatus appStatus = (AppStatus) spinnerStatus.getSelectedItem();

                    List<AppSessionConfig> appSessionConfigList = null;
                    appSessionConfigList = new ArrayList<>();
                    AppSessionConfig appSessionConfig = new AppSessionConfig(startTime, endTime, duration, appStatus);
                    appSessionConfigList.add(appSessionConfig);
                    AppConfig appConfig = null;
                    for (AppConfig appConfigIndex: appConfigList){
                        if (appConfigIndex.getAppName().equals(appPackageName)){
                            appConfig = appConfigIndex;
                        }
                    }
                    if (appConfig != null){
                        appConfigList.remove(appConfig);

                    }
                    appConfig = new AppConfig(appPackageName, displayName, appSessionConfigList);
                    appConfigList.add(appConfig);
                    policy.appConfigList = appConfigList;
                    networkInterface.setChildUsagePolicy(childDetails.childId, parentId, policy, new NetworkResponse<BaseResponse>(){

                        @Override
                        public void success(BaseResponse baseResponse, Response response) {
                            Toast.makeText(UpdatePolicyActivity.this, "Successfully updated policy", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failure(BaseResponse baseResponse) {
                            Toast.makeText(UpdatePolicyActivity.this, "Failed to update policy", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void networkFailure(Throwable error) {
                            Toast.makeText(UpdatePolicyActivity.this, "Failed to update policy", Toast.LENGTH_LONG).show();
                        }
                    });
                    bindData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private boolean validateFields(String text){
        return (text != null && !"".equals(text));
    }
}
