package com.acubeapps.childconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acubeapps.childconnect.model.ChildRegisterResponse;
import com.acubeapps.childconnect.network.NetworkInterface;
import com.acubeapps.childconnect.network.NetworkResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private ProgressDialog mProgressDialog;

    @BindView(R.id.btnSignIn)
    Button btnSignIn;

    @BindView(R.id.btnSignOut)
    Button btnSignOut;

    @BindView(R.id.editParentId)
    EditText editParentId;

    @Inject
    NetworkInterface networkInterface;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Injectors.appComponent().injectSignInActivity(this);
        String childId = sharedPreferences.getString(Constants.CHILD_ID, null);
        Log.e("AASHA", "Child " + childId);
        if (null != childId) {
            launchMainActivity();
            finish();
        }
        ButterKnife.bind(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                if (validateParentId()) {
                    showProgressDialog();
                    signIn();
                }
                break;
            case R.id.btnSignOut:
                signOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            boolean status = handleSignInResult(result);
            if (status) {
                GoogleSignInAccount acct = result.getSignInAccount();
                registerChild(acct);
            }
        }
    }

    private boolean handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            updateUI(true);
            return true;
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
            return false;
        }
    }

    private void registerChild(GoogleSignInAccount acct) {
        String parentId = editParentId.getText().toString();
        networkInterface.registerChild(acct.getDisplayName(), acct.getEmail(), acct.getIdToken(),
                Constants.SOURCE_GOOGLE, parentId, new NetworkResponse<ChildRegisterResponse>() {
                    @Override
                    public void success(ChildRegisterResponse childRegisterResponse, Response response) {
                        hideProgressDialog();
                        sharedPreferences.edit().putString(Constants.CHILD_ID, childRegisterResponse.childId).apply();
                        launchMainActivity();
                        finish();
                    }

                    @Override
                    public void failure(ChildRegisterResponse childRegisterResponse) {
                        hideProgressDialog();
                        Toast.makeText(SignInActivity.this, "Failed to register child " ,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkFailure(Throwable error) {
                        hideProgressDialog();
                        Toast.makeText(SignInActivity.this, "Failed to register child " + error.getMessage() ,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, PermissionsActivity.class);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private boolean validateParentId(){
        String parentId = editParentId.getText().toString();
        if (parentId == null || "".equals(parentId.trim())) {
            Toast.makeText(this, Constants.ERROR_INVALID_PARENT_ID, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}

