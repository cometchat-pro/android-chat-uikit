package com.cometchat.chatuikit.extensions.reaction;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.extensions.reaction.emojikeyboard.CometChatEmojiKeyboard;
import com.cometchat.chatuikit.extensions.reaction.emojikeyboard.EmojiKeyboardStyle;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.chatuikit.shared.models.CometChatMessageOption;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReactionExtensionDecorator extends DataSourceDecorator {
    private MessageReactionsStyle messageReactionsStyle;
    private EmojiKeyboardStyle emojiKeyboardStyle;

    public ReactionExtensionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    public ReactionExtensionDecorator(MessageReactionsStyle messageReactionsStyle, EmojiKeyboardStyle emojiKeyboardStyle, DataSource dataSource) {
        super(dataSource);
        this.messageReactionsStyle = messageReactionsStyle;
        this.emojiKeyboardStyle = emojiKeyboardStyle;
    }

    @Override
    public View getBottomView(Context context, BaseMessage baseMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        CometChatMessageReaction reaction = new CometChatMessageReaction(context);
        if (messageReactionsStyle == null) {
            if (alignment.equals(UIKitConstants.MessageBubbleAlignment.RIGHT) && baseMessage instanceof TextMessage) {
                messageReactionsStyle = new MessageReactionsStyle().setActiveBackground(CometChatTheme.getInstance(context).getPalette().getAccent900()).setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent700()).setBackground(CometChatTheme.getInstance(context).getPalette().getPrimary()).setBorderColor(CometChatTheme.getInstance(context).getPalette().getAccent900()).setAddReactionIconBackground(CometChatTheme.getInstance(context).getPalette().getAccent100()).setAddReactionIconTint(CometChatTheme.getInstance(context).getPalette().getAccent700()).setBorderWidth(1);
            } else {
                messageReactionsStyle = new MessageReactionsStyle().setActiveBackground(CometChatTheme.getInstance(context).getPalette().getPrimary()).setTextColor(CometChatTheme.getInstance(context).getPalette().getAccent700()).setBackground(CometChatTheme.getInstance(context).getPalette().getAccent900()).setBorderColor(CometChatTheme.getInstance(context).getPalette().getAccent100()).setAddReactionIconBackground(CometChatTheme.getInstance(context).getPalette().getAccent100()).setAddReactionIconTint(CometChatTheme.getInstance(context).getPalette().getAccent700()).setBorderWidth(1);
            }
        }
        reaction.setStyle(messageReactionsStyle);
        reaction.setMessage(baseMessage);
        reaction.setOnAddReactionClickListener(this::showEmojiKeyBoard);
        reaction.setReactionEventListener((reaction1, baseMessageID) -> sendReaction(context, reaction1, baseMessage));
        return reaction;
    }

    @Override
    public List<CometChatMessageOption> getCommonOptions(Context context, BaseMessage baseMessage, Group group) {
        List<CometChatMessageOption> optionList = super.getCommonOptions(context, baseMessage, group);
        optionList.add(new CometChatMessageOption("ReactionExtensionDecorator", context.getString(R.string.add_reaction), CometChatTheme.getInstance(context).getPalette().getAccent(), R.drawable.ic_reactions, CometChatTheme.getInstance(context).getPalette().getAccent700(), CometChatTheme.getInstance(context).getTypography().getSubtitle1(), android.R.color.transparent, () -> {
            showEmojiKeyBoard(context, baseMessage);
        }));
        return optionList;
    }

    private void showEmojiKeyBoard(Context context, BaseMessage baseMessage) {
        //setting default Style for emoji keyboard
        Drawable drawable = context.getDrawable(R.drawable.cc_action_item_top_curve);
        drawable.setTint(CometChatTheme.getInstance(context).getPalette().getAccent900());
        CometChatEmojiKeyboard emojiKeyboard = new CometChatEmojiKeyboard(context);
        if (emojiKeyboardStyle == null)
            emojiKeyboardStyle = new EmojiKeyboardStyle().setSeparatorColor(CometChatTheme.getInstance(context).getPalette().getAccent100()).setCloseButtonTint(CometChatTheme.getInstance(context).getPalette().getAccent100()).setTitleColor(CometChatTheme.getInstance(context).getPalette().getAccent()).setTitleAppearance(CometChatTheme.getInstance(context).getTypography().getName()).setCategoryIconTint(CometChatTheme.getInstance(context).getPalette().getAccent600()).setSelectedCategoryIconTint(CometChatTheme.getInstance(context).getPalette().getPrimary()).setSectionHeaderColor(CometChatTheme.getInstance(context).getPalette().getAccent700()).setSectionHeaderAppearance(CometChatTheme.getInstance(context).getTypography().getSubtitle1()).setBackground(drawable);

        emojiKeyboard.setStyle(emojiKeyboardStyle);
        emojiKeyboard.show();
        emojiKeyboard.setOnClick(new CometChatEmojiKeyboard.onClick() {
            @Override
            public void onClick(String emoji) {
                sendReaction(context, emoji, baseMessage);
                emojiKeyboard.dismiss();
            }

            @Override
            public void onLongClick(String emoji) {

            }
        });
    }

    private void sendReaction(Context context, String reaction, BaseMessage baseMessage) {

        JSONObject body = new JSONObject();
        try {
            body.put("msgId", baseMessage.getId());
            body.put("emoji", reaction);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CometChat.callExtension("reactions", "POST", "/v1/react", body, new CometChat.CallbackListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject responseObject) {
                // ReactionModel added successfully.
            }

            @Override
            public void onError(CometChatException e) {
                showError(context, context.getString(R.string.something_went_wrong));
            }
        });
    }

    public void showError(Context context, String message) {
        String errorMessage = message;
        CometChatTheme theme = CometChatTheme.getInstance(context);
        new CometChatDialog(context, 0, theme.getTypography().getText1(), theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, theme.getPalette().getAccent(), errorMessage, "", "", context.getString(R.string.okay), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                alertDialog.dismiss();
            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                alertDialog.dismiss();
            }
        }, 0, false);
    }

    @Override
    public String getId() {
        return ReactionExtensionDecorator.class.getName();
    }
}
