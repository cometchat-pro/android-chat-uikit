package com.cometchatworkspace.components.chats;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList.CometChatConversationList;

import java.util.HashMap;
import java.util.List;

public abstract class CometChatConversationEvents {
    public static final HashMap<String, CometChatConversationEvents> conversationEvents = new HashMap<>();

    /**
     * On item click.
     *
     * @param conversation the var
     * @param position     the position
     */
    public abstract void onItemClick(Conversation conversation, int position);

    /**
     * On delete conversation.
     *
     * @param conversation the var
     */
    public void onDeleteConversation(Conversation conversation) {
    }
    /**
     * Whenever a CometChatConversationList loads the list of conversation successfully
     * @param conversationList is a list of conversations loaded.
     *
     * @see CometChatConversationList
     */
    public void onSuccess(List<Conversation> conversationList) {}
    /**
     * On start conversation.
     */
    public void onStartConversation() {
    }

    /**
     * On item long click.
     *
     * @param conversation the var
     * @param position     the position
     */
    public void onItemLongClick(Conversation conversation, int position) {
    }

    public void onError(CometChatException exception) {
    }

    public static void addListener(String tag, CometChatConversationEvents cometChatConversationEvents) {
        conversationEvents.put(tag, cometChatConversationEvents);
    }

    public static void removeListener() {
        conversationEvents.clear();
    }

}
