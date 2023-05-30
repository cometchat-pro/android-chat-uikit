package com.cometchat.chatuikit.users;

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
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.models.User;

import java.util.List;

/**
 * UsersConfiguration is a class that provides configuration options for customizing the behavior and appearance of a user component related to user management.
 * It allows setting various properties such as icons, text, styles, and callbacks related to user listing and selection.
 */
public class UsersConfiguration {
    Function2<Context, User, View> subtitle;
    private boolean disableUsersPresence;
    private Function2<Context, User, View> listItemView;
    private View menu;
    private Function2<Context, User, List<CometChatOption>> options;
    private boolean hideSeparator;
    private String searchPlaceholderText, emptyStateText, errorStateText;
    private @DrawableRes
    int backButtonIcon;
    private boolean showBackButton;
    private UIKitConstants.SelectionMode selectionMode;
    private CometChatUsers.OnSelection onSelection;
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
    private UsersRequest.UsersRequestBuilder usersRequestBuilder;
    private UsersRequest.UsersRequestBuilder usersSearchRequestBuilder;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private ListItemStyle listItemStyle;
    private UsersStyle style;
    private @DrawableRes
    int submitIcon, selectionIcon;
    private OnItemClickListener<User> onItemClickListener;
    private OnError onError;

    /**
     * Sets the submit icon resource for the user component.
     *
     * @param submitIcon The resource ID of the submit icon.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setSubmitIcon(int submitIcon) {
        this.submitIcon = submitIcon;
        return this;
    }

    /**
     * Sets the selection icon resource for the user component.
     *
     * @param selectionIcon The resource ID of the selection icon.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setSelectionIcon(int selectionIcon) {
        this.selectionIcon = selectionIcon;
        return this;
    }

    /**
     * Sets the item click listener for handling user item clicks in the user component.
     *
     * @param onItemClickListener The item click listener to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setOnItemClickListener(OnItemClickListener<User> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    /**
     * Sets the error callback for handling errors in the user component.
     *
     * @param onError The error callback to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets the text to be displayed when the user list is empty.
     *
     * @param emptyStateText The text to be displayed for the empty state.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        return this;
    }

    /**
     * Sets the text to be displayed when an error occurs in the user component.
     *
     * @param errorStateText The text to be displayed for the error state.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setErrorStateText(String errorStateText) {
        this.errorStateText = errorStateText;
        return this;
    }

    /**
     * Sets the avatar style for the user component.
     *
     * @param avatarStyle The avatar style to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    /**
     * Sets the status indicator style for the user component.
     *
     * @param statusIndicatorStyle The status indicator style to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        this.statusIndicatorStyle = statusIndicatorStyle;
        return this;
    }

    /**
     * Sets the list item style for the user component.
     *
     * @param listItemStyle The list item style to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    /**
     * Sets the style for the user component.
     *
     * @param style The style to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setStyle(UsersStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the subtitle function for generating a custom subtitle view for user items in the user component.
     *
     * @param subtitle The subtitle function to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setSubtitle(Function2<Context, User, View> subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * Disables the presence feature for users in the user component.
     *
     * @param disableUsersPresence Whether to disable users' presence.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration disableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
        return this;
    }

    /**
     * Sets the custom list item view for user items in the user component.
     *
     * @param listItemView The custom list item view to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setListItemView(Function2<Context, User, View> listItemView) {
        this.listItemView = listItemView;
        return this;
    }

    /**
     * Sets the custom menu view for the user component.
     *
     * @param menu The custom menu view to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }

    /**
     * Sets the options function for generating a list of custom options for user items in the user component.
     *
     * @param options The options function to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setOptions(Function2<Context, User, List<CometChatOption>> options) {
        this.options = options;
        return this;
    }

    /**
     * Hides or shows the separator between user items in the user component.
     *
     * @param hideSeparator Whether to hide the separator.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration hideSeparator(boolean hideSeparator) {
        this.hideSeparator = hideSeparator;
        return this;
    }

    /**
     * Sets the placeholder text for the search input field in the user component.
     *
     * @param searchPlaceholderText The placeholder text to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setSearchPlaceholderText(String searchPlaceholderText) {
        this.searchPlaceholderText = searchPlaceholderText;
        return this;
    }

    /**
     * Sets the icon resource for the back button in the user component.
     *
     * @param backButtonIcon The resource ID of the back button icon.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setBackButtonIcon(int backButtonIcon) {
        this.backButtonIcon = backButtonIcon;
        return this;
    }

    /**
     * Shows or hides the back button in the user component.
     *
     * @param showBackButton Whether to show the back button.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration showBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }

    /**
     * Sets the selection mode for the user component.
     *
     * @param selectionMode The selection mode to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

    /**
     * Sets the selection callback for handling user selection in the user component.
     *
     * @param onSelection The selection callback to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setOnSelection(CometChatUsers.OnSelection onSelection) {
        this.onSelection = onSelection;
        return this;
    }

    /**
     * Sets the icon resource for the search box in the user component.
     *
     * @param searchBoxIcon The resource ID of the search box icon.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setSearchBoxIcon(int searchBoxIcon) {
        this.searchBoxIcon = searchBoxIcon;
        return this;
    }

    /**
     * Hides or shows the search input field in the user component.
     *
     * @param hideSearch Whether to hide the search input field.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration hideSearch(boolean hideSearch) {
        this.hideSearch = hideSearch;
        return this;
    }

    /**
     * Sets the title for the user component.
     *
     * @param title The title to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the layout resource for the empty state view in the user component.
     *
     * @param emptyStateView The layout resource ID of the empty state view.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setEmptyStateView(int emptyStateView) {
        this.emptyStateView = emptyStateView;
        return this;
    }

    /**
     * Sets the layout resource for the error state view in the user component.
     *
     * @param errorStateView The layout resource ID of the error state view.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setErrorStateView(int errorStateView) {
        this.errorStateView = errorStateView;
        return this;
    }

    /**
     * Sets the layout resource for the loading state view in the user component.
     *
     * @param loadingStateView The layout resource ID of the loading state view.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setLoadingStateView(int loadingStateView) {
        this.loadingStateView = loadingStateView;
        return this;
    }

    /**
     * Sets the users request builder for fetching users in the user component.
     *
     * @param usersRequestBuilder The users request builder to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setUsersRequestBuilder(UsersRequest.UsersRequestBuilder usersRequestBuilder) {
        this.usersRequestBuilder = usersRequestBuilder;
        return this;
    }

    /**
     * Sets the users search request builder for searching users in the user component.
     *
     * @param usersSearchRequestBuilder The users search request builder to be set.
     * @return The UsersConfiguration instance for method chaining.
     */
    public UsersConfiguration setUsersSearchRequestBuilder(UsersRequest.UsersRequestBuilder usersSearchRequestBuilder) {
        this.usersSearchRequestBuilder = usersSearchRequestBuilder;
        return this;
    }

    public Function2<Context, User, View> getSubtitle() {
        return subtitle;
    }

    public boolean isDisableUsersPresence() {
        return disableUsersPresence;
    }

    public Function2<Context, User, View> getListItemView() {
        return listItemView;
    }

    public View getMenu() {
        return menu;
    }

    public Function2<Context, User, List<CometChatOption>> getOptions() {
        return options;
    }

    public boolean isHideSeparator() {
        return hideSeparator;
    }

    public String getSearchPlaceholderText() {
        return searchPlaceholderText;
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

    public CometChatUsers.OnSelection getOnSelection() {
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

    public UsersRequest.UsersRequestBuilder getUsersRequestBuilder() {
        return usersRequestBuilder;
    }

    public UsersRequest.UsersRequestBuilder getUsersSearchRequestBuilder() {
        return usersSearchRequestBuilder;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public OnError getOnError() {
        return onError;
    }

    public int getSubmitIcon() {
        return submitIcon;
    }

    public int getSelectionIcon() {
        return selectionIcon;
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

    public UsersStyle getStyle() {
        return style;
    }

    public String getEmptyStateText() {
        return emptyStateText;
    }

    public String getErrorStateText() {
        return errorStateText;
    }
}
