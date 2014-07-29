package com.ryochin.qittaro.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ryochin.qittaro.R;
import com.ryochin.qittaro.utils.AppController;
import com.ryochin.qittaro.utils.AppSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kosugeryou on 2014/07/23.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private final LoginFragment self = this;

    private static final String LOGIN_RESPONSE_TOKEN_KEY = "token";
    private FragmentListener listener;
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
        if (activity instanceof FragmentListener) {
            this.listener = (FragmentListener)activity;
        } else {
            throw new ClassCastException("activity が FragmentListener を実装していません.");
        }
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
        InputMethodManager inputMethodManager = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String loginURL = "https://qiita.com/api/v1/auth";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String token = jsonObject.getString(LOGIN_RESPONSE_TOKEN_KEY);
                    boolean result = AppSharedPreference.setToken(self.getActivity(), token);
                    self.listener.onCompletedLoggedin(result);
                } catch (JSONException e) {
                    Log.e(TAG, "JSONException", e);
                    self.listener.onCompletedLoggedin(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                self.listener.onCompletedLoggedin(false);
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
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}
