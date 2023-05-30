package com.cometchat.chatuikit.bannedmembers;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;

import com.cometchat.chatuikit.shared.Interfaces.Function3;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;

import java.util.List;

/**
 * The configuration class for the BannedMembers component. It allows customization and fine-tuning of the
 * BannedMembers component's behavior and appearance.
 */
public class BannedMembersConfiguration {

    private Function3<Context, GroupMember, Group, View> subtitle, tail, customView;
    private Function3<Context, GroupMember, Group, List<CometChatOption>> options;
    private boolean disableUsersPresence = true;
    private View menu;
    private boolean hideSeparator;
    private String searchPlaceholderText, emptyStateText, errorStateText;
    private @DrawableRes
    int backButtonIcon;
    private boolean showBackButton;
    private UIKitConstants.SelectionMode selectionMode;
    private CometChatBannedMembers.OnSelection onSelection;
    private @DrawableRes
    int searchBoxIcon;
    private boolean hideSearch;
    private String title;
    private @LayoutRes
    int emptyStateView;
    private @LayoutRes
    int errorStateView;
    private @LayoutRes
    int loadingStateView;
    private BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedMembersRequestBuilder;
    private BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedMembersSearchRequestBuilder;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private ListItemStyle listItemStyle;
    private BannedMembersStyle style;
    private @DrawableRes
    int submitIcon, selectionIcon;
    private OnItemClickListener<GroupMember> onItemClickListener;
    private OnError onError;

    /**
     * Sets the subtitle view for each item in the banned members list.
     *
     * @param subtitle A function that takes the context, a group member, and a group as parameters
     *                 and returns a custom subtitle view for the item.
     */
    public BannedMembersConfiguration setSubtitle(Function3<Context, GroupMember, Group, View> subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * This method helps to get Click events of CometChatGroupMemberList
     *
     * @param onItemClickListener object of the OnItemClickListener
     */
    public BannedMembersConfiguration setOnItemClickListener(OnItemClickListener<GroupMember> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    /**
     * Sets the error handler for handling errors.
     *
     * @param onError The error handler to be set.
     */
    public BannedMembersConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets the tail view for each item in the banned members list.
     *
     * @param tail A function that takes the context, a group member, and a group as parameter
     *             and returns a custom tail view for the item.
     */
    public BannedMembersConfiguration setTail(Function3<Context, GroupMember, Group, View> tail) {
        this.tail = tail;
        return this;
    }

    /**
     * Sets the custom list item view for each item in the banned members list.
     *
     * @param customView A function that takes the context, a group member, and a group as parameters
     *                   and returns a custom view for the item.
     */
    public BannedMembersConfiguration setCustomView(Function3<Context, GroupMember, Group, View> customView) {
        this.customView = customView;
        return this;
    }

    /**
     * Sets the options for handling CometChat actions related to group members.
     *
     * @param options A Function3 that defines the options based on the context, group member, and group. It should return a List of CometChatOption objects.
     */
    public BannedMembersConfiguration setOptions(Function3<Context, GroupMember, Group, List<CometChatOption>> options) {
        this.options = options;
        return this;
    }

    /**
     * Disables or enables the presence status of users in the banned members list.
     *
     * @param disableUsersPresence {@code true} to disable users' presence, {@code false} to enable it.
     */
    public BannedMembersConfiguration setDisableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
        return this;
    }

    /**
     * Sets a menu view for the toolbar.
     *
     * @param menu The view representing the menu.
     */
    public BannedMembersConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }

    /**
     * Hides or shows the separator in the banned members list.
     *
     * @param hideSeparator {@code true} to hide the separator, {@code false} to show it.
     */
    public BannedMembersConfiguration setHideSeparator(boolean hideSeparator) {
        this.hideSeparator = hideSeparator;
        return this;
    }

    /**
     * Sets the placeholder text for the search input field.
     *
     * @param searchPlaceholderText The text to be set as the placeholder.
     */
    public BannedMembersConfiguration setSearchPlaceholderText(String searchPlaceholderText) {
        this.searchPlaceholderText = searchPlaceholderText;
        return this;
    }

    /**
     * Sets the text to display when the banned members list is empty.
     *
     * @param emptyStateText The message to display.
     *                       If {@code null} or empty, the default message "No members" will be used.
     */
    public BannedMembersConfiguration setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        return this;
    }

    /**
     * Sets the error state text.
     *
     * @param errorStateText The error text to set.
     *                       If {@code null} or empty, no text will be set.
     */
    public BannedMembersConfiguration setErrorStateText(String errorStateText) {
        this.errorStateText = errorStateText;
        return this;
    }

    /**
     * Sets the drawable as the back icon.
     *
     * @param backButtonIcon The drawable to set as the back icon.
     */
    public BannedMembersConfiguration setBackButtonIcon(int backButtonIcon) {
        this.backButtonIcon = backButtonIcon;
        return this;
    }

    /**
     * Shows or hides the back button based on the specified visibility.
     *
     * @param showBackButton Determines whether the back button should be visible or hidden.
     */
    public BannedMembersConfiguration setShowBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }

    /**
     * Sets the selection mode for the banned members list.
     *
     * @param selectionMode The selection mode to be set for the banned members list.
     */
    public BannedMembersConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

    /**
     * Sets the selection listener for the banned members list.
     *
     * @param onSelection The listener to be invoked when a member is selected or deselected.
     */
    public BannedMembersConfiguration setOnSelection(CometChatBannedMembers.OnSelection onSelection) {
        this.onSelection = onSelection;
        return this;
    }

    /**
     * Sets the icon for the search box.
     *
     * @param searchBoxIcon The resource ID of the icon to be set.
     */
    public BannedMembersConfiguration setSearchBoxIcon(int searchBoxIcon) {
        this.searchBoxIcon = searchBoxIcon;
        return this;
    }

    /**
     * Sets the visibility of the search box.
     *
     * @param hideSearch A boolean indicating whether to hide or show the search box. True to hide the search box, false to show it.
     */
    public BannedMembersConfiguration setHideSearch(boolean hideSearch) {
        this.hideSearch = hideSearch;
        return this;
    }

    /**
     * Sets the title text for the toolbar.
     *
     * @param title The text to be set as the title.
     */
    public BannedMembersConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the empty state view for the banned members list.
     *
     * @param emptyStateView The layout resource ID of the empty state view.
     *                       If {@code 0}, no empty state view will be set.
     */
    public BannedMembersConfiguration setEmptyStateView(int emptyStateView) {
        this.emptyStateView = emptyStateView;
        return this;
    }

    /**
     * Sets the error state view for the banned members list.
     *
     * @param errorStateView The layout resource ID of the error state view.
     *                       If {@code 0}, no error state view will be set.
     */
    public BannedMembersConfiguration setErrorStateView(int errorStateView) {
        this.errorStateView = errorStateView;
        return this;
    }

    /**
     * Sets the loading state view for the banned members list.
     *
     * @param loadingStateView The layout resource ID of the loading state view.
     *                         If {@code 0}, no loading state view will be set.
     */
    public BannedMembersConfiguration setLoadingStateView(int loadingStateView) {
        this.loadingStateView = loadingStateView;
        return this;
    }

    /**
     * Sets the request builder for fetching banned group members.
     *
     * @param bannedMembersRequestBuilder The request builder for configuring the parameters of the banned group members request.
     */
    public BannedMembersConfiguration setBannedMembersRequestBuilder(BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedMembersRequestBuilder) {
        this.bannedMembersRequestBuilder = bannedMembersRequestBuilder;
        return this;
    }

    /**
     * Sets the request builder for searching banned group members.
     *
     * @param bannedMembersSearchRequestBuilder The request builder for configuring the parameters of the search request for banned group members.
     */
    public BannedMembersConfiguration setBannedMembersSearchRequestBuilder(BannedGroupMembersRequest.BannedGroupMembersRequestBuilder bannedMembersSearchRequestBuilder) {
        this.bannedMembersSearchRequestBuilder = bannedMembersSearchRequestBuilder;
        return this;
    }

    /**
     * Sets the avatar style for the banned members list.
     *
     * @param avatarStyle The style object containing various style attributes for the avatars of banned members.
     */
    public BannedMembersConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    /**
     * Sets the status indicator style for the banned members list.
     *
     * @param statusIndicatorStyle The style object containing various style attributes for the status indicators of banned members.
     */
    public BannedMembersConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        this.statusIndicatorStyle = statusIndicatorStyle;
        return this;
    }

    /**
     * Sets the list item style for the banned members list.
     *
     * @param listItemStyle The style object containing various style attributes for the list items of banned members.
     */
    public BannedMembersConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    /**
     * Sets the style for the banned members list.
     *
     * @param style The style object containing various style attributes for the banned members list.
     */
    public BannedMembersConfiguration setStyle(BannedMembersStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the submit icon for the selection mode.
     *
     * @param submitIcon The drawable resource ID of the submit icon to be set.
     */
    public BannedMembersConfiguration setSubmitIcon(int submitIcon) {
        this.submitIcon = submitIcon;
        return this;
    }

    /**
     * Sets the selection icon for the banned members list items.
     *
     * @param selectionIcon The drawable resource ID of the selection icon to be set.
     */
    public BannedMembersConfiguration setSelectionIcon(int selectionIcon) {
        this.selectionIcon = selectionIcon;
        return this;
    }

    public Function3<Context, GroupMember, Group, View> getSubtitle() {
        return subtitle;
    }

    public Function3<Context, GroupMember, Group, View> getTail() {
        return tail;
    }

    public Function3<Context, GroupMember, Group, View> getCustomView() {
        return customView;
    }

    public Function3<Context, GroupMember, Group, List<CometChatOption>> getOptions() {
        return options;
    }

    public boolean isDisableUsersPresence() {
        return disableUsersPresence;
    }

    public View getMenu() {
        return menu;
    }

    public boolean isHideSeparator() {
        return hideSeparator;
    }

    public String getSearchPlaceholderText() {
        return searchPlaceholderText;
    }

    public String getEmptyStateText() {
        return emptyStateText;
    }

    public String getErrorStateText() {
        return errorStateText;
    }

    public int getBackButtonIcon() {
        return backButtonIcon;
    }

    public boolean isShowBackButton() {
        return showBackButton;
    }

    public UIKitConstants.SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public CometChatBannedMembers.OnSelection getOnSelection() {
        return onSelection;
    }

    public int getSearchBoxIcon() {
        return searchBoxIcon;
    }

    public boolean isHideSearch() {
        return hideSearch;
    }

    public String getTitle() {
        return title;
    }

    public int getEmptyStateView() {
        return emptyStateView;
    }

    public int getErrorStateView() {
        return errorStateView;
    }

    public int getLoadingStateView() {
        return loadingStateView;
    }

    public BannedGroupMembersRequest.BannedGroupMembersRequestBuilder getBannedMembersRequestBuilder() {
        return bannedMembersRequestBuilder;
    }

    public BannedGroupMembersRequest.BannedGroupMembersRequestBuilder getBannedMembersSearchRequestBuilder() {
        return bannedMembersSearchRequestBuilder;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public StatusIndicatorStyle getStatusIndicatorStyle() {
        return statusIndicatorStyle;
    }

    public ListItemStyle getListItemStyle() {
        return listItemStyle;
    }

    public BannedMembersStyle getStyle() {
        return style;
    }

    public int getSubmitIcon() {
        return submitIcon;
    }

    public int getSelectionIcon() {
        return selectionIcon;
    }

    public OnItemClickListener<GroupMember> getOnItemClickListener() {
        return onItemClickListener;
    }

    public OnError getOnError() {
        return onError;
    }
}
