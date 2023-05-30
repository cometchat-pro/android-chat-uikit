package com.cometchat.chatuikit.extensions.imagemoderation;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

public class ImageModerationStyle extends BaseStyle {

    private @ColorInt
    int warningIconTint;
    private @ColorInt
    int warningTextColor;
    private @ColorInt
    int alertTextColor;
    private @ColorInt
    int alertPositiveButtonColor;
    private @ColorInt
    int alertNegativeButtonColor;
    private @ColorInt
    int alertDialogBackgroundColor;
    private @StyleRes
    int warningTextAppearance;
    private @StyleRes
    int alertTextAppearance;

    public ImageModerationStyle setWarningIconTint(int warningIconTint) {
        this.warningIconTint = warningIconTint;
        return this;
    }

    public ImageModerationStyle setWarningTextColor(int warningTextColor) {
        this.warningTextColor = warningTextColor;
        return this;
    }

    public ImageModerationStyle setAlertTextColor(int alertTextColor) {
        this.alertTextColor = alertTextColor;
        return this;
    }

    public ImageModerationStyle setAlertPositiveButtonColor(int alertPositiveButtonColor) {
        this.alertPositiveButtonColor = alertPositiveButtonColor;
        return this;
    }

    public ImageModerationStyle setAlertNegativeButtonColor(int alertNegativeButtonColor) {
        this.alertNegativeButtonColor = alertNegativeButtonColor;
        return this;
    }

    public ImageModerationStyle setAlertDialogBackgroundColor(int alertDialogBackgroundColor) {
        this.alertDialogBackgroundColor = alertDialogBackgroundColor;
        return this;
    }

    public ImageModerationStyle setWarningTextAppearance(int warningTextAppearance) {
        this.warningTextAppearance = warningTextAppearance;
        return this;
    }

    public ImageModerationStyle setAlertTextAppearance(int alertTextAppearance) {
        this.alertTextAppearance = alertTextAppearance;
        return this;
    }

    @Override
    public ImageModerationStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public ImageModerationStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public ImageModerationStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public ImageModerationStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public ImageModerationStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public ImageModerationStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    public int getWarningIconTint() {
        return warningIconTint;
    }

    public int getWarningTextColor() {
        return warningTextColor;
    }

    public int getAlertTextColor() {
        return alertTextColor;
    }

    public int getAlertPositiveButtonColor() {
        return alertPositiveButtonColor;
    }

    public int getAlertNegativeButtonColor() {
        return alertNegativeButtonColor;
    }

    public int getAlertDialogBackgroundColor() {
        return alertDialogBackgroundColor;
    }

    public int getWarningTextAppearance() {
        return warningTextAppearance;
    }

    public int getAlertTextAppearance() {
        return alertTextAppearance;
    }
}
