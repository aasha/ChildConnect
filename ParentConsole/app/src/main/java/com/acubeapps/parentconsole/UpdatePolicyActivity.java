package com.acubeapps.parentconsole;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.acubeapps.parentconsole.model.AppConfig;
import com.acubeapps.parentconsole.model.AppSessionConfig;
import com.acubeapps.parentconsole.model.AppStatus;
import com.acubeapps.parentconsole.model.ChildDetails;
import com.acubeapps.parentconsole.model.GetUsageConfigResponse;
import com.acubeapps.parentconsole.model.Policy;
import com.acubeapps.parentconsole.network.NetworkInterface;
import com.acubeapps.parentconsole.network.NetworkResponse;

import java.util.ArrayList;
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
    NetworkInterface networkInterface;

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
                Policy policy = getUsageConfigResponse.policy;
                bindData(policy);
            }

            @Override
            public void failure(GetUsageConfigResponse getUsageConfigResponse) {

            }

            @Override
            public void networkFailure(Throwable error) {

            }
        });
        btnSubmitPolicy.setOnClickListener(this);

    }

    private void bindData(final Policy policy){
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
        txtAppName.setText(appConfig.getDisplayName());
        AppSessionConfig appSessionConfig = appConfig.getAppSessionConfigList().get(0);
        int minutes = (int) (appSessionConfig.getSessionStartTime() % 3600000);
        int hours = (int) (appSessionConfig.getSessionStartTime() / 3600000);
        txtStartTime.setText(hours + ":" + minutes);
        minutes = (int) (appSessionConfig.getSessionEndTime() % 3600000);
        hours = (int) (appSessionConfig.getSessionEndTime() / 3600000);
        txtEndTime.setText(hours + ":" + minutes);
        txtDuration.setText("" + appSessionConfig.getSessionAllowedDuration()/60000);
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

    }

    @Override
    public void onClick(View v) {

    }

    private boolean validateFields(String text){
        return (text != null && !"".equals(text));
    }
}
