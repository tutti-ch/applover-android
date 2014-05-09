package ch.tutti.android.applover.criteria;

import android.content.Context;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;

/**
 * Criteria that requires the app to have been launched for the first time the number of days as
 * set in {@link ch.tutti.android.applover.AppLover#setFirstLaunchDaysThreshold(int)}.
 */
public class AppLoverFirstLaunchDaysCriteria implements AppLoverCriteria {

    @Override
    public boolean isCriteriaMet(Context context, AppLover appLover,
            AppLoverPreferences preferences) {
        return appLover.getDaysSinceFirstLaunch() >= appLover.getFirstLaunchDaysThreshold();
    }
}
