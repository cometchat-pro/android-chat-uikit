package com.cometchat.chatuikit.calls.calldetails;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.details.DetailsStyle;
import com.cometchat.chatuikit.shared.models.BaseStyle;

 class CallDetailsStyle extends BaseStyle {
    private final String TAG = CallDetailsStyle.class.getName();

    private String titleFont;
    private @StyleRes
    int titleAppearance;
    private @ColorInt
    int titleColor;
    private @ColorInt
    int closeIconTint;

    @Override
    public CallDetailsStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public CallDetailsStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public CallDetailsStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public CallDetailsStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public CallDetailsStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public CallDetailsStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    public CallDetailsStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public CallDetailsStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    public CallDetailsStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public CallDetailsStyle setCloseIconTint(int closeIconTint) {
        this.closeIconTint = closeIconTint;
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

}
