package com.cometchatworkspace.components.messages.common.hover;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.CometChatOptions;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatAudioBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatDocumentBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatFileBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatImageBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatLocationBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatPollBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatStickerBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatTextBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatVideoBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.CometChatWhiteboardBubble;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.utils.Utils;

public class CometChatHoverDialog {

    private final List<OnHoverEventListener> onHoverEvents = new ArrayList<>();
    private CometChatMessageTemplate messageTemplate;
    private CometChatTextBubble textBubble;
    private CometChatImageBubble imageBubble;
    private CometChatVideoBubble videoBubble;
    private CometChatAudioBubble audioBubble;
    private CometChatFileBubble fileBubble;
    private CometChatWhiteboardBubble whiteboardBubble;
    private CometChatDocumentBubble documentBubble;
    private CometChatStickerBubble stickerBubble;
    private CometChatPollBubble pollBubble;
    private CometChatLocationBubble locationBubble;

    private LinearLayout reactionsLayout,optionsLayout;

    private final BaseMessage baseMessage;

    private final Context context;

    private LinearLayout parentLayout;

    private MaterialCardView optionsCardView;

    private boolean isLoggedInUser;

    private boolean hideIcon;

    public CometChatHoverDialog(Context context, BaseMessage baseMessage, DialogInterface.OnDismissListener onDismissListener) {
        this.context = context;
        this.baseMessage = baseMessage;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cometchat_hover_layout,null);
        alertDialog.setView(view);
        initView(view);
        setupOptions();
        setupReaction();
        Dialog dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onDismissListener.onDismiss(dialogInterface);
            }
        });
    }

    private void initView(View view) {
        parentLayout = view.findViewById(R.id.parent_layout);
        reactionsLayout = view.findViewById(R.id.reactions_layout);
        optionsLayout = view.findViewById(R.id.options_layout);
        optionsCardView = view.findViewById(R.id.options_card);
        textBubble = view.findViewById(R.id.textMessageBubble);
        imageBubble = view.findViewById(R.id.imageMessageBubble);
        videoBubble = view.findViewById(R.id.videoMessageBubble);
        audioBubble = view.findViewById(R.id.audioMessageBubble);
        fileBubble = view.findViewById(R.id.fileMessageBubble);
        whiteboardBubble = view.findViewById(R.id.whiteboardMessageBubble);
        documentBubble = view.findViewById(R.id.documentMessageBubble);
        stickerBubble = view.findViewById(R.id.stickerMessageBubble);
        pollBubble = view.findViewById(R.id.pollMessageBubble);
        locationBubble = view.findViewById(R.id.locationMessageBubble);

        if (baseMessage.getSender().getUid().equalsIgnoreCase(CometChat.getLoggedInUser().getUid())) {
            isLoggedInUser = true;
            LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            containerLayoutParams.gravity = Gravity.END;
            containerLayoutParams.topMargin = 16;
            optionsCardView.setLayoutParams(containerLayoutParams);
            parentLayout.setLayoutParams(containerLayoutParams);
        }
    }

    private void setupReaction() {
        List<Reaction> reactions = Extensions.getInitialReactions(7);
        for(Reaction reaction : reactions) {
            View vw = LayoutInflater.from(context).inflate(R.layout.reaction_list_row,null);
            TextView textView = vw.findViewById(R.id.reaction);
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;
            params.rightMargin = 16;
            params.bottomMargin = 8;
            params.topMargin = 8;
            textView.setLayoutParams(params);
            textView.setTextSize(20f);
            textView.setTypeface(null, Typeface.BOLD);

            textView.setText(reaction.getName());
            reactionsLayout.addView(vw);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        ImageView addEmojiView = new ImageView(context);
        addEmojiView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reactions));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) Utils.dpToPx(context,24),(int)Utils.dpToPx(context,24));
        layoutParams.topMargin = 16;
        layoutParams.leftMargin = 16;
        addEmojiView.setLayoutParams(layoutParams);
        reactionsLayout.addView(addEmojiView);
        addEmojiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (messageActionListener!=null)
//                    messageActionListener.onReactionClick(new Reaction("add_emoji",0));
//                dismiss();
            }
        });
    }
    private void setupOptions() {
        if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_TEXT)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("text");
            textBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                textBubble.messageAlignment(Alignment.RIGHT);
            textBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("image");
            imageBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                imageBubble.messageAlignment(Alignment.RIGHT);
            imageBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("video");
            videoBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                videoBubble.messageAlignment(Alignment.RIGHT);
            videoBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("audio");
            audioBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                audioBubble.messageAlignment(Alignment.RIGHT);
            audioBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_FILE)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("file");
            fileBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                fileBubble.messageAlignment(Alignment.RIGHT);
            fileBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.whiteboard)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("whiteboard");
            whiteboardBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                whiteboardBubble.messageAlignment(Alignment.RIGHT);
            whiteboardBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.document)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("document");
            documentBubble.setVisibility(View.VISIBLE);
            documentBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.sticker)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("sticker");
            stickerBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
                stickerBubble.messageAlignment(Alignment.RIGHT);
            stickerBubble.messageObject(baseMessage);
        } else if (baseMessage.getType().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.poll)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("poll");
            pollBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                pollBubble.messageAlignment(Alignment.RIGHT);
            pollBubble.messageObject(baseMessage);
        }  else if (baseMessage.getType().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.location)) {
            messageTemplate = CometChatMessagesConfigurations.getMessageTemplateById("location");
            locationBubble.setVisibility(View.VISIBLE);
            if (isLoggedInUser)
//                locationBubble.messageAlignment(Alignment.RIGHT);
            locationBubble.messageObject(baseMessage);
        }
        List<CometChatOptions> options = messageTemplate.getOptions();
        for (CometChatOptions item : options) {
            RelativeLayout itemLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    600,RelativeLayout.LayoutParams.WRAP_CONTENT);
            itemLayout.setLayoutParams(layoutParams);
            itemLayout.setPadding(16,16,16,16);

            ImageView iconView = new ImageView(context);
            iconView.setImageResource(item.getIcon());

            RelativeLayout.LayoutParams iconParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            iconParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            iconView.setLayoutParams(iconParam);

            TextView textView = new TextView(context);
            textView.setText(item.getTitle());
            RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            textParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            textParam.addRule(RelativeLayout.LEFT_OF,iconView.getId());
            textView.setLayoutParams(textParam);

            itemLayout.addView(textView);
            if (!hideIcon)
                itemLayout.addView(iconView);
            itemLayout.setBackground(context.getDrawable(R.drawable.action_item_bottom_border));
            optionsLayout.addView(itemLayout);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getTitle().equalsIgnoreCase(context.getString(R.string.edit_message))) {
                        for (OnHoverEventListener listener : onHoverEvents) {
                            listener.OnEditAction(baseMessage);
                        }
                    } else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.delete_message))){
                        for (OnHoverEventListener listener : onHoverEvents) {

                        }
                    }
                }
            });
        }

    }


    public void addHoverListener(String Tag, OnHoverEventListener eventListener) {
        for (OnHoverEventListener listener : onHoverEvents) {
            listener = eventListener;
        }
    }

    public interface OnHoverEventListener {
        void OnEditAction(BaseMessage baseMessage);
        void OnDeleteAction();
        void onReplyAction();
        void onForwardAction();
        void onCopyAction();
        void onStartThreadAction();
        void onStartPrivateAction();
        void onReplyPrivateAction();
        void onReactAction();
        void onMessageInfoAction();
        void onTranslateAction();
    }
}
