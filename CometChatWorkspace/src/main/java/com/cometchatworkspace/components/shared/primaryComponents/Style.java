package com.cometchatworkspace.components.shared.primaryComponents;

import androidx.annotation.ColorInt;

public class Style {


    private final int background;
    private final int border;
    private final float cornerRadius;
    private String emptyStateTextFont;
    private int emptyStateTextColor;
    private String errorStateTextFont;
    private int errorStateTextColor;


    private String titleFont;
    private int titleColor;
    private int backIconTint = 0;
    private int searchBorder = 0;
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

    //UserListItem,GroupListItem
    public Style(int border, float cornerRadius, @ColorInt int titleColor, @ColorInt int subTitleColor, String titleFont, String subTitleFont, int background) {
        this.border = border;
        this.cornerRadius = cornerRadius;
        this.titleColor = titleColor;
        this.subTitleColor = subTitleColor;
        this.titleFont = titleFont;
        this.subTitleFont = subTitleFont;
        this.background = background;
    }

    //ConversationListItem
    public Style(int border, float cornerRadius, int titleColor, int subTitleColor, String titleFont, String subTitleFont, int background, int typingIndicatorTextColor, String typingIndicatorTextFont, int threadIndicatorTextColor, String threadIndicatorTextFont) {
        this.border = border;
        this.cornerRadius = cornerRadius;
        this.titleColor = titleColor;
        this.subTitleColor = subTitleColor;
        this.titleFont = titleFont;
        this.subTitleFont = subTitleFont;
        this.background = background;
        this.typingIndicatorTextColor = typingIndicatorTextColor;
        this.typingIndicatorTextFont = typingIndicatorTextFont;
        this.threadIndicatorTextColor = threadIndicatorTextColor;
        this.threadIndicatorTextFont = threadIndicatorTextFont;
    }

    //Users,Groups,Conversations
    public Style(int background, int border, float cornerRadius, String titleFont, int titleColor, int backIconTint, int searchBorder, int searchCornerRadius, int searchBackground, String searchTextFont, int searchTextColor, int searchIconTint) {
        this.background = background;
        this.border = border;
        this.cornerRadius = cornerRadius;
        this.titleFont = titleFont;
        this.titleColor = titleColor;
        this.backIconTint = backIconTint;
        this.searchBorder = searchBorder;
        this.searchCornerRadius = searchCornerRadius;
        this.searchBackground = searchBackground;
        this.searchTextFont = searchTextFont;
        this.searchTextColor = searchTextColor;
        this.searchIconTint = searchIconTint;
    }

    //UserList,GroupList,ConversationList
    public Style(int background, int border, float cornerRadius, String emptyStateTextFont, int emptyStateTextColor, String errorStateTextFont, int errorStateTextColor) {
        this.background = background;
        this.border = border;
        this.cornerRadius = cornerRadius;
        this.emptyStateTextFont = emptyStateTextFont;
        this.emptyStateTextColor = emptyStateTextColor;
        this.errorStateTextFont = errorStateTextFont;
        this.errorStateTextColor = errorStateTextColor;
    }

    public String getEmptyStateTextFont() {
        return emptyStateTextFont;
    }

    public int getEmptyStateTextColor() {
        return emptyStateTextColor;
    }

    public String getErrorStateTextFont() {
        return errorStateTextFont;
    }

    public int getErrorStateTextColor() {
        return errorStateTextColor;
    }

    public int getBackground() {
        return background;
    }

    public int getBorder() {
        return border;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getBackIconTint() {
        return backIconTint;
    }

    public int getSearchBorder() {
        return searchBorder;
    }

    public int getSearchCornerRadius() {
        return searchCornerRadius;
    }

    public int getSearchBackground() {
        return searchBackground;
    }

    public String getSearchTextFont() {
        return searchTextFont;
    }

    public int getSearchTextColor() {
        return searchTextColor;
    }

    public int getSearchIconTint() {
        return searchIconTint;
    }

    public int getSubTitleColor() {
        return subTitleColor;
    }

    public String getSubTitleFont() {
        return subTitleFont;
    }

    public int getTypingIndicatorTextColor() {
        return typingIndicatorTextColor;
    }

    public String getTypingIndicatorTextFont() {
        return typingIndicatorTextFont;
    }

    public int getThreadIndicatorTextColor() {
        return threadIndicatorTextColor;
    }

    public String getThreadIndicatorTextFont() {
        return threadIndicatorTextFont;
    }


}
