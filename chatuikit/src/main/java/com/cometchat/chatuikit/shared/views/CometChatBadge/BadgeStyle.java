package com.cometchat.chatuikit.shared.views.CometChatBadge;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * BadgeStyle is a class that represents the style configuration for a badge.
 * It extends the BaseStyle class and provides methods to set the text font, text color,
 * text appearance, text size, background, corner radius, border width, and border color of the badge.
 */
public class BadgeStyle extends BaseStyle {

    private String textFont;
    private @ColorInt
    int textColor=0;
    private @StyleRes
    int textAppearance;
    private int textSize;

    /**
     * Sets the text font for the badge.
     *
     * @param textFont The font to set for the badge text.
     */
    public BadgeStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * Sets the text color for the badge.
     *
     * @param textColor The color to set for the badge text.
     */
    public BadgeStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Sets the text appearance for the badge.
     *
     * @param textAppearance The style resource ID to set as the text appearance for the badge.
     */
    public BadgeStyle setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    /**
     * Sets the text size for the badge.
     *
     * @param textSize The text size to set for the badge.
     */
    public BadgeStyle setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * Sets the background color or drawable for the badge.
     *
     * @param background The color or drawable to set as the background for the badge.
     */
    @Override
    public BadgeStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    /**
     * Sets the drawable background for the badge.
     *
     * @param drawableBackground The drawable to set as the background for the badge.
     */
    @Override
    public BadgeStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    /**
     * Sets the corner radius for the badge.
     *
     * @param cornerRadius The corner radius to set for the badge.
     */
    @Override
    public BadgeStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    /**
     * Sets the border width for the badge.
     *
     * @param borderWidth The border width to set for the badge.
     */
    @Override
    public BadgeStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    /**
     * Sets the border color for the badge.
     *
     * @param borderColor The color to set as the border color for the badge.
     */
    @Override
    public BadgeStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    public String getTextFont() {
        return textFont;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextAppearance() {
        return textAppearance;
    }

    public int getTextSize() {
        return textSize;
    }
}
