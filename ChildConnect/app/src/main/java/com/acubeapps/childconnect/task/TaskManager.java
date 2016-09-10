package com.acubeapps.childconnect.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.acubeapps.childconnect.Constants;
import com.acubeapps.childconnect.events.ShowTaskScreenEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class TaskManager {

    private Context context;
    private EventBus eventBus;

    public TaskManager(Context context, EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void onShowTaskScreenEvent(ShowTaskScreenEvent event) {
        Log.d(Constants.LOG_TAG, "show task screen event received");
        Intent intent = null;
        if (event.getCourseId() == null) {
            intent = new Intent(context, WaitTimerActivity.class);
        } else {
            intent = new Intent(context, ProblemActivity.class);
            intent.putExtra(Constants.COURSE_ID, event.getCourseId());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
