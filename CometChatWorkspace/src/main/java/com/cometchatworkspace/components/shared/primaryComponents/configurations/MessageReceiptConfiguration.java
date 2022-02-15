package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;
import android.graphics.drawable.Drawable;


public class MessageReceiptConfiguration extends CometChatConfigurations {
    private Drawable readIcon;
    private Drawable deliveredIcon;
    private Drawable sentIcon;
    private Drawable inProgressIcon;

    private float width;
    private float height;
    private final Context context;

    public MessageReceiptConfiguration(Context context) {
        this.context = context;
    }

    public MessageReceiptConfiguration setReadIcon(Drawable icon) {
        this.readIcon = icon;
        return this;
    }


    public MessageReceiptConfiguration setDeliveredIcon(Drawable icon) {
        this.deliveredIcon = icon;
        return this;
    }

    public MessageReceiptConfiguration setSentIcon(Drawable icon) {
        this.sentIcon = icon;
        return this;
    }

    public MessageReceiptConfiguration setProgressIcon(Drawable icon) {
        this.inProgressIcon = icon;
        return this;
    }

    public Drawable getReadIcon() {
        return readIcon;
    }

    public Drawable getDeliveredIcon() {
        return deliveredIcon;
    }

    public Drawable getSentIcon() {
        return sentIcon;
    }

    public Drawable getInProgressIcon() {
        return inProgressIcon;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
