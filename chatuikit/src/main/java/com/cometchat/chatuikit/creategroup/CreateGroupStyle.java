package com.cometchat.chatuikit.creategroup;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Style class for customizing the Create Group view.
 */
public class CreateGroupStyle extends BaseStyle {

    private @ColorInt int placeHolderTextColor;
    private @ColorInt int titleTextColor;
    private @ColorInt int backIconTintColor;
    private @ColorInt int createGroupIconTint, tabSelectedTextColor, tabTextColor, tabIndicatorColor, tabBackgroundTint;
    private String textFont;
    private String titleTextFont;
    private @StyleRes int textAppearance;
    private @StyleRes int titleTextAppearance;
    private int editTextCornerRadius = -1;
    private @ColorInt int editTextBackgroundColor;
    private Drawable tabBackground, tabBackgroundState;

    @Override
    public CreateGroupStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public CreateGroupStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public CreateGroupStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public CreateGroupStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public CreateGroupStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public CreateGroupStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    /**
     * Sets the corner radius of the EditText fields in the Create Group view.
     *
     * @param editTextCornerRadius The corner radius value to be set.
     */
    public CreateGroupStyle setEditTextCornerRadius(int editTextCornerRadius) {
        this.editTextCornerRadius = editTextCornerRadius;
        return this;
    }

    /**
     * Sets the background color of the EditText fields in the Create Group view.
     *
     * @param editTextBackgroundColor The background color value to be set.
     */
    public CreateGroupStyle setEditTextBackgroundColor(int editTextBackgroundColor) {
        this.editTextBackgroundColor = editTextBackgroundColor;
        return this;
    }

    /**
     * Sets the background drawable of the tabs in the Create Group view.
     *
     * @param tabBackground The drawable resource to be set as the tab background.
     */
    public CreateGroupStyle setTabBackground(Drawable tabBackground) {
        this.tabBackground = tabBackground;
        return this;
    }

    /**
     * Sets the background state drawable of the tabs in the Create Group view.
     *
     * @param tabBackgroundState The drawable resource to be set as the tab background state.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTabBackgroundState(Drawable tabBackgroundState) {
        this.tabBackgroundState = tabBackgroundState;
        return this;
    }

    /**
     * Sets the text color of the placeholder in the EditText fields in the Create Group view.
     *
     * @param placeHolderTextColor The text color value to be set.
     */
    public CreateGroupStyle setPlaceHolderTextColor(int placeHolderTextColor) {
        this.placeHolderTextColor = placeHolderTextColor;
        return this;
    }

    /**
     * Sets the text color of the title in the Create Group view.
     *
     * @param titleTextColor The text color value to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        return this;
    }

    /**
     * Sets the tint color of the back icon in the Create Group view.
     *
     * @param backIconTintColor The tint color value to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setBackIconTintColor(int backIconTintColor) {
        this.backIconTintColor = backIconTintColor;
        return this;
    }

    /**
     * Sets the tint color of the create group icon in the Create Group view.
     *
     * @param createGroupIconTint The tint color value to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setCreateGroupIconTint(int createGroupIconTint) {
        this.createGroupIconTint = createGroupIconTint;
        return this;
    }

    /**
     * Sets the text color of the selected tab in the Create Group view.
     *
     * @param tabSelectedTextColor The text color value to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTabSelectedTextColor(int tabSelectedTextColor) {
        this.tabSelectedTextColor = tabSelectedTextColor;
        return this;
    }

    /**
     * Sets the text color of the tabs in the Create Group view.
     *
     * @param tabTextColor The text color value to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTabTextColor(int tabTextColor) {
        this.tabTextColor = tabTextColor;
        return this;
    }

    /**
     * Sets the color of the tab indicator in the Create Group view.
     *
     * @param tabIndicatorColor The color value to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTabIndicatorColor(int tabIndicatorColor) {
        this.tabIndicatorColor = tabIndicatorColor;
        return this;
    }

    /**
     * Sets the background tint of the tabs in the Create Group view.
     *
     * @param tabBackgroundTint The background tint value to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTabBackgroundTint(int tabBackgroundTint) {
        this.tabBackgroundTint = tabBackgroundTint;
        return this;
    }

    /**
     * Sets the font family for the text in the Create Group view.
     *
     * @param textFont The font family to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTextFont(String textFont) {
        this.textFont = textFont;
        return this;
    }

    /**
     * Sets the font family for the title in the Create Group view.
     *
     * @param titleTextFont The font family to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTitleTextFont(String titleTextFont) {
        this.titleTextFont = titleTextFont;
        return this;
    }

    /**
     * Sets the text appearance for the text in the Create Group view.
     *
     * @param textAppearance The text appearance style to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    /**
     * Sets the text appearance for the title in the Create Group view.
     *
     * @param titleTextAppearance The text appearance style to be set.
     * @return The updated CreateGroupStyle object.
     */
    public CreateGroupStyle setTitleTextAppearance(int titleTextAppearance) {
        this.titleTextAppearance = titleTextAppearance;
        return this;
    }

    public int getPlaceHolderTextColor() {
        return placeHolderTextColor;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public int getBackIconTintColor() {
        return backIconTintColor;
    }

    public int getCreateGroupIconTint() {
        return createGroupIconTint;
    }

    public int getTabSelectedTextColor() {
        return tabSelectedTextColor;
    }

    public int getTabTextColor() {
        return tabTextColor;
    }

    public int getTabIndicatorColor() {
        return tabIndicatorColor;
    }

    public int getTabBackgroundTint() {
        return tabBackgroundTint;
    }

    public String getTextFont() {
        return textFont;
    }

    public String getTitleTextFont() {
        return titleTextFont;
    }

    public int getTextAppearance() {
        return textAppearance;
    }

    public int getTitleTextAppearance() {
        return titleTextAppearance;
    }

    public int getEditTextCornerRadius() {
        return editTextCornerRadius;
    }

    public int getEditTextBackgroundColor() {
        return editTextBackgroundColor;
    }

    public Drawable getTabBackground() {
        return tabBackground;
    }

    public Drawable getTabBackgroundState() {
        return tabBackgroundState;
    }
}
