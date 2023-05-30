package com.cometchat.chatuikit.transferownership;

import android.content.Context;
import android.view.View;

import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.chatuikit.groupmembers.CometChatGroupMembers;
import com.cometchat.chatuikit.groupmembers.GroupMembersConfiguration;

import java.util.List;
/**
 * TransferOwnershipConfiguration is a configuration class that extends GroupMembersConfiguration and provides additional
 * options for configuring the transfer ownership feature.
 * <p>
 * It allows setting a listener for transfer ownership events using the setOnTransferOwnership() method.
 * <p>
 * To use this configuration, create an instance of TransferOwnershipConfiguration and chain the method calls to set the
 * desired configuration options.
 */
public class TransferOwnershipConfiguration extends GroupMembersConfiguration {
    private CometChatTransferOwnership.OnTransferOwnership onTransferOwnership;
    /**
     * Sets the listener for transfer ownership events.
     *
     * @param onTransferOwnership The listener to be invoked when transfer ownership events occur.
     * @return The updated TransferOwnershipConfiguration instance.
     */
    public TransferOwnershipConfiguration setOnTransferOwnership(CometChatTransferOwnership.OnTransferOwnership onTransferOwnership) {
        this.onTransferOwnership = onTransferOwnership;
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setOnItemClickListener(OnItemClickListener<GroupMember> onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setOnError(OnError onError) {
        super.setOnError(onError);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setSubtitle(Function3<Context, GroupMember, Group, View> subtitle) {
        super.setSubtitle(subtitle);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setTail(Function3<Context, GroupMember, Group, View> tail) {
        super.setTail(tail);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setCustomView(Function3<Context, GroupMember, Group, View> customView) {
        super.setCustomView(customView);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setOptions(Function3<Context, GroupMember, Group, List<CometChatOption>> options) {
        super.setOptions(options);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setDisableUsersPresence(boolean disableUsersPresence) {
        super.setDisableUsersPresence(disableUsersPresence);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setMenu(View menu) {
        super.setMenu(menu);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setHideSeparator(boolean hideSeparator) {
        super.setHideSeparator(hideSeparator);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setSearchPlaceholderText(String searchPlaceholderText) {
        super.setSearchPlaceholderText(searchPlaceholderText);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setEmptyStateText(String emptyStateText) {
        super.setEmptyStateText(emptyStateText);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setErrorStateText(String errorStateText) {
        super.setErrorStateText(errorStateText);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setBackButtonIcon(int backButtonIcon) {
        super.setBackButtonIcon(backButtonIcon);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setShowBackButton(boolean showBackButton) {
        super.setShowBackButton(showBackButton);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        super.setSelectionMode(selectionMode);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setOnSelection(CometChatGroupMembers.OnSelection onSelection) {
        super.setOnSelection(onSelection);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setSearchBoxIcon(int searchBoxIcon) {
        super.setSearchBoxIcon(searchBoxIcon);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setHideSearch(boolean hideSearch) {
        super.setHideSearch(hideSearch);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setEmptyStateView(int emptyStateView) {
        super.setEmptyStateView(emptyStateView);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setErrorStateView(int errorStateView) {
        super.setErrorStateView(errorStateView);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setLoadingStateView(int loadingStateView) {
        super.setLoadingStateView(loadingStateView);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setMembersRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder membersRequestBuilder) {
        super.setMembersRequestBuilder(membersRequestBuilder);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setMembersSearchRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder membersSearchRequestBuilder) {
        super.setMembersSearchRequestBuilder(membersSearchRequestBuilder);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        super.setAvatarStyle(avatarStyle);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        super.setStatusIndicatorStyle(statusIndicatorStyle);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        super.setListItemStyle(listItemStyle);
        return this;
    }

    public TransferOwnershipConfiguration setStyle(TransferOwnershipStyle style) {
        super.setStyle(style);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setSubmitIcon(int submitIcon) {
        super.setSubmitIcon(submitIcon);
        return this;
    }

    @Override
    public TransferOwnershipConfiguration setSelectionIcon(int selectionIcon) {
        super.setSelectionIcon(selectionIcon);
        return this;
    }

    public CometChatTransferOwnership.OnTransferOwnership getOnTransferOwnership() {
        return onTransferOwnership;
    }
}
