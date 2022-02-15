package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

import androidx.annotation.ColorInt;

public class AvatarConfiguration extends CometChatConfigurations {

    private int cornerRadius=-1;
    private int borderWidth=-1;
    private int outerViewWidth=-1;
    private float width=-1;
    private float height=-1;
    private int outerViewColor;
    private final Context context;

    public AvatarConfiguration(Context context) {
            this.context = context;
    }

    public AvatarConfiguration setCornerRadius(int radius) {
        cornerRadius = radius;
        return this;
    }

    public int getCornerRadius() {
            return cornerRadius;
    }
    public AvatarConfiguration setBorderWidth(int width) {
        borderWidth = width;
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public AvatarConfiguration setOuterViewWidth(int outerViewWidth) {
        this.outerViewWidth = outerViewWidth;
        return this;
    }

    public int getOuterViewWidth() {
        return outerViewWidth;
    }

    public AvatarConfiguration setOuterViewColor(@ColorInt int color) {
        this.outerViewColor = color;
        return this;
    }

    public int getOuterViewColor() {
        return outerViewColor;
    }

    public AvatarConfiguration setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public AvatarConfiguration setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getHeight() {
        return height;
    }


}
