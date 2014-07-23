package com.ryochin.qittaro.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.ryochin.qittaro.R;
import com.ryochin.qittaro.fragments.LoginFragment;

/**
 * Created by kosugeryou on 2014/07/23.
 */
public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            LoginFragment loginFragment = LoginFragment.getInstance();
            fragmentManager.beginTransaction().add(R.id.fragment_container, loginFragment, null).commit();
        }
    }

}
