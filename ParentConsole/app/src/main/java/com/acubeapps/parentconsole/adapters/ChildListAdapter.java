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
public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MyViewHolder> {
    Context context;
    private List<ChildDetails> childDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChildDetails childDetails = childDetailsList.get(getAdapterPosition());
                    launchChildDetailsActivity(childDetails);
                }
            });
        }
    }


    public ChildListAdapter(List<ChildDetails> childDetailsList, Context context) {
        this.context = context;
        this.childDetailsList = childDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChildDetails childDetails = childDetailsList.get(position);
        holder.name.setText(childDetails.name);
    }

    @Override
    public int getItemCount() {
        return childDetailsList.size();
    }

    private void launchChildDetailsActivity(ChildDetails childDetails) {
        Intent intent = new Intent(context, ChildDetailsActivity.class);
        intent.putExtra(Constants.CHILD_DETAILS, childDetails);
        context.startActivity(intent);
    }
}
