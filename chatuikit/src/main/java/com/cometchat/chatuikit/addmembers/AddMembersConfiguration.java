package com.cometchat.chatuikit.addmembers;

import android.content.Context;
import android.view.View;

import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.chatuikit.users.CometChatUsers;
import com.cometchat.chatuikit.users.UsersConfiguration;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.models.User;

import java.util.List;

/**
 * AddMembersConfiguration extends the UsersConfiguration class.
 * It overrides several methods from the parent class and provides additional methods specific to the "Add Members" functionality.
 */
public class AddMembersConfiguration extends UsersConfiguration {

    @Override
    public AddMembersConfiguration setSubmitIcon(int submitIcon) {
        super.setSubmitIcon(submitIcon);
        return this;
    }

    @Override
    public AddMembersConfiguration setOnItemClickListener(OnItemClickListener<User> onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
        return this;
    }

    @Override
    public AddMembersConfiguration setOnError(OnError onError) {
        super.setOnError(onError);
        return this;
    }

    @Override
    public AddMembersConfiguration setSelectionIcon(int selectionIcon) {
        super.setSelectionIcon(selectionIcon);
        return this;
    }

    @Override
    public AddMembersConfiguration setEmptyStateText(String emptyStateText) {
        super.setEmptyStateText(emptyStateText);
        return this;
    }

    @Override
    public AddMembersConfiguration setErrorStateText(String errorStateText) {
        super.setErrorStateText(errorStateText);
        return this;
    }

    @Override
    public AddMembersConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        super.setAvatarStyle(avatarStyle);
        return this;
    }

    @Override
    public AddMembersConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        super.setStatusIndicatorStyle(statusIndicatorStyle);
        return this;
    }

    @Override
    public AddMembersConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        super.setListItemStyle(listItemStyle);
        return this;
    }

    public AddMembersConfiguration setStyle(AddMembersStyle style) {
        super.setStyle(style);
        return this;
    }

    @Override
    public AddMembersConfiguration setSubtitle(Function2<Context, User, View> subtitle) {
        super.setSubtitle(subtitle);
        return this;
    }

    @Override
    public AddMembersConfiguration disableUsersPresence(boolean disableUsersPresence) {
        super.disableUsersPresence(disableUsersPresence);
        return this;
    }

    @Override
    public AddMembersConfiguration setListItemView(Function2<Context, User, View> listItemView) {
        super.setListItemView(listItemView);
        return this;
    }

    @Override
    public AddMembersConfiguration setMenu(View menu) {
        super.setMenu(menu);
        return this;
    }

    @Override
    public AddMembersConfiguration setOptions(Function2<Context, User, List<CometChatOption>> options) {
        super.setOptions(options);
        return this;
    }

    @Override
    public AddMembersConfiguration hideSeparator(boolean hideSeparator) {
        super.hideSeparator(hideSeparator);
        return this;
    }

    @Override
    public AddMembersConfiguration setSearchPlaceholderText(String searchPlaceholderText) {
        super.setSearchPlaceholderText(searchPlaceholderText);
        return this;
    }

    @Override
    public AddMembersConfiguration setBackButtonIcon(int backButtonIcon) {
        super.setBackButtonIcon(backButtonIcon);
        return this;
    }

    @Override
    public AddMembersConfiguration showBackButton(boolean showBackButton) {
        super.showBackButton(showBackButton);
        return this;
    }

    @Override
    public AddMembersConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        super.setSelectionMode(selectionMode);
        return this;
    }

    @Override
    public AddMembersConfiguration setOnSelection(CometChatUsers.OnSelection onSelection) {
        super.setOnSelection(onSelection);
        return this;
    }

    @Override
    public AddMembersConfiguration setSearchBoxIcon(int searchBoxIcon) {
        super.setSearchBoxIcon(searchBoxIcon);
        return this;
    }

    @Override
    public AddMembersConfiguration hideSearch(boolean hideSearch) {
        super.hideSearch(hideSearch);
        return this;
    }

    @Override
    public AddMembersConfiguration setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public AddMembersConfiguration setEmptyStateView(int emptyStateView) {
        super.setEmptyStateView(emptyStateView);
        return this;
    }

    @Override
    public AddMembersConfiguration setErrorStateView(int errorStateView) {
        super.setErrorStateView(errorStateView);
        return this;
    }

    @Override
    public AddMembersConfiguration setLoadingStateView(int loadingStateView) {
        super.setLoadingStateView(loadingStateView);
        return this;
    }

    @Override
    public AddMembersConfiguration setUsersRequestBuilder(UsersRequest.UsersRequestBuilder usersRequestBuilder) {
        super.setUsersRequestBuilder(usersRequestBuilder);
        return this;
    }

    @Override
    public AddMembersConfiguration setUsersSearchRequestBuilder(UsersRequest.UsersRequestBuilder usersSearchRequestBuilder) {
        super.setUsersSearchRequestBuilder(usersSearchRequestBuilder);
        return this;
    }

}
