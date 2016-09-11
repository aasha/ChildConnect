package com.acubeapps.parentconsole.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acubeapps.parentconsole.R;
import com.acubeapps.parentconsole.model.AppUsage;

import java.util.List;

/**
 * Created by aasha.medhi on 9/11/16.
 */
public class AppUsageListAdapter extends RecyclerView.Adapter<AppUsageListAdapter.MyViewHolder> {
    Context context;
    private List<AppUsage> appUsageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView appName, appTime;

        public MyViewHolder(View view) {
            super(view);
            appName = (TextView) view.findViewById(R.id.txt_app_name);
            appTime = (TextView) view.findViewById(R.id.txt_app_time);
        }
    }


    public AppUsageListAdapter(List<AppUsage> appUsageList, Context context) {
        this.context = context;
        this.appUsageList = appUsageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_usage_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AppUsage appUsage = appUsageList.get(position);
        holder.appName.setText(appUsage.displayName);
        holder.appTime.setText("" + appUsage.duration / 60000 + " mins");
    }

    @Override
    public int getItemCount() {
        return appUsageList.size();
    }

}
