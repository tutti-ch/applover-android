package ch.tutti.android.applover.criteria;

import android.content.Context;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;

public class AppLoverCustomEventCriteria implements AppLoverCriteria {

    private final String mEvent;

    public AppLoverCustomEventCriteria(String event) {
        mEvent = event;
    }

    @Override
    public boolean isCriteriaMet(Context context, AppLover appLover,
            AppLoverPreferences preferences) {
        return preferences.getCustomEventCount(mEvent)
                >= appLover.getCustomEventCountThreshold(mEvent);
    }
}
