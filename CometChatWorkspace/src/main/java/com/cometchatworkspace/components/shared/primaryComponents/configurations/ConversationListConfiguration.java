package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

public class ConversationListConfiguration extends CometChatConfigurations {

    private boolean hideDeleteConversation;
    private boolean hideStartConversation;
    private boolean hideSearchConversation;
    private final Context context;

    public ConversationListConfiguration(Context context) {
        this.context = context;
    }

    public ConversationListConfiguration hideDeleteConversation(boolean isHidden) {
        this.hideDeleteConversation = isHidden;
        return this;
    }

    public boolean isDeleteConversationHidden() {
        return hideDeleteConversation;
    }

    public ConversationListConfiguration hideStartConversation(boolean isHidden) {
        this.hideStartConversation = isHidden;
        return this;
    }

    public boolean isStartConversationHidden() {
        return hideStartConversation;
    }

    public ConversationListConfiguration hideSearchConversation(boolean isHidden) {
        this.hideSearchConversation = isHidden;
        return this;
    }

    public boolean isSearchConversationHidden() {
        return hideSearchConversation;
    }
}
