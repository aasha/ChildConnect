package com.acubeapps.parentconsole;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.acubeapps.parentconsole.adapters.AppUsageListAdapter;
import com.acubeapps.parentconsole.adapters.ChildListAdapter;
import com.acubeapps.parentconsole.model.ChildDetails;
import com.acubeapps.parentconsole.model.GetChildListResponse;
import com.acubeapps.parentconsole.model.GetChildUsageResponse;
import com.acubeapps.parentconsole.network.NetworkInterface;
import com.acubeapps.parentconsole.network.NetworkResponse;
import com.acubeapps.parentconsole.utils.CommonUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class AppUsageActivity extends AppCompatActivity {

    @Inject
    NetworkInterface networkInterface;

    @BindView(R.id.recycler_view_app_usage_list)
    RecyclerView appUsageRecyclerView;

    AppUsageListAdapter appUsageListAdapter;

    ChildDetails childDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);
        Injectors.appComponent().injectAppUsageActivity(this);
        ButterKnife.bind(this);
        childDetails = getIntent().getParcelableExtra(Constants.CHILD_DETAILS);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        appUsageRecyclerView.setLayoutManager(mLayoutManager);
        appUsageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        networkInterface.getChildUsageList(childDetails.childId, CommonUtils.getStartOfTheDayTime() - TimeUnit.HOURS.toMillis(24)+"", new NetworkResponse<GetChildUsageResponse>() {
            @Override
            public void success(GetChildUsageResponse getChildListResponse, Response response) {
                try {
                    appUsageListAdapter = new AppUsageListAdapter(getChildListResponse.appUsage, AppUsageActivity.this);
                    appUsageRecyclerView.setAdapter(appUsageListAdapter);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(GetChildUsageResponse getChildListResponse) {

            }

            @Override
            public void networkFailure(Throwable error) {

            }
        });
    }
}
