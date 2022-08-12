package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.graphics.drawable.shapes.Shape;

import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageInputData;
import com.google.android.material.shape.ShapeAppearanceModel;

public class MessageBubbleConfiguration extends CometChatConfigurations {

    private AvatarConfiguration avatarConfiguration;
    private MessageReceiptConfiguration messageReceiptConfiguration;
    private String timeFormat;
    private MessageInputData sentMessageInputData,receiverMessageInputData;

    private float leftMessageRadius = 10;
    private float rightMessageRadius = 10;
    private ShapeAppearanceModel leftMessageModel,rightMessageModel;

    public ShapeAppearanceModel getLeftMessageCornerRadius() {
        return leftMessageModel;
    }

    public float getLeftMessageRadius() {
        return leftMessageRadius;
    }

    public MessageBubbleConfiguration setLeftMessageRadius(float leftMessageRadius) {
        this.leftMessageRadius = leftMessageRadius;
        return this;
    }

    public MessageBubbleConfiguration setLeftMessageRadius(float topLeft,float topRight, float bottomLeft, float bottomRight) {
        leftMessageModel = ShapeAppearanceModel.builder()
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight)
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .build();
        return this;
    }

    public ShapeAppearanceModel getRightMessageCornerRadius() {
        return rightMessageModel;
    }

    public MessageBubbleConfiguration setRightMessageRadius(float topLeft,float topRight, float bottomLeft, float bottomRight) {
        rightMessageModel = ShapeAppearanceModel.builder()
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight)
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .build();
        return this;
    }

    public float getRightMessageRadius() {
        return rightMessageRadius;
    }

    public MessageBubbleConfiguration setRightMessageRadius(float rightMessageRadius) {
        this.rightMessageRadius = rightMessageRadius;
        return this;
    }

    public AvatarConfiguration getAvatarConfiguration() {
        return avatarConfiguration;
    }

    public MessageBubbleConfiguration setAvatarConfiguration(AvatarConfiguration avatarConfiguration) {
        this.avatarConfiguration = avatarConfiguration;
        return this;
    }

    public MessageReceiptConfiguration getMessageReceiptConfiguration() {
        return messageReceiptConfiguration;
    }

    public MessageBubbleConfiguration setMessageReceiptConfiguration(MessageReceiptConfiguration messageReceiptConfiguration) {
        this.messageReceiptConfiguration = messageReceiptConfiguration;
        return this;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public MessageBubbleConfiguration setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

    public MessageBubbleConfiguration setSentMessageInputData(MessageInputData data) {
        sentMessageInputData = data;
        return this;
    }

    public MessageInputData getSentMessageInputData() {
        return sentMessageInputData;
    }

    public MessageBubbleConfiguration setReceiverMessageInputData(MessageInputData data) {
        receiverMessageInputData = data;
        return this;
    }

    public MessageInputData getReceiverMessageInputData() {
        return receiverMessageInputData;
    }
}
