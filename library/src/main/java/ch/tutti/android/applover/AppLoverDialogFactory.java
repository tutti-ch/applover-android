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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.phrase.Phrase;

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
        final MaterialDialog.Builder builder =
                new MaterialDialog.Builder(context);
        builder.content(Phrase.from(context, R.string.applover_do_you_like_this_app)
                .putOptional("app_name", context.getString(appNameResId))
                .format());
        builder.positiveText(R.string.applover_yes);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                AppLover.get(null)
                        .trackDialogButtonPressed(AppLover.DIALOG_TYPE_FIRST, AppLover.BUTTON_YES);
                listener.showRateDialog();
            }
        });
        builder.negativeText(R.string.applover_no);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                AppLover.get(null)
                        .trackDialogButtonPressed(AppLover.DIALOG_TYPE_FIRST, AppLover.BUTTON_NO);
                listener.showEmailDialog();
            }
        });
        return builder.build();
    }

    private static Dialog createRateDialog(final Context context, final int appNameResId) {
        final MaterialDialog.Builder builder =
                new MaterialDialog.Builder(context);
        builder.content(Phrase.from(context, R.string.applover_rate_text)
                .putOptional("app_name", context.getString(appNameResId))
                .format());
        builder.positiveText(R.string.applover_rate_now);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                AppLover.get(null).trackDialogButtonPressed(
                        AppLover.DIALOG_TYPE_RATE, AppLover.BUTTON_YES);
                String url =
                        "https://play.google.com/store/apps/details?id="
                                + context.getPackageName();
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                new AppLoverPreferences(context).setDoNotShowAnymore();
            }
        });
        builder.neutralText(R.string.applover_later);
        builder.onNeutral(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                AppLover.get(null).trackDialogButtonPressed(
                        AppLover.DIALOG_TYPE_RATE, AppLover.BUTTON_LATER);
                AppLover.get(context).reset(context);
            }
        });
        builder.negativeText(R.string.applover_no_thanks);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                AppLover.get(null).trackDialogButtonPressed(
                        AppLover.DIALOG_TYPE_RATE, AppLover.BUTTON_NO);
                new AppLoverPreferences(context).setDoNotShowAnymore();
            }
        });
        return builder.build();
    }

    private static Dialog createEmailDialog(final Context context, final int appNameResId) {
        final MaterialDialog.Builder builder =
                new MaterialDialog.Builder(context);
        builder.content(Phrase.from(context, R.string.applover_feedback_text)
                .putOptional("app_name", context.getString(appNameResId))
                .format());
        builder.positiveText(R.string.applover_feedback_now);
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                               @Override
                               public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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
                                   try {
                                       context.startActivity(intent);
                                       new AppLoverPreferences(context).setDoNotShowAnymore();
                                   } catch (Exception ex) { //no activity to send email}
                                       AppLover.get(context).reset(context);
                                   }
                               }
                           }

        );
        builder.neutralText(R.string.applover_later);
        builder.onNeutral(new MaterialDialog.SingleButtonCallback()

                          {
                              @Override
                              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                  AppLover.get(null).trackDialogButtonPressed(
                                          AppLover.DIALOG_TYPE_EMAIL, AppLover.BUTTON_LATER);
                                  AppLover.get(context).reset(context);
                              }
                          }

        );
        builder.negativeText(R.string.applover_no_thanks);
        builder.onNegative(new MaterialDialog.SingleButtonCallback()

                           {
                               @Override
                               public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                   AppLover.get(null).trackDialogButtonPressed(
                                           AppLover.DIALOG_TYPE_EMAIL, AppLover.BUTTON_NO);
                                   new AppLoverPreferences(context).setDoNotShowAnymore();
                               }
                           }

        );
        return builder.build();
    }
}
