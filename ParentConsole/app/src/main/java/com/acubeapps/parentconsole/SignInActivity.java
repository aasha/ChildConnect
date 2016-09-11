package com.acubeapps.parentconsole;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acubeapps.parentconsole.model.ParentRegisterResponse;
import com.acubeapps.parentconsole.network.NetworkInterface;
import com.acubeapps.parentconsole.network.NetworkResponse;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.btn_register)
    Button btnRegister;

    @BindView(R.id.edit_name)
    EditText editName;

    @BindView(R.id.edit_email)
    EditText editEmail;

    @BindView(R.id.edit_password)
    EditText editPassword;

    @Inject
    NetworkInterface networkInterface;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Injectors.appComponent().injectSignInActivity(this);
        if (sharedPreferences.getString(Constants.PARENT_ID, null) != null) {
            launchChildActivity();
            finish();
        }
        ButterKnife.bind(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_register:
                networkInterface.register(editName.getText().toString(), editEmail.getText().toString(),
                        editPassword.getText().toString(), new NetworkResponse<ParentRegisterResponse>() {
                            @Override
                            public void success(ParentRegisterResponse parentRegisterResponse, Response response) {
                                sharedPreferences.edit().putString(Constants.PARENT_ID, parentRegisterResponse.userId).apply();
                                launchChildActivity();
                                finish();
                            }

                            @Override
                            public void failure(ParentRegisterResponse parentRegisterResponse) {
                                Toast.makeText(SignInActivity.this, Constants.FAILED_TO_REGISTER_PARENT, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void networkFailure(Throwable error) {
                                Toast.makeText(SignInActivity.this, Constants.FAILED_TO_REGISTER_PARENT, Toast.LENGTH_LONG).show();
                            }
                        });
                break;
        }
    }

    private void launchChildActivity(){
        Intent intent = new Intent(this, ListChildActivity.class);
        startActivity(intent);
    }
}
