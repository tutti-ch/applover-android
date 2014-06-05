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

public class AppLoverCriteriaBuilder {

    private AppLoverCriteria mCriteria;

    public AppLoverCriteriaBuilder(AppLoverCriteria criteria) {
        mCriteria = criteria;
    }

    public AppLoverCriteriaBuilder and(AppLoverCriteria criteria) {
        mCriteria = new AppLoverAndCriteria(mCriteria, criteria);
        return this;
    }

    public AppLoverCriteriaBuilder or(AppLoverCriteria criteria) {
        mCriteria = new AppLoverOrCriteria(mCriteria, criteria);
        return this;
    }

    public AppLoverCriteria build() {
        return mCriteria;
    }
}
