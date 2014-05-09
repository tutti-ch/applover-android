package ch.tutti.android.applover.criteria;

import android.content.Context;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;

public class AppLoverAppLaunchCriteria implements AppLoverCriteria {

    @Override
    public boolean isCriteriaMet(Context context, AppLover appLover,
            AppLoverPreferences preferences) {
        return appLover.getLaunchCount() >= appLover.getLaunchCountThreshold();
    }
}
