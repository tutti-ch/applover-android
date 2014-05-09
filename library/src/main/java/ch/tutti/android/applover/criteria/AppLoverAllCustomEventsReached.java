package ch.tutti.android.applover.criteria;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;

public class AppLoverAllCustomEventsReached implements AppLoverCriteria {

    @Override
    public boolean isCriteriaMet(Context context, AppLover appLover,
            AppLoverPreferences preferences) {
        HashMap<String, Integer> mCustomEventThresholdMap = appLover.getCustomEventCountThresholds();
        for (Map.Entry<String, Integer> thresholdEntry : mCustomEventThresholdMap.entrySet()) {
            Integer eventCount = preferences.getCustomEventCount(thresholdEntry.getKey());
            if (thresholdEntry.getValue() > eventCount) {
                return false;
            }
        }
        return true;
    }
}
