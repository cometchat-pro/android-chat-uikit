package com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;

import java.util.List;

import com.cometchatworkspace.components.messages.MessageStatus;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatUIKitHelper;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.CometChatSoundManager;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.Sound;
import com.cometchatworkspace.resources.utils.recycler_touch.ClickListener;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerTouchListener;
import com.google.android.material.card.MaterialCardView;

/**
 * Purpose - SmartReply class is a subclass of recyclerview and used as component by
 * developer to display Smart Reply in his message list. Developer just need to pass the list of reply at their end
 * recieved at their end. It helps user show smart reply at thier end easily.
 * <p>
 * <p>
 * Created on - 23th January 2020
 * <p>
 * Modified on  - 13th september 2022
 */
public class CometChatSmartReplies extends MaterialCardView {

    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private ImageView closeIcon;
    private SmartRepliesAdapter smartReplyListAdapter;
    private BaseMessage lastMessage;
    private User loggedInUser;
    private onClose onClose;
    private onClick onClick;
    private boolean enableSoundForMessages;
    private @RawRes
    int customOutGoingMessageSound = 0;
    private CometChatSoundManager soundManager;

    public CometChatSmartReplies(@NonNull Context context) {
        super(context);
        initComponent(context, null, 0);
    }

    public CometChatSmartReplies(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs, 0);
    }

    public CometChatSmartReplies(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context, attrs, defStyleAttr);
    }

    private void initComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        view = View.inflate(context, R.layout.smart_reply_layout, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SmartReplyList, 0, 0);
        recyclerView = view.findViewById(R.id.recycler_view);
        closeIcon = view.findViewById(R.id.close);
        soundManager = new CometChatSoundManager(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        smartReplyListAdapter = new SmartRepliesAdapter(context);
        recyclerView.setAdapter(smartReplyListAdapter);
        if (CometChat.isInitialized() && CometChat.getLoggedInUser() != null)
            loggedInUser = CometChat.getLoggedInUser();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                String reply = (String) var1.getTag(R.string.reply_txt);
                if (onClick != null)
                    onClick.onClick(reply + "", var2);
                else {
                    markMessageAsRead(lastMessage);
                    sendMessage(reply);
                }
            }

            @Override
            public void onLongClick(View var1, int var2) {
                String reply = (String) var1.getTag(R.string.reply_txt);
                if (onClick != null)
                    onClick.onLongClick(reply + "", var2);
            }
        }));

        closeIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClose != null) {
                    onClose.onClose(view);
                }
            }
        });
        addView(view);
    }

    /**
     * This method is used to mark users & group message as read.
     *
     * @param lastMessage is object of BaseMessage.class. It is message which is been marked as read.
     */
    private void markMessageAsRead(BaseMessage lastMessage) {
        CometChat.markAsRead(lastMessage, new CometChat.CallbackListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                lastMessage.setReadAt(System.currentTimeMillis() / 1000);
                CometChatUIKitHelper.onMessageRead(lastMessage);
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendMessage(String reply) {
        try {
            TextMessage textMessage;
            if (lastMessage.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
                textMessage = new TextMessage(lastMessage.getSender().getUid(), reply, CometChatConstants.RECEIVER_TYPE_USER);
            } else {
                Group group = (Group) lastMessage.getReceiver();
                textMessage = new TextMessage(group.getGuid(), reply, CometChatConstants.RECEIVER_TYPE_GROUP);
            }

            textMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
            textMessage.setSender(loggedInUser);
            textMessage.setSentAt(System.currentTimeMillis() / 1000);
            textMessage.setMuid(System.currentTimeMillis() + "");
            CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.IN_PROGRESS);
            if (enableSoundForMessages) {
                soundManager.play(Sound.incomingMessage, customOutGoingMessageSound);
            }
            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    view.setVisibility(GONE);
                    CometChatUIKitHelper.onMessageSent(textMessage, MessageStatus.SUCCESS);
                }

                @Override
                public void onError(CometChatException e) {
                    CometChatUIKitHelper.onMessageError(e, textMessage);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to set list of replies in SmartReplyComponent.
     *
     * @param replyList is object of List<String> . It is list of smart replies.
     */
    private void setSmartReplyList(List<String> replyList) {
        if (smartReplyListAdapter != null) {
            smartReplyListAdapter.updateList(replyList);
            smartReplyListAdapter.notifyDataSetChanged();
        }

    }


    public void setMessageObject(BaseMessage lastMessage) {
        this.lastMessage = lastMessage;
        if (lastMessage != null && lastMessage.getSender() != null && lastMessage.getSender().getUid() != null && !lastMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            if (lastMessage.getMetadata() != null) {
                List<String> smartReplies = Extensions.getSmartReplyList(lastMessage);
                if (!smartReplies.isEmpty()) {
                    setSmartReplyList(smartReplies);
                    view.setVisibility(VISIBLE);
                } else {
                    view.setVisibility(GONE);
                }
            } else {
                view.setVisibility(GONE);
            }
        } else {
            view.setVisibility(GONE);
        }
    }

    public void enableSoundForMessages(boolean enable) {
        this.enableSoundForMessages = enable;
    }

    public void setCustomOutGoingMessageSound(int customOutGoingMessageSound) {
        this.customOutGoingMessageSound = customOutGoingMessageSound;
    }

    public void setCloseIcon(@DrawableRes int icon) {
        closeIcon.setImageResource(icon);
    }

    public void setCloseIcon(@NonNull Drawable icon) {
        closeIcon.setImageDrawable(icon);
    }

    public void setOnClick(CometChatSmartReplies.onClick onClick) {
        this.onClick = onClick;
    }

    public void setStyle(SmartRepliesStyle style) {
        if (style.getDrawableBackground() != null) {
            setBackground(style.getDrawableBackground());
        } else if (style.getBackground() != 0) {
            setBackgroundColor(style.getBackground());
        }
        if (style.getBorderRadius() != 0) {
            setRadius(style.getBorderRadius());
        }
        if (style.getBorderWidth() != 0) {
            setStrokeWidth(style.getTextBorderWidth());
        }
        if (style.getBorderColor() != 0) {
            setStrokeColor(style.getTextColor());
        }
        if(style.getCloseIconTint()!=0){
            closeIcon.setColorFilter(style.getCloseIconTint());
        }
        smartReplyListAdapter.setStyle(style);
    }

    public void setOnClose(CometChatSmartReplies.onClose onClose) {
        this.onClose = onClose;
    }

    public interface onClose {
        void onClose(View view);
    }

    public interface onClick {
        void onClick(String text, int pos);

        void onLongClick(String text, int pos);
    }

}
