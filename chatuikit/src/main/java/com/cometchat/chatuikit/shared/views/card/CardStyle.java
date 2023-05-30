package com.cometchat.chatuikit.shared.views.card;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Represents a style configuration for a card view.
 * <p>
 * This class extends the BaseStyle class.
 */
public class CardStyle extends BaseStyle {

    private String titleFont;

    private @ColorInt
    int titleColor;

    private @StyleRes
    int titleAppearance;

    @Override
    public CardStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public CardStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public CardStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public CardStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public CardStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public CardStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    /**
     * Sets the font for the title in the card view.
     *
     * @param titleFont The name of the font file in the assets directory.
     */
    public CardStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the color for the title in the card view.
     *
     * @param titleColor The color resource ID for the title text.
     */
    public CardStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the text appearance for the title in the card view.
     *
     * @param titleAppearance The style resource ID for the title text appearance.
     */
    public CardStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }
}
