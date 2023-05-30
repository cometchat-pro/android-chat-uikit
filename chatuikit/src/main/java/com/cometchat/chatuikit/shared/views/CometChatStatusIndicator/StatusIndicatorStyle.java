package com.cometchat.chatuikit.shared.views.CometChatStatusIndicator;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * StatusIndicatorStyle Represents a style  for a status indicator view.
 * <p>
 * It provides properties for customizing the appearance of the status indicator view such as width, height,
 * <p>
 * background, corner radius, border width, and border color.
 */
public class StatusIndicatorStyle extends BaseStyle {

    private int width = 0;
    private int height = 0;

    @Override
    public StatusIndicatorStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public StatusIndicatorStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public StatusIndicatorStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    /**
     * Sets the width of the status indicator view.
     *
     * @param width The width to set.
     */
    public StatusIndicatorStyle setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the height of the status indicator view.
     *
     * @param height The height to set.
     * @return The current instance of the StatusIndicatorStyle for method chaining.
     */
    public StatusIndicatorStyle setHeight(int height) {
        this.height = height;
        return this;
    }

    @Override
    public StatusIndicatorStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public StatusIndicatorStyle setBorderColor(@ColorInt int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
