package com.acubeapps.parentconsole;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.acubeapps.parentconsole.adapters.ChildListAdapter;
import com.acubeapps.parentconsole.model.GetChildListResponse;
import com.acubeapps.parentconsole.network.NetworkInterface;
import com.acubeapps.parentconsole.network.NetworkResponse;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ListChildActivity extends AppCompatActivity {

    @Inject
    NetworkInterface networkInterface;

    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.recycler_view_child_list)
    RecyclerView childRecyclerView;

    ChildListAdapter childListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_child);
        setTitle("Your Kids");
        Injectors.appComponent().injectListChildActivity(this);
        ButterKnife.bind(this);
        String parentId = sharedPreferences.getString(Constants.PARENT_ID, null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        childRecyclerView.setLayoutManager(mLayoutManager);
        childRecyclerView.setItemAnimator(new DefaultItemAnimator());
        networkInterface.getChildList(parentId, new NetworkResponse<GetChildListResponse>() {
            @Override
            public void success(GetChildListResponse getChildListResponse, Response response) {
                childListAdapter = new ChildListAdapter(getChildListResponse.childDetailList, ListChildActivity.this);
                childRecyclerView.setAdapter(childListAdapter);
            }

            @Override
            public void failure(GetChildListResponse getChildListResponse) {

            }

            @Override
            public void networkFailure(Throwable error) {

            }
        });
    }
}
