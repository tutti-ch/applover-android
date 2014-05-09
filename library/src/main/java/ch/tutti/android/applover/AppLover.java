package ch.tutti.android.applover;

import android.app.Activity;
import android.content.Context;

import java.util.Date;
import java.util.HashMap;

import ch.tutti.android.applover.criteria.AppLoverAllCustomEventsReached;
import ch.tutti.android.applover.criteria.AppLoverAppLaunchCriteria;
import ch.tutti.android.applover.criteria.AppLoverCriteria;
import ch.tutti.android.applover.criteria.AppLoverCriteriaBuilder;
import ch.tutti.android.applover.criteria.AppLoverInstallDaysCriteria;

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

    private long mInstallDateTime;

    private int mLaunchCount;

    private int mAppNameResId;

    private String mFeedbackEmail;

    private AppLoverDialogStyle mStyle;

    private HashMap<String, Integer> mCustomEventThresholdMap
            = new HashMap<String, Integer>();

    private OnTrackListener mOnTrackListener;

    private AppLoverCriteria mShowDialogCriteria;

    private AppLover() {
        mShowDialogCriteria = new AppLoverCriteriaBuilder(
                new AppLoverInstallDaysCriteria())
                .and(new AppLoverAppLaunchCriteria())
                .and(new AppLoverAllCustomEventsReached())
                .build();
    }

    public static AppLover get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppLover();
            AppLoverPreferences preferences = new AppLoverPreferences(context);
            INSTANCE.mInstallDateTime = preferences.getInstallationDate();
            INSTANCE.mLaunchCount = preferences.getLaunchCount();
        }
        return INSTANCE;
    }

    public AppLover setAppName(int textResId) {
        mAppNameResId = textResId;
        return this;
    }

    public AppLoverDialogStyle getStyle() {
        return mStyle == null ? new AppLoverDialogStyle() : mStyle;
    }

    public AppLover setStyle(AppLoverDialogStyle style) {
        mStyle = style;
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
    public void setShowDialogCriteria(AppLoverCriteria criteria) {
        mShowDialogCriteria = criteria;
    }

    public AppLover setLaunchCountThreshold(final int launchTimesThreshold) {
        mLaunchCountThreshold = launchTimesThreshold;
        return this;
    }

    public int getLaunchCountThreshold() {
        return mLaunchCountThreshold;
    }

    public AppLover setInstallDaysThreshold(final int installDaysThreshold) {
        mInstallDaysThreshold = installDaysThreshold;
        return this;
    }

    public int getDaysSinceInstallThreshold() {
        return mInstallDaysThreshold;
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
        if (mInstallDateTime == 0) {
            mInstallDateTime = new Date().getTime();
            preferences.setInstallationDate(mInstallDateTime);
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

    public int getDaysSinceInstall() {
        if (mInstallDateTime == 0) {
            return 0;
        }
        return (int) ((new Date().getTime() - mInstallDateTime) / ONE_DAY);
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
        mInstallDateTime = 0;
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