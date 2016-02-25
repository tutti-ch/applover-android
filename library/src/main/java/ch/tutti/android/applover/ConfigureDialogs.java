package ch.tutti.android.applover;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by adrianbusuioc on 2/25/16.
 */
public interface ConfigureDialogs {
    void configureFirstDialog(@NonNull MaterialDialog.Builder builder);

    void configureRateDialog(@NonNull MaterialDialog.Builder builder);

    void configureEmailDialog(@NonNull MaterialDialog.Builder builder);
}
