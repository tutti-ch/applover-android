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
        HashMap<String, Integer> mCustomEventThresholdMap =
                appLover.getCustomEventCountThresholds();
        for (Map.Entry<String, Integer> thresholdEntry : mCustomEventThresholdMap.entrySet()) {
            Integer eventCount = preferences.getCustomEventCount(thresholdEntry.getKey());
            if (thresholdEntry.getValue() > eventCount) {
                return false;
            }
        }
        return true;
    }
}
