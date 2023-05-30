package com.cometchat.chatuikit.shared.views.CometChatListItem;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * A style class that defines the appearance of a list item.
 * <p>
 * This class extends the {@link BaseStyle} class and provides additional properties for customizing the list item.
 */
public class ListItemStyle extends BaseStyle {

    private String titleFont;

    private @ColorInt
    int titleColor;

    private @StyleRes
    int titleAppearance;

    private @ColorInt
    int separatorColor;

    private Drawable separatorDrawable;

    @Override
    public ListItemStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public ListItemStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public ListItemStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public ListItemStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public ListItemStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public ListItemStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    /**
     * Sets the font for the title of the list item.
     *
     * @param titleFont The name of the font to apply to the title.
     */
    public ListItemStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the color for the title of the list item.
     *
     * @param titleColor The color to set for the title text.
     */
    public ListItemStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the text appearance for the title of the list item.
     *
     * @param titleAppearance The style resource ID to apply to the title.
     */
    public ListItemStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    /**
     * Sets the color for the separator of the list item.
     *
     * @param separatorColor The color to set for the separator.
     */
    public ListItemStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    /**
     * Sets the drawable for the separator of the list item.
     *
     * @param separatorDrawable The drawable to set as the separator.
     */
    public ListItemStyle setSeparatorDrawable(Drawable separatorDrawable) {
        this.separatorDrawable = separatorDrawable;
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

    public int getSeparatorColor() {
        return separatorColor;
    }

    public Drawable getSeparatorDrawable() {
        return separatorDrawable;
    }
}
