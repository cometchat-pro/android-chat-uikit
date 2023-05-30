package com.cometchat.chatuikit.conversationswithmessages;

import com.cometchat.chatuikit.conversations.ConversationsConfiguration;
import com.cometchat.chatuikit.messages.MessagesConfiguration;

 class ConversationsWithMessagesConfiguration {

    private ConversationsConfiguration conversationsConfiguration;

    private MessagesConfiguration messagesConfiguration;

    public ConversationsWithMessagesConfiguration setConversationsConfiguration(ConversationsConfiguration conversationsConfiguration) {
        this.conversationsConfiguration = conversationsConfiguration;
        return this;
    }

    public ConversationsWithMessagesConfiguration setMessagesConfiguration(MessagesConfiguration messagesConfiguration) {
        this.messagesConfiguration = messagesConfiguration;
        return this;
    }

    public ConversationsConfiguration getConversationsConfiguration() {
        return conversationsConfiguration;
    }

    public MessagesConfiguration getMessagesConfiguration() {
        return messagesConfiguration;
    }
}
