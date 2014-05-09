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
