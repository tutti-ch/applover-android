package ch.tutti.android.applover.criteria;

import android.content.Context;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;

/**
 * Criteria that requires the app to have been installed for a specific amount of days.
 */
public class AppLoverInstallDaysCriteria implements AppLoverCriteria {

    public AppLoverInstallDaysCriteria() {
    }

    @Override
    public boolean isCriteriaMet(Context context, AppLover appLover,
            AppLoverPreferences preferences) {
        return appLover.getInstalledDays(context) >= appLover.getInstallDaysThreshold();
    }
}
