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

import android.content.Context;
import android.content.SharedPreferences;

public class AppLoverPreferences {

    private static final String PREFERENCES_NAME = "android-applover";

    private static final String KEY_FIRST_LAUNCH_DATE = "applover_first_launch_date";

    private static final String KEY_APP_LAUNCH_COUNT = "applover_app_launch_count";

    private static final String KEY_CUSTOM_EVENT = "applover_custom_event_";

    private static final String KEY_DO_NOT_SHOW_ANYMORE = "applover_do_not_show_anymore";

    private final SharedPreferences mPreferences;

    public AppLoverPreferences(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public long getFirstLaunchDate() {
        return mPreferences.getLong(KEY_FIRST_LAUNCH_DATE, 0);
    }

    public void setFirstLaunchDate(long firstLaunchDate) {
        mPreferences.edit().putLong(KEY_FIRST_LAUNCH_DATE, firstLaunchDate).commit();
    }

    public int getLaunchCount() {
        return mPreferences.getInt(KEY_APP_LAUNCH_COUNT, 0);
    }

    /**
     * Increments the launch count.
     *
     * @return the new launch count after the increment
     */
    public int incLaunchCount() {
        return inc(KEY_APP_LAUNCH_COUNT);
    }

    /**
     * Increments the count of a custom event.
     *
     * @param event the event for which the count should be incremented
     * @return the new count after the increment
     */
    public int incCustomEventCount(String event) {
        return inc(KEY_CUSTOM_EVENT + event);
    }

    private int inc(String key) {
        int launchCount = mPreferences.getInt(key, 0) + 1;
        mPreferences.edit().putInt(key, launchCount).commit();
        return launchCount;
    }

    public int getCustomEventCount(String event) {
        return mPreferences.getInt(KEY_CUSTOM_EVENT + event, 0);
    }

    /**
     * Retrieves if dialog should not be shown anymore. Meaning the user already went through the
     * dialog and finished the task it was designed to do.
     *
     * @return true if the dialog should not be shown anymore
     */
    public boolean isDoNotShowAnymore() {
        return mPreferences.getBoolean(KEY_DO_NOT_SHOW_ANYMORE, false);
    }

    /**
     * Sets if the dialog should not be shown anymore hereafter. Called when the dialogs task was
     * fulfilled.
     */
    public void setDoNotShowAnymore() {
        mPreferences.edit().putBoolean(KEY_DO_NOT_SHOW_ANYMORE, true).commit();
    }

    /**
     * Clears all the preferences.
     */
    public void clear() {
        mPreferences.edit().clear().commit();
    }
}
