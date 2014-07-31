/**
 * PACKAGE NAME xyz.ryochin.qittaro.fragments
 * CREATED BY kosugeryou
 * CREATED AT 2014/07/26
 */
package xyz.ryochin.qittaro.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {

    private static final String TAG = AlertDialogFragment.class.getSimpleName();
    private final AlertDialogFragment self = this;

    private static final String BUNDLE_TITLE_KEY = "title";
    private static final String BUNDLE_MESSAGE_KEY = "message";

    public AlertDialogFragment() {
    }

    public static AlertDialogFragment newInstance(String title, String message) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_TITLE_KEY, title);
        args.putString(BUNDLE_MESSAGE_KEY, message);

        AlertDialogFragment fragment = new AlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        String title = args.getString(BUNDLE_TITLE_KEY);
        String message = args.getString(BUNDLE_MESSAGE_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

}