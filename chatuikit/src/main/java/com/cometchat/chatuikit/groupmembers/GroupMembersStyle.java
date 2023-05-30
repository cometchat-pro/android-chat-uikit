package com.cometchat.chatuikit.groupmembers;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * Represents the style configuration for the group members list in CometChat UI.
 */
public class GroupMembersStyle extends BaseStyle {

    public final String TAG = GroupMembersStyle.class.getName();

    private String titleFont;
    private String emptyTextFont;
    private String searchTextFont;

    private @StyleRes int titleAppearance;
    private @StyleRes int emptyTextAppearance;
    private @StyleRes int errorTextAppearance;

    private @StyleRes int searchTextAppearance;

    private int searchBorderWidth = -1;
    private int searchBorderRadius = 0;

    private @ColorInt int titleColor;
    private @ColorInt int backIconTint;
    private @ColorInt int onlineStatusColor;
    private @ColorInt int separatorColor;
    private @ColorInt int loadingIconTint;
    private @ColorInt int emptyTextColor;
    private @ColorInt int errorTextColor;
    private @ColorInt int searchBackground;
    private @ColorInt int searchTextColor;
    private @ColorInt int searchIconTint;


    @Override
    public GroupMembersStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public GroupMembersStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;

    }

    @Override
    public GroupMembersStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;

    }

    @Override
    public GroupMembersStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public GroupMembersStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public GroupMembersStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        super.setActiveBackground(activeBackground);
        return this;
    }

    /**
     * Sets the font for the title.
     *
     * @param titleFont The font to set.
     */
    public GroupMembersStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the font for the empty state text.
     *
     * @param emptyTextFont The font to set.
     */
    public GroupMembersStyle setEmptyTextFont(String emptyTextFont) {
        this.emptyTextFont = emptyTextFont;
        return this;
    }

    /**
     * Sets the font for the search text.
     *
     * @param searchTextFont The font to set.
     */
    public GroupMembersStyle setSearchTextFont(String searchTextFont) {
        this.searchTextFont = searchTextFont;
        return this;
    }

    /**
     * Sets the appearance for the title.
     *
     * @param titleAppearance The appearance to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    /**
     * Sets the appearance for the empty state text.
     *
     * @param emptyTextAppearance The appearance to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setEmptyTextAppearance(int emptyTextAppearance) {
        this.emptyTextAppearance = emptyTextAppearance;
        return this;
    }

    /**
     * Sets the appearance for the error text.
     *
     * @param errorTextAppearance The appearance to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setErrorTextAppearance(int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        return this;
    }

    /**
     * Sets the width of the search bar's border.
     *
     * @param searchBorderWidth The width to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setSearchBorderWidth(int searchBorderWidth) {
        this.searchBorderWidth = searchBorderWidth;
        return this;
    }

    /**
     * Sets the border radius of the search bar.
     *
     * @param searchBorderRadius The border radius to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setSearchBorderRadius(int searchBorderRadius) {
        this.searchBorderRadius = searchBorderRadius;
        return this;
    }

    /**
     * Sets the color of the title.
     *
     * @param titleColor The color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the tint color of the back icon.
     *
     * @param backIconTint The tint color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setBackIconTint(int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }

    /**
     * Sets the color of the online status indicator.
     *
     * @param onlineStatusColor The color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setOnlineStatusColor(int onlineStatusColor) {
        this.onlineStatusColor = onlineStatusColor;
        return this;
    }

    /**
     * Sets the color of the separator between list items.
     *
     * @param separatorColor The color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    /**
     * Sets the tint color of the loading icon.
     *
     * @param loadingIconTint The tint color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setLoadingIconTint(int loadingIconTint) {
        this.loadingIconTint = loadingIconTint;
        return this;
    }

    /**
     * Sets the color of the empty state text.
     *
     * @param emptyTextColor The color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setEmptyTextColor(int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        return this;
    }

    /**
     * Sets the color of the error text.
     *
     * @param errorTextColor The color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
        return this;
    }

    /**
     * Sets the background resource for the search bar.
     *
     * @param searchBackground The background resource to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setSearchBackground(int searchBackground) {
        this.searchBackground = searchBackground;
        return this;
    }

    /**
     * Sets the color of the search text.
     *
     * @param searchTextColor The color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setSearchTextColor(int searchTextColor) {
        this.searchTextColor = searchTextColor;
        return this;
    }

    /**
     * Sets the tint color of the search icon.
     *
     * @param searchIconTint The tint color to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setSearchIconTint(int searchIconTint) {
        this.searchIconTint = searchIconTint;
        return this;
    }

    /**
     * Sets the appearance for the search text.
     *
     * @param searchTextAppearance The appearance to set.
     * @return The updated GroupMembersStyle object.
     */
    public GroupMembersStyle setSearchTextAppearance(int searchTextAppearance) {
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

    public int getSearchTextAppearance() {
        return searchTextAppearance;
    }
}
