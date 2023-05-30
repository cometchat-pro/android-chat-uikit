package com.cometchat.chatuikit.conversations;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.RawRes;

import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.Interfaces.Function2;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatOption;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatBadge.BadgeStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.models.Conversation;

import java.util.List;

/**
 * The configuration class for customizing Conversations view.
 */
public class ConversationsConfiguration {

    private boolean disableUsersPresence;
    private boolean disableReadReceipt;
    private boolean disableTyping;
    private boolean disableSoundForMessages;
    private String emptyStateText, errorStateText;
    private @RawRes
    int customSoundForMessage = 0;
    private @DrawableRes
    int protectedGroupIcon;
    private @DrawableRes
    int privateGroupIcon;
    private @DrawableRes
    int readIcon;
    private @DrawableRes
    int deliveredIcon;
    private @DrawableRes
    int sentIcon;
    private Function1<Conversation, String> datePattern;
    private Function2<Context, Conversation, View> subtitle, tail, customView;
    private View menu;
    private Function2<Context, Conversation, List<CometChatOption>> options;
    private boolean hideSeparator;
    private @DrawableRes
    int backButtonIcon;
    private boolean showBackButton;
    private UIKitConstants.SelectionMode selectionMode;
    private CometChatConversations.OnSelection onSelection;
    private String title;
    private @LayoutRes
    int emptyStateView;
    private @LayoutRes
    int errorStateView;
    private @LayoutRes
    int loadingStateView;
    private ConversationsRequest conversationsRequest;
    private AvatarStyle avatarStyle;
    private StatusIndicatorStyle statusIndicatorStyle;
    private DateStyle dateStyle;
    private ListItemStyle listItemStyle;
    private BadgeStyle badgeStyle;
    private ConversationsStyle style;
    private OnItemClickListener<Conversation> itemClickListener;
    private OnError onError;

    /**
     * Sets the click event listener for conversation items.
     *
     * @param itemClickListener The OnItemClickListener instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setItemClickListener(OnItemClickListener<Conversation> itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    /**
     * Sets the error event listener for handling errors.
     *
     * @param onError The OnError instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets the avatar style for conversation avatars.
     *
     * @param avatarStyle The AvatarStyle instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    /**
     * Sets the text to be displayed when the conversation list is empty.
     *
     * @param emptyStateText The text for the empty state.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        return this;
    }

    /**
     * Sets the text to be displayed when an error occurs while fetching conversations.
     *
     * @param errorStateText The text for the error state.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setErrorStateText(String errorStateText) {
        this.errorStateText = errorStateText;
        return this;
    }

    /**
     * Sets the style for status indicators of conversations.
     *
     * @param statusIndicatorStyle The StatusIndicatorStyle instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setStatusIndicatorStyle(StatusIndicatorStyle statusIndicatorStyle) {
        this.statusIndicatorStyle = statusIndicatorStyle;
        return this;
    }

    /**
     * Sets the style for displaying dates in conversations.
     *
     * @param dateStyle The DateStyle instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setDateStyle(DateStyle dateStyle) {
        this.dateStyle = dateStyle;
        return this;
    }

    /**
     * Sets the style for conversation list items.
     *
     * @param listItemStyle The ListItemStyle instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    /**
     * Sets the style for conversation badges.
     *
     * @param badgeStyle The BadgeStyle instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setBadgeStyle(BadgeStyle badgeStyle) {
        this.badgeStyle = badgeStyle;
        return this;
    }

    /**
     * Sets the custom style for Conversations view.
     *
     * @param style The ConversationsStyle instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setStyle(ConversationsStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Disables the presence indicator for users in conversations.
     *
     * @param disableUsersPresence True to disable presence indicator, false otherwise.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration disableUsersPresence(boolean disableUsersPresence) {
        this.disableUsersPresence = disableUsersPresence;
        return this;
    }

    /**
     * Disables the read receipt indicator for messages in conversations.
     *
     * @param disableReadReceipt True to disable read receipt indicator, false otherwise.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration disableReceipt(boolean disableReadReceipt) {
        this.disableReadReceipt = disableReadReceipt;
        return this;
    }

    /**
     * Disables the typing indicator for conversations.
     *
     * @param disableTyping True to disable typing indicator, false otherwise.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration disableTyping(boolean disableTyping) {
        this.disableTyping = disableTyping;
        return this;
    }

    /**
     * Disables the sound for incoming messages in conversations.
     *
     * @param disableSoundForMessages True to disable message sound, false otherwise.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration disableSoundForMessages(boolean disableSoundForMessages) {
        this.disableSoundForMessages = disableSoundForMessages;
        return this;
    }

    /**
     * Sets a custom sound resource for incoming messages in conversations.
     *
     * @param customSoundForMessage The custom sound resource ID.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setCustomSoundForMessage(int customSoundForMessage) {
        this.customSoundForMessage = customSoundForMessage;
        return this;
    }

    /**
     * Sets the icon resource for protected group conversations.
     *
     * @param protectedGroupIcon The protected group icon resource ID.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setProtectedGroupIcon(int protectedGroupIcon) {
        this.protectedGroupIcon = protectedGroupIcon;
        return this;
    }

    /**
     * Sets the icon resource for private group conversations.
     *
     * @param privateGroupIcon The private group icon resource ID.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setPrivateGroupIcon(int privateGroupIcon) {
        this.privateGroupIcon = privateGroupIcon;
        return this;
    }

    /**
     * Sets the icon resource for read status indicator in conversations.
     *
     * @param readIcon The read status icon resource ID.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setReadIcon(@DrawableRes int readIcon) {
        this.readIcon = readIcon;
        return this;
    }

    /**
     * Sets the icon resource for delivered status indicator in conversations.
     *
     * @param deliveredIcon The delivered status icon resource ID.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setDeliveredIcon(@DrawableRes int deliveredIcon) {
        this.deliveredIcon = deliveredIcon;
        return this;
    }

    /**
     * Sets the icon resource for sent status indicator in conversations.
     *
     * @param sentIcon The sent status icon resource ID.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setSentIcon(@DrawableRes int sentIcon) {
        this.sentIcon = sentIcon;
        return this;
    }

    /**
     * Sets the date pattern for displaying dates in conversations.
     *
     * @param datePattern The Function1 instance that provides the date pattern.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setDatePattern(Function1<Conversation, String> datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    /**
     * Sets the subtitle view for conversation items.
     *
     * @param subtitle The Function2 instance that provides the subtitle view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setSubtitle(Function2<Context, Conversation, View> subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * Sets the tail view for conversation items.
     *
     * @param tail The Function2 instance that provides the tail view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setTail(Function2<Context, Conversation, View> tail) {
        this.tail = tail;
        return this;
    }

    /**
     * Sets the custom view for conversation items.
     *
     * @param customView The Function2 instance that provides the custom view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setCustomView(Function2<Context, Conversation, View> customView) {
        this.customView = customView;
        return this;
    }

    /**
     * Sets the menu view for conversation items.
     *
     * @param menu The menu view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }

    /**
     * Sets the options for conversation items.
     *
     * @param options The Function2 instance that provides the options.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setOptions(Function2<Context, Conversation, List<CometChatOption>> options) {
        this.options = options;
        return this;
    }

    /**
     * Hides the separator between conversation items.
     *
     * @param hideSeparator True to hide the separator, false otherwise.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration hideSeparator(boolean hideSeparator) {
        this.hideSeparator = hideSeparator;
        return this;
    }

    /**
     * Sets the icon resource for the back button in the toolbar.
     *
     * @param backButtonIcon The back button icon resource ID.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setBackButtonIcon(int backButtonIcon) {
        this.backButtonIcon = backButtonIcon;
        return this;
    }

    /**
     * Sets whether to show the back button in the toolbar.
     *
     * @param showBackButton True to show the back button, false otherwise.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration showBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }

    /**
     * Sets the selection mode for conversations.
     *
     * @param selectionMode The selection mode.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

    /**
     * Sets the event listener for conversation selection.
     *
     * @param onSelection The OnSelection instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setOnSelection(CometChatConversations.OnSelection onSelection) {
        this.onSelection = onSelection;
        return this;
    }

    /**
     * Sets the title for the Conversations view.
     *
     * @param title The title for the Conversations view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the custom view for the empty state.
     *
     * @param emptyStateView The empty state view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setEmptyStateView(int emptyStateView) {
        this.emptyStateView = emptyStateView;
        return this;
    }

    /**
     * Sets the custom view for the error state.
     *
     * @param errorStateView The error state view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setErrorStateView(int errorStateView) {
        this.errorStateView = errorStateView;
        return this;
    }

    /**
     * Sets the custom view for the loading state.
     *
     * @param loadingStateView The loading state view.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setLoadingStateView(int loadingStateView) {
        this.loadingStateView = loadingStateView;
        return this;
    }

    /**
     * Sets the ConversationsRequest instance to fetch conversations.
     *
     * @param conversationsRequest The ConversationsRequest instance.
     * @return The ConversationsConfiguration instance.
     */
    public ConversationsConfiguration setConversationsRequest(ConversationsRequest conversationsRequest) {
        this.conversationsRequest = conversationsRequest;
        return this;
    }

    public boolean isDisableUsersPresence() {
        return disableUsersPresence;
    }

    public boolean isDisableReadReceipt() {
        return disableReadReceipt;
    }

    public boolean isDisableTyping() {
        return disableTyping;
    }

    public boolean isDisableSoundForMessages() {
        return disableSoundForMessages;
    }

    public int getCustomSoundForMessage() {
        return customSoundForMessage;
    }

    public int getProtectedGroupIcon() {
        return protectedGroupIcon;
    }

    public int getPrivateGroupIcon() {
        return privateGroupIcon;
    }

    public @DrawableRes
    int getReadIcon() {
        return readIcon;
    }

    public @DrawableRes
    int getDeliveredIcon() {
        return deliveredIcon;
    }

    public @DrawableRes
    int getSentIcon() {
        return sentIcon;
    }

    public Function1<Conversation, String> getDatePattern() {
        return datePattern;
    }

    public Function2<Context, Conversation, View> getSubtitle() {
        return subtitle;
    }

    public Function2<Context, Conversation, View> getTail() {
        return tail;
    }

    public Function2<Context, Conversation, View> getCustomView() {
        return customView;
    }

    public View getMenu() {
        return menu;
    }

    public Function2<Context, Conversation, List<CometChatOption>> getOptions() {
        return options;
    }

    public boolean isHideSeparator() {
        return hideSeparator;
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

    public CometChatConversations.OnSelection getOnSelection() {
        return onSelection;
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

    public ConversationsRequest getConversationsRequest() {
        return conversationsRequest;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public StatusIndicatorStyle getStatusIndicatorStyle() {
        return statusIndicatorStyle;
    }

    public DateStyle getDateStyle() {
        return dateStyle;
    }

    public ListItemStyle getListItemStyle() {
        return listItemStyle;
    }

    public BadgeStyle getBadgeStyle() {
        return badgeStyle;
    }

    public ConversationsStyle getStyle() {
        return style;
    }

    public String getEmptyStateText() {
        return emptyStateText;
    }

    public String getErrorStateText() {
        return errorStateText;
    }

    public OnItemClickListener<Conversation> getItemClickListener() {
        return itemClickListener;
    }

    public OnError getOnError() {
        return onError;
    }
}
