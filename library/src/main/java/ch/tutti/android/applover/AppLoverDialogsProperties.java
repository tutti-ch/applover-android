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
package ch.tutti.android.applover;

import android.support.annotation.NonNull;

public class AppLoverDialogsProperties {

    @NonNull
    public Style loveDialogStyle = new AppLoverDialogsProperties.Style();

    @NonNull
    public Style rateDialogStyle = new AppLoverDialogsProperties.Style();

    @NonNull
    public Style emailDialogStyle = new AppLoverDialogsProperties.Style();

    public AppLoverDialogsProperties loveStyle(Style style) {
        loveDialogStyle = style;
        return this;
    }

    public AppLoverDialogsProperties rateStyle(Style style) {
        rateDialogStyle = style;
        return this;
    }

    public AppLoverDialogsProperties emailStyle(Style style) {
        emailDialogStyle = style;
        return this;
    }

    public static class Style {

        int theme;
        
        int positiveButtonBackground;
        
        int neutralButtonBackground;
        
        int negativeButtonBackground;
        
        // string resources ids for text customization
        private int message, positiveButtonText, negativeButtonText, neutralButtonText;
        
        boolean showNeutralButton = true; // show by default

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

        public Style message(int stringId) {
            message = stringId;
            return this;
        }
        
        public Style positiveButtonText(int stringId) {
            positiveButtonText = stringId;
            return this;
        }


        public Style negativeButtonText(int stringId) {
            negativeButtonText = stringId;
            return this;
        }


        public Style neutralButtonText(int stringId) {
            neutralButtonText = stringId;
            return this;
        }

        /**
         * get the string id if previously set, default value otherwise
         */
        int getMessage(int defaultValue) {
           return message > 0 ? message : defaultValue;
        }

        /**
         * get the string id if previously set, default value otherwise
         */
        int getPositiveButtonText(int defaultValue) {
            return positiveButtonText > 0 ? positiveButtonText : defaultValue;
        }

        /**
         * get the string id if previously set, default value otherwise
         */
        int getNegativeButtonText(int defaultValue) {
            return negativeButtonText > 0 ? negativeButtonText : defaultValue;
        }

        /**
         * get the string id if previously set, default value otherwise
         */
        int getNeutralButtonText(int defaultValue) {
            return neutralButtonText > 0 ? neutralButtonText : defaultValue;
        }
        
        public void showNeutralButton(boolean show) {
            showNeutralButton = show;
        }
    }
}
