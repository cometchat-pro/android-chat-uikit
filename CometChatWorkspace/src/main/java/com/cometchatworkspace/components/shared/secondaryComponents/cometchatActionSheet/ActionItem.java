package com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet;

import android.os.Parcel;
import android.os.Parcelable;

public class ActionItem implements Parcelable {
    String id;
    String name;
    int icon;

    public ActionItem(String name,int icon) {
        this.name = name;
        this.icon = icon;
    }

    protected ActionItem(Parcel in) {
        name = in.readString();
        icon = in.readInt();
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
        dest.writeString(name);
        dest.writeInt(icon);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
