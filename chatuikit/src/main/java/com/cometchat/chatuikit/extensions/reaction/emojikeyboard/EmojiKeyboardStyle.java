package com.cometchat.chatuikit.extensions.reaction.emojikeyboard;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

public class EmojiKeyboardStyle extends BaseStyle {

    private String sectionHeaderFont;
    private @ColorInt
    int sectionHeaderColor;
    private @ColorInt
    int categoryIconTint;
    private @ColorInt
    int selectedCategoryIconTint;
    private @ColorInt
    int titleColor;
    private String titleFont;
    private @ColorInt
    int closeButtonTint;
    private @StyleRes
    int titleAppearance;
    private @StyleRes
    int sectionHeaderAppearance;
    private @ColorInt
    int separatorColor;


    @Override
    public EmojiKeyboardStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public EmojiKeyboardStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public EmojiKeyboardStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public EmojiKeyboardStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public EmojiKeyboardStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    public EmojiKeyboardStyle setSectionHeaderFont(String sectionHeaderFont) {
        this.sectionHeaderFont = sectionHeaderFont;
        return this;
    }

    public EmojiKeyboardStyle setSectionHeaderColor(int sectionHeaderColor) {
        this.sectionHeaderColor = sectionHeaderColor;
        return this;
    }

    public EmojiKeyboardStyle setCategoryIconTint(int categoryIconTint) {
        this.categoryIconTint = categoryIconTint;
        return this;
    }

    public EmojiKeyboardStyle setSelectedCategoryIconTint(int selectedCategoryIconTint) {
        this.selectedCategoryIconTint = selectedCategoryIconTint;
        return this;
    }

    public EmojiKeyboardStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public EmojiKeyboardStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public EmojiKeyboardStyle setCloseButtonTint(int closeButtonTint) {
        this.closeButtonTint = closeButtonTint;
        return this;
    }

    public EmojiKeyboardStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    public EmojiKeyboardStyle setSectionHeaderAppearance(int sectionHeaderStyle) {
        this.sectionHeaderAppearance = sectionHeaderStyle;
        return this;
    }

    public EmojiKeyboardStyle setSeparatorColor(int separator) {
        this.separatorColor = separator;
        return this;
    }

    public int getSeparatorColor() {
        return separatorColor;
    }

    public String getSectionHeaderFont() {
        return sectionHeaderFont;
    }

    public int getSectionHeaderColor() {
        return sectionHeaderColor;
    }

    public int getCategoryIconTint() {
        return categoryIconTint;
    }

    public int getSelectedCategoryIconTint() {
        return selectedCategoryIconTint;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public int getCloseButtonTint() {
        return closeButtonTint;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getSectionHeaderAppearance() {
        return sectionHeaderAppearance;
    }
}
