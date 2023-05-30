package com.cometchat.chatuikit.messagecomposer;

import android.content.Context;
import android.view.View;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageComposerAction;
import com.cometchat.chatuikit.shared.views.CometChatMessageInput.CometChatMessageInput;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.HashMap;
import java.util.List;

import kotlin.jvm.functions.Function4;

public class MessageComposerConfiguration {
    private String text;
    private String placeHolderText;
    private View headerView;
    private View footerView;
    private CometChatMessageInput.TextChangedListener textChangedListener;
    private int maxLine;
    private @DrawableRes
    int attachmentIcon, sendButtonIcon;
    private Function4<Context, User, Group, HashMap<String, String>, List<CometChatMessageComposerAction>> attachmentOption;
    private Function4<Context, User, Group, HashMap<String, String>, View> secondaryButtonView;
    private View sendButtonView;
    private Function4<Context, User, Group, HashMap<String, String>, View> auxiliaryButtonView;
    private UIKitConstants.AuxiliaryButtonAlignment auxiliaryButtonAlignment = UIKitConstants.AuxiliaryButtonAlignment.RIGHT;
    private boolean hideLiveReaction;
    private @DrawableRes
    int liveReactionIcon;
    private MessageComposerStyle style;
    private CometChatMessageComposer.SendButtonClick sendButtonClick;
    private OnError onError;

    /**
     * Sets the error listener for the message composer. The error listener will be triggered
     * when an error occurs during message composition or sending.
     *
     * @param onError The {@link OnError} listener to be set.
     */
    public MessageComposerConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }

    /**
     * Sets the text of the input field.
     *
     * @param text The text to set.
     */
    public MessageComposerConfiguration setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the placeholder text of the input field.
     *
     * @param placeHolderText The placeholder text to set.
     */
    public MessageComposerConfiguration setPlaceHolderText(String placeHolderText) {
        this.placeHolderText = placeHolderText;
        return this;
    }

    /**
     * Sets the header view.
     *
     * @param headerView The header view to set.
     */
    public MessageComposerConfiguration setHeaderView(View headerView) {
        this.headerView = headerView;
        return this;
    }

    /**
     * Sets the footer view.
     *
     * @param footerView The footer view to set.
     */
    public MessageComposerConfiguration setFooterView(View footerView) {
        this.footerView = footerView;
        return this;
    }

    /**
     * Sets the listener for text changed events in the message input field.
     *
     * @param textChangedListener The text changed listener to set.
     */
    public MessageComposerConfiguration setTextChangedListener(CometChatMessageInput.TextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
        return this;
    }

    /**
     * Sets the maximum number of lines for the input field.
     *
     * @param maxLine The maximum number of lines to set.
     */
    public MessageComposerConfiguration setMaxLine(int maxLine) {
        this.maxLine = maxLine;
        return this;
    }

    /**
     * Sets the icon for the attachment button.
     *
     * @param attachmentIcon The drawable resource ID of the attachment icon to set.
     */
    public MessageComposerConfiguration setAttachmentIcon(int attachmentIcon) {
        this.attachmentIcon = attachmentIcon;
        return this;
    }

    /**
     * Sets the icon for the send button.
     *
     * @param sendButtonIcon The resource ID of the icon to set.
     */
    public MessageComposerConfiguration setSendButtonIcon(int sendButtonIcon) {
        this.sendButtonIcon = sendButtonIcon;
        return this;
    }

    /**
     * Sets the attachment options for the composer.
     *
     * @param attachmentOption The function to retrieve the list of composer actions.
     */
    public MessageComposerConfiguration setAttachmentOption(Function4<Context, User, Group, HashMap<String, String>, List<CometChatMessageComposerAction>> attachmentOption) {
        this.attachmentOption = attachmentOption;
        return this;
    }

    /**
     * Sets the secondary button view for the composer.
     *
     * @param secondaryButtonView The function to retrieve the secondary button view.
     */
    public MessageComposerConfiguration setSecondaryButtonView(Function4<Context, User, Group, HashMap<String, String>, View> secondaryButtonView) {
        this.secondaryButtonView = secondaryButtonView;
        return this;
    }

    /**
     * Sets the send button view for the composer.
     *
     * @param sendButtonView The send button view.
     */
    public MessageComposerConfiguration setSendButtonView(View sendButtonView) {
        this.sendButtonView = sendButtonView;
        return this;
    }

    /**
     * Sets the auxiliary button view for the composer.
     *
     * @param auxiliaryButtonView The function to retrieve the auxiliary button view.
     */
    public MessageComposerConfiguration setAuxiliaryButtonView(Function4<Context, User, Group, HashMap<String, String>, View> auxiliaryButtonView) {
        this.auxiliaryButtonView = auxiliaryButtonView;
        return this;
    }

    /**
     * Sets the alignment of the auxiliary button.
     *
     * @param auxiliaryButtonAlignment The alignment to set for the auxiliary button.
     */
    public MessageComposerConfiguration setAuxiliaryButtonAlignment(UIKitConstants.AuxiliaryButtonAlignment auxiliaryButtonAlignment) {
        this.auxiliaryButtonAlignment = auxiliaryButtonAlignment;
        return this;
    }

    /**
     * Hides or shows the live reaction view.
     *
     * @param hideLiveReaction True to hide the live reaction view, false to show.
     */
    public MessageComposerConfiguration hideLiveReaction(boolean hideLiveReaction) {
        this.hideLiveReaction = hideLiveReaction;
        return this;
    }

    /**
     * Sets the live reaction icon for the composer.
     *
     * @param liveReactionIcon The live reaction icon.
     */
    public MessageComposerConfiguration setLiveReactionIcon(int liveReactionIcon) {
        this.liveReactionIcon = liveReactionIcon;
        return this;
    }

    /**
     * Sets the style of the message composer.
     *
     * @param style The style to be applied to the message composer.
     */
    public MessageComposerConfiguration setStyle(MessageComposerStyle style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the listener for send button clicks.
     *
     * @param sendButtonClick The callback function to be invoked when the send button is clicked.
     */
    public MessageComposerConfiguration setSendButtonClick(CometChatMessageComposer.SendButtonClick sendButtonClick) {
        this.sendButtonClick = sendButtonClick;
        return this;
    }

    public String getText() {
        return text;
    }

    public String getPlaceHolderText() {
        return placeHolderText;
    }

    public View getHeaderView() {
        return headerView;
    }

    public View getFooterView() {
        return footerView;
    }

    public CometChatMessageInput.TextChangedListener getTextChangedListener() {
        return textChangedListener;
    }

    public OnError getOnError() {
        return onError;
    }

    public int getMaxLine() {
        return maxLine;
    }

    public int getAttachmentIcon() {
        return attachmentIcon;
    }

    public Function4<Context, User, Group, HashMap<String, String>, List<CometChatMessageComposerAction>> getAttachmentOption() {
        return attachmentOption;
    }

    public Function4<Context, User, Group, HashMap<String, String>, View> getSecondaryButtonView() {
        return secondaryButtonView;
    }

    public View getSendButtonView() {
        return sendButtonView;
    }

    public Function4<Context, User, Group, HashMap<String, String>, View> getAuxiliaryButtonView() {
        return auxiliaryButtonView;
    }

    public UIKitConstants.AuxiliaryButtonAlignment getAuxiliaryButtonAlignment() {
        return auxiliaryButtonAlignment;
    }

    public boolean isHideLiveReaction() {
        return hideLiveReaction;
    }

    public int getLiveReactionIcon() {
        return liveReactionIcon;
    }

    public MessageComposerStyle getStyle() {
        return style;
    }

    public int getSendButtonIcon() {
        return sendButtonIcon;
    }

    public CometChatMessageComposer.SendButtonClick getSendButtonClick() {
        return sendButtonClick;
    }
}
