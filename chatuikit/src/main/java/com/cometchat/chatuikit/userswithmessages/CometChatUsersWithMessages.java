package com.cometchat.chatuikit.userswithmessages;


import android.content.Context;
import android.util.AttributeSet;

import com.cometchat.chatuikit.messages.MessageActivity;
import com.cometchat.chatuikit.messages.MessagesConfiguration;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.users.CometChatUsers;
import com.cometchat.chatuikit.users.UsersConfiguration;
import com.cometchat.pro.models.User;

/**
 * The custom implementation of the CometChatUsers class called CometChatUsersWithMessages.
 * It extends the CometChatUsers class and adds additional functionality for opening the message screen for a selected user.
 * <br>
 * Example:
 * <pre>{@code
 *  <LinearLayout
 *       xmlns:android="http://schemas.android.com/apk/res/android"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent">
 *     <com.cometchat.chatuikit.userswithmessages.CometChatUsersWithMessages
 *         android:id="@+id/user_with_messages"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" />
 *  </LinearLayout>
 *  }
 *  </pre>
 */
public class CometChatUsersWithMessages extends CometChatUsers {

    private MessagesConfiguration messagesConfiguration;
    private User user;

    public CometChatUsersWithMessages(Context context) {
        super(context);
        init(context);
    }

    public CometChatUsersWithMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatUsersWithMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        super.setItemClickListener(new OnItemClickListener<User>() {
            @Override
            public void OnItemClick(User user, int position) {
                if (user != null) openMessages(context, user);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        openMessages(getContext(), user);
    }

    private void openMessages(Context context, User user) {
        if (user != null) MessageActivity.launch(context, user, messagesConfiguration);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUsersConfiguration(UsersConfiguration usersConfiguration) {
        if (usersConfiguration != null) {
            super.setSubtitle(usersConfiguration.getSubtitle());
            super.disableUsersPresence(usersConfiguration.isDisableUsersPresence());
            super.setListItemView(usersConfiguration.getListItemView());
            super.setMenu(usersConfiguration.getMenu());
            super.setOptions(usersConfiguration.getOptions());
            super.hideSeparator(usersConfiguration.isHideSeparator());
            super.setSearchPlaceholderText(usersConfiguration.getSearchPlaceholderText());
            super.showBackButton(usersConfiguration.isShowBackButton());
            super.backIcon(usersConfiguration.getBackButtonIcon());
            super.setSelectionMode(usersConfiguration.getSelectionMode());
            super.setOnSelection(usersConfiguration.getOnSelection());
            super.setSearchBoxIcon(usersConfiguration.getBackButtonIcon());
            super.hideSearch(usersConfiguration.isHideSearch());
            super.setTitle(usersConfiguration.getTitle());
            super.setEmptyStateView(usersConfiguration.getEmptyStateView());
            super.setErrorStateView(usersConfiguration.getErrorStateView());
            super.setLoadingStateView(usersConfiguration.getLoadingStateView());
            super.setUsersRequestBuilder(usersConfiguration.getUsersRequestBuilder());
            super.setSearchRequestBuilder(usersConfiguration.getUsersSearchRequestBuilder());
            super.setSearchBoxIcon(usersConfiguration.getSearchBoxIcon());
            super.setAvatarStyle(usersConfiguration.getAvatarStyle());
            super.setListItemStyle(usersConfiguration.getListItemStyle());
            super.setStatusIndicatorStyle(usersConfiguration.getStatusIndicatorStyle());
            super.errorStateText(usersConfiguration.getErrorStateText());
            super.emptyStateText(usersConfiguration.getEmptyStateText());
            super.setStyle(usersConfiguration.getStyle());
            super.setSelectionIcon(usersConfiguration.getSelectionIcon());
            super.setSubmitIcon(usersConfiguration.getSubmitIcon());
        }
    }

    public void setMessagesConfiguration(MessagesConfiguration messagesConfiguration) {
        this.messagesConfiguration = messagesConfiguration;
    }
}
