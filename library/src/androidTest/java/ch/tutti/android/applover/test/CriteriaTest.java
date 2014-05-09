package ch.tutti.android.applover.test;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import ch.tutti.android.applover.AppLover;
import ch.tutti.android.applover.AppLoverPreferences;
import ch.tutti.android.applover.criteria.AppLoverAndCriteria;
import ch.tutti.android.applover.criteria.AppLoverAppLaunchCriteria;
import ch.tutti.android.applover.criteria.AppLoverCriteria;
import ch.tutti.android.applover.criteria.AppLoverCriteriaBuilder;
import ch.tutti.android.applover.criteria.AppLoverCustomEventCriteria;
import ch.tutti.android.applover.criteria.AppLoverOrCriteria;

public class CriteriaTest extends AndroidTestCase {

    private static final String EVENT_TEST1 = "test1";

    private static final String EVENT_TEST2 = "test2";

    private AppLover mAppLover;

    private AppLoverPreferences mPreferences;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAppLover = AppLover.get(getContext());
        mPreferences = new AppLoverPreferences(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SmallTest
    public void testSingleCriteria() {
        assertTrue(new TestCriteria(true).isCriteriaMet(getContext(), mAppLover, mPreferences));
        assertFalse(new TestCriteria(false).isCriteriaMet(getContext(), mAppLover, mPreferences));
    }

    @SmallTest
    public void testAndCriteria() {
        // true && true == true
        assertTrue(new AppLoverAndCriteria(new TestCriteria(true), new TestCriteria(true)));

        // true && false == false
        assertFalse(new AppLoverAndCriteria(new TestCriteria(true), new TestCriteria(false)));

        // false && true == false
        assertFalse(new AppLoverAndCriteria(new TestCriteria(false), new TestCriteria(true)));

        // false && false == false
        assertFalse(new AppLoverAndCriteria(new TestCriteria(false), new TestCriteria(false)));
    }

    @SmallTest
    public void testOrCriteria() {
        // true && true == true
        assertTrue(new AppLoverOrCriteria(new TestCriteria(true), new TestCriteria(true)));

        // true && false == true
        assertTrue(new AppLoverOrCriteria(new TestCriteria(true), new TestCriteria(false)));

        // false && true == true
        assertTrue(new AppLoverOrCriteria(new TestCriteria(false), new TestCriteria(true)));

        // false && false == false
        assertFalse(new AppLoverOrCriteria(new TestCriteria(false), new TestCriteria(false)));
    }

    @SmallTest
    public void testCriteriaBuilder() {
        assertTrue(
                new AppLoverCriteriaBuilder(
                        new TestCriteria(true)
                ).or(
                        new TestCriteria(false)
                ).build()
        );
        assertFalse(
                new AppLoverCriteriaBuilder(
                        new TestCriteria(true)
                ).and(
                        new TestCriteria(false)
                ).build()
        );

        assertTrue(
                new AppLoverCriteriaBuilder(
                        new AppLoverCriteriaBuilder(
                                new TestCriteria(true)
                        ).and(
                                new TestCriteria(true)
                        ).build()
                ).and(
                        new AppLoverCriteriaBuilder(
                                new TestCriteria(true)
                        ).and(
                                new TestCriteria(true)
                        ).build()
                ).build()
        );
    }

    @SmallTest
    public void testAppLaunchesCriteria() {
        AppLoverCriteria criteria = new AppLoverAppLaunchCriteria();

        mAppLover.setLaunchCountThreshold(3);
        mAppLover.monitorLaunch(getContext());
        mAppLover.monitorLaunch(getContext());
        assertFalse(criteria);

        mAppLover.monitorLaunch(getContext());
        assertTrue(criteria);

        mAppLover.monitorLaunch(getContext());
        assertTrue(criteria);
    }

    @SmallTest
    public void testCustomEventCriteria() {
        AppLoverCriteria criteria = new AppLoverCustomEventCriteria(EVENT_TEST1);
        AppLoverCriteria criteria2 = new AppLoverCustomEventCriteria(EVENT_TEST2);

        mAppLover.setCustomEventCountThreshold(EVENT_TEST1, 3);
        mAppLover.monitorCustomEvent(getContext(), EVENT_TEST1);
        mAppLover.monitorCustomEvent(getContext(), EVENT_TEST1);
        mAppLover.monitorCustomEvent(getContext(), EVENT_TEST2);
        assertFalse(criteria);
        assertFalse(criteria2);

        mAppLover.monitorCustomEvent(getContext(), EVENT_TEST1);
        mAppLover.monitorCustomEvent(getContext(), EVENT_TEST2);
        assertTrue(criteria);
        assertFalse(criteria2);

        mAppLover.monitorCustomEvent(getContext(), EVENT_TEST1);
        mAppLover.monitorCustomEvent(getContext(), EVENT_TEST2);
        assertTrue(criteria);
        assertTrue(criteria2);
    }

    private boolean assertTrue(AppLoverCriteria criteria) {
        return assertBool(true, criteria);
    }

    private boolean assertFalse(AppLoverCriteria criteria) {
        return assertBool(false, criteria);
    }

    private boolean assertBool(boolean assertTrue, AppLoverCriteria criteria) {
        return assertTrue == criteria.isCriteriaMet(getContext(), mAppLover, mPreferences);
    }

    private class TestCriteria implements AppLoverCriteria {

        private final boolean mIsMet;

        public TestCriteria(boolean isMet) {
            mIsMet = isMet;
        }

        @Override
        public boolean isCriteriaMet(Context context, AppLover appLover,
                AppLoverPreferences preferences) {
            return mIsMet;
        }
    }
}
