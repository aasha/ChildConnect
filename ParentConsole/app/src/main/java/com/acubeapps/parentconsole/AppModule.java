package com.acubeapps.parentconsole;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.acubeapps.parentconsole.network.NetworkInterface;

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

    public AppModule(Application context) {
        this.context = context;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.eventBus = EventBus.getDefault();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.networkInterface = NetworkInterface.getInstance();
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

}
