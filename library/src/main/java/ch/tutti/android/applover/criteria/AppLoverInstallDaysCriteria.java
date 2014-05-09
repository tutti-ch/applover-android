package ch.tutti.android.applover.criteria;

import android.content.Context;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;

/**
 * Criteria that requires the app to have been installed for the days set in AppLover.
 */
public class AppLoverInstallDaysCriteria implements AppLoverCriteria {

    @Override
    public boolean isCriteriaMet(Context context, AppLover appLover,
            AppLoverPreferences preferences) {
        return appLover.getDaysSinceInstall() >= appLover.getDaysSinceInstallThreshold();
    }
}
