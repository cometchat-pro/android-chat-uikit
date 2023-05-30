package com.cometchat.chatuikit.messagelist;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;

import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.MessageBubbleStyle;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.ActionSheetStyle;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.models.BaseMessage;

import java.util.List;

public class MessageListConfiguration {

    private String emptyStateText;
    private String errorStateText;
    private @LayoutRes
    int loadingStateView;
    private @LayoutRes
    int emptyStateView;
    private @LayoutRes
    int errorStateView;
    private boolean disableReceipt;
    private @DrawableRes
    int readIcon;
    private @DrawableRes
    int deliverIcon;
    private @DrawableRes
    int sentIcon;
    private @DrawableRes
    int waitIconIcon;
    private UIKitConstants.MessageListAlignment listAlignment = UIKitConstants.MessageListAlignment.STANDARD;
    private boolean showAvatar;
    private Function1<BaseMessage, String> datePattern;
    private UIKitConstants.TimeStampAlignment timeStampAlignment = UIKitConstants.TimeStampAlignment.BOTTOM;
    private List<CometChatMessageTemplate> messageTemplates;
    private MessagesRequest.MessagesRequestBuilder messagesRequestBuilder;
    private String newMessageIndicatorText;
    private boolean scrollToBottomOnNewMessages;
    private CometChatMessageList.ThreadReplyClick onThreadRepliesClick;
    private View headerView;
    private View footerView;
    private MessageBubbleStyle wrapperMessageBubbleStyle;
    private AvatarStyle avatarStyle;
    private DateStyle dateSeparatorStyle;
    private ActionSheetStyle actionSheetStyle;
    private MessageListStyle messageListStyle;

    /**
     * Sets the text to be displayed in the empty state of the message list.
     *
     * @param emptyStateText The text to be displayed in the empty state.
     */
    public MessageListConfiguration setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        return this;
    }

    /**
     * Sets the text for the error state of the message list.
     *
     * @param errorStateText The text to be displayed in the error state.
     */
    public MessageListConfiguration setErrorStateText(String errorStateText) {
        this.errorStateText = errorStateText;
        return this;
    }

    /**
     * Sets the layout resource for the loading state view.
     *
     * @param loadingStateView The resource ID of the layout for the loading state view.
     */
    public MessageListConfiguration setLoadingStateView(int loadingStateView) {
        this.loadingStateView = loadingStateView;
        return this;
    }

    /**
     * Sets the layout resource for the empty state view.
     *
     * @param emptyStateView The resource ID of the layout for the empty state view.
     */
    public MessageListConfiguration setEmptyStateView(int emptyStateView) {
        this.emptyStateView = emptyStateView;
        return this;
    }

    /**
     * Sets the layout resource for the error state view.
     *
     * @param errorStateView The resource ID of the layout for the error state view.
     */
    public MessageListConfiguration setErrorStateView(int errorStateView) {
        this.errorStateView = errorStateView;
        return this;
    }

    /**
     * Disables read receipt for messages in the message list.
     *
     * @param disableReceipt True to disable read receipt, false to enable read receipt.
     */
    public MessageListConfiguration disableReceipt(boolean disableReceipt) {
        this.disableReceipt = disableReceipt;
        return this;
    }

    /**
     * Sets the icon resource for the read receipt in the message list.
     *
     * @param readIcon The resource ID of the read receipt icon.
     */
    public MessageListConfiguration setReadIcon(int readIcon) {
        this.readIcon = readIcon;
        return this;
    }

    /**
     * Sets the icon resource for the deliver receipt in the message list.
     *
     * @param deliverIcon The resource ID of the deliver receipt icon.
     */
    public MessageListConfiguration setDeliverIcon(int deliverIcon) {
        this.deliverIcon = deliverIcon;
        return this;
    }

    /**
     * Sets the icon resource for the sent receipt in the message list.
     *
     * @param sentIcon The resource ID of the sent receipt icon.
     */
    public MessageListConfiguration setSentIcon(int sentIcon) {
        this.sentIcon = sentIcon;
        return this;
    }

    /**
     * Sets the icon resource for the waiting receipt in the message list.
     *
     * @param waitIconIcon The resource ID of the waiting receipt icon.
     */
    public MessageListConfiguration setWaitIconIcon(int waitIconIcon) {
        this.waitIconIcon = waitIconIcon;
        return this;
    }

    /**
     * Sets the alignment of messages in the message list.
     *
     * @param listAlignment The MessageListAlignment enum representing the alignment.
     */
    public MessageListConfiguration setListAlignment(UIKitConstants.MessageListAlignment listAlignment) {
        this.listAlignment = listAlignment;
        return this;
    }

    /**
     * Shows or hides the avatar in the message list.
     *
     * @param showAvatar True to show the avatar, false to hide the avatar.
     */
    public MessageListConfiguration showAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
        return this;
    }

    /**
     * Sets the date pattern for displaying message dates in the message list.
     *
     * @param datePattern The Function1 object representing the date pattern.
     */
    public MessageListConfiguration setDatePattern(Function1<BaseMessage, String> datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    /**
     * Sets the alignment of the timestamp in messages in the message list.
     *
     * @param timeStampAlignment The TimeStampAlignment enum representing the timestamp alignment.
     */
    public MessageListConfiguration setTimeStampAlignment(UIKitConstants.TimeStampAlignment timeStampAlignment) {
        this.timeStampAlignment = timeStampAlignment;
        return this;
    }

    /**
     * Sets the message templates for the message list.
     *
     * @param messageTemplates The list of CometChatMessageTemplate objects representing the message templates. Fetches the message filter based on the message templates.
     *                                  This method is called internally to update the message filter.
     *                                  You do not need to call this method directly.
     */
    public MessageListConfiguration setTemplates(List<CometChatMessageTemplate> messageTemplates) {
        this.messageTemplates = messageTemplates;
        return this;
    }

    /**
     * Sets the MessagesRequestBuilder for fetching messages in the message list.
     *
     * @param messagesRequestBuilder The MessagesRequestBuilder to set.
     */
    public MessageListConfiguration setMessagesRequestBuilder(MessagesRequest.MessagesRequestBuilder messagesRequestBuilder) {
        this.messagesRequestBuilder = messagesRequestBuilder;
        return this;
    }

    /**
     * Sets the text for the new message indicator.
     *
     * @param newMessageIndicatorText The text to be displayed as the new message indicator.
     */
    public MessageListConfiguration setNewMessageIndicatorText(String newMessageIndicatorText) {
        this.newMessageIndicatorText = newMessageIndicatorText;
        return this;
    }

    /**
     * Sets whether to scroll to the bottom of the message list automatically on new messages.
     *
     * @param scrollToBottomOnNewMessages {@code true} to enable auto-scrolling to the bottom on new messages, {@code false} otherwise.
     */
    public MessageListConfiguration scrollToBottomOnNewMessages(boolean scrollToBottomOnNewMessages) {
        this.scrollToBottomOnNewMessages = scrollToBottomOnNewMessages;
        return this;
    }

    /**
     * Sets the listener for handling thread reply clicks in the message list.
     *
     * @param onThreadRepliesClick The ThreadReplyClick object representing the listener.
     */
    public MessageListConfiguration setOnThreadRepliesClick(CometChatMessageList.ThreadReplyClick onThreadRepliesClick) {
        this.onThreadRepliesClick = onThreadRepliesClick;
        return this;
    }

    /**
     * Sets a custom header view for the message list.
     *
     * @param headerView The View object representing the custom header view.
     */
    public MessageListConfiguration setHeaderView(View headerView) {
        this.headerView = headerView;
        return this;
    }

    /**
     * Sets a custom footer view for the message list.
     *
     * @param footerView The View object representing the custom footer view.
     */
    public MessageListConfiguration setFooterView(View footerView) {
        this.footerView = footerView;
        return this;
    }

    /**
     * Sets the message bubble style for the wrapper message in the message list.
     *
     * @param wrapperMessageBubbleStyle The MessageBubbleStyle object defining the style of the message bubble.
     */
    public MessageListConfiguration setWrapperMessageBubbleStyle(MessageBubbleStyle wrapperMessageBubbleStyle) {
        this.wrapperMessageBubbleStyle = wrapperMessageBubbleStyle;
        return this;
    }

    /**
     * Sets the avatar style for the message list.
     *
     * @param avatarStyle The AvatarStyle object defining the style of avatars.
     */
    public MessageListConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    /**
     * Sets the date separator style for the message list.
     *
     * @param dateSeparatorStyle The DateStyle object defining the style of date separators.
     */
    public MessageListConfiguration setDateSeparatorStyle(DateStyle dateSeparatorStyle) {
        this.dateSeparatorStyle = dateSeparatorStyle;
        return this;
    }

    /**
     * Sets the action sheet style for the message list.
     *
     * @param actionSheetStyle The ActionSheetStyle object defining the style of the action sheet.
     */
    public MessageListConfiguration setActionSheetStyle(ActionSheetStyle actionSheetStyle) {
        this.actionSheetStyle = actionSheetStyle;
        return this;
    }

    /**
     * Sets the style for the message list.
     *
     * @param messageListStyle The style to apply to the message list.
     */
    public MessageListConfiguration setStyle(MessageListStyle messageListStyle) {
        this.messageListStyle = messageListStyle;
        return this;
    }

    public String getEmptyStateText() {
        return emptyStateText;
    }

    public String getErrorStateText() {
        return errorStateText;
    }

    public int getLoadingStateView() {
        return loadingStateView;
    }

    public int getEmptyStateView() {
        return emptyStateView;
    }

    public int getErrorStateView() {
        return errorStateView;
    }

    public boolean isDisableReceipt() {
        return disableReceipt;
    }

    public int getReadIcon() {
        return readIcon;
    }

    public int getDeliverIcon() {
        return deliverIcon;
    }

    public int getSentIcon() {
        return sentIcon;
    }

    public int getWaitIconIcon() {
        return waitIconIcon;
    }

    public UIKitConstants.MessageListAlignment getListAlignment() {
        return listAlignment;
    }

    public boolean isShowAvatar() {
        return showAvatar;
    }

    public Function1<BaseMessage, String> getDatePattern() {
        return datePattern;
    }

    public UIKitConstants.TimeStampAlignment getTimeStampAlignment() {
        return timeStampAlignment;
    }

    public List<CometChatMessageTemplate> getMessageTemplates() {
        return messageTemplates;
    }

    public MessagesRequest.MessagesRequestBuilder getMessagesRequestBuilder() {
        return messagesRequestBuilder;
    }

    public String getNewMessageIndicatorText() {
        return newMessageIndicatorText;
    }

    public boolean isScrollToBottomOnNewMessages() {
        return scrollToBottomOnNewMessages;
    }

    public CometChatMessageList.ThreadReplyClick getOnThreadRepliesClick() {
        return onThreadRepliesClick;
    }

    public View getHeaderView() {
        return headerView;
    }

    public View getFooterView() {
        return footerView;
    }

    public MessageBubbleStyle getWrapperMessageBubbleStyle() {
        return wrapperMessageBubbleStyle;
    }

    public AvatarStyle getAvatarStyle() {
        return avatarStyle;
    }

    public DateStyle getDateSeparatorStyle() {
        return dateSeparatorStyle;
    }

    public ActionSheetStyle getActionSheetStyle() {
        return actionSheetStyle;
    }

    public MessageListStyle getMessageListStyle() {
        return messageListStyle;
    }
}
