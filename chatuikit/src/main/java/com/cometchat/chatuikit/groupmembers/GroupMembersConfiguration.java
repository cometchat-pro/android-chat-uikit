package com.cometchat.chatuikit.groupmembers;

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
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;

import java.util.List;

/**
 * Represents the configuration options for the group members list in CometChat UI.
 */
public class GroupMembersConfiguration {

    private Function3<Context, GroupMember, Group, View> subtitle, tail, customView;
    private Function3<Context, GroupMember, Group, List<CometChatOption>> options;
    private boolean disableUsersPresence;
    private View menu;
    private boolean hideSeparator;
    private String searchPlaceholderText, emptyStateText, errorStateText;
    private @DrawableRes int backButtonIcon;
    private boolean showBackButton;
    private UIKitConstants.SelectionMode selectionMode;
    private CometChatGroupMembers.OnSelection onSelection;
    private @DrawableRes int searchBoxIcon;
    private boolean hideSearch;
    private String title;
    private @LayoutRes int emptyStateView;
    private @LayoutRes int errorStateView;
    private @LayoutRes int loadingStateView;
    private GroupMembersRequest.GroupMembersRequestBuilder membersRequestBuilder;
    private GroupMembersRequest.GroupMembersRequestBuilder membersSearchRequestBuilder;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private ListItemStyle listItemStyle;
    private GroupMembersStyle style;
    private @DrawableRes int submitIcon, selectionIcon;
    private OnItemClickListener<GroupMember> onItemClickListener;
    private OnError onError;

    /**
     * Sets the click listener for the group member items in the list.
     *
     * @param onItemClickListener The click listener to set.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setOnItemClickListener(OnItemClickListener<GroupMember> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    /**
     * Sets the error callback for handling errors in the group members list.
     *
     * @param onError The error callback to set.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets the function to customize the subtitle view for each group member item.
     *
     * @param subtitle The function to set for customizing the subtitle view.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setSubtitle(Function3<Context, GroupMember, Group, View> subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * Sets the function to customize the tail view for each group member item.
     *
     * @param tail The function to set for customizing the tail view.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setTail(Function3<Context, GroupMember, Group, View> tail) {
        this.tail = tail;
        return this;
    }

    /**
     * Sets the function to provide a custom view for each group member item.
     *
     * @param customView The function to set for providing a custom view.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setCustomView(Function3<Context, GroupMember, Group, View> customView) {
        this.customView = customView;
        return this;
    }

    /**
     * Sets the function to provide options for each group member item.
     *
     * @param options The function to set for providing options.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setOptions(Function3<Context, GroupMember, Group, List<CometChatOption>> options) {
        this.options = options;
        return this;
    }

    /**
     * Sets whether to disable users' presence indicators in the group members list.
     *
     * @param disableUsersPresence Whether to disable users' presence indicators.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setDisableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
        return this;
    }

    /**
     * Sets the menu view for the group members list.
     *
     * @param menu The menu view to set.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }

    /**
     * Sets whether to hide the separator lines between group member items in the list.
     *
     * @param hideSeparator Whether to hide the separator lines.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setHideSeparator(boolean hideSeparator) {
        this.hideSeparator = hideSeparator;
        return this;
    }

    /**
     * Sets the placeholder text for the search box in the group members list.
     *
     * @param searchPlaceholderText The placeholder text to set.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setSearchPlaceholderText(String searchPlaceholderText) {
        this.searchPlaceholderText = searchPlaceholderText;
        return this;
    }

    /**
     * Sets the text to display in the empty state view of the group members list.
     *
     * @param emptyStateText The text to set for the empty state.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        return this;
    }

    /**
     * Sets the text to display in the error state view of the group members list.
     *
     * @param errorStateText The text to set for the error state.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setErrorStateText(String errorStateText) {
        this.errorStateText = errorStateText;
        return this;
    }

    /**
     * Sets the icon resource for the back button in the group members list.
     *
     * @param backButtonIcon The icon resource to set for the back button.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setBackButtonIcon(int backButtonIcon) {
        this.backButtonIcon = backButtonIcon;
        return this;
    }

    /**
     * Sets whether to show the back button in the group members list.
     *
     * @param showBackButton Whether to show the back button.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setShowBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }

    /**
     * Sets the selection mode for the group members list.
     *
     * @param selectionMode The selection mode to set.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

    /**
     * Sets the selection callback for handling selection events in the group members list.
     *
     * @param onSelection The selection callback to set.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setOnSelection(CometChatGroupMembers.OnSelection onSelection) {
        this.onSelection = onSelection;
        return this;
    }

    /**
     * Sets the icon resource for the search box in the group members list.
     *
     * @param searchBoxIcon The icon resource to set for the search box.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setSearchBoxIcon(int searchBoxIcon) {
        this.searchBoxIcon = searchBoxIcon;
        return this;
    }

    /**
     * Sets whether to hide the search box in the group members list.
     *
     * @param hideSearch Whether to hide the search box.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setHideSearch(boolean hideSearch) {
        this.hideSearch = hideSearch;
        return this;
    }

    /**
     * Sets the title for the group members list.
     *
     * @param title The title to set.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the layout resource for the empty state view in the group members list.
     *
     * @param emptyStateView The layout resource to set for the empty state view.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setEmptyStateView(int emptyStateView) {
        this.emptyStateView = emptyStateView;
        return this;
    }

    /**
     * Sets the layout resource for the error state view in the group members list.
     *
     * @param errorStateView The layout resource to set for the error state view.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setErrorStateView(int errorStateView) {
        this.errorStateView = errorStateView;
        return this;
    }

    /**
     * Sets the layout resource for the loading state view in the group members list.
     *
     * @param loadingStateView The layout resource to set for the loading state view.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setLoadingStateView(int loadingStateView) {
        this.loadingStateView = loadingStateView;
        return this;
    }

    /**
     * Sets the request builder for retrieving group members in the group members list.
     *
     * @param membersRequestBuilder The request builder to set for retrieving group members.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setMembersRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder membersRequestBuilder) {
        this.membersRequestBuilder = membersRequestBuilder;
        return this;
    }

    /**
     * Sets the request builder for searching group members in the group members list.
     *
     * @param membersSearchRequestBuilder The request builder to set for searching group members.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setMembersSearchRequestBuilder(GroupMembersRequest.GroupMembersRequestBuilder membersSearchRequestBuilder) {
        this.membersSearchRequestBuilder = membersSearchRequestBuilder;
        return this;
    }

    /**
     * Sets the style configuration for the avatars in the group members list.
     *
     * @param avatarStyle The style configuration to set for the avatars.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    /**
     * Sets the style configuration for the status indicators in the group members list.
     *
     * @param statusIndicatorStyle The style configuration to set for the status indicators.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        this.statusIndicatorStyle = statusIndicatorStyle;
        return this;
    }

    /**
     * Sets the style configuration for the list items in the group members list.
     *
     * @param listItemStyle The style configuration to set for the list items.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    /**
     * Sets the style configuration for the group members list.
     *
     * @param style The style configuration to set for the group members list.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setStyle(GroupMembersStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the icon resource for the submit action in the group members list.
     *
     * @param submitIcon The icon resource to set for the submit action.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setSubmitIcon(int submitIcon) {
        this.submitIcon = submitIcon;
        return this;
    }

    /**
     * Sets the icon resource for the selection action in the group members list.
     *
     * @param selectionIcon The icon resource to set for the selection action.
     * @return The updated GroupMembersConfiguration instance.
     */
    public GroupMembersConfiguration setSelectionIcon(int selectionIcon) {
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

    public CometChatGroupMembers.OnSelection getOnSelection() {
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

    public OnItemClickListener<GroupMember> getOnItemClickListener() {
        return onItemClickListener;
    }

    public OnError getOnError() {
        return onError;
    }

    public GroupMembersRequest.GroupMembersRequestBuilder getMembersRequestBuilder() {
        return membersRequestBuilder;
    }

    public GroupMembersRequest.GroupMembersRequestBuilder getMembersSearchRequestBuilder() {
        return membersSearchRequestBuilder;
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

    public GroupMembersStyle getStyle() {
        return style;
    }

    public int getSubmitIcon() {
        return submitIcon;
    }

    public int getSelectionIcon() {
        return selectionIcon;
    }
}
