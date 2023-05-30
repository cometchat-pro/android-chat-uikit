package com.cometchat.chatuikit.shared.events;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.pro.models.BaseMessage;

import java.util.HashMap;

/**
 * Abstract class for handling CometChat message events.
 */
public abstract class CometChatMessageEvents {

    /**
     * Map to store the registered message event listeners.
     */
    public static final HashMap<String, CometChatMessageEvents> messageEvents = new HashMap();

    /**
     * Called when a message is sent.
     *
     * @param baseMessage The sent message object.
     * @param status      The status of the sent message.
     */
    public void ccMessageSent(BaseMessage baseMessage, @MessageStatus int status) {
    }

    /**
     * Called when a message is edited.
     *
     * @param baseMessage The edited message object.
     * @param status      The status of the edited message.
     */
    public void ccMessageEdited(BaseMessage baseMessage, @MessageStatus int status) {
    }

    /**
     * Called when a message is deleted.
     *
     * @param baseMessage The deleted message object.
     */
    public void ccMessageDeleted(BaseMessage baseMessage) {
    }

    /**
     * Called when a message is read.
     *
     * @param baseMessage The read message object.
     */
    public void ccMessageRead(BaseMessage baseMessage) {
    }

    /**
     * Called when a live reaction is received.
     *
     * @param icon The drawable resource ID of the live reaction.
     */
    public void ccLiveReaction(@DrawableRes int icon) {
    }

    /**
     * Adds a message event listener with the specified tag.
     *
     * @param TAG    The tag to identify the listener.
     * @param events The message event listener to be added.
     */
    public static void addListener(String TAG, CometChatMessageEvents events) {
        messageEvents.put(TAG, events);
    }

    /**
     * Removes the message event listener associated with the specified tag.
     *
     * @param TAG The tag of the listener to be removed.
     */
    public static void removeListener(String TAG) {
        messageEvents.remove(TAG);
    }
}