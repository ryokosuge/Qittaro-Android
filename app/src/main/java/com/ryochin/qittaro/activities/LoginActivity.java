package com.ryochin.qittaro.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.fragments.AlertDialogFragment;
import com.ryochin.qittaro.fragments.LoginFragment;

/**
 * Created by kosugeryou on 2014/07/23.
 */
public class LoginActivity extends ActionBarActivity implements LoginFragment.LoginFragmentCallBack {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private final LoginActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            LoginFragment loginFragment = LoginFragment.getInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, loginFragment, null)
                    .commit();
        }
    }

    @Override
    public void onSuccessLogin(String jsonResponse) {
        Log.e(TAG, jsonResponse);

        this.setResult(RESULT_OK);
        Log.e(TAG, "RESULT_OK = " + RESULT_OK);
        Toast.makeText(this, R.string.login_success_message, Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void onErrorLogin() {
        String title = this.getResources().getString(R.string.login_error_title);
        String message = this.getResources().getString(R.string.login_error_message);
        AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(title, message);
        alertDialogFragment.show(this.getSupportFragmentManager(), null);
    }
}
