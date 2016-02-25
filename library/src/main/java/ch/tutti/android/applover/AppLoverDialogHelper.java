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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

class AppLoverDialogHelper {

    public static final String ARGUMENT_APP_NAME = "app_name";

    public static final String ARGUMENT_DIALOG_TYPE = "dialog_type";

    private static final String DIALOG_TAG = "applover_dialog";

    private AppLoverDialogHelper() {
    }

    public static Bundle createArguments(int dialogType, int appNameResId) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_APP_NAME, appNameResId);
        args.putInt(ARGUMENT_DIALOG_TYPE, dialogType);
        return args;
    }

    public static Dialog createDialog(DialogListener listener, Bundle arguments,
                                      Bundle savedInstanceState) {
        int dialogType = arguments.getInt(ARGUMENT_DIALOG_TYPE);
        if (savedInstanceState == null) {
            AppLover.get(null).trackDialogShown(dialogType);
        }
        return AppLoverDialogFactory.create(
                listener, dialogType, arguments.getInt(ARGUMENT_APP_NAME));
    }

    public static void showDialog(Activity activity, int dialogType, Bundle arguments) {
        showDialog(activity, dialogType, arguments.getInt(ARGUMENT_APP_NAME));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static boolean isDialogShown(Activity activity) {
        if (activity instanceof FragmentActivity) {
            return ((FragmentActivity) activity).getSupportFragmentManager()
                    .findFragmentByTag(DIALOG_TAG) != null;
        } else {
            return activity.getFragmentManager().findFragmentByTag(DIALOG_TAG) != null;
        }
    }

    public static void showDialog(Activity activity, int dialogType, int appNameResId) {
        if (activity instanceof FragmentActivity) {
            showDialogSupport((FragmentActivity) activity, dialogType, appNameResId);
        } else {
            showDialogSupport(activity, dialogType, appNameResId);
        }
    }

    /**
     * Show rate dialog with support library.
     *
     * @param activity fragment activity
     */
    private static void showDialogSupport(final FragmentActivity activity, int dialogType,
                                          int appNameResId) {
        final AppLoverDialogSupportFragment fragment =
                AppLoverDialogSupportFragment.newInstance(dialogType, appNameResId);
        fragment.show(activity.getSupportFragmentManager(), DIALOG_TAG);
    }

    /**
     * Show rate dialog for api that use the native fragment API
     *
     * @param activity fragment activity
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void showDialogSupport(final Activity activity, int dialogType,
                                          int appNameResId) {
        final AppLoverDialogFragment fragment =
                AppLoverDialogFragment.newInstance(dialogType, appNameResId);
        fragment.show(activity.getFragmentManager(), DIALOG_TAG);
    }

    public static void onCancel(Context context) {
        AppLover.get(context).reset(context);
    }

    public static interface DialogListener {

        public void showRateDialog();

        public void showEmailDialog();

        public Activity getActivity();
    }
}
