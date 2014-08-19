/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/08/13
 */

package xyz.ryochin.qittaro.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.ryochin.qittaro.R;
import xyz.ryochin.qittaro.models.UserModel;
import xyz.ryochin.qittaro.views.UserInfoView;

public class UserInfoFragment extends Fragment {
    private static final String TAG = UserInfoFragment.class.getSimpleName();
    private final UserInfoFragment self = this;
    private static final String ARGS_USER_MODEL_KEY = "userModel";

    protected UserInfoView view;

    public static UserInfoFragment newInstance(UserModel model) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_USER_MODEL_KEY, model);
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = this.getArguments();
        UserModel userModel = args.getParcelable(ARGS_USER_MODEL_KEY);
        this.view = new UserInfoView(this.getView());
        this.view.setUserInfo(userModel);
    }

}
