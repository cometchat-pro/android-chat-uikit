package com.cometchat.chatuikit.shared.views.cometchatActionSheet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

public class ActionItem implements Parcelable {
    private String id;
    private String text;
    private @DrawableRes
    int startIcon;
    private @DrawableRes
    int endIcon;
    private @ColorInt
    int startIconTint;
    private @ColorInt
    int iconBackgroundColor;
    private String textFont = null;
    private @ColorInt
    int endIconTint;
    private @StyleRes
    int appearance;
    private @ColorInt
    int textColor;
    private @ColorInt
    int background;
    private int cornerRadius = -1;
    /**
     * Creates an action item with text and a start icon.
     * @param text The text to display for the item.
     * @param startIcon The resource ID of the icon to display at the start of the item.
     */
    public ActionItem(String text, int startIcon) {
        this.text = text;
        this.startIcon = startIcon;
    }
    /**
     * Creates an action item with an ID, text, and a start icon.
     * @param id The unique ID of the item.
     * @param text The text to display for the item.
     * @param startIcon The resource ID of the icon to display at the start of the item.
     */
    public ActionItem(String id, String text, int startIcon) {
        this.id = id;
        this.text = text;
        this.startIcon = startIcon;
    }

    protected ActionItem(Parcel in) {
        text = in.readString();
        startIcon = in.readInt();
    }
    /**
     * Creates an action item with advanced options, including start and end icons, icon colors, appearance, and text color.
     * @param id The unique ID of the item.
     * @param text The text to display for the item.
     * @param startIcon The resource ID of the icon to display at the start of the item.
     * @param endIcon The resource ID of the icon to display at the end of the item.
     * @param startIconTint The color to apply to the start icon.
     * @param endIconTint The color to apply to the end icon.
     * @param appearance The resource ID of the appearance to apply to the item.
     * @param textColor The color to apply to the item text.
     */
    public ActionItem(String id, String text, @DrawableRes int startIcon, @DrawableRes int endIcon, @ColorInt int startIconTint, @ColorInt int endIconTint, @StyleRes int appearance, @ColorInt int textColor) {
        this.id = id;
        this.text = text;
        this.startIcon = startIcon;
        this.endIcon = endIcon;
        this.startIconTint = startIconTint;
        this.endIconTint = endIconTint;
        this.appearance = appearance;
        this.textColor = textColor;
    }

    public ActionItem(String id, @DrawableRes int startIcon, @ColorInt int startIconTint, @ColorInt int iconBackgroundColor, String text, String textFont, @StyleRes int appearance, @ColorInt int textColor, @ColorInt int background, int cornerRadius) {
        this.id = id;
        this.text = text;
        this.textFont = textFont;
        this.startIcon = startIcon;
        this.startIconTint = startIconTint;
        this.iconBackgroundColor = iconBackgroundColor;
        this.appearance = appearance;
        this.textColor = textColor;
        this.background = background;
        this.cornerRadius = cornerRadius;
    }

    public static final Creator<ActionItem> CREATOR = new Creator<ActionItem>() {
        @Override
        public ActionItem createFromParcel(Parcel in) {
            return new ActionItem(in);
        }

        @Override
        public ActionItem[] newArray(int size) {
            return new ActionItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeInt(startIcon);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String name) {
        this.text = name;
    }

    public int getStartIcon() {
        return startIcon;
    }

    public void setStartIcon(int startIcon) {
        this.startIcon = startIcon;
    }

    public int getAppearance() {
        return appearance;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getEndIcon() {
        return endIcon;
    }

    public int getStartIconTint() {
        return startIconTint;
    }

    public int getEndIconTint() {
        return endIconTint;
    }

    public int getIconBackgroundColor() {
        return iconBackgroundColor;
    }

    public void setIconBackgroundColor(int iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public String getTextFont() {
        return textFont;
    }

    public void setTextFont(String textFont) {
        this.textFont = textFont;
    }
}
