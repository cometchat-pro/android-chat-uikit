package com.cometchat.chatuikit.groups;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;
/**
 * Represents the style configuration for groups.
 * Extends {@link BaseStyle}.
 */
public class GroupsStyle extends BaseStyle {

    public final String TAG = GroupsStyle.class.getName();

    private String titleFont;
    private String emptyTextFont;
    private String searchTextFont;
    private String subTitleTextFont;

    private @StyleRes
    int titleAppearance;
    private @StyleRes
    int emptyTextAppearance;
    private @StyleRes
    int errorTextAppearance;
    private @StyleRes
    int subTitleTextAppearance;
    private @StyleRes
    int searchTextAppearance;

    private int searchBorderWidth = -1;
    private int searchBorderRadius = 0;

    private @ColorInt
    int titleColor;
    private @ColorInt
    int backIconTint;
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
    int subTitleTextColor;


    @Override
    public GroupsStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public GroupsStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public GroupsStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public GroupsStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public GroupsStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public GroupsStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        super.setActiveBackground(activeBackground);
        return this;
    }
    /**
     * Sets the font for the title.
     *
     * @param titleFont The font to set for the title.
     */
    public GroupsStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }
    /**
     * Sets the font for the empty text.
     *
     * @param emptyTextFont The font to set for the empty text.
     */
    public GroupsStyle setEmptyTextFont(String emptyTextFont) {
        this.emptyTextFont = emptyTextFont;
        return this;
    }
    /**
     * Sets the font for the search text.
     *
     * @param searchTextFont The font to set for the search text.
     */
    public GroupsStyle setSearchTextFont(String searchTextFont) {
        this.searchTextFont = searchTextFont;
        return this;
    }
    /**
     * Sets the font for the subtitle text.
     *
     * @param subTitleTextFont The font to set for the subtitle text.
     */
    public GroupsStyle setSubTitleTextFont(String subTitleTextFont) {
        this.subTitleTextFont = subTitleTextFont;
        return this;
    }
    /**
     * Sets the appearance for the title.
     *
     * @param titleAppearance The appearance to set for the title.
     */
    public GroupsStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }
    /**
     * Sets the appearance for the empty text.
     *
     * @param emptyTextAppearance The appearance to set for the empty text.
     */
    public GroupsStyle setEmptyTextAppearance(int emptyTextAppearance) {
        this.emptyTextAppearance = emptyTextAppearance;
        return this;
    }
    /**
     * Sets the appearance for the error text.
     *
     * @param errorTextAppearance The appearance to set for the error text.
     */
    public GroupsStyle setErrorTextAppearance(int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        return this;
    }
    /**
     * Sets the border width for the search field.
     *
     * @param searchBorderWidth The border width to set for the search field.
     */
    public GroupsStyle setSearchBorderWidth(int searchBorderWidth) {
        this.searchBorderWidth = searchBorderWidth;
        return this;
    }
    /**
     * Sets the border radius for the search field.
     *
     * @param searchBorderRadius The border radius to set for the search field.
     */
    public GroupsStyle setSearchBorderRadius(int searchBorderRadius) {
        this.searchBorderRadius = searchBorderRadius;
        return this;
    }
    /**
     * Sets the appearance for the subtitle text.
     *
     * @param subTitleTextAppearance The appearance to set for the subtitle text.
     */
    public GroupsStyle setSubTitleTextAppearance(int subTitleTextAppearance) {
        this.subTitleTextAppearance = subTitleTextAppearance;
        return this;
    }
    /**
     * Sets the color for the title.
     *
     * @param titleColor The color to set for the title.
     * @return This {@code GroupsStyle} instance.
     */
    public GroupsStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }
    /**
     * Sets the tint color for the back icon.
     *
     * @param backIconTint The tint color to set for the back icon.
     */
    public GroupsStyle setBackIconTint(int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }
    /**
     * Sets the color for the separator.
     *
     * @param separatorColor The color to set for the separator.
     */
    public GroupsStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }
    /**
     * Sets the tint color for the loading icon.
     *
     * @param loadingIconTint The tint color to set for the loading icon.
     */
    public GroupsStyle setLoadingIconTint(int loadingIconTint) {
        this.loadingIconTint = loadingIconTint;
        return this;
    }
    /**
     * Sets the color for the empty text.
     *
     * @param emptyTextColor The color to set for the empty text.
     */
    public GroupsStyle setEmptyTextColor(int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        return this;
    }
    /**
     * Sets the color for the error text.
     *
     * @param errorTextColor The color to set for the error text.
     */
    public GroupsStyle setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
        return this;
    }
    /**
     * Sets the background resource for the search field.
     *
     * @param searchBackground The background resource to set for the search field.
     */
    public GroupsStyle setSearchBackground(int searchBackground) {
        this.searchBackground = searchBackground;
        return this;
    }
    /**
     * Sets the text color for the search field.
     *
     * @param searchTextColor The text color to set for the search field.
     */
    public GroupsStyle setSearchTextColor(int searchTextColor) {
        this.searchTextColor = searchTextColor;
        return this;
    }
    /**
     * Sets the tint color for the search icon.
     *
     * @param searchIconTint The tint color to set for the search icon.
     */
    public GroupsStyle setSearchIconTint(int searchIconTint) {
        this.searchIconTint = searchIconTint;
        return this;
    }
    /**
     * Sets the text color for the subtitle text.
     *
     * @param subTitleTextColor The text color to set for the subtitle text.
     */
    public GroupsStyle setSubTitleTextColor(int subTitleTextColor) {
        this.subTitleTextColor = subTitleTextColor;
        return this;
    }
    /**
     * Sets the appearance for the search text.
     *
     * @param searchTextAppearance The appearance to set for the search text.
     */
    public GroupsStyle setSearchTextAppearance(int searchTextAppearance) {
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

    public String getSubTitleTextFont() {
        return subTitleTextFont;
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

    public int getSubTitleTextColor() {
        return subTitleTextColor;
    }

    public int getSubTitleTextAppearance() {
        return subTitleTextAppearance;
    }

    public int getSearchTextAppearance() {
        return searchTextAppearance;
    }
}
