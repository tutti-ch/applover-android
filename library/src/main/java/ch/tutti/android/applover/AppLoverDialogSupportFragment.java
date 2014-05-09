package ch.tutti.android.applover;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AppLoverDialogSupportFragment extends DialogFragment
        implements AppLoverDialogHelper.DialogListener {

    public static AppLoverDialogSupportFragment newInstance(int dialogType, int appNameResId) {
        AppLoverDialogSupportFragment fragment = new AppLoverDialogSupportFragment();
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
