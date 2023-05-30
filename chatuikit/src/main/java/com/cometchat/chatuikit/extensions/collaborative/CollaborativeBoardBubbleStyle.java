package com.cometchat.chatuikit.extensions.collaborative;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

public class CollaborativeBoardBubbleStyle extends BaseStyle {

    private @ColorRes
    int titleColor;
    private @ColorRes
    int subtitleColor;
    private @ColorRes
    int buttonTextColor;
    private @ColorRes
    int separatorColor;
    private @StyleRes
    int buttonTextAppearance;
    private @StyleRes
    int subtitleTextAppearance;
    private @StyleRes
    int titleTextAppearance;
    private @ColorRes
    int iconTint;

    @Override
    public CollaborativeBoardBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public CollaborativeBoardBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public CollaborativeBoardBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    public CollaborativeBoardBubbleStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    @Override
    public CollaborativeBoardBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public CollaborativeBoardBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public CollaborativeBoardBubbleStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    public CollaborativeBoardBubbleStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public CollaborativeBoardBubbleStyle setSubtitleColor(int subtitleColor) {
        this.subtitleColor = subtitleColor;
        return this;
    }

    public CollaborativeBoardBubbleStyle setButtonTextColor(int buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
        return this;
    }

    public CollaborativeBoardBubbleStyle setButtonTextAppearance(int buttonTextAppearance) {
        this.buttonTextAppearance = buttonTextAppearance;
        return this;
    }

    public CollaborativeBoardBubbleStyle setSubtitleTextAppearance(int subtitleTextAppearance) {
        this.subtitleTextAppearance = subtitleTextAppearance;
        return this;
    }

    public CollaborativeBoardBubbleStyle setTitleTextAppearance(int titleTextAppearance) {
        this.titleTextAppearance = titleTextAppearance;
        return this;
    }

    public CollaborativeBoardBubbleStyle setIconTint(int iconTint) {
        this.iconTint = iconTint;
        return this;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getSubtitleColor() {
        return subtitleColor;
    }

    public int getButtonTextColor() {
        return buttonTextColor;
    }

    public int getButtonTextAppearance() {
        return buttonTextAppearance;
    }

    public int getSubtitleTextAppearance() {
        return subtitleTextAppearance;
    }

    public int getTitleTextAppearance() {
        return titleTextAppearance;
    }

    public int getIconTint() {
        return iconTint;
    }

    public int getSeparatorColor() {
        return separatorColor;
    }
}
