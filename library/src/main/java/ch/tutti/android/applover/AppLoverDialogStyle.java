package ch.tutti.android.applover;

public class AppLoverDialogStyle {

    public Style loveDialogStyle;

    public Style rateDialogStyle;

    public Style emailDialogStyle;

    public AppLoverDialogStyle loveStyle(Style style) {
        loveDialogStyle = style;
        return this;
    }

    public AppLoverDialogStyle rateStyle(Style style) {
        rateDialogStyle = style;
        return this;
    }

    public AppLoverDialogStyle emailStyle(Style style) {
        emailDialogStyle = style;
        return this;
    }

    public static class Style {

        int theme;

        int positiveButtonBackground;

        int neutralButtonBackground;

        int negativeButtonBackground;

        public Style theme(int theme) {
            this.theme = theme;
            return this;
        }

        public Style positiveBackground(int background) {
            positiveButtonBackground = background;
            return this;
        }

        public Style neutralBackground(int background) {
            neutralButtonBackground = background;
            return this;
        }

        public Style negativeBackground(int background) {
            negativeButtonBackground = background;
            return this;
        }
    }
}
