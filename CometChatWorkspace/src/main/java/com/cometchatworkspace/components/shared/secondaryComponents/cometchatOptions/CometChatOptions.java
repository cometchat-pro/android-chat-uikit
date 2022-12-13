package com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions;

import com.cometchat.pro.models.BaseMessage;

public class CometChatOptions {
    String id;
    String title;
    int icon;
    onItemClick<BaseMessage> onClick;

    public CometChatOptions(String id, String title, int icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public CometChatOptions(String id, String title, int icon, onItemClick<BaseMessage> onClick) {
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

    public onItemClick<BaseMessage> getOnClick() {
        return onClick;
    }

    public void setOnClickListener(onItemClick<BaseMessage> onClickListener) {
        onClick = onClickListener;
    }
}
