package com.cometchat.chatuikit.calls;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.calls.callbutton.CometChatCallButtons;
import com.cometchat.chatuikit.calls.callbubble.CometChatCallBubble;
import com.cometchat.chatuikit.calls.callmanger.CallManager;
import com.cometchat.chatuikit.calls.outgoingcall.CometChatCallActivity;
import com.cometchat.chatuikit.calls.utils.CallUtils;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatCallEvents;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.utils.MessageBubbleUtils;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.chatuikit.shared.views.button.ButtonStyle;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.List;

public class CallingExtensionDecorator extends DataSourceDecorator {
    private final CallManager callManager;
    private Call tempCall;

    public CallingExtensionDecorator(Context context_, DataSource dataSource) {
        super(dataSource);
        callManager = new CallManager(context_);
        addListener();
    }

    public void addListener() {
        String LISTENER_ID = System.currentTimeMillis() + "";
        CometChat.addCallListener(LISTENER_ID, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {
                if (tempCall == null) {
                    tempCall = call;
                    initiateCallService(call);
                } else {
                    rejectCall(call.getSessionId());
                }
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {
                tempCall = call;
            }

            @Override
            public void onOutgoingCallRejected(Call call) {
                tempCall = null;
            }

            @Override
            public void onIncomingCallCancelled(Call call) {
                tempCall = null;
                endCallService();
            }
        });

        CometChatCallEvents.addListener(LISTENER_ID, new CometChatCallEvents() {
            @Override
            public void ccOutgoingCall(Call call) {
                tempCall = call;
                super.ccOutgoingCall(call);
            }

            @Override
            public void ccCallAccepted(Call call) {
                tempCall = call;
            }

            @Override
            public void ccCallRejected(Call call) {
                resetCall(call);
            }

            @Override
            public void ccCallEnded(Call call) {
                resetCall(call);
            }
        });
    }

    private void resetCall(Call call) {
        if (tempCall != null && call != null && tempCall.getSessionId().equalsIgnoreCase(call.getSessionId())) {
            tempCall = null;
        }
    }

    public void rejectCall(String sessionId) {
        CometChat.rejectCall(sessionId, CometChatConstants.CALL_STATUS_BUSY, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {
                for (CometChatCallEvents events : CometChatCallEvents.callingEvents.values()) {
                    events.ccCallRejected(call);
                }
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    private void endCallService() {
        callManager.endCall();
    }

    private void initiateCallService(Call call) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                callManager.startIncomingCall(call);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getDefaultMessageTypes() {
        List<String> messageTypes = super.getDefaultMessageTypes();
        messageTypes.add(CometChatConstants.CALL_TYPE_AUDIO);
        messageTypes.add(CometChatConstants.CALL_TYPE_VIDEO);
        messageTypes.add(UIKitConstants.MessageType.MEETING);
        return messageTypes;
    }

    @Override
    public List<String> getDefaultMessageCategories() {
        List<String> categories = super.getDefaultMessageCategories();
        categories.add(UIKitConstants.MessageCategory.CALL);
        categories.add(UIKitConstants.MessageCategory.CUSTOM);
        return categories;
    }

    @Override
    public List<CometChatMessageTemplate> getMessageTemplates() {
        List<CometChatMessageTemplate> templates = super.getMessageTemplates();
        templates.add(getConferenceCallTemplate());
        templates.add(getDefaultVoiceCallActionTemplate());
        templates.add(getDefaultVideoCallActionTemplate());
        return templates;
    }

    public CometChatMessageTemplate getConferenceCallTemplate() {
        return new CometChatMessageTemplate().setCategory(UIKitConstants.MessageCategory.CUSTOM).setOptions((context, baseMessage, group) -> ChatConfigurator.getUtils().getImageMessageOptions(context, baseMessage, group)).setBottomView((context, baseMessage, isLeftAlign) -> ChatConfigurator.getUtils().getBottomView(context, baseMessage, isLeftAlign)).setType(UIKitConstants.MessageType.MEETING).setContentView((context, baseMessage, isLeftAlign) -> {
            if (baseMessage.getDeletedAt() == 0) {
                CometChatTheme theme = CometChatTheme.getInstance(context);
                CometChatCallBubble cometChatCallBubble = new CometChatCallBubble(context);

                if (!baseMessage.getSender().getUid().equals(CometChatUIKit.getLoggedInUser().getUid())) {
                    cometChatCallBubble.setTitle(baseMessage.getSender().getName() + " " + context.getString(R.string.has_shared_group_call));
                } else {
                    cometChatCallBubble.setTitle(context.getString(R.string.you_created_group_call));
                }
                Drawable drawable = context.getDrawable(R.drawable.action_sheet_background);
                if (isLeftAlign.equals(UIKitConstants.MessageBubbleAlignment.LEFT)) {
                    cometChatCallBubble.setTitleColor(theme.getPalette().getAccent());
                    cometChatCallBubble.setIconTint(theme.getPalette().getPrimary());
                } else if (isLeftAlign.equals(UIKitConstants.MessageBubbleAlignment.RIGHT)) {
                    cometChatCallBubble.setTitleColor(theme.getPalette().getAccent900());
                    cometChatCallBubble.setIconTint(theme.getPalette().getAccent900());
                    cometChatCallBubble.setBackgroundColor(theme.getPalette().getPrimary());
                }
                drawable.setTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
                cometChatCallBubble.setButtonBackgroundDrawable(drawable);
                cometChatCallBubble.setButtonBackgroundColor(context.getResources().getColor(R.color.textColorWhite));
                cometChatCallBubble.setButtonTextColor(theme.getPalette().getPrimary());
                cometChatCallBubble.setButtonText(context.getResources().getString(R.string.join));
                cometChatCallBubble.setTitleAppearance(theme.getTypography().getName());
                cometChatCallBubble.setButtonTextAppearance(theme.getTypography().getText3());

                cometChatCallBubble.setOnClick(() -> CometChatCallActivity.launch(context, baseMessage, baseMessage.getReceiverType()));
                return cometChatCallBubble;
            } else return MessageBubbleUtils.getDeletedBubble(context);
        });
    }

    private CometChatMessageTemplate getDefaultVoiceCallActionTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_CALL).setType(CometChatConstants.CALL_TYPE_AUDIO).setContentView((context, baseMessage, isLeftAlign) -> {
            CometChatTheme theme = CometChatTheme.getInstance(context);
            TextBubbleStyle textBubbleStyle = new TextBubbleStyle();
            if (baseMessage instanceof Call) {
                if (CallUtils.isMissedCall((Call) baseMessage)) {
                    textBubbleStyle.setCompoundDrawableIconTint(theme.getPalette().getError()).setTextColor(theme.getPalette().getError());
                } else
                    textBubbleStyle.setCompoundDrawableIconTint(theme.getPalette().getAccent()).setTextColor(theme.getPalette().getAccent());

                textBubbleStyle.setBackground(context.getResources().getDrawable(R.drawable.action_dotted_border)).setTextAppearance(theme.getTypography().getCaption1());
                return MessageBubbleUtils.getActionContentView(context, CallUtils.getCallStatus(context, baseMessage), null, textBubbleStyle, R.drawable.call_icon);
            } else return null;
        });
    }

    private CometChatMessageTemplate getDefaultVideoCallActionTemplate() {
        return new CometChatMessageTemplate().setCategory(CometChatConstants.CATEGORY_CALL).setType(CometChatConstants.CALL_TYPE_VIDEO).setContentView((context, baseMessage, isLeftAlign) -> {
            CometChatTheme theme = CometChatTheme.getInstance(context);
            TextBubbleStyle textBubbleStyle = new TextBubbleStyle();
            if (baseMessage instanceof Call) {
                if (CallUtils.isMissedCall((Call) baseMessage)) {
                    textBubbleStyle.setCompoundDrawableIconTint(theme.getPalette().getError()).setTextColor(theme.getPalette().getError());
                } else
                    textBubbleStyle.setCompoundDrawableIconTint(theme.getPalette().getAccent()).setTextColor(theme.getPalette().getAccent());

                textBubbleStyle.setBackground(context.getResources().getDrawable(R.drawable.action_dotted_border)).setTextAppearance(theme.getTypography().getCaption1());
                return MessageBubbleUtils.getActionContentView(context, CallUtils.getCallStatus(context, baseMessage), null, textBubbleStyle, R.drawable.ic_video_call);
            } else return null;
        });
    }

    @Override
    public String getLastConversationMessage(Context context, Conversation conversation) {
        return _getLastConversationMessage(context, conversation);
    }

    private String _getLastConversationMessage(Context context, Conversation conversation) {
        String lastMessageText;
        BaseMessage baseMessage = conversation.getLastMessage();
        if (baseMessage != null) {
            String message = getLastMessage(context, baseMessage);
            if (message != null) {
                lastMessageText = getLastMessage(context, baseMessage);
            } else lastMessageText = super.getLastConversationMessage(context, conversation);
            if (baseMessage.getDeletedAt() > 0) {
                lastMessageText = context.getString(R.string.this_message_deleted);
            }
        } else {
            lastMessageText = context.getResources().getString(R.string.tap_to_start_conversation);
        }
        return lastMessageText;
    }

    private String getLastMessage(Context context, BaseMessage lastMessage) {
        String message = null;
        if (CometChatConstants.CATEGORY_CALL.equals(lastMessage.getCategory())) {
            if (lastMessage instanceof Call) {
                if (!CallUtils.isVideoCall((Call) lastMessage))
                    message = "📞 " + CallUtils.getCallStatus(context, lastMessage);
                else message = "📹 " + CallUtils.getCallStatus(context, lastMessage);
            }
        } else if (CometChatConstants.CATEGORY_CUSTOM.equals(lastMessage.getCategory()) && UIKitConstants.MessageType.MEETING.equalsIgnoreCase(lastMessage.getType())) {
            message = Utils.getMessagePrefix(lastMessage, context) + context.getString(R.string.custom_message_meeting);
        }
        return message;
    }

    @Override
    public View getAuxiliaryHeaderMenu(Context context, User user, Group group) {
        LinearLayout layout = new LinearLayout(context);
        View presentView = super.getAuxiliaryHeaderMenu(context, user, group);
        CometChatCallButtons cometChatCallButtons = new CometChatCallButtons(context);
        cometChatCallButtons.getVideoCallButton().hideButtonBackground(true);
        cometChatCallButtons.getVoiceCallButton().hideButtonBackground(true);
        cometChatCallButtons.setVideoCallIcon(R.drawable.video_icon);
        cometChatCallButtons.setVoiceCallIcon(R.drawable.call_icon);
        cometChatCallButtons.hideButtonText(true);
        cometChatCallButtons.setMarginForButtons(Utils.convertDpToPx(context, 1));
        cometChatCallButtons.setButtonStyle(new ButtonStyle().setButtonIconTint(CometChatTheme.getInstance(context).getPalette().getPrimary()));
        cometChatCallButtons.setUser(user);
        cometChatCallButtons.setGroup(group);
        layout.addView(cometChatCallButtons);
        if (presentView != null) {
            Utils.removeParentFromView(presentView);
            layout.addView(presentView);
        }
        return layout;
    }

    @Override
    public String getId() {
        return CallingExtensionDecorator.class.getName();
    }
}
