package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

public class ConversationsConfiguration extends CometChatConfigurations {

    private boolean hideDeleteConversation;
    private boolean hideStartConversation;
    private boolean hideSearchConversation;
    private final Context context;

    public ConversationsConfiguration(Context context) {
        this.context = context;
    }

    public ConversationsConfiguration hideDeleteConversation(boolean isHidden) {
        this.hideDeleteConversation = isHidden;
        return this;
    }

    public boolean isDeleteConversationHidden() {
        return hideDeleteConversation;
    }

    public ConversationsConfiguration hideStartConversation(boolean isHidden) {
        this.hideStartConversation = isHidden;
        return this;
    }

    public boolean isStartConversationHidden() {
        return hideStartConversation;
    }

    public ConversationsConfiguration hideSearchConversation(boolean isHidden) {
        this.hideSearchConversation = isHidden;
        return this;
    }

    public boolean isSearchConversationHidden() {
        return hideSearchConversation;
    }
}
