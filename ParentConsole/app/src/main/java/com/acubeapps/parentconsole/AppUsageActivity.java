package com.acubeapps.parentconsole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AppUsageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);
        Injectors.appComponent().injectAppUsageActivity(this);
    }
}
