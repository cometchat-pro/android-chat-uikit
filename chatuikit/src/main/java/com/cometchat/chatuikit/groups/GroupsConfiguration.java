package com.cometchat.chatuikit.groups;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;

import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.models.Group;

import java.util.List;
/**
 * This class represents the configuration options for the groups feature in an application.
 * It provides methods to set various customization options for the groups UI and behavior.
 */
public class GroupsConfiguration {
    private Function2<Context, Group, View> subtitle, tail, customView;
    private View menu;
    private Function2<Context, Group, List<CometChatOption>> options;
    private boolean hideSeparator;
    private GroupsStyle style;
    private String searchPlaceholderText, emptyStateText, errorStateText;
    private @DrawableRes
    int backButtonIcon, privateGroupIcon, passwordGroupIcon;
    private boolean showBackButton;
    private UIKitConstants.SelectionMode selectionMode;
    private CometChatGroups.OnSelection onSelection;
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
    private GroupsRequest.GroupsRequestBuilder groupsRequestBuilder;
    private GroupsRequest.GroupsRequestBuilder groupsSearchRequestBuilder;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private ListItemStyle listItemStyle;
    private OnItemClickListener<Group> itemClickListener;
    private OnError onError;
    /**
     * Sets the item click listener for handling group item clicks.
     *
     * @param itemClickListener The listener to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setItemClickListener(OnItemClickListener<Group> itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }
    /**
     * Sets the error callback for handling errors related to groups.
     *
     * @param onError The error callback to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }
    /**
     * Sets the subtitle view generator for customizing the subtitle view of each group item.
     *
     * @param subtitle The subtitle view generator to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setSubtitle(Function2<Context, Group, View> subtitle) {
        this.subtitle = subtitle;
        return this;
    }
    /**
     * Sets the tail view generator for customizing the tail view of each group item.
     *
     * @param tail The tail view generator to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setTail(Function2<Context, Group, View> tail) {
        this.tail = tail;
        return this;
    }
    /**
     * Sets the custom view generator for creating a custom view for each group item.
     *
     * @param customView The custom view generator to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setCustomView(Function2<Context, Group, View> customView) {
        this.customView = customView;
        return this;
    }
    /**
     * Sets the menu view for each group item.
     *
     * @param menu The menu view to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }
    /**
     * Sets the options generator for providing a list of options for each group item.
     *
     * @param options The options generator to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setOptions(Function2<Context, Group, List<CometChatOption>> options) {
        this.options = options;
        return this;
    }
    /**
     * Sets whether to hide the separator between group items.
     *
     * @param hideSeparator True to hide the separator, false otherwise.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setHideSeparator(boolean hideSeparator) {
        this.hideSeparator = hideSeparator;
        return this;
    }
    /**
     * Sets the style for the groups UI.
     *
     * @param style The style to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setStyle(GroupsStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the placeholder text for the search box.
     *
     * @param searchPlaceholderText The placeholder text to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setSearchPlaceholderText(String searchPlaceholderText) {
        this.searchPlaceholderText = searchPlaceholderText;
        return this;
    }
    /**
     * Sets the text to be displayed when the group list is empty.
     *
     * @param emptyStateText The empty state text to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        return this;
    }
    /**
     * Sets the text to be displayed when an error occurs while loading groups.
     *
     * @param errorStateText The error state text to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setErrorStateText(String errorStateText) {
        this.errorStateText = errorStateText;
        return this;
    }
    /**
     * Sets the icon resource for the back button.
     *
     * @param backButtonIcon The icon resource for the back button.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setBackButtonIcon(int backButtonIcon) {
        this.backButtonIcon = backButtonIcon;
        return this;
    }
    /**
     * Sets the icon resource for private groups.
     *
     * @param privateGroupIcon The icon resource for private groups.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setPrivateGroupIcon(int privateGroupIcon) {
        this.privateGroupIcon = privateGroupIcon;
        return this;
    }
    /**
     * Sets the icon resource for password-protected groups.
     *
     * @param passwordGroupIcon The icon resource for password-protected groups.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setPasswordGroupIcon(int passwordGroupIcon) {
        this.passwordGroupIcon = passwordGroupIcon;
        return this;
    }
    /**
     * Sets whether to show the back button.
     *
     * @param showBackButton True to show the back button, false otherwise.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setShowBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }
    /**
     * Sets the selection mode for group items.
     *
     * @param selectionMode The selection mode to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }
    /**
     * Sets the selection callback for handling group selection.
     *
     * @param onSelection The selection callback to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setOnSelection(CometChatGroups.OnSelection onSelection) {
        this.onSelection = onSelection;
        return this;
    }

    /**
     * Sets the icon resource for the search box.
     *
     * @param searchBoxIcon The icon resource for the search box.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setSearchBoxIcon(int searchBoxIcon) {
        this.searchBoxIcon = searchBoxIcon;
        return this;
    }
    /**
     * Sets whether to hide the search box.
     *
     * @param hideSearch True to hide the search box, false otherwise.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setHideSearch(boolean hideSearch) {
        this.hideSearch = hideSearch;
        return this;
    }
    /**
     * Sets the title of the groups screen.
     *
     * @param title The title to be set.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }
    /**
     * Sets the layout resource for the empty state view.
     *
     * @param emptyStateView The layout resource for the empty state view.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setEmptyStateView(int emptyStateView) {
        this.emptyStateView = emptyStateView;
        return this;
    }
    /**
     * Sets the layout resource for the error state view.
     *
     * @param errorStateView The layout resource for the error state view.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setErrorStateView(int errorStateView) {
        this.errorStateView = errorStateView;
        return this;
    }
    /**
     * Sets the layout resource for the loading state view.
     *
     * @param loadingStateView The layout resource for the loading state view.
     * @return The updated GroupsConfiguration object.
     */
    public GroupsConfiguration setLoadingStateView(int loadingStateView) {
        this.loadingStateView = loadingStateView;
        return this;
    }
    /**
     * Sets the {@link GroupsRequest.GroupsRequestBuilder} for configuring groups request.
     *
     * @param groupsRequestBuilder The {@link GroupsRequest.GroupsRequestBuilder} to set.
     * @return This {@code GroupsConfiguration} instance.
     */
    public GroupsConfiguration setGroupsRequestBuilder(GroupsRequest.GroupsRequestBuilder groupsRequestBuilder) {
        this.groupsRequestBuilder = groupsRequestBuilder;
        return this;
    }
    /**
     * Sets the {@link GroupsRequest.GroupsRequestBuilder} for configuring groups search request.
     *
     * @param groupsSearchRequestBuilder The {@link GroupsRequest.GroupsRequestBuilder} to set for search request.
     * @return This {@code GroupsConfiguration} instance.
     */
    public GroupsConfiguration setGroupsSearchRequestBuilder(GroupsRequest.GroupsRequestBuilder groupsSearchRequestBuilder) {
        this.groupsSearchRequestBuilder = groupsSearchRequestBuilder;
        return this;
    }
    /**
     * Sets the {@link AvatarStyle} for configuring avatar style.
     *
     * @param avatarStyle The {@link AvatarStyle} to set.
     * @return This {@code GroupsConfiguration} instance.
     */
    public GroupsConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }
    /**
     * Sets the {@link StatusIndicatorStyle} for configuring status indicator style.
     *
     * @param statusIndicatorStyle The {@link StatusIndicatorStyle} to set.
     * @return This {@code GroupsConfiguration} instance.
     */
    public GroupsConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        this.statusIndicatorStyle = statusIndicatorStyle;
        return this;
    }

    /**
     * Sets the {@link ListItemStyle} for configuring list item style.
     *
     * @param listItemStyle The {@link ListItemStyle} to set.
     * @return This {@code GroupsConfiguration} instance.
     */
    public GroupsConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    public OnItemClickListener<Group> getItemClickListener() {
        return itemClickListener;
    }

    public OnError getOnError() {
        return onError;
    }

    public Function2<Context, Group, View> getSubtitle() {
        return subtitle;
    }

    public Function2<Context, Group, View> getTail() {
        return tail;
    }

    public Function2<Context, Group, View> getCustomView() {
        return customView;
    }

    public View getMenu() {
        return menu;
    }

    public Function2<Context, Group, List<CometChatOption>> getOptions() {
        return options;
    }

    public boolean isHideSeparator() {
        return hideSeparator;
    }

    public GroupsStyle getStyle() {
        return style;
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

    public int getPrivateGroupIcon() {
        return privateGroupIcon;
    }

    public int getPasswordGroupIcon() {
        return passwordGroupIcon;
    }

    public boolean isShowBackButton() {
        return showBackButton;
    }

    public UIKitConstants.SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public CometChatGroups.OnSelection getOnSelection() {
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

    public GroupsRequest.GroupsRequestBuilder getGroupsRequestBuilder() {
        return groupsRequestBuilder;
    }

    public GroupsRequest.GroupsRequestBuilder getGroupsSearchRequestBuilder() {
        return groupsSearchRequestBuilder;
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
}
