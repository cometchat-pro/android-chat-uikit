package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatBadgeCount.CometChatBadgeCount;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;

/**
 * CometChatConversationListItem is a independent and custom UI which is used to handle each conversation
 * in conversation list.
 *
 * Modified at : Feb 08, 2022
 *
 * Copyright &copy; CometChat Team
 *
 */
public class CometChatConversationListItem extends RelativeLayout {

    FontUtils fontUtils;

    Context context;

    RelativeLayout avatarSection;

    Conversation conversation;
    private TextView conversationListItemTitle;
    private TextView conversationListItemSubTitle;
    private CometChatMessageReceipt conversationListItemReceipt;
    private TextView conversationListItemTypingIndicator;
    private TextView conversationListItemHelperText;
    private TextView conversationListItemTime;
    private CometChatAvatar conversationListItemAvatar;
    private CometChatStatusIndicator conversationListUserPresence;
    private CometChatBadgeCount conversationListBadgeCount;
    private int typingIndicatorColor = 0;

    public CometChatConversationListItem(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    public CometChatConversationListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);
    }

    public CometChatConversationListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs, defStyleAttr);
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        String lastMessageText;
        BaseMessage baseMessage = conversation.getLastMessage();
        if (baseMessage != null) {
            setReceipt(baseMessage);
            setTime(baseMessage.getSentAt());
            lastMessageText=Utils.getLastMessage(context,baseMessage);

            if (conversation.getLastMessage().getParentMessageId()!=0) {
                setThreadIndicator(context.getString(R.string.in_thread));
            } else {
                hideThreadIndicator(true);
            }

            if (baseMessage.getDeletedAt()>0) {
                hideThreadIndicator(true);
                lastMessageText = "";
            }

        } else {
            lastMessageText = context.getResources().getString(R.string.tap_to_start_conversation);
            setTime(conversation.getUpdatedAt());
        }


        if (lastMessageText.trim().isEmpty())
            hideThreadIndicator(true);

        setSubTitle(lastMessageText);
        if (baseMessage!=null) {
            boolean isSentimentNegative = Extensions.checkSentiment(baseMessage);
            if (isSentimentNegative && !baseMessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                setSubTitle(context.getResources().getString(R.string.sentiment_content));
            }
        }
        setSubTitleFont(FontUtils.robotoRegular);
        setTitleFont(FontUtils.robotoMedium);
        setTimeFont(FontUtils.robotoRegular);

        String name,avatar,status;
        if (conversation.getConversationType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            User conversationUser = ((User) conversation.getConversationWith());
            name = conversationUser.getName();
            avatar = conversationUser.getAvatar();
            status = conversationUser.getStatus();
            Log.e("Conversation:",conversation.getConversationWith().toString());
            setStatusIndicator(status);
        } else {
            name = ((Group) conversation.getConversationWith()).getName();
            avatar = ((Group) conversation.getConversationWith()).getIcon();
            hideStatusIndicator(true);
        }

        setUnreadCount(conversation.getUnreadMessageCount());
        setTitle(name);
//        conversationViewHolder.conversationListRowBinding.messageCount.setCountBackground(Color.parseColor(FeatureRestriction.getColor()));

        if (avatar != null) {
            setAvatar(avatar, name);
        } else {
            setAvatar("", name);
        }
    }

    private void initViewComponent(Context context,AttributeSet attrs,int defStyle) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        View view = View.inflate(context, R.layout.cometchat_conversation_item, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatConversationListItem,
                0, 0);
        addView(view);

        boolean avatarHidden = a.getBoolean(R.styleable
                .CometChatConversationListItem_conversationListItem_hideAvatar,false);
        boolean titleHidden = a.getBoolean(R.styleable
                .CometChatConversationListItem_conversationListItem_hideTitle,false);
        int titleColor = a.getColor(R.styleable
                .CometChatConversationListItem_conversationListItem_titleColor,0);
        boolean subTitleHidden = a.getBoolean(R.styleable
                .CometChatConversationListItem_conversationListItem_hideSubTitle,false);
        int subTitleColor = a.getColor(R.styleable
                .CometChatConversationListItem_conversationListItem_subTitleColor,0);
        boolean userPresenceHidden = a.getBoolean(R.styleable
                .CometChatConversationListItem_conversationListItem_hideUserPresence,false);
        boolean helperTextHidden = a.getBoolean(R.styleable
                .CometChatConversationListItem_conversationListItem_hideHelperText,true);
        int helperTextColor = a.getColor(R.styleable
                .CometChatConversationListItem_conversationListItem_helperTextColor,0);
        boolean timeHidden = a.getBoolean(R.styleable
                .CometChatConversationListItem_conversationListItem_hideTime,false);
        int timeColor = a.getColor(R.styleable
                .CometChatConversationListItem_conversationListItem_timeColor,0);

        avatarSection = view.findViewById(R.id.avatar_section);
        conversationListItemTitle = view.findViewById(R.id.conversationListItem_title);
        conversationListItemSubTitle = view.findViewById(R.id.conversationListItem_subtitle);
        conversationListItemTypingIndicator = view.findViewById(R.id.conversationListItem_typingIndicator);
        conversationListItemAvatar = view.findViewById(R.id.conversationListItem_avatar);
        conversationListUserPresence = view.findViewById(R.id.conversationListItem_userPresence);
        conversationListBadgeCount = view.findViewById(R.id.conversationListItem_unreadcount);
        conversationListItemHelperText = view.findViewById(R.id.conversationListItem_helperText);
        conversationListItemTime = view.findViewById(R.id.conversationListItem_time);
        conversationListItemReceipt = view.findViewById(R.id.conversationListItem_receipt);
        hideAvatar(avatarHidden);
//        hideTitle(titleHidden);
        setTitleColor(titleColor);
//        hideSubTitle(subTitleHidden);
        setSubTitleColor(subTitleColor);
        hideStatusIndicator(userPresenceHidden);
        hideThreadIndicator(helperTextHidden);
        setThreadIndicatorColor(helperTextColor);
        hideReceipt(false);
        hideTime(timeHidden);
        setTimeColor(timeColor);
    }


    public void setTitle(String titleStr) {
        conversationListItemTitle.setText(titleStr);
    }

    //
//    public void hideTitle(boolean isHidden) {
//        if (isHidden)
//            conversationListItemTitle.setVisibility(View.GONE);
//        else
//            conversationListItemTitle.setVisibility(View.VISIBLE);
//    }

    public void setTitleFont(String fonts) {
        conversationListItemTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }


    public void setTitleColor(@ColorInt int color) {
        if (conversationListItemTitle!=null && color!=0)
            conversationListItemTitle.setTextColor(color);
    }

    public void setSubTitle(String subTitleStr) {
        conversationListItemSubTitle.setText(subTitleStr);
    }

    public void setSubTitleColor(@ColorInt int color) {
        if (color!=0 && conversationListItemSubTitle!=null)
            conversationListItemSubTitle.setTextColor(color);
    }

    public void setSubTitleFont(String fonts) {
        conversationListItemSubTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }

//    public void hideSubTitle(boolean isHidden) {
//        if (isHidden)
//            conversationListItemSubTitle.setVisibility(View.GONE);
//        else
//            conversationListItemSubTitle.setVisibility(View.VISIBLE);
//    }

    public void setStatusIndicator(@CometChatStatusIndicator.Presence String userPresenceStr) {
        if (conversationListUserPresence!=null)
            conversationListUserPresence.status(userPresenceStr);
    }

    public void hideStatusIndicator(boolean isHidden) {
        if (conversationListUserPresence!=null) {
            if (isHidden)
                conversationListUserPresence.setVisibility(View.GONE);
            else
                conversationListUserPresence.setVisibility(View.VISIBLE);
        }
    }

    public void setUnreadCount(int count) {
        if (conversationListBadgeCount != null) {
            conversationListBadgeCount.setCount(count);
            conversationListBadgeCount.cornerRadius(32f);
        }
    }

    public void hideUnreadCount(boolean isHidden) {
        if (conversationListBadgeCount!=null) {
            if (isHidden)
                conversationListBadgeCount.setVisibility(View.GONE);
            else
                conversationListBadgeCount.setVisibility(View.VISIBLE);
        }
    }

    public void setAvatar(@NonNull String url, String initials) {
        if (conversationListItemAvatar!=null) {
            conversationListItemAvatar.setAvatar(url);
            if (url == null)
                conversationListItemAvatar.setInitials(initials);
            else if (url != null && url.isEmpty())
                conversationListItemAvatar.setInitials(initials);
        }
    }

    public void setAvatar(Drawable drawable) {
        if (conversationListItemAvatar!=null)
            conversationListItemAvatar.setDrawable(drawable);
    }

    public void hideAvatar(boolean isHidden) {
        if (conversationListItemAvatar!=null) {
            if (isHidden)
                conversationListItemAvatar.setVisibility(View.GONE);
            else
                conversationListItemAvatar.setVisibility(View.VISIBLE);
        }
    }
    public void setThreadIndicator(String title) {
        conversationListItemHelperText.setText(title);
    }

    public void setThreadIndicatorColor(@ColorInt int color) {
        if (conversationListItemHelperText!=null && color!=0) {
            conversationListItemHelperText.setTextColor(color);
        }
    }
    public void hideThreadIndicator(boolean isHidden) {
        if (conversationListItemHelperText!=null) {
            if (isHidden)
                conversationListItemHelperText.setVisibility(View.GONE);
            else
                conversationListItemHelperText.setVisibility(View.VISIBLE);
        }
    }

    public void setThreadIndicatorFont(String font) {
        if (conversationListItemHelperText!=null)
            conversationListItemHelperText.setTypeface(fontUtils.getTypeFace(font));
    }

    public void setTime(long timestamp) {
        if (context!=null)
            conversationListItemTime.setText(Utils.getLastMessageDate(context,timestamp));
    }

    public void setTimeFont(String fonts) {
        conversationListItemTime.setTypeface(fontUtils.getTypeFace(fonts));
    }

    public void hideTime(boolean isHidden) {
        if (conversationListItemTime!=null) {
            if (isHidden)
                conversationListItemTime.setVisibility(View.GONE);
            else
                conversationListItemTime.setVisibility(View.VISIBLE);
        }
    }

    public void setTimeColor(@ColorInt int color) {
        if (conversationListItemTime!=null && color!=0)
            conversationListItemTime.setTextColor(color);
    }

    public void setReceipt(BaseMessage baseMessage) {
         conversationListItemReceipt.messageObject(baseMessage);
    }

    public void hideReceipt(boolean isHidden) {
        if (isHidden)
            conversationListItemReceipt.setVisibility(View.GONE);
        else
            conversationListItemReceipt.setVisibility(View.VISIBLE);
    }

    public void setTypingIndicatorColor(@ColorInt int color) {
        if (color!=0) {
            typingIndicatorColor = color;
            conversationListItemTypingIndicator.setTextColor(color);
        }
    }

    public void showTypingIndicator(boolean isVisible){
        if (isVisible) {
            conversationListItemTypingIndicator.setVisibility(View.VISIBLE);
            conversationListItemSubTitle.setVisibility(View.GONE);
        }
        else {
            conversationListItemSubTitle.setVisibility(View.VISIBLE);
            conversationListItemTypingIndicator.setVisibility(View.GONE);
        }
    }

    public void setTypingIndicator(String title ) {
        conversationListItemTypingIndicator.setText(title);
    }

    public void setTypingIndicatorFont(String font) {
        if (conversationListItemTypingIndicator!=null)
            conversationListItemTypingIndicator.setTypeface(fontUtils.getTypeFace(font));
    }

    public CometChatAvatar getAvatar() {
        return conversationListItemAvatar;
    }

    public CometChatStatusIndicator getStatusIndicator() {
        return conversationListUserPresence;
    }

    public CometChatBadgeCount getBadgeCount() {
        return conversationListBadgeCount;
    }

    public CometChatMessageReceipt getMessageReceipt() {
        return conversationListItemReceipt;
    }

    public void hideDeletedMessage(boolean isDeleteMessageHidden) {
        if (conversation!=null) {
            if (conversation.getLastMessage() != null) {
                if (conversation.getLastMessage().getDeletedAt() > 0 && isDeleteMessageHidden)
                    conversationListItemSubTitle.setText("");
            }
        }
    }

    public void hideGroupActionMessage(boolean isGroupActionHidden) {
        if (isGroupActionHidden && conversation.getLastMessage().getType()
                .equalsIgnoreCase(CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)) {
            conversationListItemSubTitle.setText("");
        }
    }
}