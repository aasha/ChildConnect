package com.acubeapps.childconnect;

import android.app.Application;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public final class Injectors {

    private Injectors() {
        throw new AssertionError();
    }

    private static volatile AppComponent appComponent;

    public static AppComponent appComponent() {
        if (appComponent == null) {
            throw new AssertionError("Injector not initialized");
        }
        return appComponent;
    }

    public static void initialize(Application application) {
        if (appComponent == null) {
            synchronized (Injectors.class) {
                if (appComponent == null) {
                    appComponent = DaggerAppComponent.builder()
                            .appModule(new AppModule(application))
                            .build();
                }
            }
        }
    }

}

