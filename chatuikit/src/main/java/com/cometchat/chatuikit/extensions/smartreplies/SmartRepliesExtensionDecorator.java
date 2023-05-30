package com.cometchat.chatuikit.extensions.smartreplies;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.extensions.smartreplies.view.CometChatSmartReplies;
import com.cometchat.chatuikit.extensions.smartreplies.view.SmartRepliesConfiguration;
import com.cometchat.chatuikit.extensions.smartreplies.view.SmartRepliesStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.shared.events.CometChatMessageEvents;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKitHelper;
import com.cometchat.chatuikit.shared.constants.MessageStatus;
import com.cometchat.chatuikit.extensions.Extensions;

import java.util.HashMap;
import java.util.List;

public class SmartRepliesExtensionDecorator extends DataSourceDecorator {
    private String LISTENER_ID;
    private SmartRepliesConfiguration smartRepliesConfiguration;
    private SmartRepliesStyle smartRepliesStyle;
    private HashMap<String, String> idMap;
    private User userTemp;
    private Group groupTemp;

    public SmartRepliesExtensionDecorator(DataSource dataSource) {
        super(dataSource);
        addMessageListener();
    }

    public SmartRepliesExtensionDecorator(DataSource dataSource, SmartRepliesConfiguration configuration) {
        super(dataSource);
        addMessageListener();
        this.smartRepliesConfiguration = configuration;
        if (configuration != null) this.smartRepliesStyle = configuration.getStyle();
    }

    private void addMessageListener() {
        LISTENER_ID = System.currentTimeMillis() + "";
        CometChat.addMessageListener(System.currentTimeMillis() + "", new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage textMessage) {
                if (textMessage != null)
                    if (textMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER) && userTemp != null) {
                        if (textMessage.getSender() != null && textMessage.getSender().getUid() != null && userTemp.getUid().equalsIgnoreCase(textMessage.getSender().getUid())) {
                            setIdMap(textMessage);
                            showRepliesPanel(idMap, UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM, textMessage);
                        }
                    } else if (textMessage.getReceiverType().equals(UIKitConstants.ReceiverType.GROUP) && groupTemp != null) {
                        if (groupTemp.getGuid() != null && groupTemp.getGuid().equalsIgnoreCase(textMessage.getReceiverUid())) {
                            setIdMap(textMessage);
                            showRepliesPanel(idMap, UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM, textMessage);
                        }
                    }
            }
        });
        CometChatUIEvents.addListener(LISTENER_ID, new CometChatUIEvents() {
            @Override
            public void ccActiveChatChanged(HashMap<String, String> id, BaseMessage message, User user, Group group) {
                idMap = id;
                userTemp = user;
                groupTemp = group;
                if (message != null && message.getSender() != null && CometChatUIKit.getLoggedInUser() != null && message.getSender().getUid() != null && !message.getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid())) {
                    showRepliesPanel(id, UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM, message);
                }
            }
        });
        CometChatMessageEvents.addListener(LISTENER_ID, new CometChatMessageEvents() {
            @Override
            public void ccMessageSent(BaseMessage baseMessage, int status) {
                if (MessageStatus.SUCCESS == status) {
                    for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                        events.hidePanel(idMap, UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM);
                    }
                }
            }
        });
    }

    public void showRepliesPanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment, BaseMessage textMessage) {
        for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
            events.showPanel(id, alignment, context -> getView(id, context, textMessage));
        }
    }

    public View getView(HashMap<String, String> idMap, Context context, BaseMessage baseMessage) {
        if (smartRepliesStyle == null)
            smartRepliesStyle = new SmartRepliesStyle().setBackground(android.R.color.transparent).setTextAppearance(CometChatTheme.getInstance(context).getTypography().getSubtitle1()).setCloseIconTint(CometChatTheme.getInstance(context).getPalette().getAccent400()).setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent());

        if (baseMessage != null && baseMessage.getSender() != null && baseMessage.getSender().getUid() != null && !baseMessage.getSender().getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
            if (baseMessage.getMetadata() != null) {
                List<String> smartRepliesList = Extensions.getSmartReplyList(baseMessage);
                if (!smartRepliesList.isEmpty()) {
                    CometChatSmartReplies smartReplies = new CometChatSmartReplies(context);
                    smartReplies.setStyle(smartRepliesStyle);
                    smartReplies.setSmartReplyList(smartRepliesList);

                    smartReplies.setOnClick(new CometChatSmartReplies.onClick() {
                        @Override
                        public void onClick(String text, int pos) {
                            for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                                events.hidePanel(idMap, UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM);
                            }
                            sendMessage(idMap, text, baseMessage);
                        }

                        @Override
                        public void onLongClick(String text, int pos) {

                        }
                    });
                    smartReplies.setOnClose(view -> {
                        for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                            events.hidePanel(idMap, UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM);
                        }
                    });
                    if (smartRepliesConfiguration != null) {
                        smartReplies.setCloseIcon(smartRepliesConfiguration.getCloseButtonIcon());
                        if (smartRepliesConfiguration.getOnClick() != null)
                            smartReplies.setOnClick(smartRepliesConfiguration.getOnClick());
                        if (smartRepliesConfiguration.getOnClose() != null)
                            smartReplies.setOnClose(smartRepliesConfiguration.getOnClose());
                    }
                    return smartReplies;
                }
            }
        }
        return new LinearLayout(context);
    }

    private void sendMessage(HashMap<String, String> idMap, String reply, BaseMessage baseMessage) {
        if (reply != null && !reply.isEmpty() && idMap != null && baseMessage != null) {

            TextMessage message;
            if (baseMessage.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.USER)) {
                message = new TextMessage(baseMessage.getSender().getUid(), reply.trim(), idMap.get(UIKitConstants.MapId.RECEIVER_TYPE));
                message.setConversationId(CometChatUIKit.getLoggedInUser().getUid() + "_user_" + userTemp.getUid());
                message.setReceiver(userTemp);
            } else {
                Group group = (Group) baseMessage.getReceiver();
                message = new TextMessage(group.getGuid(), reply.trim(), UIKitConstants.ReceiverType.GROUP);
                message.setConversationId("group_" + groupTemp.getGuid());
                message.setReceiver(groupTemp);
            }
            if (baseMessage.getParentMessageId() > 0)
                message.setParentMessageId(baseMessage.getParentMessageId());

            message.setCategory(CometChatConstants.CATEGORY_MESSAGE);
            message.setSender(CometChatUIKit.getLoggedInUser());
            message.setSentAt(System.currentTimeMillis() / 1000);
            message.setMuid(System.currentTimeMillis() + "");
            CometChatUIKitHelper.onMessageSent(message, MessageStatus.IN_PROGRESS);
            CometChat.sendMessage(message, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.SUCCESS);
//                    for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
//                        events.hidePanel(idMap, UIKitConstants.CustomUIPosition.MESSAGE_LIST_BOTTOM);
//                    }
                }

                @Override
                public void onError(CometChatException e) {
                    message.setMetadata(Utils.placeErrorObjectInMetaData(e));
                    CometChatUIKitHelper.onMessageSent(message, MessageStatus.ERROR);
                }
            });
        }
    }

    public void setIdMap(TextMessage textMessage) {
        if (textMessage.getReceiverType().equalsIgnoreCase(UIKitConstants.ReceiverType.USER)) {
            idMap.put(UIKitConstants.MapId.RECEIVER_ID, userTemp.getUid());
            idMap.put(UIKitConstants.MapId.RECEIVER_TYPE, UIKitConstants.ReceiverType.USER);
        } else {
            Group group = (Group) textMessage.getReceiver();
            idMap.put(UIKitConstants.MapId.RECEIVER_ID, group.getGuid());
            idMap.put(UIKitConstants.MapId.RECEIVER_TYPE, UIKitConstants.ReceiverType.GROUP);
        }
        if (textMessage.getParentMessageId() > 0) {
            idMap.put(UIKitConstants.MapId.PARENT_MESSAGE_ID, String.valueOf(textMessage.getParentMessageId()));
        } else idMap.remove(UIKitConstants.MapId.PARENT_MESSAGE_ID);
    }

    @Override
    public String getId() {
        return SmartRepliesExtensionDecorator.class.getName();
    }
}
