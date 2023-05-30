package com.cometchat.chatuikit.extensions.linkpreview;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

public class LinkPreviewBubbleStyle extends BaseStyle {

    private @ColorInt
    int titleColor;
    private @ColorInt
    int subTitleColor;
    private @ColorInt
    int playIconTint;
    private @ColorInt
    int playIconBackgroundTint;
    private @StyleRes
    int titleAppearance;
    private @StyleRes
    int subTitleAppearance;
    private String titleFont;
    private String subTitleFont;

    @Override
    public LinkPreviewBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public LinkPreviewBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public LinkPreviewBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public LinkPreviewBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public LinkPreviewBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public LinkPreviewBubbleStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    public LinkPreviewBubbleStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public LinkPreviewBubbleStyle setPlayIconTint(int playIconTint) {
        this.playIconTint = playIconTint;
        return this;
    }

    public LinkPreviewBubbleStyle setPlayIconBackgroundTint(int playIconBackgroundTint) {
        this.playIconBackgroundTint = playIconBackgroundTint;
        return this;
    }

    public LinkPreviewBubbleStyle setSubTitleColor(int subTitleColor) {
        this.subTitleColor = subTitleColor;
        return this;
    }

    public LinkPreviewBubbleStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    public LinkPreviewBubbleStyle setSubTitleAppearance(int subTitleAppearance) {
        this.subTitleAppearance = subTitleAppearance;
        return this;
    }

    public LinkPreviewBubbleStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public LinkPreviewBubbleStyle setSubTitleFont(String subTitleFont) {
        this.subTitleFont = subTitleFont;
        return this;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getSubTitleColor() {
        return subTitleColor;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getSubTitleAppearance() {
        return subTitleAppearance;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public String getSubTitleFont() {
        return subTitleFont;
    }

    public int getPlayIconTint() {
        return playIconTint;
    }

    public int getPlayIconBackgroundTint() {
        return playIconBackgroundTint;
    }
}
