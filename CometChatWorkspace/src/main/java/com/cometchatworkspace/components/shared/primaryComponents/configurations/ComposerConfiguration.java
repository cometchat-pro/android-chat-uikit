package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

public class ComposerConfiguration extends CometChatConfigurations {


    private int cornerRadius;
    private int borderWidth;
    private float width;
    private float height;
    private int textSize;
    private int maxLines;
    private boolean isAttachmentVisible,isMicrophoneVisible,isLiveReactionVisible,isSendButtonVisible;
    private final Context context;

    public ComposerConfiguration(Context context) {
            this.context = context;
    }

    public ComposerConfiguration setCornerRadius(int radius) {
        cornerRadius = radius;
        return this;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public ComposerConfiguration setBorderWidth(int width) {
        borderWidth = width;
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public ComposerConfiguration setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public ComposerConfiguration setHeight(float height) {
        this.height = height;
        return this;
    }

    public float getHeight() {
        return height;
    }

    public ComposerConfiguration setTextSize(int size) {
        this.textSize = size;
        return this;
    }

    public int getTextSize() {
        return textSize;
    }

    public ComposerConfiguration setMaxLines(int numberOfLines) {
        this.maxLines = numberOfLines;
        return this;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public ComposerConfiguration hideAttachment(boolean isHidden) {
        isAttachmentVisible = isHidden;
        return this;
    }

    public boolean isAttachmentHidden() { return isAttachmentVisible; }

    public ComposerConfiguration hideMicrophone(boolean isHidden) {
        isMicrophoneVisible = isHidden;
        return this;
    }

    public boolean isMicrophoneHidden() { return isMicrophoneVisible; }

    public ComposerConfiguration hideLiveReaction(boolean isHidden) {
        isLiveReactionVisible = isHidden;
        return this;
    }

    public boolean isLiveReactionHidden() { return isLiveReactionVisible; }

    public ComposerConfiguration hideSendButton(boolean isHidden) {
        isSendButtonVisible = isHidden;
        return this;
    }

    public boolean isSendButtonHidden() { return isSendButtonVisible; }
}
