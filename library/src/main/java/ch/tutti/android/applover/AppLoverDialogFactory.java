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

import com.squareup.phrase.Phrase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextThemeWrapper;

import java.security.InvalidParameterException;

class AppLoverDialogFactory {

    private AppLoverDialogFactory() {
    }

    /**
     * Create rate dialog.
     *
     * @param listener     the listener for farious operations
     * @param appNameResId string resource for app name
     */
    public static Dialog create(final AppLoverDialogHelper.DialogListener listener, int dialogType,
            int appNameResId) {
        switch (dialogType) {
            case AppLover.DIALOG_TYPE_FIRST:
                return createFirstDialog(listener, appNameResId);
            case AppLover.DIALOG_TYPE_RATE:
                return createRateDialog(listener.getActivity(), appNameResId);
            case AppLover.DIALOG_TYPE_EMAIL:
                return createEmailDialog(listener.getActivity(), appNameResId);
        }
        throw new InvalidParameterException(
                "Parameter dialogType is not valid. Needs to be a dialog defined in DialogFactory.");
    }

    private static Dialog createFirstDialog(final AppLoverDialogHelper.DialogListener listener,
            final int appNameResId) {
        final Context context = listener.getActivity();
        final AlertDialog.Builder builder =
                createBuilder(context, AppLover.get(context).getStyle().loveDialogStyle);
        builder.setMessage(Phrase.from(context, R.string.applover_do_you_like_this_app)
                .putOptional("app_name", context.getString(appNameResId))
                .format());
        builder.setPositiveButton(R.string.applover_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppLover.get(null)
                        .trackDialogButtonPressed(AppLover.DIALOG_TYPE_FIRST, AppLover.BUTTON_YES);
                listener.showRateDialog();
            }
        });
        builder.setNegativeButton(R.string.applover_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppLover.get(null)
                        .trackDialogButtonPressed(AppLover.DIALOG_TYPE_FIRST, AppLover.BUTTON_NO);
                listener.showEmailDialog();
            }
        });
        return builder.create();
    }

    private static Dialog createRateDialog(final Context context, final int appNameResId) {
        final AlertDialog.Builder builder =
                createBuilder(context, AppLover.get(context).getStyle().rateDialogStyle);
        builder.setMessage(Phrase.from(context, R.string.applover_rate_text)
                .putOptional("app_name", context.getString(appNameResId))
                .format());
        builder.setPositiveButton(R.string.applover_rate_now,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLover.get(null).trackDialogButtonPressed(
                                AppLover.DIALOG_TYPE_RATE, AppLover.BUTTON_YES);
                        String url =
                                "https://play.google.com/store/apps/details?id="
                                        + context.getPackageName();
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        new AppLoverPreferences(context).setDoNotShowAnymore();
                    }
                }
        );
        builder.setNeutralButton(R.string.applover_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppLover.get(null).trackDialogButtonPressed(
                        AppLover.DIALOG_TYPE_RATE, AppLover.BUTTON_LATER);
                AppLover.get(context).reset(context);
            }
        });
        builder.setNegativeButton(R.string.applover_no_thanks,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLover.get(null).trackDialogButtonPressed(
                                AppLover.DIALOG_TYPE_RATE, AppLover.BUTTON_NO);
                        new AppLoverPreferences(context).setDoNotShowAnymore();
                    }
                }
        );
        return builder.create();
    }

    private static Dialog createEmailDialog(final Context context, final int appNameResId) {
        final AlertDialog.Builder builder =
                createBuilder(context, AppLover.get(context).getStyle().emailDialogStyle);
        builder.setMessage(Phrase.from(context, R.string.applover_feedback_text)
                .putOptional("app_name", context.getString(appNameResId))
                .format());
        builder.setPositiveButton(R.string.applover_feedback_now,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLover.get(null).trackDialogButtonPressed(
                                AppLover.DIALOG_TYPE_EMAIL, AppLover.BUTTON_YES);
                        String feedbackEmail = AppLover.get(context).getFeedbackEmail();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{feedbackEmail});
                        intent.putExtra(Intent.EXTRA_SUBJECT,
                                Phrase.from(context, R.string.applover_feedback_subject)
                                        .putOptional("app_name", context.getString(appNameResId))
                                        .format().toString()
                        );
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        context.startActivity(intent);
                        new AppLoverPreferences(context).setDoNotShowAnymore();
                    }
                }
        );
        builder.setNeutralButton(R.string.applover_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppLover.get(null).trackDialogButtonPressed(
                        AppLover.DIALOG_TYPE_EMAIL, AppLover.BUTTON_LATER);
                AppLover.get(context).reset(context);
            }
        });
        builder.setNegativeButton(R.string.applover_no_thanks,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppLover.get(null).trackDialogButtonPressed(
                                AppLover.DIALOG_TYPE_EMAIL, AppLover.BUTTON_NO);
                        new AppLoverPreferences(context).setDoNotShowAnymore();
                    }
                }
        );
        return builder.create();
    }

    private static AlertDialog.Builder createBuilder(Context context,
            AppLoverDialogStyle.Style style) {
        if (style != null && style.theme != 0) {
            return new AlertDialog.Builder(new ContextThemeWrapper(context, style.theme));
        } else {
            return new AlertDialog.Builder(context);
        }
    }
}
