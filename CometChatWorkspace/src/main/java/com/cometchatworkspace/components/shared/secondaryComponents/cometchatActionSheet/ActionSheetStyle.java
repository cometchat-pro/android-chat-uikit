package com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FontRes;
import androidx.annotation.StyleRes;

import com.cometchatworkspace.components.shared.base.CometChatStyle;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.google.android.material.shape.ShapeAppearanceModel;

public class ActionSheetStyle extends CometChatStyle {
    private @ColorInt
    int titleColor = 0;
    private @ColorInt
    int listItemTitleColor = 0;
    private @ColorInt
    int listItemIconBackground = 0;
    private @ColorInt
    int layoutModeIconTint = 0;
    private @ColorInt
    int layoutModeIconBackgroundColor = 0;
    private @StyleRes
    int titleAppearance = 0;
    private @StyleRes
    int listItemTitleTextAppearance = 0;
    private String titleFont = null;
    private String listItemTitleFont = null;
    private @ColorInt
    int listItemIconTint = 0;
    private @ColorInt
    int listItemBackgroundColor = 0;
    private @ColorInt
    int listItemSeparatorColor = 0;
    private int listItemIconBorderRadius = 0;
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

    public ActionSheetStyle setListItemBackgroundColor(int listItemBackgroundColor) {
        this.listItemBackgroundColor = listItemBackgroundColor;
        return this;
    }

    public ActionSheetStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public ActionSheetStyle setListItemTitleColor(int listItemTitleColor) {
        this.listItemTitleColor = listItemTitleColor;
        return this;

    }

    public ActionSheetStyle setListItemIconBackground(int listItemIconBackground) {
        this.listItemIconBackground = listItemIconBackground;
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

    public ActionSheetStyle setListItemTitleTextAppearance(int listItemTitleTextAppearance) {
        this.listItemTitleTextAppearance = listItemTitleTextAppearance;
        return this;

    }

    public ActionSheetStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;

    }

    public ActionSheetStyle setListItemTitleFont(String listItemTitleFont) {
        this.listItemTitleFont = listItemTitleFont;
        return this;

    }

    public ActionSheetStyle setListItemIconTint(int listItemIconTint) {
        this.listItemIconTint = listItemIconTint;
        return this;

    }

    public ActionSheetStyle setListItemIconBorderRadius(int listItemIconBorderRadius) {
        this.listItemIconBorderRadius = listItemIconBorderRadius;
        return this;

    }

    @Override
    public ActionSheetStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public ActionSheetStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public ActionSheetStyle setBorderRadius(float borderRadius) {
        super.setBorderRadius(borderRadius);
        return this;
    }

    @Override
    public ActionSheetStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

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

    public int getListItemTitleColor() {
        return listItemTitleColor;
    }

    public int getListItemIconBackground() {
        return listItemIconBackground;
    }

    public int getLayoutModeIconTint() {
        return layoutModeIconTint;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getListItemTitleTextAppearance() {
        return listItemTitleTextAppearance;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public String getListItemTitleFont() {
        return listItemTitleFont;
    }

    public int getListItemIconTint() {
        return listItemIconTint;
    }

    public int getListItemBackgroundColor() {
        return listItemBackgroundColor;
    }

    public int getListItemSeparatorColor() {
        return listItemSeparatorColor;
    }

    public Drawable getListItemDrawable() {
        return listItemDrawable;
    }

    public int getListItemIconBorderRadius() {
        return listItemIconBorderRadius;
    }

}
