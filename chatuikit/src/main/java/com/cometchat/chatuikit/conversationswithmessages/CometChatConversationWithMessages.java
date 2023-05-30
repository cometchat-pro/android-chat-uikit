package com.cometchat.chatuikit.conversationswithmessages;

import android.content.Context;
import android.util.AttributeSet;

import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.conversations.CometChatConversations;
import com.cometchat.chatuikit.conversations.ConversationsConfiguration;
import com.cometchat.chatuikit.messages.MessageActivity;
import com.cometchat.chatuikit.messages.MessagesConfiguration;

/**
 * The custom implementation of the CometChatConversations class called CometChatConversationWithMessages.
 * It extends the CometChatConversations class and adds additional functionality for opening the message screen for a selected conversation.
 * <br>
 * Example:
 * <pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *            <com.cometchat.chatuikit.conversationswithmessages.CometChatConversationWithMessages
 *               android:id="@+id/conversationWithMessages"
 *               android:layout_width="match_parent"
 *               android:layout_height="match_parent" />
 *
 *  </LinearLayout>
 *  }
 *  </pre>
 */
public class CometChatConversationWithMessages extends CometChatConversations {

    private MessagesConfiguration messagesConfiguration;
    private User user;
    private Group group;

    public CometChatConversationWithMessages(Context context) {
        super(context);
        init(context);
    }

    public CometChatConversationWithMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatConversationWithMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        super.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation conversation, int position) {
                if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_GROUP))
                    MessageActivity.launch(context, ((Group) conversation.getConversationWith()), messagesConfiguration);
                else
                    MessageActivity.launch(context, ((User) conversation.getConversationWith()), messagesConfiguration);
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        openMessagesForUser(getContext(), user);
        openMessagesForGroup(getContext(), group);
    }

    private void openMessagesForUser(Context context, User user) {
        if (user != null && !user.isBlockedByMe() && !user.isHasBlockedMe())
            MessageActivity.launch(context, user, messagesConfiguration);
    }

    private void openMessagesForGroup(Context context, Group group) {
        if (group != null && group.isJoined())
            MessageActivity.launch(context, group, messagesConfiguration);
    }

    public void setConversationsConfiguration(ConversationsConfiguration conversationsConfiguration) {
        if (conversationsConfiguration != null) {
            super.setSubtitleView(conversationsConfiguration.getSubtitle());
            super.disableUsersPresence(conversationsConfiguration.isDisableUsersPresence());
            super.setListItemView(conversationsConfiguration.getCustomView());
            super.setMenu(conversationsConfiguration.getMenu());
            super.setOptions(conversationsConfiguration.getOptions());
            super.hideSeparator(conversationsConfiguration.isHideSeparator());
            super.showBackButton(conversationsConfiguration.isShowBackButton());
            super.backIcon(conversationsConfiguration.getBackButtonIcon());
            super.setSelectionMode(conversationsConfiguration.getSelectionMode());
            super.setOnSelection(conversationsConfiguration.getOnSelection());
            super.setSearchBoxIcon(conversationsConfiguration.getBackButtonIcon());
            super.setTitle(conversationsConfiguration.getTitle());
            super.setEmptyStateView(conversationsConfiguration.getEmptyStateView());
            super.setErrorStateView(conversationsConfiguration.getErrorStateView());
            super.setLoadingStateView(conversationsConfiguration.getLoadingStateView());
            super.setConversationsRequestBuilder(conversationsConfiguration.getConversationsRequest());
            super.disableSoundForMessages(conversationsConfiguration.isDisableSoundForMessages());
            super.setCustomSoundForMessages(conversationsConfiguration.getCustomSoundForMessage());
            super.setPrivateGroupIcon(conversationsConfiguration.getPrivateGroupIcon());
            super.setProtectedGroupIcon(conversationsConfiguration.getProtectedGroupIcon());
            super.setTail(conversationsConfiguration.getTail());
            super.disableReceipt(conversationsConfiguration.isDisableReadReceipt());
            super.disableTyping(conversationsConfiguration.isDisableTyping());
            super.setReadIcon(conversationsConfiguration.getReadIcon());
            super.setDeliveredIcon(conversationsConfiguration.getDeliveredIcon());
            super.setSentIcon(conversationsConfiguration.getSentIcon());
            super.setStyle(conversationsConfiguration.getStyle());
            super.setListItemStyle(conversationsConfiguration.getListItemStyle());
            super.setDateStyle(conversationsConfiguration.getDateStyle());
            super.setBadgeStyle(conversationsConfiguration.getBadgeStyle());
            super.setAvatarStyle(conversationsConfiguration.getAvatarStyle());
            super.setStatusIndicatorStyle(conversationsConfiguration.getStatusIndicatorStyle());
            super.emptyStateText(conversationsConfiguration.getEmptyStateText());
            super.errorStateText(conversationsConfiguration.getErrorStateText());
            super.setItemClickListener(conversationsConfiguration.getItemClickListener());
            super.setOnError(conversationsConfiguration.getOnError());
        }
    }

    public void setMessagesConfiguration(MessagesConfiguration messagesConfiguration) {
        this.messagesConfiguration = messagesConfiguration;
    }
}
