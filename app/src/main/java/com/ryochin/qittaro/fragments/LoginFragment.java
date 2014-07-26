package com.ryochin.qittaro.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ryochin.qittaro.R;
import com.ryochin.qittaro.utils.AppController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kosugeryou on 2014/07/23.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    public interface LoginFragmentCallBack {
        public void onSuccessLogin(String jsonResponse);
        public void onErrorLogin();
    }

    private static final String TAG = LoginFragment.class.getSimpleName();
    private final LoginFragment self = this;

    private LoginFragmentCallBack callBack;
    private EditText userNameEditText;
    private EditText passwordEditText;

    public LoginFragment(){}

    public static LoginFragment getInstance(){
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if ((activity instanceof LoginFragmentCallBack) == false) {
            throw new ClassCastException("activity が LoginFragmentCallBack を実装していません.");
        }

        this.callBack = (LoginFragmentCallBack)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        this.userNameEditText = (EditText)rootView.findViewById(R.id.user_name_edittext);
        this.passwordEditText = (EditText)rootView.findViewById(R.id.password_edittext);
        ((Button)rootView.findViewById(R.id.qiita_login_btn)).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "R.id.qiita_login_btn OnCLick()");
        String login_tag = "qiita_login_tag";
        String loginURL = "https://qiita.com/api/v1/auth";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                self.callBack.onSuccessLogin(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                self.callBack.onErrorLogin();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String url_name = self.userNameEditText.getText().toString();
                String password = self.passwordEditText.getText().toString();
                params.put("url_name", url_name);
                params.put("password", password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, login_tag);
    }
}
