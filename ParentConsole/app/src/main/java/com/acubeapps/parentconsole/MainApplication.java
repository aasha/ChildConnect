package com.acubeapps.parentconsole;

import android.app.Application;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injectors.initialize(this);
        Injectors.appComponent().injectMainApplication(this);
    }
}

