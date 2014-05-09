package ch.tutti.android.applover.criteria;

import android.content.Context;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;

public class AppLoverOrCriteria implements AppLoverCriteria {

    private AppLoverCriteria first;

    private AppLoverCriteria second;

    public AppLoverOrCriteria(AppLoverCriteria first, AppLoverCriteria second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isCriteriaMet(Context context, AppLover appLover,
            AppLoverPreferences preferences) {
        return first.isCriteriaMet(context, appLover, preferences)
                || second.isCriteriaMet(context, appLover, preferences);
    }
}
