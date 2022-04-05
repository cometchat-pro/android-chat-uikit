package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

import androidx.annotation.ColorInt;

import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;

public class StatusIndicatorConfiguration extends CometChatConfigurations {

    private int color;
    private @CometChatStatusIndicator.STATUS
    String status;
    private int cornerRadius;
    private int borderWidth;
    private int width;
    private int height;
    private final Context context;

    public StatusIndicatorConfiguration(Context context) {
        this.context = context;
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

    public StatusIndicatorConfiguration setColor(@ColorInt int color,@CometChatStatusIndicator.STATUS String status) {
        this.color = color;
        this.status = status;
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

    public String getStatus() {
        return status;
    }
}
