package com.cometchat.chatuikit.extensions.sticker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.extensions.sticker.bubble.CometChatStickerBubble;
import com.cometchat.chatuikit.extensions.sticker.keyboard.CometChatStickerKeyboard;
import com.cometchat.chatuikit.extensions.sticker.keyboard.StickerKeyboardConfiguration;
import com.cometchat.chatuikit.extensions.sticker.keyboard.model.Sticker;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.utils.MessageBubbleUtils;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.extensions.reaction.ExtensionResponseListener;
import com.cometchat.chatuikit.extensions.ExtensionConstants;
import com.cometchat.chatuikit.extensions.Extensions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class StickerExtensionDecorator extends DataSourceDecorator {

    private final DataSource anInterface;
    private final String stickerTypeConstant = ExtensionConstants.ExtensionType.STICKER;
    private StickerKeyboardConfiguration configuration;

    public StickerExtensionDecorator(DataSource dataSource) {
        super(dataSource);
        this.anInterface = dataSource;
    }

    public StickerExtensionDecorator(DataSource dataSource, StickerKeyboardConfiguration configuration) {
        super(dataSource);
        this.anInterface = dataSource;
        this.configuration = configuration;
    }

    @Override
    public List<CometChatMessageTemplate> getMessageTemplates() {
        List<CometChatMessageTemplate> templates = super.getMessageTemplates();
        if (templates != null && !templates.contains(getStickerTemplate()))
            templates.add(getStickerTemplate());
        return templates;
    }

    @Override
    public List<String> getDefaultMessageTypes() {
        List<String> types = super.getDefaultMessageTypes();
        if (!types.contains(stickerTypeConstant)) {
            types.add(stickerTypeConstant);
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
    public View getAuxiliaryOption(Context context, User user, Group group, HashMap<String, String> id) {
        LinearLayout layout = new LinearLayout(context);
        Utils.handleView(layout, super.getAuxiliaryOption(context, user, group, id), false);
        if (super.getAuxiliaryOption(context, user, group, id) != null) {
            Utils.removeParentFromView(super.getAuxiliaryOption(context, user, group, id));
            layout.addView(super.getAuxiliaryOption(context, user, group, id));
        }
        layout.addView(getStickerIcon(context, id, user, group));
        return layout;
    }

    public View getStickerIcon(Context context, HashMap<String, String> mapId, User user, Group group) {
        CometChatTheme theme = CometChatTheme.getInstance(context);
        View view = View.inflate(context, R.layout.sticker_extension_button, null);
        ImageView stickerImage = view.findViewById(R.id.iv_sticker);
        ImageView keyboardImage = view.findViewById(R.id.iv_keyboard);
        if (configuration != null && configuration.getStickerIcon() != 0)
            stickerImage.setImageResource(configuration.getStickerIcon());
        else {
            stickerImage.setImageResource(R.drawable.ic_sticker);
            stickerImage.setImageTintList(ColorStateList.valueOf(theme.getPalette().getAccent()));
        }
        CometChatUIEvents.addListener(System.currentTimeMillis() + "", new CometChatUIEvents() {
            @Override
            public void hidePanel(HashMap<String, String> id, UIKitConstants.CustomUIPosition alignment) {
                super.hidePanel(id, alignment);
                keyboardImage.setVisibility(View.GONE);
                stickerImage.setVisibility(View.VISIBLE);
            }
        });

        stickerImage.setOnClickListener(view1 -> {
            Utils.hideKeyBoard(context, view1);
            CometChatStickerKeyboard stickerKeyboard = getStickerKeyboard(context, user, group, mapId, configuration);
            for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                events.showPanel(mapId, UIKitConstants.CustomUIPosition.COMPOSER_BOTTOM, var1 -> stickerKeyboard);
            }
            keyboardImage.setVisibility(View.VISIBLE);
            stickerImage.setVisibility(View.GONE);
        });

        keyboardImage.setOnClickListener(v -> {
            for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                events.hidePanel(mapId, UIKitConstants.CustomUIPosition.COMPOSER_BOTTOM);
            }
            Utils.showKeyBoard(context, v);
            keyboardImage.setVisibility(View.GONE);
            stickerImage.setVisibility(View.VISIBLE);
        });
        return view;
    }

    public CometChatStickerKeyboard getStickerKeyboard(Context context, User user, Group group, HashMap<String, String> idMap, StickerKeyboardConfiguration configuration) {
        CometChatStickerKeyboard cometChatStickerKeyboard = new CometChatStickerKeyboard(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDpToPx(context, 230));
        cometChatStickerKeyboard.setLayoutParams(layoutParams);
        if (configuration != null) {
            cometChatStickerKeyboard.setStyle(configuration.getStyle());
            cometChatStickerKeyboard.setEmptyStateView(configuration.getEmptyStateView());
            cometChatStickerKeyboard.setErrorStateView(configuration.getErrorStateView());
            cometChatStickerKeyboard.setLoadingStateView(configuration.getLoadingStateView());
            cometChatStickerKeyboard.errorStateText(configuration.getErrorStateText());
            cometChatStickerKeyboard.emptyStateText(configuration.getEmptyStateText());
        }

        cometChatStickerKeyboard.setState(UIKitConstants.States.LOADING);
        Extensions.fetchStickers(new ExtensionResponseListener() {
            @Override
            public void OnResponseSuccess(Object var) {
                cometChatStickerKeyboard.setState(UIKitConstants.States.LOADED);
                JSONObject stickersJSON = (JSONObject) var;
                HashMap<String, List<Sticker>> stringListHashMap = Extensions.extractStickersFromJSON(stickersJSON);
                if (!stringListHashMap.isEmpty()) {
                    cometChatStickerKeyboard.setState(UIKitConstants.States.NON_EMPTY);
                    cometChatStickerKeyboard.setData(Extensions.extractStickersFromJSON(stickersJSON));
                } else cometChatStickerKeyboard.setState(UIKitConstants.States.EMPTY);
            }

            @Override
            public void OnResponseFailed(CometChatException e) {
                cometChatStickerKeyboard.setState(UIKitConstants.States.ERROR);
            }
        });
        cometChatStickerKeyboard.setStickerClickListener(sticker -> {
            JSONObject stickerData = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            String pushNotificationMessage = context.getString(R.string.shared_a_sticker);
            try {
                stickerData.put("sticker_url", sticker.getUrl());
                stickerData.put("sticker_name", sticker.getName());
                jsonObject.put("incrementUnreadCount", true);
                jsonObject.put("pushNotification", pushNotificationMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomMessage customMessage = null;
            if (user != null) {
                customMessage = new CustomMessage(user.getUid(), UIKitConstants.ReceiverType.USER, stickerTypeConstant, stickerData);
                customMessage.setConversationId(user.getUid() + "_user_" + CometChatUIKit.getLoggedInUser().getUid());
                customMessage.setReceiver(user);
            } else if (group != null) {
                customMessage = new CustomMessage(group.getGuid(), UIKitConstants.ReceiverType.GROUP, stickerTypeConstant, stickerData);
                customMessage.setConversationId("group_" + group.getGuid());
                customMessage.setReceiver(group);
            }

            if (idMap.containsKey(UIKitConstants.MapId.PARENT_MESSAGE_ID)) {
                customMessage.setParentMessageId(Integer.valueOf(idMap.get(UIKitConstants.MapId.PARENT_MESSAGE_ID)));
            }
            customMessage.setMetadata(jsonObject);
            customMessage.setSentAt(System.currentTimeMillis() / 1000);
            customMessage.setMuid("" + System.currentTimeMillis());
            customMessage.setCategory(CometChatConstants.CATEGORY_CUSTOM);
            customMessage.setSender(CometChatUIKit.getLoggedInUser());
            CometChatUIKit.sendCustomMessage(customMessage);
        });

        return cometChatStickerKeyboard;
    }

    public CometChatMessageTemplate getStickerTemplate() {
        return new CometChatMessageTemplate().setCategory(UIKitConstants.MessageCategory.CUSTOM).setType(stickerTypeConstant).setOptions((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getCommonOptions(context, baseMessage, isLeftAlign)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatStickerBubble bubble = new CometChatStickerBubble(context);
                try {
                    bubble.setImageUrl(((CustomMessage) baseMessage).getCustomData().getString("sticker_url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        if (UIKitConstants.MessageCategory.CUSTOM.equals(lastMessage.getCategory()) && ExtensionConstants.ExtensionType.STICKER.equalsIgnoreCase(lastMessage.getType()))
            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.custom_message_sticker);
        return message;
    }


    @Override
    public String getId() {
        return StickerExtensionDecorator.class.getName();
    }
}
