package com.cometchat.chatuikit.shared.models;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;

/**
 * BaseStyle class represents the common style attributes for views in CometChat UI Kit.
 * It contains attributes for background color, active background color, drawable background, corner radius,
 * border width and border color.
 */
public class BaseStyle {
    private @ColorInt
    int background;
    private @ColorInt
    int activeBackground;
    private Drawable drawableBackground;
    private float cornerRadius = -1;
    private @Dimension
    int borderWidth = -1;
    private @ColorInt
    int borderColor;

    /**
     * Sets the background color of the view.
     *
     * @param background The background color of the view.
     */
    public BaseStyle setBackground(int background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the drawable background of the view.
     *
     * @param drawableBackground The drawable background of the view.
     */
    public BaseStyle setBackground(Drawable drawableBackground) {
        this.drawableBackground = drawableBackground;
        return this;
    }

    /**
     * Sets the corner radius of the view.
     *
     * @param cornerRadius The corner radius of the view.
     */
    public BaseStyle setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    /**
     * Sets the width of the view border.
     *
     * @param borderWidth The width of the view border.
     */
    public BaseStyle setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * Sets the color of the view border.
     *
     * @param borderColor The color of the view border.
     */
    public BaseStyle setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * Sets the background color of the view when it is in active state.
     *
     * @param activeBackground The background color of the view when it is in active state.
     */
    public BaseStyle setActiveBackground(int activeBackground) {
        this.activeBackground = activeBackground;
        return this;
    }

    public int getBackground() {
        return background;
    }

    public Drawable getDrawableBackground() {
        return drawableBackground;
    }

    public float getCornerRadius() {
        return cornerRadius;
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
