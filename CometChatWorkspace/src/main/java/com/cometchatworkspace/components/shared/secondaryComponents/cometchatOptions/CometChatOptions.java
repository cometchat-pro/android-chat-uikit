package com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions;

import android.view.View;

public class CometChatOptions {
    String id;
    String title;
    int icon;
    OnOptionClick onClick; //Change to onItemClick

    public CometChatOptions(String id, String title, int icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public CometChatOptions(String id, String title, int icon, OnOptionClick onClick) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.onClick = onClick;
  }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public OnOptionClick getOnClick() {
        return onClick;
    }

    public void setOnClickListener(OnOptionClick onClickListener) {
        onClick = onClickListener;
    }
}
