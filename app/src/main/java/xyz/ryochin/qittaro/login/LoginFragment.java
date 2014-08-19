/**
 * =====================================
 * ENCODE : UTF-8
 * CREATED AT 2014/08/19
 * CREATED BY kosugeryou
 * =====================================
 */
package xyz.ryochin.qittaro.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.fragments.AlertDialogFragment;

public class LoginFragment extends Fragment implements LoginView, View.OnClickListener {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private final LoginFragment self = this;

    public interface Listener {
        public void onLoginCompleted();
    }

    private EditText urlNameEditText;
    private EditText passwordEditText;
    private View progress;
    private LoginPresenter presenter;
    private Listener listener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Listener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Please implement LoginFragment.Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.presenter = new LoginPresenterImpl(this, this.getActivity());
        this.progress = getView().findViewById(R.id.login_loading_layout);
        this.urlNameEditText = (EditText)getView().findViewById(R.id.user_name_edittext);
        this.passwordEditText = (EditText)getView().findViewById(R.id.password_edittext);
        getView().findViewById(R.id.qiita_login_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        String urlName = urlNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        presenter.validateCredentials(urlName, password);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String title, String message) {
        AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(title, message);
        alertDialogFragment.show(this.getActivity().getSupportFragmentManager(), null);
    }

    @Override
    public void navigateToHome() {
        listener.onLoginCompleted();
    }
}
