package com.acubeapps.parentconsole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.acubeapps.parentconsole.model.ChildDetails;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.btn_appUsage)
    Button btnAppUsage;

    @BindView(R.id.btn_browserHistory)
    Button btnBrowserHistory;

    @BindView(R.id.btn_update_policy)
    Button btnUpdatePolicy;

    @BindView(R.id.btn_performance)
    Button btnPerformance;

    ChildDetails childDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_details);
        Injectors.appComponent().injectChildDetailsActivity(this);
        ButterKnife.bind(this);
        childDetails = getIntent().getParcelableExtra(Constants.CHILD_DETAILS);
        setTitle(childDetails.name);
        btnAppUsage.setOnClickListener(this);
        btnBrowserHistory.setOnClickListener(this);
        btnUpdatePolicy.setOnClickListener(this);
        btnPerformance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btn_appUsage:
                intent = new Intent(ChildDetailsActivity.this, AppUsageActivity.class);
                break;
            case R.id.btn_browserHistory:
                intent = new Intent(ChildDetailsActivity.this, BrowserHistoryActivity.class);
                break;
            case R.id.btn_update_policy:
                intent = new Intent(ChildDetailsActivity.this, UpdatePolicyActivity.class);
                break;
            case R.id.btn_performance:
                intent = new Intent(ChildDetailsActivity.this, PerformanceActivity.class);
                break;
        }
        intent.putExtra(Constants.CHILD_DETAILS, childDetails);
        startActivity(intent);
    }
}
