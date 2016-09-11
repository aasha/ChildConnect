package com.acubeapps.parentconsole.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acubeapps.parentconsole.ChildDetailsActivity;
import com.acubeapps.parentconsole.Constants;
import com.acubeapps.parentconsole.R;
import com.acubeapps.parentconsole.model.ChildDetails;

import java.util.List;

/**
 * Created by aasha.medhi on 9/11/16.
 */
public class BrowserHistoryListAdapter extends RecyclerView.Adapter<BrowserHistoryListAdapter.MyViewHolder> {
    Context context;
    private List<String> browserHistoryUrlList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView url;

        public MyViewHolder(View view) {
            super(view);
            url = (TextView) view.findViewById(R.id.txt_url);
        }
    }


    public BrowserHistoryListAdapter(List<String> browserHistoryUrlList, Context context) {
        this.context = context;
        this.browserHistoryUrlList = browserHistoryUrlList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.browser_history_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String url = browserHistoryUrlList.get(position);
        holder.url.setText(url);
    }

    @Override
    public int getItemCount() {
        return browserHistoryUrlList.size();
    }

}
