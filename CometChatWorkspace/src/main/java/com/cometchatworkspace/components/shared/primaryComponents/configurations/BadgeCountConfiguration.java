package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

public class BadgeCountConfiguration extends CometChatConfigurations {


    private int cornerRadius = -1;
    private int borderWidth= -1;
    private float width= -1;
    private float height= -1;
    private int textSize= -1;
    private final Context context;

    public BadgeCountConfiguration(Context context) {
            this.context = context;
    }

    public BadgeCountConfiguration setCornerRadius(int radius) {
        cornerRadius = radius;
        return this;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public BadgeCountConfiguration setBorderWidth(int width) {
        borderWidth = width;
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public BadgeCountConfiguration setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public BadgeCountConfiguration setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public BadgeCountConfiguration setTextSize(int size) {
        this.textSize = size;
        return this;
    }

    public int getTextSize() {
        return textSize;
    }
}
