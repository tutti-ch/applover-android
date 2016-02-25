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
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Date;
import java.util.HashMap;

import ch.tutti.android.applover.criteria.AppLoverAllCustomEventsReached;
import ch.tutti.android.applover.criteria.AppLoverAppLaunchCriteria;
import ch.tutti.android.applover.criteria.AppLoverCriteria;
import ch.tutti.android.applover.criteria.AppLoverCriteriaBuilder;
import ch.tutti.android.applover.criteria.AppLoverFirstLaunchDaysCriteria;

/**
 * Let users who like your app review it and those who don't write you an e-mail.
 * That way you get positive reviews on Google Play and still hear about what users don't like.
 */
public class AppLover {

    public static final int DIALOG_TYPE_FIRST = 0;

    public static final int DIALOG_TYPE_RATE = 1;

    public static final int DIALOG_TYPE_EMAIL = 2;

    public static final String BUTTON_YES = "yes";

    public static final String BUTTON_NO = "no";

    public static final String BUTTON_LATER = "later";

    private static final int DEFAULT_THRESHOLD = 10;

    private static final int ONE_DAY = 24 * 60 * 60 * 1000;

    private static AppLover INSTANCE;

    private int mLaunchCountThreshold = DEFAULT_THRESHOLD;

    private int mInstallDaysThreshold = DEFAULT_THRESHOLD;

    private int mFirstLaunchDaysThreshold = DEFAULT_THRESHOLD;

    private long mFirstLaunchDateTime;

    private int mLaunchCount;

    private int mAppNameResId;

    private String mFeedbackEmail;

    private HashMap<String, Integer> mCustomEventThresholdMap
            = new HashMap<String, Integer>();

    private OnTrackListener mOnTrackListener;

    private AppLoverCriteria mShowDialogCriteria;

    private AppLover() {
        mShowDialogCriteria = new AppLoverCriteriaBuilder(
                new AppLoverFirstLaunchDaysCriteria())
                .and(new AppLoverAppLaunchCriteria())
                .and(new AppLoverAllCustomEventsReached())
                .build();
    }

    public static AppLover get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppLover();
            AppLoverPreferences preferences = new AppLoverPreferences(context);
            INSTANCE.mFirstLaunchDateTime = preferences.getFirstLaunchDate();
            INSTANCE.mLaunchCount = preferences.getLaunchCount();
        }
        return INSTANCE;
    }

    public AppLover setAppName(int textResId) {
        mAppNameResId = textResId;
        return this;
    }

    public String getFeedbackEmail() {
        return mFeedbackEmail;
    }

    public AppLover setFeedbackEmail(String email) {
        mFeedbackEmail = email;
        return this;
    }

    /**
     * Sets the listener for tracking/analytics purposes. This object will be kept around for a
     * while. So do not register anything like an Activity/Fragment or similar.
     *
     * @param listener listener with tracking/analytics purpose callbacks
     */
    public AppLover setOnTrackListener(OnTrackListener listener) {
        mOnTrackListener = listener;
        return this;
    }

    /**
     * Set up a custom criteria for when the dialog should be shown.
     * Default criteria is every criteria with AND. Setting criteria to null = no criteria. Which
     * will show it at first opportunity.
     *
     * @param criteria criteria for the dialog to be shown
     */
    public AppLover setShowDialogCriteria(AppLoverCriteria criteria) {
        mShowDialogCriteria = criteria;
        return this;
    }

    public AppLover setLaunchCountThreshold(final int launchTimesThreshold) {
        mLaunchCountThreshold = launchTimesThreshold;
        return this;
    }

    public int getLaunchCountThreshold() {
        return mLaunchCountThreshold;
    }


    public AppLover setInstallDaysThreshold(int installDaysThreshold) {
        mInstallDaysThreshold = installDaysThreshold;
        return this;
    }

    public int getInstallDaysThreshold() {
        return mInstallDaysThreshold;
    }

    public AppLover setFirstLaunchDaysThreshold(final int installDaysThreshold) {
        mFirstLaunchDaysThreshold = installDaysThreshold;
        return this;
    }

    public int getFirstLaunchDaysThreshold() {
        return mFirstLaunchDaysThreshold;
    }

    public AppLover setCustomEventCountThreshold(String event, int eventCountThreshold) {
        mCustomEventThresholdMap.put(event, eventCountThreshold);
        return this;
    }

    public HashMap<String, Integer> getCustomEventCountThresholds() {
        return mCustomEventThresholdMap;
    }

    public int getCustomEventCountThreshold(String event) {
        Integer threshold = mCustomEventThresholdMap.get(event);
        return threshold == null ? DEFAULT_THRESHOLD : threshold;
    }

    /**
     * Increase launch count.<br/>
     * Call this method in the MAIN Activity's onCreate() method.
     *
     * @param context context
     */
    public void monitorLaunch(final Context context) {
        AppLoverPreferences preferences = new AppLoverPreferences(context);
        if (mFirstLaunchDateTime == 0) {
            mFirstLaunchDateTime = new Date().getTime();
            preferences.setFirstLaunchDate(mFirstLaunchDateTime);
        }
        mLaunchCount = preferences.incLaunchCount();
    }

    /**
     * Increase count of occurrence of custom event. Call whenever custom event occurs.
     *
     * @param context context
     * @param event   name of the custom event
     */
    public void monitorCustomEvent(Context context, String event) {
        new AppLoverPreferences(context).incCustomEventCount(event);
    }

    /**
     * Show feedback dialog if conditions are met.
     *
     * @param activity activity
     */
    public void showDialogIfConditionsMet(final Activity activity) {
        if (shouldShowRateDialog(activity) && !AppLoverDialogHelper.isDialogShown(activity)) {
            AppLoverDialogHelper.showDialog(
                    activity, AppLover.DIALOG_TYPE_FIRST, getAppNameResId(activity));
        }
    }

    private int getAppNameResId(Context context) {
        return mAppNameResId == 0 ? context.getApplicationInfo().labelRes : mAppNameResId;
    }

    private boolean shouldShowRateDialog(Context context) {
        AppLoverPreferences preferences = new AppLoverPreferences(context);
        boolean stillShow = !(preferences.isDoNotShowAnymore());
        return stillShow &&
                (mShowDialogCriteria == null
                        || mShowDialogCriteria.isCriteriaMet(context, this, preferences));
    }

    public int getLaunchCount() {
        return mLaunchCount;
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public int getInstalledDays(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            try {
                long installedTime = System.currentTimeMillis() -
                        context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                                .firstInstallTime;
                return (int) (installedTime / ONE_DAY);
            } catch (PackageManager.NameNotFoundException e) {
                return getDaysSinceFirstLaunch();
            }
        } else {
            return getDaysSinceFirstLaunch();
        }
    }

    public int getDaysSinceFirstLaunch() {
        if (mFirstLaunchDateTime == 0) {
            return 0;
        }
        return (int) ((new Date().getTime() - mFirstLaunchDateTime) / ONE_DAY);
    }

    public int getCustomEventCount(Context context, String event) {
        return new AppLoverPreferences(context).getCustomEventCount(event);
    }

    public boolean isDoNotShowAnymore(Context context) {
        return new AppLoverPreferences(context).isDoNotShowAnymore();
    }

    public void trackDialogShown(int dialogType) {
        if (mOnTrackListener != null) {
            mOnTrackListener.onTrackDialogShown(dialogType);
        }
    }

    public void trackDialogCanceled(int dialogType) {
        if (mOnTrackListener != null) {
            mOnTrackListener.onTrackDialogCanceled(dialogType);
        }
    }

    public void trackDialogButtonPressed(int dialogType, String button) {
        if (mOnTrackListener != null) {
            mOnTrackListener.onTrackDialogButtonPressed(dialogType, button);
        }
    }

    /**
     * Reset the statistics and start at 0.
     */
    public void reset(Context context) {
        mFirstLaunchDateTime = 0;
        mLaunchCount = 0;
        new AppLoverPreferences(context).clear();
    }

    public static interface OnTrackListener {

        /**
         * Called when a dialog is shown.
         *
         * @param dialogType {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_FIRST},
         *                   {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_RATE} or
         *                   {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_EMAIL}
         */
        void onTrackDialogShown(int dialogType);

        /**
         * Called when the dialog gets cancelled through the back button.
         *
         * @param dialogType {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_FIRST},
         *                   {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_RATE} or
         *                   {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_EMAIL}
         */
        void onTrackDialogCanceled(int dialogType);

        /**
         * Called when user clicks on a button of the dialog.
         *
         * @param dialogType {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_FIRST},
         *                   {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_RATE} or
         *                   {@link ch.tutti.android.applover.AppLover#DIALOG_TYPE_EMAIL}
         * @param button     {@link ch.tutti.android.applover.AppLover#BUTTON_YES},
         *                   {@link ch.tutti.android.applover.AppLover#BUTTON_NO} or
         *                   {@link ch.tutti.android.applover.AppLover#BUTTON_LATER}
         */
        void onTrackDialogButtonPressed(int dialogType, String button);
    }
}