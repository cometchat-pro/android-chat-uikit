package com.cometchat.chatuikit.shared.resources.theme;

import android.content.Context;

import androidx.annotation.StringDef;

/**
 * This class represents the theme configuration for CometChat UI components.
 */
public class CometChatTheme {

    @StringDef({MODE.LIGHT, MODE.DARK})
    public @interface MODE {
        String LIGHT = "light";
        String DARK = "dark";
    }

    private Palette palette;
    private Typography typography;
    private static Context contextt;
    private static final CometChatTheme instance = new CometChatTheme();

    private CometChatTheme() {
    }

    /**
     * Returns the singleton instance of CometChatTheme.
     *
     * @param context The application context.
     * @return The singleton instance of CometChatTheme.
     */
    public static CometChatTheme getInstance(Context context) {
        contextt = context;
        return instance;
    }

    /**
     * Sets the color palette for the theme.
     *
     * @param palette The color palette to set.
     */
    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    /**
     * Sets the typography for the theme.
     *
     * @param typography The typography to set.
     */
    public void setTypography(Typography typography) {
        this.typography = typography;
    }

    /**
     * Returns the color palette of the theme.
     * If no palette is set, it returns the default palette instance.
     *
     * @return The color palette.
     */
    public Palette getPalette() {
        return palette != null ? palette : Palette.getInstance(contextt);
    }

    /**
     * Returns the typography of the theme.
     * If no typography is set, it returns the default typography instance.
     *
     * @return The typography.
     */
    public Typography getTypography() {
        return typography != null ? typography : Typography.getInstance();
    }
}
