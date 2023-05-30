package com.cometchat.chatuikit.shared.resources.Localise;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * The CometChatLocalize class provides utility methods for managing localization in an Android application.
 * <p>
 * It allows setting the locale and retrieving the locale country code for an activity.
 */
public class CometChatLocalize {
    /**
     * Sets the locale for the specified activity.
     *
     * @param activity the activity for which to set the locale
     * @param language the language code representing the desired locale (e.g., "en" for English, "fr" for French)
     */
    public static void setLocale(Activity activity, @Language.Code String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * Retrieves the locale country code for the specified activity.
     *
     * @param activity the activity for which to retrieve the locale country code
     * @return the country code of the current locale in the specified activity
     */
    public String getLocale(Activity activity) {
        Configuration config = activity.getResources().getConfiguration();
        return config.locale.getCountry();
    }

}
