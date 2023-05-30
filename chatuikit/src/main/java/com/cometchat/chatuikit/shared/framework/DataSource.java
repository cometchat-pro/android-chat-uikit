package com.cometchat.chatuikit.shared.framework;

import android.content.Context;
import android.view.View;

import androidx.annotation.GravityInt;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageComposerAction;
import com.cometchat.chatuikit.shared.models.CometChatMessageOption;
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

import java.util.HashMap;
import java.util.List;

/**
 * The DataSource interface provides methods to retrieve various data related to CometChat messages and UI components.
 */
public interface DataSource {

    /**
     * Returns a list of message options for a text message.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param group       The group object associated with the message.
     * @return A list of CometChatMessageOption objects representing the available options for the text message.
     */
    List<CometChatMessageOption> getTextMessageOptions(Context context, BaseMessage baseMessage, Group group);

    /**
     * Returns a list of message options for an image message.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param group       The group object associated with the message.
     * @return A list of CometChatMessageOption objects representing the available options for the image message.
     */
    List<CometChatMessageOption> getImageMessageOptions(Context context, BaseMessage baseMessage, Group group);

    /**
     * Returns a list of message options for a video message.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param group       The group object associated with the message.
     * @return A list of CometChatMessageOption objects representing the available options for the video message.
     */
    List<CometChatMessageOption> getVideoMessageOptions(Context context, BaseMessage baseMessage, Group group);

    /**
     * Returns a list of message options for an audio message.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param group       The group object associated with the message.
     * @return A list of CometChatMessageOption objects representing the available options for the audio message.
     */
    List<CometChatMessageOption> getAudioMessageOptions(Context context, BaseMessage baseMessage, Group group);

    /**
     * Returns a list of message options for a file message.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param group       The group object associated with the message.
     * @return A list of CometChatMessageOption objects representing the available options for the file message.
     */
    List<CometChatMessageOption> getFileMessageOptions(Context context, BaseMessage baseMessage, Group group);

    /**
     * Returns the view for the bottom section of a message bubble.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param alignment   The alignment of the message bubble.
     * @return The view representing the bottom section of the message bubble.
     */
    View getBottomView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment);

    /**
     * Returns the view for the content of a text bubble.
     *
     * @param context     The context of the application.
     * @param textMessage The text message object.
     * @param bubbleStyle The style of the text bubble.
     * @param gravity     The gravity of the text inside the bubble.
     * @param alignment   The alignment of the message bubble.
     * @return The view representing the content of the text bubble.
     */
    View getTextBubbleContentView(Context context, TextMessage textMessage, TextBubbleStyle bubbleStyle, @GravityInt int gravity, UIKitConstants.MessageBubbleAlignment alignment);

    /**
     * Returns the view for the content of an image bubble.
     *
     * @param context          The context of the application.
     * @param mediaMessage     The media message object.
     * @param imageBubbleStyle The style of the image bubble.
     * @param alignment        The alignment of the message bubble.
     * @return The view representing the content of the image bubble.
     */
    View getImageBubbleContentView(Context context, MediaMessage mediaMessage, ImageBubbleStyle imageBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment);

    /**
     * Returns the view for the content of a video bubble.
     *
     * @param context          The context of the application.
     * @param thumbnailUrl     The URL of the video thumbnail.
     * @param videoBubbleStyle The style of the video bubble.
     * @param mediaMessage     The media message object.
     * @param alignment        The alignment of the message bubble.
     * @return The view representing the content of the video bubble.
     */
    View getVideoBubbleContentView(Context context, String thumbnailUrl, VideoBubbleStyle videoBubbleStyle, MediaMessage mediaMessage, UIKitConstants.MessageBubbleAlignment alignment);

    /**
     * Returns the view for the content of a file bubble.
     *
     * @param context         The context of the application.
     * @param mediaMessage    The media message object.
     * @param fileBubbleStyle The style of the file bubble.
     * @param alignment       The alignment of the message bubble.
     * @return The view representing the content of the file bubble.
     */
    View getFileBubbleContentView(Context context, MediaMessage mediaMessage, FileBubbleStyle fileBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment);

    /**
     * Returns the view for the content of an audio bubble.
     *
     * @param context          The context of the application.
     * @param mediaMessage     The media message object.
     * @param audioBubbleStyle The style of the audio bubble.
     * @param alignment        The alignment of the message bubble.
     * @return The view representing the content of the audio bubble.
     */
    View getAudioBubbleContentView(Context context, MediaMessage mediaMessage, AudioBubbleStyle audioBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment);

    /**
     * Returns the view for a delete message bubble.
     *
     * @param context   The context of the application.
     * @param alignment The alignment of the message bubble.
     * @return The view representing the delete message bubble.
     */
    View getDeleteMessageBubble(Context context, UIKitConstants.MessageBubbleAlignment alignment);

    /**
     * Returns the audio template for messages.
     *
     * @return The CometChatMessageTemplate object representing the audio template.
     */
    CometChatMessageTemplate getAudioTemplate();


    /**
     * Returns the video template for messages.
     *
     * @return The CometChatMessageTemplate object representing the video template.
     */
    CometChatMessageTemplate getVideoTemplate();

    /**
     * Returns the image template for messages.
     *
     * @return The CometChatMessageTemplate object representing the image template.
     */
    CometChatMessageTemplate getImageTemplate();

    /**
     * Returns the group actions template for messages.
     *
     * @return The CometChatMessageTemplate object representing the group actions template.
     */
    CometChatMessageTemplate getGroupActionsTemplate();

    /**
     * Returns the file template for messages.
     *
     * @return The CometChatMessageTemplate object representing the file template.
     */
    CometChatMessageTemplate getFileTemplate();

    /**
     * Returns the text template for messages.
     *
     * @return The CometChatMessageTemplate object representing the text template.
     */
    CometChatMessageTemplate getTextTemplate();

    /**
     * Returns a list of all available message templates.
     *
     * @return A list of CometChatMessageTemplate objects representing all available message templates.
     */
    List<CometChatMessageTemplate> getMessageTemplates();

    /**
     * Returns the message template for the specified category and type.
     *
     * @param category The category of the message.
     * @param type     The type of the message.
     * @return The CometChatMessageTemplate object representing the specified message template.
     */
    CometChatMessageTemplate getMessageTemplate(String category, String type);

    /**
     * Returns a list of message options for a message.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param group       The group object associated with the message.
     * @return A list of CometChatMessageOption objects representing the available options for the message.
     */
    List<CometChatMessageOption> getMessageOptions(Context context, BaseMessage baseMessage, Group group);

    /**
     * Returns the subtitle corresponding to the given message type.
     *
     * @param messageType The type of the message.
     * @param context     The context of the application.
     * @return The subtitle text for the specified message type.
     */
    String getMessageTypeToSubtitle(String messageType, Context context);

    /**
     * Returns a list of attachment options for the message composer.
     *
     * @param context The context of the application.
     * @param user    The user object associated with the composer.
     * @param group   The group object associated with the composer.
     * @param idMap   The map of attachment IDs.
     * @return A list of CometChatMessageComposerAction objects representing the available attachment options.
     */
    List<CometChatMessageComposerAction> getAttachmentOptions(Context context, User user, Group group, HashMap<String, String> idMap);

    /**
     * Returns the auxiliary option view for the message composer.
     *
     * @param context The context of the application.
     * @param user    The user object associated with the composer.
     * @param group   The group object associated with the composer.
     * @param id      The ID of the auxiliary option.
     * @return The auxiliary option view.
     */
    View getAuxiliaryOption(Context context, User user, Group group, HashMap<String, String> id);

    /**
     * Returns a list of common options for the message.
     *
     * @param context     The context of the application.
     * @param baseMessage The base message object.
     * @param group       The group object associated with the message.
     * @return A list of CometChatMessageOption objects representing the common options for the message.
     */
    List<CometChatMessageOption> getCommonOptions(Context context, BaseMessage baseMessage, Group group);

    /**
     * Returns a list of default message types.
     *
     * @return A list of default message types.
     */
    List<String> getDefaultMessageTypes();

    /**
     * Returns a list of default message categories.
     *
     * @return A list of default message categories.
     */
    List<String> getDefaultMessageCategories();

    /**
     * Returns the last conversation message for the specified conversation.
     *
     * @param context      The context of the application.
     * @param conversation The conversation object.
     * @return The last conversation message.
     */
    String getLastConversationMessage(Context context, Conversation conversation);

    /**
     * Returns the auxiliary header menu view for the user or group.
     *
     * @param context The context of the application.
     * @param user    The user object associated with the header menu.
     * @param group   The group object associated with the header menu.
     * @return The auxiliary header menu view.
     */
    View getAuxiliaryHeaderMenu(Context context, User user, Group group);

    /**
     * Returns the ID of the data source.
     *
     * @return The ID of the data source.
     */
    String getId();
}
