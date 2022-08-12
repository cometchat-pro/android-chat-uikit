package com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

public class ActionItem implements Parcelable {
    String id;
    String text;
    int startIcon;
    int endIcon;
    int startIconTint;
    int endIconTint;
    int appearance; // addition
    int textColor ; // addition

    public ActionItem(String text, int startIcon) {
        this.text = text;
        this.startIcon = startIcon;
    }

    public ActionItem(String id, String text, int startIcon) {
        this.id = id;
        this.text = text;
        this.startIcon = startIcon;
    }

    protected ActionItem(Parcel in) {
        text = in.readString();
        startIcon = in.readInt();
    }
    //UserAction Component , SharedMedia TabLayout
    public ActionItem(String id, String text, @DrawableRes int startIcon, @DrawableRes int endIcon, @ColorInt int startIconTint,@ColorInt int endIconTint, @StyleRes int appearance, @ColorInt int textColor) {
        this.id = id;
        this.text = text;
        this.startIcon = startIcon;
        this.endIcon = endIcon;
        this.startIconTint = startIconTint;
        this.endIconTint = endIconTint;
        this.appearance = appearance;
        this.textColor = textColor;
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

    public void setTitle(String name) {
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
}
