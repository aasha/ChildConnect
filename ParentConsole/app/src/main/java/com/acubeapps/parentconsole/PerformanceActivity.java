package com.acubeapps.parentconsole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PerformanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        Injectors.appComponent().injectPerformaceActivity(this);
    }
}
