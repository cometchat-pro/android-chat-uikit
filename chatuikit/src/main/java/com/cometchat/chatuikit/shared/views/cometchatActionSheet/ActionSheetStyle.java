package com.cometchat.chatuikit.shared.views.cometchatActionSheet;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * ActionSheetStyle is a class that defines the style properties for the ActionSheet dialog.
 * It inherits from the BaseStyle class and adds additional properties specific to the ActionSheet
 */
public class ActionSheetStyle extends BaseStyle {
    private @ColorInt
    int titleColor = 0;
    private @ColorInt
    int layoutModeIconTint = 0;
    private @ColorInt
    int layoutModeIconBackgroundColor = 0;
    private @StyleRes
    int titleAppearance = 0;
    private String titleFont = null;
    private @ColorInt
    int listItemSeparatorColor = 0;
    private Drawable listItemDrawable = null;

    public ActionSheetStyle setListItemDrawable(Drawable listItemDrawable) {
        this.listItemDrawable = listItemDrawable;
        return this;
    }

    public ActionSheetStyle setLayoutModeIconBackgroundColor(int layoutModeIconBackgroundColor) {
        this.layoutModeIconBackgroundColor = layoutModeIconBackgroundColor;
        return this;
    }

    public ActionSheetStyle setListItemSeparatorColor(int listItemSeparatorColor) {
        this.listItemSeparatorColor = listItemSeparatorColor;
        return this;
    }

    public ActionSheetStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public ActionSheetStyle setLayoutModeIconTint(int layoutModeIconTint) {
        this.layoutModeIconTint = layoutModeIconTint;
        return this;
    }

    public ActionSheetStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    public ActionSheetStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the background color or drawable for the badge.
     *
     * @param background The color or drawable to set as the background for the badge.
     */
    @Override
    public ActionSheetStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    /**
     * Sets the drawable background for the badge.
     *
     * @param drawableBackground The drawable to set as the background for the badge.
     */
    @Override
    public ActionSheetStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    /**
     * Sets the corner radius for the badge.
     *
     * @param cornerRadius The corner radius to set for the badge.
     */
    @Override
    public ActionSheetStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    /**
     * Sets the border width for the badge.
     *
     * @param borderWidth The border width to set for the badge.
     */
    @Override
    public ActionSheetStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    /**
     * Sets the border color for the badge.
     *
     * @param borderColor The color to set as the border color for the badge.
     */
    @Override
    public ActionSheetStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    public int getLayoutModeIconBackgroundColor() {
        return layoutModeIconBackgroundColor;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getLayoutModeIconTint() {
        return layoutModeIconTint;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public int getListItemSeparatorColor() {
        return listItemSeparatorColor;
    }

    public Drawable getListItemDrawable() {
        return listItemDrawable;
    }
}
