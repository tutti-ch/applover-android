/*
 * Copyright (c) 2014 tutti.ch AG
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package ch.tutti.android.applover;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AppLoverDialogFragment extends DialogFragment
        implements AppLoverDialogHelper.DialogListener {

    public static AppLoverDialogFragment newInstance(int dialogType, int appNameResId) {
        AppLoverDialogFragment fragment = new AppLoverDialogFragment();
        fragment.setArguments(AppLoverDialogHelper.createArguments(dialogType, appNameResId));
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return AppLoverDialogHelper.createDialog(this, getArguments(), savedInstanceState);
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
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        AppLover.get(null).trackDialogCanceled(
                getArguments().getInt(AppLoverDialogHelper.ARGUMENT_DIALOG_TYPE));
        AppLoverDialogHelper.onCancel(getActivity());
    }
}
