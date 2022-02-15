package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

public class ConversationListItemConfiguration extends CometChatConfigurations {

    //Avatar Variables
    private final AvatarConfiguration avatarConfiguration;
    private StatusIndicatorConfiguration statusIndicatorConfiguration;
    private BadgeCountConfiguration badgeCountConfiguration;
    private boolean isAvatarHidden;
    private boolean isStatusIndicatorHidden;
    private boolean isBadgeCountHidden;
    private boolean isMessageReceiptHidden;
    private boolean isTypingIndicatorVisible;
    private boolean isThreadIndicatorHidden;
    private boolean isGroupActionMessageHidden;
    private boolean isDeleteMessageHidden;
    private final Context context;

    public ConversationListItemConfiguration(Context context) {
        this.context = context;
        avatarConfiguration = new AvatarConfiguration(context);
    }

    public void hideAvatar(boolean isHidden) {
        this.isAvatarHidden = isHidden;
    }
    //Avatar
    //TODO(borderWidth,outerViewWidth,cornerRadius,width,height)
    public ConversationListItemConfiguration avatarBorderWidth(int borderWidth) {
        avatarConfiguration.setBorderWidth(borderWidth);
        return this;
    }

    public ConversationListItemConfiguration avatarOuterViewWidth(int outerViewWidth) {
        avatarConfiguration.setOuterViewWidth(outerViewWidth);
        return this;
    }

    public ConversationListItemConfiguration avatarCornerRadius(int radius) {
        avatarConfiguration.setCornerRadius(radius);
        return this;
    }

    public ConversationListItemConfiguration avatarWidth(int width) {
        avatarConfiguration.setWidth(width);
        return this;
    }

    public ConversationListItemConfiguration avatarHeight(int height) {
        avatarConfiguration.setHeight(height);
        return this;
    }

    public AvatarConfiguration getAvatarConfiguration() {
        return avatarConfiguration;
    }

    //StatusIndicator
    public ConversationListItemConfiguration statusIndicatorCornerRadius(int radius) {
        statusIndicatorConfiguration.setCornerRadius(radius);
        return this;
    }

    public ConversationListItemConfiguration statusIndicatorWidth(int width) {
        statusIndicatorConfiguration.setWidth(width);
        return this;
    }

    public ConversationListItemConfiguration statusIndicatorHeight(int height) {
        statusIndicatorConfiguration.setHeight(height);
        return this;
    }

    public ConversationListItemConfiguration hideStatusIndicator(boolean isHidden) {
        isStatusIndicatorHidden = isHidden;
        return this;
    }

    //BadgeCount
    public ConversationListItemConfiguration badgeCountCornerRadius(int radius) {
        statusIndicatorConfiguration.setCornerRadius(radius);
        return this;
    }

    public ConversationListItemConfiguration badgeCountWidth(int width) {
        statusIndicatorConfiguration.setWidth(width);
        return this;
    }

    public ConversationListItemConfiguration badgeCountHeight(int height) {
        statusIndicatorConfiguration.setHeight(height);
        return this;
    }

    public ConversationListItemConfiguration hideUnreadCount(boolean isHidden) {
        isBadgeCountHidden = isHidden;
        return this;
    }

    public ConversationListItemConfiguration hideReceipt(boolean isHidden) {
        isMessageReceiptHidden = isHidden;
        return this;
    }

    public boolean isMessageReceiptHidden() {
        return isMessageReceiptHidden;
    }

    public ConversationListItemConfiguration showTypingIndicator(boolean isVisible) {
        isTypingIndicatorVisible = isVisible;
        return this;
    }

    public boolean isTypingIndicatorVisible() {
        return isTypingIndicatorVisible;
    }

    public ConversationListItemConfiguration hideThreadIndicator(boolean isHidden) {
        isThreadIndicatorHidden = isHidden;
        return this;
    }

    public boolean isThreadIndicatorHidden() {
        return isThreadIndicatorHidden;
    }

    public ConversationListItemConfiguration hideGroupActionMessages(boolean isHidden) {
        isGroupActionMessageHidden = isHidden;
        return this;
    }

    public boolean isGroupActionMessageHidden() {
        return isGroupActionMessageHidden;
    }

    public ConversationListItemConfiguration hideDeletedMessages(boolean isHidden) {
        isDeleteMessageHidden = isHidden;
        return this;
    }

    public boolean isDeleteMessageHidden() {
        return isDeleteMessageHidden;
    }

    public StatusIndicatorConfiguration getStatusIndicatorConfiguration() {
        return statusIndicatorConfiguration;
    }

    public BadgeCountConfiguration getBadgeCountConfiguration() {
        return badgeCountConfiguration;
    }

    public boolean isAvatarHidden() {
        return isAvatarHidden;
    }

    public boolean isStatusIndicatorHidden() {
        return isStatusIndicatorHidden;
    }

    public boolean isBadgeCountHidden() {
        return isBadgeCountHidden;
    }
}
