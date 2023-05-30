package com.cometchat.chatuikit.shared.views.CometChatDate;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Represents the style configuration for the CometChatDate view.
 */
public class DateStyle extends BaseStyle {
    private String textFont;
    private @ColorInt
    int textColor;
    private @StyleRes
    int textAppearance;
    private int textSize;

    /**
     * Sets the font for the text of the date.
     *
     * @param textFont the font to set
     */
    public DateStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * Sets the color for the text of the date.
     *
     * @param textColor the text color to set
     */
    public DateStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * Sets the text appearance for the date.
     *
     * @param textAppearance the text appearance to set
     */
    public DateStyle setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    /**
     * Sets the text size for the date.
     *
     * @param textSize the text size to set
     */
    public DateStyle setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * Sets the background color for the date.
     *
     * @param background the background color to set
     */
    @Override
    public DateStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    /**
     * Sets the drawable background for the date.
     *
     * @param drawableBackground the drawable background to set
     */
    @Override
    public DateStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    /**
     * Sets the corner radius for the date.
     *
     * @param cornerRadius the corner radius to set
     */
    @Override
    public DateStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    /**
     * Sets the border width for the date.
     *
     * @param borderWidth the border width to set
     */
    @Override
    public DateStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    /**
     * Sets the border color for the date.
     *
     * @param borderColor the border color to set
     */
    @Override
    public DateStyle setBorderColor(int borderColor) {
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
