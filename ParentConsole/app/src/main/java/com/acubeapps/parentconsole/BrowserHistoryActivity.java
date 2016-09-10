package com.acubeapps.parentconsole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BrowserHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_history);
        Injectors.appComponent().injectBrowserHistoryActivity(this);
    }
}
