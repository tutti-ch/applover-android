package ch.tutti.android.applover;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AppLoverDialogFragment extends DialogFragment implements AppLoverDialogHelper.DialogListener {

    public static AppLoverDialogFragment newInstance(int dialogType, int appNameResId) {
        AppLoverDialogFragment fragment = new AppLoverDialogFragment();
        fragment.setArguments(AppLoverDialogHelper.createArguments(dialogType, appNameResId));
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return AppLoverDialogHelper.createDialog(this, getArguments());
    }

    @Override
    public void showRateDialog() {
        AppLoverDialogHelper.showDialog(getActivity(), AppLover.DIALOG_TYPE_RATE, getArguments());
    }

    @Override
    public void showEmailDialog() {
        AppLoverDialogHelper.showDialog(getActivity(), AppLover.DIALOG_TYPE_EMAIL, getArguments());
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLoverDialogHelper.updateDialogStyle(getArguments(), getDialog());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        AppLover.get(null)
                .trackDialogCanceled(getArguments().getInt(AppLoverDialogHelper.ARGUMENT_DIALOG_TYPE));
        AppLoverDialogHelper.onCancel(getActivity());
    }
}
