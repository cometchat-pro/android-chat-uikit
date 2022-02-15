package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class CometChatCallActionBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private RelativeLayout rlMessageBubble;

    private ImageView icon;
    private TextView title;

    private MaterialButton joinBtn;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;
    private CometChatAvatar ivUser;
    private TextView tvUser;

    private CometChatDate txtTime;
    private CometChatMessageReceipt messageReceipt;
    private View receiptLayout;

    private TextView tvThreadReplyCount;

    private CometChatMessageReaction reactionLayout;

    private String alignment = Alignment.RIGHT;

    private final String TAG = "CallActionBubble";

    private MessageBubbleListener messageBubbleListener;

    private final int reactionStrokeColor = Color.parseColor(CometChatTheme.primaryColor);

    private int layoutId;

    public CometChatCallActionBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatCallActionBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatCallActionBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatCallActionBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.ActionMessageBubble,
                0, 0);

        float cornerRadius  = a.getFloat(R.styleable.ActionMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.ActionMessageBubble_backgroundColor,0);
        Drawable messageAvatar = a.getDrawable(R.styleable.ActionMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.ActionMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.ActionMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.ActionMessageBubble_userName);
        int color = a.getColor(R.styleable.ActionMessageBubble_userNameColor,0);

        String titleStr = a.getString(R.styleable.ActionMessageBubble_title);
        int titleColor = a.getColor(R.styleable.ActionMessageBubble_titleColor,0);

        String time = a.getString(R.styleable.ActionMessageBubble_time);
        int timeColor = a.getColor(R.styleable.ActionMessageBubble_timeColor,0);

        Drawable iconDrawable = a.getDrawable(R.styleable.ActionMessageBubble_icon);
        int iconColor = a.getColor(R.styleable.ActionMessageBubble_iconTint,0);

        int borderColor = a.getColor(R.styleable.ActionMessageBubble_borderColor,0);
        int borderWidth = a.getInt(R.styleable.ActionMessageBubble_borderWidth,0);

        alignment = Alignment.getValue(a.getInt(R.styleable.ActionMessageBubble_messageAlignment,0));
        if (alignment.equalsIgnoreCase(Alignment.LEFT))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_call_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_call_bubble,null);

        initView(view);
        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        avatar(messageAvatar);
        avatarVisibility(hideAvatar);
        userName(userName);
        userNameVisibility(hideUserName);
        userNameColor(color);
        title(titleStr);
        titleColor(titleColor);
        time(time);
        timeColor(timeColor);
        icon(iconDrawable);
        iconTint(iconColor);
        borderColor(borderColor);
        borderWidth(borderWidth);

//        setColorFilter(baseMessage,cvMessageView);



    }

    public void borderColor(@ColorInt int color) {
        if (color!=0 && cvMessageBubble!=null) {
            cvMessageBubble.setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setStrokeWidth(width);
    }

    private void initView(View view) {
        addView(view);

        icon = view.findViewById(R.id.icon);
        title = view.findViewById(R.id.title);

        tvUser= view.findViewById(R.id.tv_user);
        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        txtTime = view.findViewById(R.id.time);

        ivUser = view.findViewById(R.id.iv_user);
        rlMessageBubble = view.findViewById(R.id.rl_message);

        if (cvMessageBubble!=null) {
            cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    messageBubbleListener.onLongCLick(baseMessage);
                    return true;
                }
            });
        }

        //CustomView
        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.callAction);
        if(messageTemplate!=null)
            layoutId = messageTemplate.getView();
//        dataView = messageTemplate.getDataView();
        if (layoutId != 0) {
            View customView = LayoutInflater.from(context).inflate(layoutId, null);
            cvMessageBubbleLayout.setVisibility(View.GONE);
            if (customView.getParent() != null)
                ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
            cvMessageBubble.addView(customView);
            title = customView.findViewById(R.id.title);
            customView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    messageBubbleListener.onLongCLick(baseMessage);
                    return true;
                }
            });
            customView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageBubbleListener.onClick(baseMessage);
                }
            });
        }
    }


    public void icon(Drawable imageDrawable) {
        if (icon!=null && imageDrawable!=null)
            icon.setImageDrawable(imageDrawable);
    }

    public void iconTint(@ColorInt int color){
        if (icon!=null && color!=0)
        icon.setImageTintList(ColorStateList.valueOf(color));
    }


    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setShapeAppearanceModel(cvMessageBubble.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopLeftCornerSize(topLeft)
                    .setTopRightCornerSize(topRight)
                    .setBottomLeftCornerSize(bottomLeft)
                    .setBottomRightCornerSize(bottomRight)
                    .build());
    }
    public void cornerRadius(float radius) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setRadius(radius);
    }

    public MaterialCardView getBubbleView() {
        return cvMessageBubble;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (cvMessageBubble !=null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
            gd.setCornerRadius(cvMessageBubble.getRadius());
            cvMessageBubble.setBackgroundDrawable(gd);
        }
    }
    public void backgroundColor(@ColorInt int bgColor) {
        if (cvMessageBubble !=null) {
            if (bgColor != 0)
                cvMessageBubble.setCardBackgroundColor(bgColor);
        }
    }

    public void avatar(Drawable avatarDrawable) {
        if (ivUser!=null)
            ivUser.setDrawable(avatarDrawable);
    }

    public void avatar(String url,String initials) {
        if (ivUser!=null) {
            ivUser.setInitials(initials);
            if (url != null)
                ivUser.setAvatar(url);
        }
    }

    public void avatarVisibility(int visibility) {
        if (ivUser!=null) {
            ivUser.setVisibility(visibility);
        }
    }

    public void userName(String username) {
        if (tvUser!=null)
            tvUser.setText(username);
    }
    public void userNameFont(String font) {
        if (tvUser!=null)
            tvUser.setTypeface(fontUtils.getTypeFace(font));
    }

    public void userNameColor(@ColorInt int color){
        if (tvUser!=null && color!=0)
            tvUser.setTextColor(color);
    }

    public void userNameVisibility(int visibility) {
        if (tvUser!=null) {
            tvUser.setVisibility(visibility);
        }
    }

    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;

        if (txtTime!=null) {
            txtTime.setDate(baseMessage.getSentAt(), "hh:mm a");
            txtTime.setTransparentBackground(true);
        }

        if (tvUser!=null)
            tvUser.setText(baseMessage.getSender().getName());
        if (ivUser!=null)
            ivUser.setAvatar(baseMessage.getSender().getAvatar());

        Call call = ((Call) baseMessage);

        String callMessageText = "";

        if(call.getType().equals(CometChatConstants.CALL_TYPE_VIDEO)) {
            callMessageText = context.getResources().getString(R.string.video_call);
        }
        else {
            callMessageText = context.getResources().getString(R.string.audio_call);
        }

        if (call.getCallStatus().equals(CometChatConstants.CALL_STATUS_INITIATED)) {
            callMessageText += " "+context.getString(R.string.started);
        }
        else if(call.getCallStatus().equals(CometChatConstants.CALL_STATUS_UNANSWERED) ||
                call.getCallStatus().equals(CometChatConstants.CALL_STATUS_CANCELLED)) {
            callMessageText = context.getString(R.string.missed)+" "+callMessageText;
        } else if(call.getCallStatus().equals(CometChatConstants.CALL_STATUS_REJECTED) ||
                call.getCallStatus().equals(CometChatConstants.CALL_STATUS_BUSY)) {
            callMessageText += " "+context.getResources().getString(R.string.rejected_call);
        } else {
            callMessageText += " "+context.getString(R.string.ended);
        }

        title(callMessageText);
        if (cvMessageBubble!=null) {
            cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (messageBubbleListener!=null)
                        messageBubbleListener.onLongCLick(baseMessage);
                    return true;
                }
            });
        }
    }


    public void title(String str) {
        if (title!=null && str!=null)
            title.setText(str);
    }
    public void titleColor(@ColorInt int color) {
        if (title!=null && color!=0)
            title.setTextColor(color);
    }
    public void titleFont(String font) {
        if (title!=null)
            title.setTypeface(fontUtils.getTypeFace(font));
    }

    public void time(String str) {
        if (txtTime!=null && str!=null)
            txtTime.text(str);
    }
    public void timeColor(@ColorInt int color) {
        if (txtTime!=null && color!=0)
            txtTime.textColor(color);
    }
    public void timeFont(String font) {
        if (txtTime!=null)
            txtTime.textFont(font);
    }


    public void setMessageBubbleListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }

    public void messageAlignment(String alignment) {
        if (alignment!=null && alignment.equalsIgnoreCase(Alignment.LEFT))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_call_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_call_bubble,null);

        removeAllViewsInLayout();
        initView(view);
    }
}
