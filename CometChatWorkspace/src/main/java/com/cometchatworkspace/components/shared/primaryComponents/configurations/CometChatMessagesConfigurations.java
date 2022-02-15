package com.cometchatworkspace.components.shared.primaryComponents.configurations;


import androidx.annotation.DrawableRes;

import java.util.ArrayList;
import java.util.List;

import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;


public class CometChatMessagesConfigurations extends CometChatConfigurations {
    private static List<CometChatMessageTemplate> cometChatMessageTemplates = new ArrayList<>();
    private boolean isDeleteMessageHidden,isUserChatEnabled,isGroupChatEnabled;
    /**
     * Below are boolean value of Compose Actions which are used to hide/show each actions.
     */
    private boolean isLiveReactionEnabled,isAttachmentVisible,isMicrophoneVisible;
    private @CometChatMessageList.MessageListAlignment String messageListAlignment;
    private int liveReactionIcon=-1,attachmentIcon=-1,microphoneIcon=-1;
    private String placeholderStr;

    private AvatarConfiguration avatarConfig;

    public CometChatMessagesConfigurations setMessageFilter(List<CometChatMessageTemplate> listOfMessageTemplate) {
        cometChatMessageTemplates = listOfMessageTemplate;
        return this;
    }

    public static List<CometChatMessageTemplate> getMessageFilterList() {
        return cometChatMessageTemplates;
    }

    public static CometChatMessageTemplate getMessageTemplateById(String id) {
        for (CometChatMessageTemplate cometChatMessageTemplate : cometChatMessageTemplates) {
            if (cometChatMessageTemplate.getId().equalsIgnoreCase(id)) {
                return cometChatMessageTemplate;
            }
        }
        return null;
    }

    public CometChatMessagesConfigurations hideDeletedMessage(boolean isHidden) {
        isDeleteMessageHidden= isHidden;
        return this;
    }

    public boolean isDeleteMessageHidden() {
        return isDeleteMessageHidden;
    }

    public CometChatMessagesConfigurations setUserChatEnabled(boolean isEnabled){
        isUserChatEnabled= isEnabled;
        return this;
    }

    public boolean isUserChatEnabled() {
        return isUserChatEnabled;
    }

    public CometChatMessagesConfigurations setGroupChatEnabled(boolean isEnabled) {
        isGroupChatEnabled = isEnabled;
        return this;
    }
    public boolean isGroupChatEnabled() {
        return isGroupChatEnabled;
    }

    public CometChatMessagesConfigurations setLiveReactionEnabled(boolean isEnabled) {
        isLiveReactionEnabled = isEnabled;
        return this;
    }
    public boolean isLiveReactionEnabled() {
        return isLiveReactionEnabled;
    }

    public CometChatMessagesConfigurations setMessageListAlignment(
            @CometChatMessageList.MessageListAlignment String alignment) {
        messageListAlignment = alignment;
        return this;
    }
    public String getMessageListAlignment() {
        return messageListAlignment;
    }

    public CometChatMessagesConfigurations setLiveReactionIcon(@DrawableRes int icon) {
        liveReactionIcon = icon;
        return this;
    }
    public int getLiveReactionIcon() {
        return liveReactionIcon;
    }


    public CometChatMessagesConfigurations setAttachmentIcon(@DrawableRes int icon) {
        attachmentIcon = icon;
        return this;
    }
    public int getAttachmentIcon() {
        return attachmentIcon;
    }


    public CometChatMessagesConfigurations setMicrophoneIcon(@DrawableRes int icon) {
        microphoneIcon = icon;
        return this;
    }
    public int getMicrophoneIcon() {
        return liveReactionIcon;
    }


    public CometChatMessagesConfigurations setPlaceholder(String placeholder) {
        placeholderStr = placeholder;
        return this;
    }
    public String getPlaceholder() {
        return placeholderStr;
    }

    public void setHeaderAvatarConfiguration(AvatarConfiguration configuration) {
        avatarConfig = configuration;
    }
    public AvatarConfiguration getHeaderAvatarConfiguration() {
        return avatarConfig;
    }
}
