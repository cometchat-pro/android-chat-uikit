package com.cometchat.chatuikit.shared.events;

import com.cometchat.pro.models.Conversation;

import java.util.HashMap;

/**
 * Abstract class for handling CometChat conversation events.
 */
public abstract class CometChatConversationEvents {
    /**
     * Map to store the registered conversation event listeners.
     */
    public static final HashMap<String, CometChatConversationEvents> conversationEvents = new HashMap<>();

    /**
     * Called when a conversation is deleted.
     *
     * @param conversation The deleted conversation object.
     */
    public void ccConversationDeleted(Conversation conversation) {
    }

    /**
     * Adds a conversation event listener with the specified tag.
     *
     * @param tag                         The tag to identify the listener.
     * @param cometChatConversationEvents The conversation event listener to be added.
     */
    public static void addListener(String tag, CometChatConversationEvents cometChatConversationEvents) {
        conversationEvents.put(tag, cometChatConversationEvents);
    }

    /**
     * Removes all conversation event listeners.
     */
    public static void removeListeners() {
        conversationEvents.clear();
    }

    /**
     * Removes the conversation event listener associated with the specified tag.
     *
     * @param tag The tag of the listener to be removed.
     */
    public static void removeListener(String tag) {
        conversationEvents.remove(tag);
    }

}
