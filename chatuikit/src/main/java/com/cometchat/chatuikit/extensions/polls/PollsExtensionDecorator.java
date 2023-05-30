package com.cometchat.chatuikit.extensions.polls;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.shared.models.CometChatMessageComposerAction;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.utils.MessageBubbleUtils;
import com.cometchat.chatuikit.extensions.ExtensionConstants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PollsExtensionDecorator extends DataSourceDecorator {

    private PollBubbleStyle pollBubbleStyle;
    private CreatePollsStyle createPollsStyle;
    private PollsConfiguration configuration;

    public PollsExtensionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    public PollsExtensionDecorator(DataSource dataSource, PollsConfiguration configuration) {
        super(dataSource);
        this.configuration = configuration;
        if (configuration != null) {
            this.pollBubbleStyle = configuration.getPollBubbleStyle();
            this.createPollsStyle = configuration.getCreatePollsStyle();
        }
    }

    @Override
    public List<String> getDefaultMessageTypes() {
        List<String> types = super.getDefaultMessageTypes();
        if (!types.contains(ExtensionConstants.ExtensionType.EXTENSION_POLL)) {
            types.add(ExtensionConstants.ExtensionType.EXTENSION_POLL);
        }
        return types;
    }

    @Override
    public List<String> getDefaultMessageCategories() {
        List<String> categories = super.getDefaultMessageCategories();
        if (!categories.contains(UIKitConstants.MessageCategory.CUSTOM))
            categories.add(UIKitConstants.MessageCategory.CUSTOM);
        return categories;
    }

    @Override
    public List<CometChatMessageTemplate> getMessageTemplates() {
        List<CometChatMessageTemplate> templates = super.getMessageTemplates();
        if (templates != null && !templates.contains(getPollTemplate()))
            templates.add(getPollTemplate());
        return templates;
    }

    @Override
    public List<CometChatMessageComposerAction> getAttachmentOptions(Context context, User user, Group group, HashMap<String, String> idMap) {
        if (!idMap.containsKey(UIKitConstants.MapId.PARENT_MESSAGE_ID)) {
            List<CometChatMessageComposerAction> messageComposerActions = super.getAttachmentOptions(context, user, group, idMap);
            CometChatTheme theme = CometChatTheme.getInstance(context);
            messageComposerActions.add(new CometChatMessageComposerAction().setId(ExtensionConstants.ExtensionType.EXTENSION_POLL).setTitle(context.getString(R.string.create_a_poll)).setIcon(R.drawable.ic_polls).setTitleColor(theme.getPalette().getAccent()).setTitleAppearance(theme.getTypography().getSubtitle1()).setIconTintColor(theme.getPalette().getAccent700()).setBackground(theme.getPalette().getAccent100()).setOnClick(() -> {

                String id, type;
                id = user != null ? user.getUid() : group.getGuid();
                type = user != null ? UIKitConstants.ReceiverType.USER : UIKitConstants.ReceiverType.GROUP;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, androidx.appcompat.R.style.AlertDialog_AppCompat);
                CometChatCreatePoll chatCreatePoll = new CometChatCreatePoll(context);
                if (createPollsStyle == null)
                    createPollsStyle = new CreatePollsStyle().setBackground(theme.getPalette().getGradientBackground()).setBackground(theme.getPalette().getBackground()).setAnswerTextColor(theme.getPalette().getAccent500()).setAnswerTextAppearance(theme.getTypography().getText3()).setTitleColor(theme.getPalette().getAccent()).setTitleAppearance(theme.getTypography().getHeading()).setSeparatorColor(theme.getPalette().getAccent100()).setOptionTextColor(theme.getPalette().getAccent()).setOptionHintColor(theme.getPalette().getAccent600()).setOptionTextAppearance(theme.getTypography().getText1()).setAddOptionAppearance(theme.getTypography().getName()).setAddOptionColor(theme.getPalette().getPrimary()).setQuestionHintColor(theme.getPalette().getAccent600()).setQuestionTextColor(theme.getPalette().getAccent()).setQuestionTextAppearance(theme.getTypography().getText1()).setCloseIconTint(theme.getPalette().getPrimary()).setCreateIconTint(theme.getPalette().getPrimary());
                chatCreatePoll.setStyle(createPollsStyle);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                chatCreatePoll.setLayoutParams(params);
                alertDialog.setView(chatCreatePoll);
                Dialog dialog = alertDialog.create();
                chatCreatePoll.setOnClosePoll(dialog::dismiss);
                chatCreatePoll.setOnCreatePoll((question, jsonArray) -> {
                    try {
                        ProgressDialog progressDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.create_a_poll));
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("question", question);
                        jsonObject.put("options", jsonArray);
                        jsonObject.put("receiver", id);
                        jsonObject.put("receiverType", type);
                        CometChat.callExtension("polls", "POST", "/v2/create", jsonObject, new CometChat.CallbackListener<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {
                                dialog.dismiss();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onError(CometChatException e) {
                                dialog.dismiss();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
                dialog.show();

            }));
            return messageComposerActions;
        } else return super.getAttachmentOptions(context, user, group, idMap);
    }

    public CometChatMessageTemplate getPollTemplate() {
        return new CometChatMessageTemplate().setCategory(UIKitConstants.MessageCategory.CUSTOM).setType(ExtensionConstants.ExtensionType.EXTENSION_POLL).setOptions((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, isLeftAlign)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatTheme theme = CometChatTheme.getInstance(context);
                CometChatPollBubble pollBubble = new CometChatPollBubble(context);
                if (pollBubbleStyle == null)
                    pollBubbleStyle = new PollBubbleStyle().setQuestionColor(theme.getPalette().getAccent()).setOptionDefaultBackgroundColor(theme.getPalette().getAccent900()).setOptionTextColor(theme.getPalette().getAccent()).setQuestionTextAppearance(theme.getTypography().getText1()).setOptionTextAppearance(theme.getTypography().getText3()).setPercentageTextAppearance(theme.getTypography().getText3());
                pollBubble.setStyle(pollBubbleStyle);
                pollBubble.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                pollBubble.messageObject(baseMessage);
                return pollBubble;
            } else return MessageBubbleUtils.getDeletedBubble(context);
        });
    }

    @Override
    public String getLastConversationMessage(Context context, Conversation conversation) {
        return getLastConversationMessage_(context, conversation);
    }

    public String getLastConversationMessage_(Context context, Conversation conversation) {
        String lastMessageText;
        BaseMessage baseMessage = conversation.getLastMessage();
        if (baseMessage != null) {
            String message = getLastMessage(context, baseMessage);
            if (message != null) {
                lastMessageText = message;
            } else lastMessageText = super.getLastConversationMessage(context, conversation);
            if (baseMessage.getDeletedAt() > 0) {
                lastMessageText = context.getString(R.string.this_message_deleted);
            }
        } else {
            lastMessageText = context.getResources().getString(R.string.tap_to_start_conversation);
        }
        return lastMessageText;
    }

    public String getLastMessage(Context context, BaseMessage lastMessage) {
        String message = null;
        if (UIKitConstants.MessageCategory.CUSTOM.equals(lastMessage.getCategory()) && ExtensionConstants.ExtensionType.EXTENSION_POLL.equalsIgnoreCase(lastMessage.getType()))
            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.custom_message_poll);
        return message;
    }

    @Override
    public String getId() {
        return PollsExtension.class.getName();
    }
}
