package com.acubeapps.childconnect;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.acubeapps.childconnect.helpers.AppPolicyManager;
import com.acubeapps.childconnect.helpers.AppUsageManager;
import com.acubeapps.childconnect.network.NetworkInterface;
import com.acubeapps.childconnect.store.SqliteAppConfigStore;
import com.acubeapps.childconnect.task.TaskManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
/**
 * Created by aasha.medhi on 9/10/16.
 */
@Module
public class AppModule {
    private final Context context;
    private final EventBus eventBus;
    private final SharedPreferences sharedPreferences;
    private Handler mainHandler;
    private NetworkInterface networkInterface;
    private SqliteAppConfigStore sqliteAppConfigStore;
    private AppPolicyManager appPolicyManager;
    private AppUsageManager appUsageManager;
    private TaskManager taskManager;

    public AppModule(Application context) {
        this.context = context;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.eventBus = EventBus.getDefault();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.networkInterface = NetworkInterface.getInstance();
        this.sqliteAppConfigStore = new SqliteAppConfigStore(context, 1);
        appPolicyManager = new AppPolicyManager(sqliteAppConfigStore);
        appUsageManager = new AppUsageManager(appPolicyManager, context, eventBus);
        taskManager = new TaskManager(context, eventBus);
    }


    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public EventBus provideEventBus() {
        return eventBus;
    }

    @Singleton
    @Provides
    public NetworkInterface provideNetworkInterface() {
        return networkInterface;
    }


    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences() {
        return this.sharedPreferences;
    }

    @Provides
    @Singleton
    public Handler provideHandler() {
        return mainHandler;
    }

    @Provides
    @Singleton
    public AppUsageManager provideAppUsageManager() {
        return appUsageManager;
    }

    @Provides
    @Singleton
    public TaskManager provideTaskManager() {
        return taskManager;
    }
}
