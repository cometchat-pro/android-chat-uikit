package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

import com.cometchatworkspace.components.shared.primaryComponents.Style;

public class ComposerConfiguration extends CometChatConfigurations {

    private Style style;
    private int cornerRadius;
    private int borderWidth;
    private float width;
    private float height;
    private int textSize;
    private int maxLines;
    private boolean isAttachmentHidden,
            isMicrophoneHidden=true, isLiveReactionHidden,
            isSendButtonHidden;

    private int liveReactionIcon=-1,attachmentIcon=-1,microphoneIcon=-1,sendButtonIcon=-1;
    private Context context;
    String placeholderStr;

    public ComposerConfiguration(Context context_) {
            this.context = context_;
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

    public ComposerConfiguration attachmentIcon(int icon) {
        attachmentIcon = icon;
        return this;
    }

    public int getAttachmentIcon() {
        return attachmentIcon;
    }

    public ComposerConfiguration hideAttachment(boolean isHidden) {
        isAttachmentHidden = isHidden;
        return this;
    }

    public boolean isAttachmentHidden() { return isAttachmentHidden; }

    public ComposerConfiguration microphoneIcon(int icon) {
        microphoneIcon = icon;
        return this;
    }

    public int getMicrophoneIcon() {
        return microphoneIcon;
    }

    public ComposerConfiguration hideMicrophone(boolean isHidden) {
        isMicrophoneHidden = isHidden;
        return this;
    }

    public boolean isMicrophoneHidden() { return isMicrophoneHidden; }

    public ComposerConfiguration hideLiveReaction(boolean isHidden) {
        isLiveReactionHidden = isHidden;
        return this;
    }

    public boolean isLiveReactionHidden() { return isLiveReactionHidden; }

    public ComposerConfiguration liveReactionIcon(int icon) {
        liveReactionIcon = icon;
        return this;
    }

    public int getLiveReactionIcon() {
        return liveReactionIcon;
    }

    public ComposerConfiguration hideSendButton(boolean isHidden) {
        isSendButtonHidden = isHidden;
        return this;
    }

    public boolean isSendButtonHidden() { return isSendButtonHidden; }

    public ComposerConfiguration sendButtonIcon(int icon) {
        sendButtonIcon = icon;
        return this;
    }

    public int getSendButtonIcon() {
        return sendButtonIcon;
    }

    public ComposerConfiguration setPlaceholder(String placeholder) {
        placeholderStr = placeholder;
        return this;
    }
    public String getPlaceholder() {
        return placeholderStr;
    }

    public ComposerConfiguration setStyle(Style style1) {
        this.style = style1;
        return this;
    }

    public Style getStyle() {
        return style;
    }
}
