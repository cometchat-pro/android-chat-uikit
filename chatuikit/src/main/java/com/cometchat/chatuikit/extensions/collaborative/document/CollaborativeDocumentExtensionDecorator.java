package com.cometchat.chatuikit.extensions.collaborative.document;

import android.content.Context;
import android.content.DialogInterface;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.shared.models.CometChatMessageComposerAction;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.utils.MessageBubbleUtils;
import com.cometchat.chatuikit.extensions.reaction.ExtensionResponseListener;
import com.cometchat.chatuikit.extensions.ExtensionConstants;
import com.cometchat.chatuikit.extensions.Extensions;
import com.cometchat.chatuikit.extensions.collaborative.CollaborativeBoardBubbleConfiguration;
import com.cometchat.chatuikit.extensions.collaborative.CollaborativeBoardBubbleStyle;
import com.cometchat.chatuikit.extensions.collaborative.CometChatCollaborativeBoardBubble;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CollaborativeDocumentExtensionDecorator extends DataSourceDecorator {

    private String collaborativeDocumentExtensionTypeConstant = ExtensionConstants.ExtensionType.DOCUMENT;
    private CollaborativeBoardBubbleConfiguration configuration;

    public CollaborativeDocumentExtensionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    public CollaborativeDocumentExtensionDecorator(DataSource dataSource, CollaborativeBoardBubbleConfiguration configuration) {
        super(dataSource);
        this.configuration = configuration;
    }

    @Override
    public List<String> getDefaultMessageTypes() {
        List<String> types = super.getDefaultMessageTypes();
        if (!types.contains(collaborativeDocumentExtensionTypeConstant)) {
            types.add(collaborativeDocumentExtensionTypeConstant);
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
        if (templates != null && !templates.contains(getWhiteBoardTemplate()))
            templates.add(getWhiteBoardTemplate());
        return templates;
    }

    @Override
    public List<CometChatMessageComposerAction> getAttachmentOptions(Context context, User user, Group group, HashMap<String, String> idMap) {
        if (!idMap.containsKey(UIKitConstants.MapId.PARENT_MESSAGE_ID)) {
            List<CometChatMessageComposerAction> messageComposerActions = super.getAttachmentOptions(context, user, group, idMap);
            CometChatTheme theme = CometChatTheme.getInstance(context);
            messageComposerActions.add(new CometChatMessageComposerAction().setId(ExtensionConstants.ExtensionType.DOCUMENT).setTitle(context.getString(R.string.share_writeboard)).setIcon(R.drawable.ic_collaborative_document).setTitleColor(theme.getPalette().getAccent()).setTitleAppearance(theme.getTypography().getSubtitle1()).setIconTintColor(theme.getPalette().getAccent700()).setBackground(theme.getPalette().getAccent100()).setOnClick(() -> {
                String id, type;
                id = user != null ? user.getUid() : group.getGuid();
                type = user != null ? UIKitConstants.ReceiverType.USER : UIKitConstants.ReceiverType.GROUP;
                Extensions.callWriteBoardExtension(id, type, new ExtensionResponseListener() {
                    @Override
                    public void OnResponseSuccess(Object var) {
                        JSONObject jsonObject = (JSONObject) var;
                    }

                    @Override
                    public void OnResponseFailed(CometChatException e) {
                        showError(context);
                    }
                });
            }));
            return messageComposerActions;
        } else return super.getAttachmentOptions(context, user, group, idMap);
    }

    private void showError(Context context) {
        String errorMessage = context.getString(R.string.something_went_wrong);
        CometChatTheme theme = CometChatTheme.getInstance(context);
        new CometChatDialog(context, 0, theme.getTypography().getText1(), theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, theme.getPalette().getAccent(), errorMessage, "", "", context.getString(R.string.okay), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                alertDialog.dismiss();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                alertDialog.dismiss();
            }
        }, 0, false);
    }


    public CometChatMessageTemplate getWhiteBoardTemplate() {
        return new CometChatMessageTemplate().setCategory(UIKitConstants.MessageCategory.CUSTOM).setType(collaborativeDocumentExtensionTypeConstant).setOptions((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, isLeftAlign)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatTheme theme = CometChatTheme.getInstance(context);
                CometChatCollaborativeBoardBubble bubble = new CometChatCollaborativeBoardBubble(context);
                bubble.icon(context.getResources().getDrawable(R.drawable.ic_collaborative_document));
                bubble.setStyle(new CollaborativeBoardBubbleStyle().setTitleColor(theme.getPalette().getAccent()).setTitleTextAppearance(theme.getTypography().getText2()).setSubtitleColor(theme.getPalette().getAccent600()).setSubtitleTextAppearance(theme.getTypography().getSubtitle2()).setButtonTextAppearance(theme.getTypography().getName()).setButtonTextColor(theme.getPalette().getPrimary()).setIconTint(theme.getPalette().getAccent700()));
                bubble.setTitle("Collaborative\n Document");
                bubble.setSubTitle("Open document to edit content \ntogether.");
                bubble.setButtonText("Open Document");
                if (configuration != null) {
                    bubble.setButtonText(configuration.getButtonText());
                    bubble.setTitle(configuration.getTitle());
                    bubble.setSubTitle(configuration.getSubtitle());
                    bubble.setStyle(configuration.getStyle());
                }
                bubble.setBoardUrl(Extensions.getWriteBoardUrl(baseMessage));
                return bubble;
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
        if (UIKitConstants.MessageCategory.CUSTOM.equals(lastMessage.getCategory()) && ExtensionConstants.ExtensionType.DOCUMENT.equalsIgnoreCase(lastMessage.getType()))
            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.custom_message_document);
        return message;
    }
    @Override
    public String getId() {
        return CollaborativeDocumentExtensionDecorator.class.getName();
    }
}
