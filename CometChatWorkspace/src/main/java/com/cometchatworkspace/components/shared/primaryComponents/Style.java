package com.cometchatworkspace.components.shared.primaryComponents;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;

public class Style {

    private int background;
    private Drawable gradientDrawable;
    private int borderWidth;
    private int borderColor;
    private float cornerRadius;
    private String emptyStateTextFont;
    private int emptyStateTextColor;
    private String errorStateTextFont;
    private int errorStateTextColor;
    private String titleFont;
    private int titleColor;
    private int backIconTint = 0;
    private int searchBorderWidth = 0;
    private int searchCornerRadius = 0;
    private int searchBackground = 0;
    private String searchTextFont = null;
    private int searchTextColor = 0;
    private int searchIconTint = 0;
    private String subTitleFont = null;
    private int subTitleColor = 0;
    private int typingIndicatorTextColor = 0;
    private String typingIndicatorTextFont = null;
    private int threadIndicatorTextColor = 0;
    private String threadIndicatorTextFont = null;

    public Style gradientBackground(int[] colors, GradientDrawable.Orientation orientation) {
        this.gradientDrawable = new GradientDrawable(orientation,colors);
        return this;
    }

    public Style background(@ColorInt int background) {
        this.background = background;
        return this;
    }

    public Style borderWidth(int width) {
        this.borderWidth = width;
        return this;
    }

    public Style borderColor(@ColorInt int color) {
        this.borderColor = color;
        return this;
    }

    public Style cornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public Style emptyStateTextFont(String emptyStateTextFont) {
        this.emptyStateTextFont = emptyStateTextFont;
        return this;
    }

    public Style emptyStateTextColor(@ColorInt int emptyStateTextColor) {
        this.emptyStateTextColor = emptyStateTextColor;
        return this;
    }

    public Style errorStateTextFont(String errorStateTextFont) {
        this.errorStateTextFont = errorStateTextFont;
        return this;
    }

    public Style errorStateTextColor(@ColorInt int errorStateTextColor) {
        this.errorStateTextColor = errorStateTextColor;
        return this;
    }

    public Style titleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public Style titleColor(@ColorInt int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public Style backIconTint(@ColorInt int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }

    public Style searchBorderWidth(int searchBorder) {
        this.searchBorderWidth = searchBorder;
        return this;
    }

    public Style searchCornerRadius(int searchCornerRadius) {
        this.searchCornerRadius = searchCornerRadius;
        return this;
    }

    public Style searchBackground(@ColorInt int searchBackground) {
        this.searchBackground = searchBackground;
        return this;
    }

    public Style searchTextFont(String searchTextFont) {
        this.searchTextFont = searchTextFont;
        return this;
    }

    public Style searchTextColor(@ColorInt int searchTextColor) {
        this.searchTextColor = searchTextColor;
        return this;
    }

    public Style searchIconTint(@ColorInt int searchIconTint) {
        this.searchIconTint = searchIconTint;
        return this;
    }

    public Style subTitleFont(String subTitleFont) {
        this.subTitleFont = subTitleFont;
        return this;

    }

    public Style subTitleColor(@ColorInt int subTitleColor) {
        this.subTitleColor = subTitleColor;
        return this;

    }

    public Style typingIndicatorTextColor(@ColorInt int typingIndicatorTextColor) {
        this.typingIndicatorTextColor = typingIndicatorTextColor;
        return this;

    }

    public Style typingIndicatorTextFont(String typingIndicatorTextFont) {
        this.typingIndicatorTextFont = typingIndicatorTextFont;
        return this;

    }

    public Style threadIndicatorTextColor(@ColorInt int threadIndicatorTextColor) {
        this.threadIndicatorTextColor = threadIndicatorTextColor;
        return this;

    }

    public Style threadIndicatorTextFont(String threadIndicatorTextFont) {
        this.threadIndicatorTextFont = threadIndicatorTextFont;
        return this;

    }


    //getters
    public String getEmptyStateTextFont() {
        return emptyStateTextFont;
    }

    public @ColorInt int getEmptyStateTextColor() {
        return emptyStateTextColor;
    }

    public String getErrorStateTextFont() {
        return errorStateTextFont;
    }

    public @ColorInt int getErrorStateTextColor() {
        return errorStateTextColor;
    }

    public @ColorInt int getBackground() {
        return background;
    }

    public int getBorder() {
        return borderWidth;
    }

    public @ColorInt int getBorderColor() {
        return borderColor;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public @ColorInt int getTitleColor() {
        return titleColor;
    }

    public @ColorInt int getBackIconTint() {
        return backIconTint;
    }

    public int getSearchBorder() {
        return searchBorderWidth;
    }

    public int getSearchCornerRadius() {
        return searchCornerRadius;
    }

    public @ColorInt int getSearchBackground() {
        return searchBackground;
    }

    public String getSearchTextFont() {
        return searchTextFont;
    }

    public @ColorInt int getSearchTextColor() {
        return searchTextColor;
    }

    public @ColorInt int getSearchIconTint() {
        return searchIconTint;
    }

    public @ColorInt int getSubTitleColor() {
        return subTitleColor;
    }

    public String getSubTitleFont() {
        return subTitleFont;
    }

    public @ColorInt int getTypingIndicatorTextColor() {
        return typingIndicatorTextColor;
    }

    public String getTypingIndicatorTextFont() {
        return typingIndicatorTextFont;
    }

    public @ColorInt int getThreadIndicatorTextColor() {
        return threadIndicatorTextColor;
    }

    public String getThreadIndicatorTextFont() {
        return threadIndicatorTextFont;
    }


    public Drawable getGradientBackground() {
        return gradientDrawable;
    }
}
