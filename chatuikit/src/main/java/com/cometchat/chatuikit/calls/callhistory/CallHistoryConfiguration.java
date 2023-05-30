package com.cometchat.chatuikit.calls.callhistory;

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
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatListItem.ListItemStyle;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.models.BaseMessage;

import java.util.List;

 class CallHistoryConfiguration {
    Function2<Context, BaseMessage, View> subtitle;
    private Function2<Context, BaseMessage, View> listItemView;
    private View menu;
    private Function2<Context, BaseMessage, List<CometChatOption>> options;
    private boolean hideSeparator;
    private String emptyStateText, errorStateText;
    private @DrawableRes
    int backButtonIcon;
    private boolean showBackButton;
    private UIKitConstants.SelectionMode selectionMode;
    private CometChatCallHistory.OnSelection onSelection;
    private String title;
    private @LayoutRes
    int emptyStateView;
    private @LayoutRes
    int errorStateView;
    private @LayoutRes
    int loadingStateView;
    private MessagesRequest.MessagesRequestBuilder messagesRequestBuilder;
    private AvatarStyle avatarStyle;
    private DateStyle dateStyle;
    private ListItemStyle listItemStyle;
    private CallHistoryStyle style;
    private @DrawableRes
    int submitIcon, selectionIcon, incomingAudioCallIcon, incomingVideoCallIcon, infoIcon;
    private OnItemClickListener<BaseMessage> onItemClickListener;
    private OnError onError;

    public CallHistoryConfiguration setSubmitIcon(int submitIcon) {
        this.submitIcon = submitIcon;
        return this;
    }

    public CallHistoryConfiguration setSelectionIcon(int selectionIcon) {
        this.selectionIcon = selectionIcon;
        return this;
    }

    public CallHistoryConfiguration setOnItemClickListener(OnItemClickListener<BaseMessage> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public CallHistoryConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    public CallHistoryConfiguration setEmptyStateText(String emptyStateText) {
        this.emptyStateText = emptyStateText;
        return this;
    }

    public CallHistoryConfiguration setErrorStateText(String errorStateText) {
        this.errorStateText = errorStateText;
        return this;
    }

    public CallHistoryConfiguration setAvatarStyle(AvatarStyle avatarStyle) {
        this.avatarStyle = avatarStyle;
        return this;
    }

    public CallHistoryConfiguration setListItemStyle(ListItemStyle listItemStyle) {
        this.listItemStyle = listItemStyle;
        return this;
    }

    public CallHistoryConfiguration setStyle(CallHistoryStyle style) {
        this.style = style;
        return this;
    }


    public CallHistoryConfiguration setSubtitle(Function2<Context, BaseMessage, View> subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public CallHistoryConfiguration setListItemView(Function2<Context, BaseMessage, View> listItemView) {
        this.listItemView = listItemView;
        return this;
    }

    public CallHistoryConfiguration setMenu(View menu) {
        this.menu = menu;
        return this;
    }

    public CallHistoryConfiguration setOptions(Function2<Context, BaseMessage, List<CometChatOption>> options) {
        this.options = options;
        return this;
    }

    public CallHistoryConfiguration hideSeparator(boolean hideSeparator) {
        this.hideSeparator = hideSeparator;
        return this;
    }

    public CallHistoryConfiguration setBackButtonIcon(int backButtonIcon) {
        this.backButtonIcon = backButtonIcon;
        return this;
    }

    public CallHistoryConfiguration showBackButton(boolean showBackButton) {
        this.showBackButton = showBackButton;
        return this;
    }

    public CallHistoryConfiguration setSelectionMode(UIKitConstants.SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        return this;
    }

    public CallHistoryConfiguration setOnSelection(CometChatCallHistory.OnSelection onSelection) {
        this.onSelection = onSelection;
        return this;
    }

    public void setMessagesRequestBuilder(MessagesRequest.MessagesRequestBuilder messagesRequestBuilder) {
        this.messagesRequestBuilder = messagesRequestBuilder;
    }

    public void setDateStyle(DateStyle dateStyle) {
        this.dateStyle = dateStyle;
    }

    public void setIncomingAudioCallIcon(int incomingAudioCallIcon) {
        this.incomingAudioCallIcon = incomingAudioCallIcon;
    }

    public void setIncomingVideoCallIcon(int incomingVideoCallIcon) {
        this.incomingVideoCallIcon = incomingVideoCallIcon;
    }

    public void setInfoIcon(int infoIcon) {
        this.infoIcon = infoIcon;
    }

    public CallHistoryConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    public CallHistoryConfiguration setEmptyStateView(int emptyStateView) {
        this.emptyStateView = emptyStateView;
        return this;
    }

    public CallHistoryConfiguration setErrorStateView(int errorStateView) {
        this.errorStateView = errorStateView;
        return this;
    }

    public CallHistoryConfiguration setLoadingStateView(int loadingStateView) {
        this.loadingStateView = loadingStateView;
        return this;
    }

    public Function2<Context, BaseMessage, View> getSubtitle() {
        return subtitle;
    }

    public Function2<Context, BaseMessage, View> getListItemView() {
        return listItemView;
    }

    public View getMenu() {
        return menu;
    }

    public Function2<Context, BaseMessage, List<CometChatOption>> getOptions() {
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

    public CometChatCallHistory.OnSelection getOnSelection() {
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

    public MessagesRequest.MessagesRequestBuilder getMessagesRequestBuilder() {
        return messagesRequestBuilder;
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

    public DateStyle getDateStyle() {
        return dateStyle;
    }

    public ListItemStyle getListItemStyle() {
        return listItemStyle;
    }

    public CallHistoryStyle getStyle() {
        return style;
    }

    public String getEmptyStateText() {
        return emptyStateText;
    }

    public String getErrorStateText() {
        return errorStateText;
    }

    public int getIncomingAudioCallIcon() {
        return incomingAudioCallIcon;
    }

    public int getIncomingVideoCallIcon() {
        return incomingVideoCallIcon;
    }

    public int getInfoIcon() {
        return infoIcon;
    }
}
