package com.cometchat.chatuikit.threadedmessages;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.messagecomposer.MessageComposerConfiguration;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;

/**
 * Configuration class for customizing the appearance and behavior of ThreadedMessages.
 */
public class ThreadedMessagesConfiguration {
    private @DrawableRes
    int closeIcon;
    private String title;
    private Function2<Context, BaseMessage, View> messageBubbleView;
    private Function2<Context, BaseMessage, View> messageActionView;
    private MessageListConfiguration messageListConfiguration;
    private MessageComposerConfiguration messageComposerConfiguration;
    private ThreadedMessagesStyle style;

    /**
     * Sets the close icon resource for the ThreadedMessages.
     *
     * @param closeIcon The close icon resource to set.
     * @return The updated ThreadedMessagesConfiguration object.
     */
    public ThreadedMessagesConfiguration setCloseIcon(int closeIcon) {
        this.closeIcon = closeIcon;
        return this;
    }

    /**
     * Sets the title for the ThreadedMessages.
     *
     * @param title The title to set.
     * @return The updated ThreadedMessagesConfiguration object.
     */
    public ThreadedMessagesConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the custom message bubble view for the ThreadedMessages.
     *
     * @param messageBubbleView The function that returns the custom message bubble view.
     * @return The updated ThreadedMessagesConfiguration object.
     */
    public ThreadedMessagesConfiguration setMessageBubbleView(Function2<Context, BaseMessage, View> messageBubbleView) {
        this.messageBubbleView = messageBubbleView;
        return this;
    }

    /**
     * Sets the custom message action view for the ThreadedMessages.
     *
     * @param messageActionView The function that returns the custom message action view.
     * @return The updated ThreadedMessagesConfiguration object.
     */
    public ThreadedMessagesConfiguration setMessageActionView(Function2<Context, BaseMessage, View> messageActionView) {
        this.messageActionView = messageActionView;
        return this;
    }

    /**
     * Sets the configuration for the message list in the ThreadedMessages.
     *
     * @param messageListConfiguration The MessageListConfiguration to set.
     * @return The updated ThreadedMessagesConfiguration object.
     */
    public ThreadedMessagesConfiguration setMessageListConfiguration(MessageListConfiguration messageListConfiguration) {
        this.messageListConfiguration = messageListConfiguration;
        return this;
    }

    /**
     * Sets the configuration for the message composer in the ThreadedMessages.
     *
     * @param messageComposerConfiguration The MessageComposerConfiguration to set.
     * @return The updated ThreadedMessagesConfiguration object.
     */
    public ThreadedMessagesConfiguration setMessageComposerConfiguration(MessageComposerConfiguration messageComposerConfiguration) {
        this.messageComposerConfiguration = messageComposerConfiguration;
        return this;
    }

    /**
     * Sets the style for the ThreadedMessages.
     *
     * @param style The ThreadedMessagesStyle to set.
     * @return The updated ThreadedMessagesConfiguration object.
     */
    public ThreadedMessagesConfiguration setStyle(ThreadedMessagesStyle style) {
        this.style = style;
        return this;
    }

    public int getCloseIcon() {
        return closeIcon;
    }

    public String getTitle() {
        return title;
    }

    public Function2<Context, BaseMessage, View> getMessageBubbleView() {
        return messageBubbleView;
    }

    public Function2<Context, BaseMessage, View> getMessageActionView() {
        return messageActionView;
    }

    public MessageListConfiguration getMessageListConfiguration() {
        return messageListConfiguration;
    }

    public MessageComposerConfiguration getMessageComposerConfiguration() {
        return messageComposerConfiguration;
    }

    public ThreadedMessagesStyle getStyle() {
        return style;
    }
}
