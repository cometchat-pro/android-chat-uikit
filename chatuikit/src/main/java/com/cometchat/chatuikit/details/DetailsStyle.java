package com.cometchat.chatuikit.details;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The DetailsStyle class represents the style configuration for CometChatDetails.
 * <p>
 * It extends the BaseStyle class.
 */
public class DetailsStyle extends BaseStyle {
    private final String TAG = DetailsStyle.class.getName();

    private String titleFont;
    private @StyleRes int titleAppearance;
    private @ColorInt int titleColor;
    private @ColorInt int closeIconTint;
    private @ColorInt int onlineStatusColor;

    @Override
    public DetailsStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public DetailsStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public DetailsStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public DetailsStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public DetailsStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public DetailsStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the font for the title in CometChatDetails.
     *
     * @param titleFont The font for the title.
     */
    public DetailsStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the appearance for the title in CometChatDetails.
     *
     * @param titleAppearance The appearance for the title.
     */
    public DetailsStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    /**
     * Sets the color for the title in CometChatDetails.
     *
     * @param titleColor The color for the title.
     */
    public DetailsStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the tint color for the close icon in CometChatDetails.
     *
     * @param closeIconTint The tint color for the close icon.
     */
    public DetailsStyle setCloseIconTint(int closeIconTint) {
        this.closeIconTint = closeIconTint;
        return this;
    }

    /**
     * Sets the color for the online status in CometChatDetails.
     *
     * @param onlineStatusColor The color for the online status.
     */
    public DetailsStyle setOnlineStatusColor(int onlineStatusColor) {
        this.onlineStatusColor = onlineStatusColor;
        return this;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getCloseIconTint() {
        return closeIconTint;
    }

    public int getOnlineStatusColor() {
        return onlineStatusColor;
    }
}
