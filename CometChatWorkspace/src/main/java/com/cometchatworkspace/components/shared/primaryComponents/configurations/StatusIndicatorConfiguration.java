package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

import androidx.annotation.ColorInt;

public class StatusIndicatorConfiguration extends CometChatConfigurations {
    private boolean isAvatarHidden;

    private int color;
    private int cornerRadius;
    private int titleColor;
    private int borderWidth;
    private int width;
    private int height;
    private final Context context;

    public StatusIndicatorConfiguration(Context context) {
        this.context = context;
    }

    public StatusIndicatorConfiguration setTextColor(@ColorInt int color) {
        titleColor = color;
        return this;
    }


    public StatusIndicatorConfiguration setCornerRadius(int radius) {
        cornerRadius = radius;
        return this;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public StatusIndicatorConfiguration setBorderWidth(int width) {
        borderWidth = width;
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public StatusIndicatorConfiguration setColor(@ColorInt int color) {
        this.color = color;
        return this;
    }


    public StatusIndicatorConfiguration setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public StatusIndicatorConfiguration setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public int getColor() {
        return color;
    }
}
