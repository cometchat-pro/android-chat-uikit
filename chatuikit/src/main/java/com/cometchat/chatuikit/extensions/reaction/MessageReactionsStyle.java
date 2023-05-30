package com.cometchat.chatuikit.extensions.reaction;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

import com.cometchat.chatuikit.shared.models.BaseStyle;

public class MessageReactionsStyle extends BaseStyle {

    private @ColorInt
    int addReactionIconTint;
    private @ColorInt
    int addReactionIconBackground;
    private @ColorInt
    int textColor;
    private @ColorInt
    int textAppearance;


    public MessageReactionsStyle setAddReactionIconTint(int addReactionIconTint) {
        this.addReactionIconTint = addReactionIconTint;
        return this;
    }

    public MessageReactionsStyle setAddReactionIconBackground(int addReactionIconBackground) {
        this.addReactionIconBackground = addReactionIconBackground;
        return this;
    }

    public MessageReactionsStyle setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public MessageReactionsStyle setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    @Override
    public MessageReactionsStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public MessageReactionsStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public MessageReactionsStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public MessageReactionsStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public MessageReactionsStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public MessageReactionsStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    public int getAddReactionIconTint() {
        return addReactionIconTint;
    }

    public int getAddReactionIconBackground() {
        return addReactionIconBackground;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextAppearance() {
        return textAppearance;
    }
}
