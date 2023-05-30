package com.cometchat.chatuikit.bannedmembers;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

/**
 * The style class for customizing the appearance of the BannedMembers component.
 */
public class BannedMembersStyle extends BaseStyle {

    public final String TAG = BannedMembersStyle.class.getName();

    private String titleFont;
    private String emptyTextFont;
    private String searchTextFont;

    private @StyleRes
    int titleAppearance;
    private @StyleRes
    int emptyTextAppearance;
    private @StyleRes
    int errorTextAppearance;

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


    @Override
    public BannedMembersStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public BannedMembersStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public BannedMembersStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public BannedMembersStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public BannedMembersStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public BannedMembersStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        super.setActiveBackground(activeBackground);
        return this;
    }

    /**
     * Sets the font for the title of the BannedMembers component.
     *
     * @param titleFont The font for the title.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    /**
     * Sets the font for the empty state text of the BannedMembers component.
     *
     * @param emptyTextFont The font for the empty state text.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setEmptyTextFont(String emptyTextFont) {
        this.emptyTextFont = emptyTextFont;
        return this;
    }

    /**
     * Sets the font for the search text of the BannedMembers component.
     *
     * @param searchTextFont The font for the search text.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSearchTextFont(String searchTextFont) {
        this.searchTextFont = searchTextFont;
        return this;
    }

    /**
     * Sets the appearance style for the title of the BannedMembers component.
     *
     * @param titleAppearance The appearance style for the title.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    /**
     * Sets the appearance style for the empty state text of the BannedMembers component.
     *
     * @param emptyTextAppearance The appearance style for the empty state text.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setEmptyTextAppearance(int emptyTextAppearance) {
        this.emptyTextAppearance = emptyTextAppearance;
        return this;
    }

    /**
     * Sets the appearance style for the error state text of the BannedMembers component.
     *
     * @param errorTextAppearance The appearance style for the error state text.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setErrorTextAppearance(int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        return this;
    }

    /**
     * Sets the border width for the search box of the BannedMembers component.
     *
     * @param searchBorderWidth The border width for the search box.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSearchBorderWidth(int searchBorderWidth) {
        this.searchBorderWidth = searchBorderWidth;
        return this;
    }

    /**
     * Sets the border radius for the search box of the BannedMembers component.
     *
     * @param searchBorderRadius The border radius for the search box.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSearchBorderRadius(int searchBorderRadius) {
        this.searchBorderRadius = searchBorderRadius;
        return this;
    }

    /**
     * Sets the color for the title of the BannedMembers component.
     *
     * @param titleColor The color for the title.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    /**
     * Sets the tint color for the back icon in the BannedMembers component.
     *
     * @param backIconTint The tint color for the back icon.
     */
    public BannedMembersStyle setBackIconTint(int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }

    /**
     * Sets the color for the online status indicator in the BannedMembers component.
     *
     * @param onlineStatusColor The color for the online status indicator.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setOnlineStatusColor(int onlineStatusColor) {
        this.onlineStatusColor = onlineStatusColor;
        return this;
    }

    /**
     * Sets the color for the separator in the BannedMembers component.
     *
     * @param separatorColor The color for the separator.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    /**
     * Sets the tint color for the loading icon in the BannedMembers component.
     *
     * @param loadingIconTint The tint color for the loading icon.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setLoadingIconTint(int loadingIconTint) {
        this.loadingIconTint = loadingIconTint;
        return this;
    }

    /**
     * Sets the color for the empty state text in the BannedMembers component.
     *
     * @param emptyTextColor The color for the empty state text.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setEmptyTextColor(int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        return this;
    }

    /**
     * Sets the color for the error state text in the BannedMembers component.
     *
     * @param errorTextColor The color for the error state text.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
        return this;
    }

    /**
     * Sets the background drawable for the search box in the BannedMembers component.
     *
     * @param searchBackground The background drawable for the search box.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSearchBackground(int searchBackground) {
        this.searchBackground = searchBackground;
        return this;
    }

    /**
     * Sets the text color for the search box in the BannedMembers component.
     *
     * @param searchTextColor The text color for the search box.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSearchTextColor(int searchTextColor) {
        this.searchTextColor = searchTextColor;
        return this;
    }

    /**
     * Sets the tint color for the search icon in the BannedMembers component.
     *
     * @param searchIconTint The tint color for the search icon.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSearchIconTint(int searchIconTint) {
        this.searchIconTint = searchIconTint;
        return this;
    }

    /**
     * Sets the appearance style for the search text in the BannedMembers component.
     *
     * @param searchTextAppearance The appearance style for the search text.
     * @return The updated BannedMembersStyle object.
     */
    public BannedMembersStyle setSearchTextAppearance(int searchTextAppearance) {
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
