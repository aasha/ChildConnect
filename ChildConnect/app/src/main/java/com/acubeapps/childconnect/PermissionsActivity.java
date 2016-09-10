package com.acubeapps.childconnect;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acubeapps.childconnect.utils.Device;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PermissionsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnEnablePermissions)
    Button enablePermissions;

    @BindView(R.id.txtDescription)
    TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        ButterKnife.bind(this);
        enablePermissions.setOnClickListener(this);
        descriptionText.setText("Hello. Please Enable App Usage Access for Enabling ChildConnect");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Device.getRunningPackageName(this) != null) {
            Intent signInIntent = new Intent(this, SignInActivity.class);
            signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(signInIntent);
            finish();
        }
    }
}
