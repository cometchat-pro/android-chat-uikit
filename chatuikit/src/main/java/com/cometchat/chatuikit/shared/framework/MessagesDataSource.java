package com.cometchat.chatuikit.shared.framework;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageComposerAction;
import com.cometchat.chatuikit.shared.models.CometChatMessageOption;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.utils.MessageBubbleUtils;
import com.cometchat.chatuikit.shared.views.CometChatAudioBubble.AudioBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatFileBubble.FileBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.CometChatImageBubble;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.ImageBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.VideoBubbleStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MessagesDataSource implements DataSource {

    private HashMap<String, CometChatMessageTemplate> cometChatMessageTemplateHashMap = new HashMap<>();

    private CometChatMessageTemplate _getDefaultTextTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_MESSAGE).setType(CometChatConstants.MESSAGE_TYPE_TEXT).setOptions((context, baseMessage, group) -> ChatConfigurator.getUtils().getTextMessageOptions(context, baseMessage, group)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                if (UIKitConstants.MessageBubbleAlignment.LEFT.equals(isLeftAlign)) {
                    return ChatConfigurator.getUtils().getTextBubbleContentView(context, (TextMessage) baseMessage, null, Gravity.START, isLeftAlign);
                } else {
                    return ChatConfigurator.getUtils().getTextBubbleContentView(context, (TextMessage) baseMessage, null, Gravity.END, isLeftAlign);
                }
            } else return ChatConfigurator.getUtils().getDeleteMessageBubble(context, isLeftAlign);
        });
    }

    private CometChatMessageTemplate _getDefaultGroupActionTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_ACTION).setType(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER).setContentView((context, baseMessage, isLeftAlign) -> {
            CometChatTheme theme = CometChatTheme.getInstance(context);
            return MessageBubbleUtils.getActionContentView(context, null, (Action) baseMessage, new TextBubbleStyle().setBackground(context.getResources().getDrawable(R.drawable.action_dotted_border)).setTextAppearance(theme.getTypography().getCaption1()).setTextColor(theme.getPalette().getAccent()), 0);
        });
    }

    private CometChatMessageTemplate _getDefaultImageTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_MESSAGE).setType(CometChatConstants.MESSAGE_TYPE_IMAGE).setOptions((context, baseMessage, group) -> ChatConfigurator.getUtils().getImageMessageOptions(context, baseMessage, group)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatTheme theme = CometChatTheme.getInstance(context);
                return ChatConfigurator.getUtils().getImageBubbleContentView(context, (MediaMessage) baseMessage, new ImageBubbleStyle().setTextAppearance(theme.getTypography().getText1()).setTextColor(theme.getPalette().getAccent()).setBackground(context.getResources().getColor(android.R.color.transparent)), isLeftAlign);
            } else return ChatConfigurator.getUtils().getDeleteMessageBubble(context, isLeftAlign);
        });
    }

    private CometChatMessageTemplate _getDefaultVideoTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_MESSAGE).setType(CometChatConstants.MESSAGE_TYPE_VIDEO).setOptions((context, baseMessage, group) -> ChatConfigurator.getUtils().getVideoMessageOptions(context, baseMessage, group)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatTheme theme = CometChatTheme.getInstance(context);
                return ChatConfigurator.getUtils().getVideoBubbleContentView(context, null, new VideoBubbleStyle().setPlayIconTint(theme.getPalette().getPrimary()).setPlayIconBackgroundColor(theme.getPalette().getBackground()).setBackground(context.getResources().getColor(android.R.color.transparent)), (MediaMessage) baseMessage, isLeftAlign);
            } else return ChatConfigurator.getUtils().getDeleteMessageBubble(context, isLeftAlign);
        });
    }

    private CometChatMessageTemplate _getDefaultFileTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_MESSAGE).setType(CometChatConstants.MESSAGE_TYPE_FILE).setOptions((context, baseMessage, group) -> ChatConfigurator.getUtils().getFileMessageOptions(context, baseMessage, group)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatTheme theme = CometChatTheme.getInstance(context);
                return ChatConfigurator.getUtils().getFileBubbleContentView(context, (MediaMessage) baseMessage, new FileBubbleStyle().setTitleTextAppearance(theme.getTypography().getName()).setTitleTextColor(theme.getPalette().getAccent()).setSubtitleTextColor(theme.getPalette().getAccent600()).setSubtitleTextAppearance(theme.getTypography().getSubtitle2()).setDownloadIconTint(theme.getPalette().getPrimary()).setBackground(context.getResources().getColor(android.R.color.transparent)), isLeftAlign);
            } else return ChatConfigurator.getUtils().getDeleteMessageBubble(context, isLeftAlign);
        });
    }

    private CometChatMessageTemplate _getDefaultAudioTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_MESSAGE).setType(CometChatConstants.MESSAGE_TYPE_AUDIO).setOptions((context, baseMessage, group) -> ChatConfigurator.getUtils().getAudioMessageOptions(context, baseMessage, group)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatTheme theme = CometChatTheme.getInstance(context);
                return ChatConfigurator.getUtils().getAudioBubbleContentView(context, (MediaMessage) baseMessage, new AudioBubbleStyle().setTitleTextAppearance(theme.getTypography().getName()).setTitleTextColor(theme.getPalette().getAccent()).setSubtitleTextColor(theme.getPalette().getAccent600()).setSubtitleTextAppearance(theme.getTypography().getSubtitle2()).setPauseIconTint(theme.getPalette().getPrimary()).setBackground(context.getResources().getColor(android.R.color.transparent)), isLeftAlign);
            } else return ChatConfigurator.getUtils().getDeleteMessageBubble(context, isLeftAlign);
        });
    }

    private List<CometChatMessageTemplate> _getDefaultMessageTemplates() {
        return new ArrayList<>(getDefaultMessageTemplatesHashMap().values());
    }

    private List<CometChatMessageOption> _getMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        List<CometChatMessageOption> cometChatOptions = new ArrayList<>();
        if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.TEXT)) {
            cometChatOptions.addAll(_getTextTemplateOptions(context, baseMessage, group));
        } else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.IMAGE)) {
            cometChatOptions.addAll(_getImageTemplateOptions(context, baseMessage, group));
        } else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.VIDEO)) {
            cometChatOptions.addAll(_getVideoTemplateOptions(context, baseMessage, group));
        } else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.AUDIO)) {
            cometChatOptions.addAll(_getAudioTemplateOptions(context, baseMessage, group));
        } else if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.FILE)) {
            cometChatOptions.addAll(_getFileTemplateOptions(context, baseMessage, group));
        }
        return cometChatOptions;
    }

    private List<CometChatMessageOption> _getCommonOptions(Context context, BaseMessage baseMessage, Group group) {
        List<CometChatMessageOption> messageOptions = new ArrayList<>();
        if (baseMessage.getDeletedAt() == 0) {
            if (baseMessage.getParentMessageId() == 0)
                messageOptions.add(_getReplyInThreadOption(context));
            if (_isCommon(baseMessage, group)) {
                if (baseMessage.getType().equalsIgnoreCase(UIKitConstants.MessageType.TEXT) && isMyMessage(baseMessage))
                    messageOptions.add(_getEditOption(context));
                messageOptions.add(_getDeleteOption(context));
            }
        }
        return messageOptions;
    }

    private List<CometChatMessageOption> _getTextTemplateOptions(Context context, BaseMessage baseMessage, Group group) {
        List<CometChatMessageOption> cometChatOptions = new ArrayList<>();
        if (baseMessage.getDeletedAt() == 0) {
            cometChatOptions.add(_getCopyOption(context));
            cometChatOptions.addAll(ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, group));
        }
        return cometChatOptions;
    }

    private List<CometChatMessageOption> _getImageTemplateOptions(Context context, BaseMessage baseMessage, Group group) {
        return ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, group);
    }

    private List<CometChatMessageOption> _getVideoTemplateOptions(Context context, BaseMessage baseMessage, Group group) {
        return ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, group);
    }

    private List<CometChatMessageOption> _getAudioTemplateOptions(Context context, BaseMessage baseMessage, Group group) {
        return ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, group);
    }

    private List<CometChatMessageOption> _getFileTemplateOptions(Context context, BaseMessage baseMessage, Group group) {
        return ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, group);
    }

    private boolean _isCommon(BaseMessage baseMessage, Group group) {
        if (baseMessage.getSender().getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
            return true;
        } else if (baseMessage.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.GROUP)) {
            return isNotParticipant(baseMessage, group);
        } else return false;
    }

    private boolean isMyMessage(BaseMessage baseMessage) {
        return baseMessage.getSender().getUid().equals(CometChatUIKit.getLoggedInUser().getUid());
    }

    private boolean isNotParticipant(BaseMessage baseMessage, Group group) {
        return group != null && group.getScope() != null && !CometChatConstants.SCOPE_PARTICIPANT.equalsIgnoreCase(group.getScope());
    }

    private CometChatMessageOption _getDeleteOption(Context context) {
        return new CometChatMessageOption(UIKitConstants.MessageOption.DELETE, context.getString(R.string.delete_message), _getTheme(context).getPalette().getError(), R.drawable.ic_delete, _getTheme(context).getPalette().getError(), _getTheme(context).getTypography().getSubtitle1(), android.R.color.transparent, null);
    }

    private CometChatMessageOption _getReplyInThreadOption(Context context) {
        return new CometChatMessageOption(UIKitConstants.MessageOption.REPLY_IN_THREAD, context.getString(R.string.reply_in_thread), _getTheme(context).getPalette().getAccent(), R.drawable.ic_threaded_message, _getTheme(context).getPalette().getAccent700(), _getTheme(context).getTypography().getSubtitle1(), android.R.color.transparent, null);
    }

    private CometChatMessageOption _getEditOption(Context context) {
        return new CometChatMessageOption(UIKitConstants.MessageOption.EDIT, context.getString(R.string.edit_message), _getTheme(context).getPalette().getAccent(), R.drawable.ic_edit, _getTheme(context).getPalette().getAccent700(), _getTheme(context).getTypography().getSubtitle1(), android.R.color.transparent, null);
    }

    private CometChatMessageOption _getCopyOption(Context context) {
        return new CometChatMessageOption(UIKitConstants.MessageOption.COPY, context.getString(R.string.copy_message), _getTheme(context).getPalette().getAccent(), R.drawable.ic_copy_paste, _getTheme(context).getPalette().getAccent700(), _getTheme(context).getTypography().getSubtitle1(), android.R.color.transparent, null);
    }

    private CometChatTheme _getTheme(Context context) {
        return CometChatTheme.getInstance(context);
    }

    private CometChatMessageTemplate _getMessageTemplate(String category, String type) {
        getDefaultMessageTemplatesHashMap();
        return cometChatMessageTemplateHashMap.get(category + "_" + type);
    }

    public HashMap<String, CometChatMessageTemplate> getDefaultMessageTemplatesHashMap() {
        cometChatMessageTemplateHashMap.put(UIKitConstants.MessageTemplateId.TEXT, ChatConfigurator.getUtils().getTextTemplate());
        cometChatMessageTemplateHashMap.put(UIKitConstants.MessageTemplateId.IMAGE, ChatConfigurator.getUtils().getImageTemplate());
        cometChatMessageTemplateHashMap.put(UIKitConstants.MessageTemplateId.AUDIO, ChatConfigurator.getUtils().getAudioTemplate());
        cometChatMessageTemplateHashMap.put(UIKitConstants.MessageTemplateId.VIDEO, ChatConfigurator.getUtils().getVideoTemplate());
        cometChatMessageTemplateHashMap.put(UIKitConstants.MessageTemplateId.FILE, ChatConfigurator.getUtils().getFileTemplate());
        cometChatMessageTemplateHashMap.put(UIKitConstants.MessageTemplateId.GROUP_ACTION, ChatConfigurator.getUtils().getGroupActionsTemplate());
        return cometChatMessageTemplateHashMap;
    }


    public View getImageContentView(Context context, MediaMessage mediaMessage, ImageBubbleStyle imageBubbleStyle) {
        if (context != null && mediaMessage != null) {
            View view = View.inflate(context, R.layout.image_bubble_layout, null);
            CometChatImageBubble cometChatImageBubble = view.findViewById(R.id.image_bubble);
            Attachment attachment = mediaMessage.getAttachment();
            if (attachment != null)
                cometChatImageBubble.setImageUrl(attachment.getFileUrl(), 0, attachment.getFileExtension().equalsIgnoreCase(".gif"));
            cometChatImageBubble.setCaption(mediaMessage.getCaption());
            cometChatImageBubble.setStyle(imageBubbleStyle);
            return view;
        }
        return null;
    }

    private List<String> _getDefaultMessageTypes() {
        return new ArrayList<>(Arrays.asList(CometChatConstants.MESSAGE_TYPE_TEXT, CometChatConstants.MESSAGE_TYPE_FILE, CometChatConstants.MESSAGE_TYPE_IMAGE, CometChatConstants.MESSAGE_TYPE_AUDIO, CometChatConstants.MESSAGE_TYPE_VIDEO, CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER));
    }

    private List<String> _getDefaultMessageCategories() {
        return new ArrayList<>(Arrays.asList(CometChatConstants.CATEGORY_MESSAGE, CometChatConstants.CATEGORY_ACTION));
    }

    private List<CometChatMessageComposerAction> _getDefaultAttachmentOptions(Context context, User user, Group group) {
        return new ArrayList<>(Arrays.asList(_getGalleryOption(context), _getCameraOption(context), _getFileOption(context), _getAudioOption(context)));
    }

    private CometChatMessageComposerAction _getGalleryOption(Context context) {
        return new CometChatMessageComposerAction().setId(UIKitConstants.ComposerAction.GALLERY).setTitle(context.getString(R.string.photo_video_library)).setIcon(R.drawable.ic_image_library).setTitleColor(_getTheme(context).getPalette().getAccent()).setTitleAppearance(_getTheme(context).getTypography().getSubtitle1()).setIconTintColor(_getTheme(context).getPalette().getAccent700()).setBackground(_getTheme(context).getPalette().getAccent100());
    }

    private CometChatMessageComposerAction _getFileOption(Context context) {
        return new CometChatMessageComposerAction().setId(UIKitConstants.ComposerAction.FILE).setTitle(context.getString(R.string.send_files)).setIcon(R.drawable.ic_file_upload).setTitleColor(_getTheme(context).getPalette().getAccent()).setTitleAppearance(_getTheme(context).getTypography().getSubtitle1()).setIconTintColor(_getTheme(context).getPalette().getAccent700()).setBackground(_getTheme(context).getPalette().getAccent100());
    }

    private CometChatMessageComposerAction _getCameraOption(Context context) {
        return new CometChatMessageComposerAction().setId(UIKitConstants.ComposerAction.CAMERA).setTitle(context.getString(R.string.take_a_photo)).setIcon(R.drawable.ic_camera).setTitleColor(_getTheme(context).getPalette().getAccent()).setTitleAppearance(_getTheme(context).getTypography().getSubtitle1()).setIconTintColor(_getTheme(context).getPalette().getAccent700()).setBackground(_getTheme(context).getPalette().getAccent100());
    }

    private CometChatMessageComposerAction _getAudioOption(Context context) {
        return new CometChatMessageComposerAction().setId(UIKitConstants.ComposerAction.AUDIO).setTitle(context.getString(R.string.send_audio_files)).setIcon(R.drawable.ic_audio).setTitleColor(_getTheme(context).getPalette().getAccent()).setTitleAppearance(_getTheme(context).getTypography().getSubtitle1()).setIconTintColor(_getTheme(context).getPalette().getAccent700()).setBackground(_getTheme(context).getPalette().getAccent100());
    }

    private static String _getLastConversationMessage(Context context, Conversation conversation) {
        String lastMessageText;
        BaseMessage baseMessage = conversation.getLastMessage();
        if (baseMessage != null) {
            lastMessageText = getLastMessage(context, baseMessage);
            if (baseMessage.getDeletedAt() > 0) {
                lastMessageText = context.getString(R.string.this_message_deleted);
            }
        } else {
            lastMessageText = context.getResources().getString(R.string.tap_to_start_conversation);
        }
        return lastMessageText;
    }

    private static String getLastMessage(Context context, BaseMessage lastMessage) {

        String message = null;

        switch (lastMessage.getCategory()) {

            case CometChatConstants.CATEGORY_MESSAGE:
                if (lastMessage instanceof TextMessage) {
                    message = Utils.getMessagePrefix(lastMessage, context) + (((TextMessage) lastMessage).getText() == null ? context.getString(R.string.this_message_deleted) : ((TextMessage) lastMessage).getText());
                } else if (lastMessage instanceof MediaMessage) {
                    if (lastMessage.getDeletedAt() == 0) {
                        if (lastMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE))
                            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.message_image);
                        else if (lastMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO))
                            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.message_video);
                        else if (lastMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE))
                            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.message_file);
                        else if (lastMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO))
                            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.message_audio);
                    } else message = context.getString(R.string.this_message_deleted);

                }
                break;
            case CometChatConstants.CATEGORY_CUSTOM:
                if (lastMessage.getDeletedAt() == 0) {
                    if (lastMessage.getMetadata() != null && lastMessage.getMetadata().has("pushNotification")) {
                        try {
                            message = lastMessage.getMetadata().getString("pushNotification");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        message = Utils.getMessagePrefix(lastMessage, context) + lastMessage.getType();
                    }
                } else message = context.getString(R.string.this_message_deleted);

                break;
            case CometChatConstants.CATEGORY_ACTION:
                if (((Action) lastMessage).getMessage() != null && !((Action) lastMessage).getMessage().isEmpty())
                    message = ((Action) lastMessage).getMessage();
                else message = getActionMessage(((Action) lastMessage));
                break;
            default:
                message = context.getString(R.string.tap_to_start_conversation);
        }
        return message;
    }

    private static String getActionMessage(Action action) {
        String message = "";
        if (action.getType().equalsIgnoreCase(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)) {
            switch (action.getAction()) {
                case CometChatConstants.ActionKeys.ACTION_JOINED: {
                    User actionBy = (User) action.getActionBy();
                    message = String.format(CometChatConstants.ActionMessages.ACTION_GROUP_JOINED_MESSAGE, actionBy.getName());
                    break;
                }
                case CometChatConstants.ActionKeys.ACTION_LEFT: {
                    User actionBy = (User) action.getActionBy();
                    message = String.format(CometChatConstants.ActionMessages.ACTION_GROUP_LEFT_MESSAGE, actionBy.getName());
                    break;
                }
                case CometChatConstants.ActionKeys.ACTION_KICKED: {
                    User actionBy = (User) action.getActionBy();
                    User actionOn = (User) action.getActionOn();
                    message = String.format(CometChatConstants.ActionMessages.ACTION_MEMBER_KICKED_MESSAGE, actionBy.getName(), actionOn.getName());
                    break;
                }
                case CometChatConstants.ActionKeys.ACTION_BANNED: {
                    User actionBy = (User) action.getActionBy();
                    User actionOn = (User) action.getActionOn();
                    message = String.format(CometChatConstants.ActionMessages.ACTION_MEMBER_BANNED_MESSAGE, actionBy.getName(), actionOn.getName());
                    break;
                }
                case CometChatConstants.ActionKeys.ACTION_UNBANNED: {
                    User actionBy = (User) action.getActionBy();
                    User actionOn = (User) action.getActionOn();
                    message = String.format(CometChatConstants.ActionMessages.ACTION_MEMBER_UNBANNED_MESSAGE, actionBy.getName(), actionOn.getName());
                    break;
                }
                case CometChatConstants.ActionKeys.ACTION_MEMBER_ADDED: {
                    User actionBy = (User) action.getActionBy();
                    User actionOn = (User) action.getActionOn();
                    message = String.format(CometChatConstants.ActionMessages.ACTION_MEMBER_ADDED_TO_GROUP, actionBy.getName(), actionOn.getName());
                    break;
                }
                case CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED: {
                    User actionBy = (User) action.getActionBy();
                    User actionOn = (User) action.getActionOn();
                    message = String.format(CometChatConstants.ActionMessages.ACTION_MEMBER_SCOPE_CHANGED, actionBy.getName(), actionOn.getName(), action.getNewScope());
                    break;
                }
            }
        } else if (action.getType().equalsIgnoreCase(CometChatConstants.ActionKeys.ACTION_TYPE_MESSAGE)) {
            switch (action.getAction()) {
                case CometChatConstants.ActionKeys.ACTION_MESSAGE_EDITED:
                    message = CometChatConstants.ActionMessages.ACTION_MESSAGE_EDITED_MESSAGE;
                    break;
                case CometChatConstants.ActionKeys.ACTION_MESSAGE_DELETED:
                    message = CometChatConstants.ActionMessages.ACTION_MESSAGE_DELETED_MESSAGE;
                    break;
            }
        }
        return message;
    }

    @Override
    public List<CometChatMessageOption> getTextMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return _getTextTemplateOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getImageMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return _getImageTemplateOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getVideoMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return _getVideoTemplateOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getAudioMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return _getAudioTemplateOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageOption> getFileMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return _getFileTemplateOptions(context, baseMessage, group);
    }

    @Override
    public CometChatMessageTemplate getAudioTemplate() {
        return _getDefaultAudioTemplate();
    }

    @Override
    public CometChatMessageTemplate getVideoTemplate() {
        return _getDefaultVideoTemplate();
    }

    @Override
    public CometChatMessageTemplate getImageTemplate() {
        return _getDefaultImageTemplate();
    }

    @Override
    public CometChatMessageTemplate getFileTemplate() {
        return _getDefaultFileTemplate();
    }

    @Override
    public CometChatMessageTemplate getTextTemplate() {
        return _getDefaultTextTemplate();
    }

    @Override
    public List<CometChatMessageTemplate> getMessageTemplates() {
        return _getDefaultMessageTemplates();
    }

    @Override
    public CometChatMessageTemplate getMessageTemplate(String category, String type) {
        return _getMessageTemplate(category, type);
    }

    @Override
    public List<CometChatMessageOption> getMessageOptions(Context context, BaseMessage baseMessage, Group group) {
        return _getMessageOptions(context, baseMessage, group);
    }

    @Override
    public List<CometChatMessageComposerAction> getAttachmentOptions(Context context, User user, Group group, HashMap<String, String> idMap) {
        return _getDefaultAttachmentOptions(context, user, group);
    }

    @Override
    public List<CometChatMessageOption> getCommonOptions(Context context, BaseMessage baseMessage, Group group) {
        return _getCommonOptions(context, baseMessage, group);
    }

    @Override
    public List<String> getDefaultMessageTypes() {
        return _getDefaultMessageTypes();
    }

    @Override
    public List<String> getDefaultMessageCategories() {
        return _getDefaultMessageCategories();
    }

    @Override
    public View getBottomView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        return null;
    }

    @Override
    public View getTextBubbleContentView(Context context, TextMessage textMessage, TextBubbleStyle bubbleStyle, int gravity, UIKitConstants.MessageBubbleAlignment alignment) {
        TextBubbleStyle textBubbleStyle;
        if (bubbleStyle == null) {
            if (gravity == Gravity.START)
                textBubbleStyle = new TextBubbleStyle().setTextAppearance(CometChatTheme.getInstance(context).getTypography().getText1()).setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent()).setBackground(context.getResources().getColor(android.R.color.transparent));
            else
                textBubbleStyle = new TextBubbleStyle().setTextAppearance(CometChatTheme.getInstance(context).getTypography().getText1()).setTextColor(context.getResources().getColor(R.color.textColorWhite)).setBackground(context.getResources().getColor(android.R.color.transparent));
            bubbleStyle = textBubbleStyle;
        }
        return MessageBubbleUtils.getTextContentView(context, textMessage, bubbleStyle, gravity);
    }

    @Override
    public View getImageBubbleContentView(Context context, MediaMessage mediaMessage, ImageBubbleStyle imageBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment) {
        return getImageContentView(context, mediaMessage, imageBubbleStyle);
    }

    @Override
    public View getVideoBubbleContentView(Context context, String thumbnailUrl, VideoBubbleStyle videoBubbleStyle, MediaMessage mediaMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        return MessageBubbleUtils.getVideoContentView(context, thumbnailUrl, videoBubbleStyle, mediaMessage);
    }

    @Override
    public View getFileBubbleContentView(Context context, MediaMessage mediaMessage, FileBubbleStyle fileBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment) {
        return MessageBubbleUtils.getFileContentView(context, mediaMessage, fileBubbleStyle);
    }

    @Override
    public View getAudioBubbleContentView(Context context, MediaMessage mediaMessage, AudioBubbleStyle audioBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment) {
        return MessageBubbleUtils.getAudioContentView(context, mediaMessage, audioBubbleStyle);
    }

    @Override
    public View getDeleteMessageBubble(Context context, UIKitConstants.MessageBubbleAlignment alignment) {
        return MessageBubbleUtils.getDeletedBubble(context);
    }

    @Override
    public CometChatMessageTemplate getGroupActionsTemplate() {
        return _getDefaultGroupActionTemplate();
    }

    @Override
    public String getMessageTypeToSubtitle(String messageType, Context context) {
        return null;
    }

    @Override
    public View getAuxiliaryOption(Context context, User user, Group group, HashMap<String, String> id) {
        return null;
    }

    @Override
    public String getLastConversationMessage(Context context, Conversation conversation) {
        return _getLastConversationMessage(context, conversation);
    }

    @Override
    public View getAuxiliaryHeaderMenu(Context context, User user, Group group) {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }
}
