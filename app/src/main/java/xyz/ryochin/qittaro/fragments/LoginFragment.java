/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/23
 */

package xyz.ryochin.qittaro.fragments;


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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.utils.AppController;
import xyz.ryochin.qittaro.utils.AppSharedPreference;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private final LoginFragment self = this;

    private static final String LOGIN_RESPONSE_URL_NAME_KEY = "url_name";
    private static final String LOGIN_RESPONSE_TOKEN_KEY = "token";
    private static final String LOGIN_API_KEY = "https://qiita.com/api/v1/auth";
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.userNameEditText = (EditText)this.getView().findViewById(R.id.user_name_edittext);
        this.userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager)self.getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
        this.passwordEditText = (EditText)this.getView().findViewById(R.id.password_edittext);
        ((Button)this.getView().findViewById(R.id.qiita_login_btn)).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().sendView(TAG);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                LOGIN_API_KEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonResponse) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    String token = jsonObject.getString(LOGIN_RESPONSE_TOKEN_KEY);
                    String urlName = jsonObject.getString(LOGIN_RESPONSE_URL_NAME_KEY);
                    boolean result = (
                            AppSharedPreference.setToken(self.getActivity(), token) &&
                                    AppSharedPreference.setURLName(self.getActivity(), urlName)
                    );
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
