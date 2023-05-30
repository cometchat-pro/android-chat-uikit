package com.cometchat.chatuikit.users;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The style class for customizing the appearance of the Users view.
 */
public class UsersStyle extends BaseStyle {

    public final String TAG = UsersStyle.class.getName();

    private String titleFont;
    private String emptyTextFont;
    private String searchTextFont;
    private String sectionHeaderTextFont;

    private @StyleRes
    int titleAppearance;
    private @StyleRes
    int emptyTextAppearance;
    private @StyleRes
    int errorTextAppearance;
    private @StyleRes
    int sectionHeaderTextAppearance;
    private @StyleRes
    int searchTextAppearance;

    private int searchBorderWidth = -1;
    private int searchBorderRadius = 0;

    private @ColorInt
    int titleColor;
    private @ColorInt
    int backIconTint;
    private @ColorInt
    int onlineStatusColor;
    private @ColorInt
    int separatorColor;
    private @ColorInt
    int loadingIconTint;
    private @ColorInt
    int emptyTextColor;
    private @ColorInt
    int errorTextColor;
    private @ColorInt
    int searchBackground;
    private @ColorInt
    int searchTextColor;
    private @ColorInt
    int searchIconTint;
    private @ColorInt
    int sectionHeaderTextColor;


    @Override
    public UsersStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public UsersStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;

    }

    @Override
    public UsersStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;

    }

    @Override
    public UsersStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public UsersStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public UsersStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    /**
     * Sets the font for the title.
     *
     * @param titleFont The font for the title.
     */
    public UsersStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the font for the empty state text.
     *
     * @param emptyTextFont The font for the empty state text.
     */
    public UsersStyle setEmptyTextFont(String emptyTextFont) {
        this.emptyTextFont = emptyTextFont;
        return this;
    }

    /**
     * Sets the font for the search text.
     *
     * @param searchTextFont The font for the search text.
     */
    public UsersStyle setSearchTextFont(String searchTextFont) {
        this.searchTextFont = searchTextFont;
        return this;
    }

    /**
     * Sets the font for the section header text.
     *
     * @param sectionHeaderTextFont The font for the section header text.
     */
    public UsersStyle setSectionHeaderTextFont(String sectionHeaderTextFont) {
        this.sectionHeaderTextFont = sectionHeaderTextFont;
        return this;
    }

    /**
     * Sets the appearance for the title.
     *
     * @param titleAppearance The appearance for the title.
     */
    public UsersStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    /**
     * Sets the appearance for the empty state text.
     *
     * @param emptyTextAppearance The appearance for the empty state text.
     */
    public UsersStyle setEmptyTextAppearance(int emptyTextAppearance) {
        this.emptyTextAppearance = emptyTextAppearance;
        return this;
    }

    /**
     * Sets the appearance for the error state text.
     *
     * @param errorTextAppearance The appearance for the error state text.
     */
    public UsersStyle setErrorTextAppearance(int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        return this;
    }

    /**
     * Sets the border width for the search field.
     *
     * @param searchBorderWidth The border width for the search field.
     */
    public UsersStyle setSearchBorderWidth(int searchBorderWidth) {
        this.searchBorderWidth = searchBorderWidth;
        return this;
    }

    /**
     * Sets the corner radius for the search field.
     *
     * @param searchBorderRadius The corner radius for the search field.
     */
    public UsersStyle setSearchBorderRadius(int searchBorderRadius) {
        this.searchBorderRadius = searchBorderRadius;
        return this;
    }

    /**
     * Sets the appearance for the section header text.
     *
     * @param sectionHeaderTextAppearance The appearance for the section header text.
     */
    public UsersStyle setSectionHeaderTextAppearance(int sectionHeaderTextAppearance) {
        this.sectionHeaderTextAppearance = sectionHeaderTextAppearance;
        return this;
    }

    /**
     * Sets the color for the title.
     *
     * @param titleColor The color for the title.
     */
    public UsersStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the tint color for the back icon.
     *
     * @param backIconTint The tint color for the back icon.
     */
    public UsersStyle setBackIconTint(int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }


    /**
     * Sets the color for the online status indicator.
     *
     * @param onlineStatusColor The color for the online status indicator.
     */
    public UsersStyle setOnlineStatusColor(int onlineStatusColor) {
        this.onlineStatusColor = onlineStatusColor;
        return this;
    }

    /**
     * Sets the color for the separator.
     *
     * @param separatorColor The color for the separator.
     */
    public UsersStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    /**
     * Sets the tint color for the loading icon.
     *
     * @param loadingIconTint The tint color for the loading icon.
     */
    public UsersStyle setLoadingIconTint(int loadingIconTint) {
        this.loadingIconTint = loadingIconTint;
        return this;
    }

    /**
     * Sets the color for the empty state text.
     *
     * @param emptyTextColor The color for the empty state text.
     */
    public UsersStyle setEmptyTextColor(int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        return this;
    }

    /**
     * Sets the color for the error state text.
     *
     * @param errorTextColor The color for the error state text.
     */
    public UsersStyle setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
        return this;
    }


    /**
     * Sets the background resource for the search field.
     *
     * @param searchBackground The background resource for the search field.
     */
    public UsersStyle setSearchBackground(int searchBackground) {
        this.searchBackground = searchBackground;
        return this;
    }

    /**
     * Sets the text color for the search field.
     *
     * @param searchTextColor The text color for the search field.
     */
    public UsersStyle setSearchTextColor(int searchTextColor) {
        this.searchTextColor = searchTextColor;
        return this;
    }

    /**
     * Sets the tint color for the search icon.
     *
     * @param searchIconTint The tint color for the search icon.
     */
    public UsersStyle setSearchIconTint(int searchIconTint) {
        this.searchIconTint = searchIconTint;
        return this;
    }

    /**
     * Sets the text color for the section header text.
     *
     * @param sectionHeaderTextColor The text color for the section header text.
     */
    public UsersStyle setSectionHeaderTextColor(int sectionHeaderTextColor) {
        this.sectionHeaderTextColor = sectionHeaderTextColor;
        return this;
    }

    /**
     * Sets the appearance for the search text.
     *
     * @param searchTextAppearance The appearance for the search text.
     */
    public UsersStyle setSearchTextAppearance(int searchTextAppearance) {
        this.searchTextAppearance = searchTextAppearance;
        return this;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public String getEmptyTextFont() {
        return emptyTextFont;
    }

    public String getSearchTextFont() {
        return searchTextFont;
    }

    public String getSectionHeaderTextFont() {
        return sectionHeaderTextFont;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getEmptyTextAppearance() {
        return emptyTextAppearance;
    }

    public int getErrorTextAppearance() {
        return errorTextAppearance;
    }

    public int getSearchBorderWidth() {
        return searchBorderWidth;
    }

    public int getSearchBorderRadius() {
        return searchBorderRadius;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getBackIconTint() {
        return backIconTint;
    }

    public int getOnlineStatusColor() {
        return onlineStatusColor;
    }

    public int getSeparatorColor() {
        return separatorColor;
    }

    public int getLoadingIconTint() {
        return loadingIconTint;
    }

    public int getEmptyTextColor() {
        return emptyTextColor;
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public int getSearchBackground() {
        return searchBackground;
    }

    public int getSearchTextColor() {
        return searchTextColor;
    }

    public int getSearchIconTint() {
        return searchIconTint;
    }

    public int getSectionHeaderTextColor() {
        return sectionHeaderTextColor;
    }

    public int getSectionHeaderTextAppearance() {
        return sectionHeaderTextAppearance;
    }

    public int getSearchTextAppearance() {
        return searchTextAppearance;
    }
}
