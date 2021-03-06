package com.acubeapps.parentconsole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.acubeapps.parentconsole.adapters.AppUsageListAdapter;
import com.acubeapps.parentconsole.adapters.BrowserHistoryListAdapter;
import com.acubeapps.parentconsole.model.ChildDetails;
import com.acubeapps.parentconsole.model.GetChildUsageResponse;
import com.acubeapps.parentconsole.network.NetworkInterface;
import com.acubeapps.parentconsole.network.NetworkResponse;
import com.acubeapps.parentconsole.utils.CommonUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class BrowserHistoryActivity extends AppCompatActivity {

    @Inject
    NetworkInterface networkInterface;

    @BindView(R.id.recycler_view_browser_history_list)
    RecyclerView appUsageRecyclerView;

    BrowserHistoryListAdapter browserHistoryAdapter;

    ChildDetails childDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_history);
        Injectors.appComponent().injectBrowserHistoryActivity(this);
        ButterKnife.bind(this);
        childDetails = getIntent().getParcelableExtra(Constants.CHILD_DETAILS);
        setTitle(childDetails.name);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        appUsageRecyclerView.setLayoutManager(mLayoutManager);
        appUsageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        networkInterface.getChildUsageList(childDetails.childId, CommonUtils.getStartOfTheDayTime() - TimeUnit.HOURS.toMillis(24)+"", new NetworkResponse<GetChildUsageResponse>() {
            @Override
            public void success(GetChildUsageResponse getChildListResponse, Response response) {
                try {
                    if (getChildListResponse.browserHistory != null) {
                        browserHistoryAdapter = new BrowserHistoryListAdapter(getChildListResponse.browserHistory, BrowserHistoryActivity.this);
                        appUsageRecyclerView.setAdapter(browserHistoryAdapter);
                    }
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
