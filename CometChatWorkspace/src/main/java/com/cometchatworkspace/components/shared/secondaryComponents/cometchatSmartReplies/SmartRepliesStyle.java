package com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;

import com.cometchatworkspace.components.shared.base.CometChatStyle;

public class SmartRepliesStyle extends CometChatStyle {
    private String textFont=null;
    private int TextAppearance=0;
    private @ColorInt
    int textColor=0;
    private @ColorInt
    int textBackground=0;
    private
    Drawable textBackgroundDrawable=null;
    private float textBorderRadius=0;
    private @Dimension int textBorderWidth=0;
    private @ColorInt
    int closeIconTint=0;
    private @ColorInt
    int textBorderColor=0;

    public SmartRepliesStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    public SmartRepliesStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public SmartRepliesStyle setTextBackground(int textBackground) {
        this.textBackground = textBackground;
        return this;
    }

    public SmartRepliesStyle setTextBackgroundDrawable(Drawable textBackgroundDrawable) {
        this.textBackgroundDrawable = textBackgroundDrawable;
        return this;
    }

    public SmartRepliesStyle setTextBorderRadius(float textBorderRadius) {
        this.textBorderRadius = textBorderRadius;
        return this;
    }

    public SmartRepliesStyle setTextBorderWidth(int textBorderWidth) {
        this.textBorderWidth = textBorderWidth;
        return this;
    }

    public SmartRepliesStyle setTextAppearance(int textAppearance) {
        TextAppearance = textAppearance;
        return this;
    }

    public SmartRepliesStyle setCloseIconTint(int closeIconTint) {
        this.closeIconTint = closeIconTint;
        return this;
    }

    @Override
    public SmartRepliesStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public SmartRepliesStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public SmartRepliesStyle setBorderRadius(float borderRadius) {
        super.setBorderRadius(borderRadius);
        return this;
    }

    @Override
    public SmartRepliesStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public SmartRepliesStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    public SmartRepliesStyle setTextBorderColor(int textBorderColor) {
        this.textBorderColor = textBorderColor;
        return this;
    }

    public int getTextBorderColor() {
        return textBorderColor;
    }

    public String getTextFont() {
        return textFont;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextBackground() {
        return textBackground;
    }

    public Drawable getTextBackgroundDrawable() {
        return textBackgroundDrawable;
    }

    public float getTextBorderRadius() {
        return textBorderRadius;
    }

    public int getTextBorderWidth() {
        return textBorderWidth;
    }

    public int getCloseIconTint() {
        return closeIconTint;
    }

    public int getTextAppearance() {
        return TextAppearance;
    }
}
