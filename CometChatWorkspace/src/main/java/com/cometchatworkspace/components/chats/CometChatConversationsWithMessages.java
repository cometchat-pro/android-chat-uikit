package com.cometchatworkspace.components.chats;

import android.content.Context;
import android.util.AttributeSet;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.components.messages.CometChatMessageEvents;
import com.cometchatworkspace.components.messages.MessageStatus;
import com.cometchatworkspace.components.messages.message_list.CometChatMessagesActivity;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ComposerConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.HeaderConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageListConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CometChatConversationsWithMessages extends CometChatConversations {

    private Context context;

    private List<CometChatConfigurations> messageConfig = new ArrayList<>();
    private List<CometChatConfigurations> conversationConfig = new ArrayList<>();

    public CometChatConversationsWithMessages(Context context) {
        super(context);
        this.context = context;
    }

    public CometChatConversationsWithMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public CometChatConversationsWithMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

    }

    private void userIntent(User user) {
        if (context != null)
            CometChatMessagesActivity.launch(context, user,messageConfig);
    }

    private void startGroupIntent(Group group) {
        if (context != null)
            CometChatMessagesActivity.launch(context, group,messageConfig);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        super.setConfiguration(conversationConfig);
        CometChatConversationEvents.addListener("CometChatConversationsWithMessages", new CometChatConversationEvents() {
            @Override
            public void onItemClick(Conversation conversation, int position) {
                if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_GROUP)) {
                    startGroupIntent(((Group) conversation.getConversationWith()));
                } else {
                    User user = ((User) conversation.getConversationWith());
                    userIntent(user);
                }
            }

            @Override
            public void onDeleteConversation(Conversation conversation) {
                super.onDeleteConversation(conversation);
            }

            @Override
            public void onStartConversation() {
                super.onStartConversation();
            }

            @Override
            public void onItemLongClick(Conversation conversation, int position) {
                super.onItemLongClick(conversation, position);
            }

            @Override
            public void onError(CometChatException exception) {
                super.onError(exception);
            }
        });
        CometChatMessageEvents.addListener("CometChatConversationsWithMessages", new CometChatMessageEvents() {

            @Override
            public void onMessageSent(BaseMessage baseMessage, int status) {
                if (status==MessageStatus.SUCCESS) {
                    if (cometchatConversationList != null) {
                        cometchatConversationList.refreshSingleConversation(baseMessage, false);
                    }
                }
            }

            @Override
            public void onMessageError(CometChatException e) {

            }

            @Override
            public void onMessageRead(BaseMessage baseMessage) {
                if (cometchatConversationList != null) {
                    cometchatConversationList.refreshSingleConversation(baseMessage,false);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CometChatConversationEvents.removeListener();
    }

    @Override
    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof CometChatMessagesConfigurations ||
                configuration instanceof MessageBubbleConfiguration ||
                configuration instanceof HeaderConfiguration ||
                configuration instanceof MessageListConfiguration ||
                configuration instanceof ComposerConfiguration) {
            messageConfig.add(configuration);
        } else {
            conversationConfig.add(configuration);
        }
    }

    @Override
    public void setConfiguration(List<CometChatConfigurations> configurations) {
        for (CometChatConfigurations cometChatConfigurations : configurations) {
            setConfiguration(cometChatConfigurations);
        }
    }
}
