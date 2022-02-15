package com.cometchatworkspace.components.shared.primaryComponents.localize;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 *
 */
public class CometChatLocalize {
    public static void setLocale(Activity activity, @Language.Code String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public String getLocale(Activity activity) {
        Configuration config = activity.getResources().getConfiguration();
        return config.locale.getCountry();

    }

}
