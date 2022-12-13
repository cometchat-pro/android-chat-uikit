package com.cometchatworkspace.components.shared.base;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;

public class CometChatStyle {
    private @ColorInt
    int background;
    private @ColorInt
    int activeBackground;
    private Drawable drawableBackground;
    private float borderRadius;
    private @Dimension
    int borderWidth;
    private @ColorInt
    int borderColor;

    public CometChatStyle setBackground(int background) {
        this.background = background;
        return this;
    }

    public CometChatStyle setBackground(Drawable drawableBackground) {
        this.drawableBackground = drawableBackground;
        return this;
    }

    public CometChatStyle setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        return this;
    }

    public CometChatStyle setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public CometChatStyle setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public CometChatStyle setActiveBackground(int activeBackground) {
        this.activeBackground = activeBackground;
        return this;
    }

    public int getBackground() {
        return background;
    }

    public Drawable getDrawableBackground() {
        return drawableBackground;
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getActiveBackground() {
        return activeBackground;
    }


}
