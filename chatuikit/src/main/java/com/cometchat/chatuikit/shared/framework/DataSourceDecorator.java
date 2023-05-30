package com.cometchat.chatuikit.shared.framework;

import android.content.Context;
import android.view.View;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageComposerAction;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.views.CometChatAudioBubble.AudioBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatFileBubble.FileBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.ImageBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.VideoBubbleStyle;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.shared.models.CometChatMessageOption;

import java.util.HashMap;
import java.util.List;

public abstract class DataSourceDecorator implements DataSource {
    DataSource anInterface;

    public DataSourceDecorator(DataSource dataSource) {
        anInterface = dataSource;
    }

    @Override
    public List<CometChatMessageOption> getTextMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return anInterface.getTextMessageOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getImageMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return anInterface.getImageMessageOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getVideoMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return anInterface.getVideoMessageOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getAudioMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return anInterface.getAudioMessageOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getFileMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return anInterface.getFileMessageOptions(context, baseMessage, group);
    }

    @Override
    public View getDeleteMessageBubble(Context context, UIKitConstants.MessageBubbleAlignment alignment) {
        return anInterface.getDeleteMessageBubble(context, alignment);
    }

    @Override
    public List<CometChatMessageComposerAction> getAttachmentOptions(Context context, User user, Group group, HashMap<String, String> idMap) {
        return anInterface.getAttachmentOptions(context, user, group, idMap);
    }

    @Override
    public View getAuxiliaryOption(Context context, User user, Group group, HashMap<String, String> id) {
        return anInterface.getAuxiliaryOption(context, user, group, id);
    }

    @Override
    public View getBottomView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        return anInterface.getBottomView(context, baseMessage, alignment);
    }

    @Override
    public CometChatMessageTemplate getTextTemplate() {
        return anInterface.getTextTemplate();
    }

    @Override
    public View getTextBubbleContentView(Context context, TextMessage textMessage, TextBubbleStyle bubbleStyle, int gravity, UIKitConstants.MessageBubbleAlignment alignment) {
        return anInterface.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment);
    }

    @Override
    public View getImageBubbleContentView(Context context, MediaMessage mediaMessage, ImageBubbleStyle imageBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment) {
        return anInterface.getImageBubbleContentView(context, mediaMessage, imageBubbleStyle, alignment);
    }

    @Override
    public View getVideoBubbleContentView(Context context, String thumbnailUrl, VideoBubbleStyle videoBubbleStyle, MediaMessage mediaMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        return anInterface.getVideoBubbleContentView(context, thumbnailUrl, videoBubbleStyle, mediaMessage, alignment);
    }

    @Override
    public View getFileBubbleContentView(Context context, MediaMessage mediaMessage, FileBubbleStyle fileBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment) {
        return anInterface.getFileBubbleContentView(context, mediaMessage, fileBubbleStyle, alignment);
    }

    @Override
    public View getAudioBubbleContentView(Context context, MediaMessage mediaMessage, AudioBubbleStyle audioBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment) {
        return anInterface.getAudioBubbleContentView(context, mediaMessage, audioBubbleStyle, alignment);
    }

    @Override
    public CometChatMessageTemplate getAudioTemplate() {
        return anInterface.getAudioTemplate();
    }

    @Override
    public CometChatMessageTemplate getVideoTemplate() {
        return anInterface.getVideoTemplate();
    }

    @Override
    public CometChatMessageTemplate getImageTemplate() {
        return anInterface.getImageTemplate();
    }

    @Override
    public CometChatMessageTemplate getGroupActionsTemplate() {
        return anInterface.getGroupActionsTemplate();
    }

    @Override
    public CometChatMessageTemplate getFileTemplate() {
        return anInterface.getFileTemplate();
    }

    @Override
    public List<CometChatMessageOption> getMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return anInterface.getMessageOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageTemplate> getMessageTemplates() {
        return anInterface.getMessageTemplates();
    }

    @Override
    public CometChatMessageTemplate getMessageTemplate(String category, String type) {
        return anInterface.getMessageTemplate(category, type);
    }


    @Override
    public String getMessageTypeToSubtitle(String messageType, Context context) {
        return anInterface.getMessageTypeToSubtitle(messageType, context);
    }

    @Override
    public List<CometChatMessageOption> getCommonOptions(Context context, BaseMessage baseMessage, Group group) {
        return anInterface.getCommonOptions(context, baseMessage, group);
    }

    @Override
    public List<String> getDefaultMessageTypes() {
        return anInterface.getDefaultMessageTypes();
    }

    @Override
    public String getLastConversationMessage(Context context, Conversation conversation) {
        return anInterface.getLastConversationMessage(context, conversation);
    }

    @Override
    public View getAuxiliaryHeaderMenu(Context context, User user, Group group) {
        return anInterface.getAuxiliaryHeaderMenu(context, user, group);
    }

    @Override
    public List<String> getDefaultMessageCategories() {
        return anInterface.getDefaultMessageCategories();
    }

}
