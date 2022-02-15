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

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.TimeAlignment;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

public class CometChatDocumentBubble extends RelativeLayout {

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

    private final String TAG = "WhiteboardMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private int reactionStrokeColor = Color.parseColor(CometChatTheme.primaryColor);

    private String url;

    private int layoutId;

    public CometChatDocumentBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatDocumentBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatDocumentBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatDocumentBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.WriteboardMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.WriteboardMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.WriteboardMessageBubble_backgroundColor,0);
        Drawable messageAvatar = a.getDrawable(R.styleable.WriteboardMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.WriteboardMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.WriteboardMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.WriteboardMessageBubble_userName);
        int color = a.getColor(R.styleable.WriteboardMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.WriteboardMessageBubble_messageAlignment,0));

        String titleStr = a.getString(R.styleable.WriteboardMessageBubble_title);
        int titleColor = a.getColor(R.styleable.WriteboardMessageBubble_titleColor,0);

        String buttonText = a.getString(R.styleable.WriteboardMessageBubble_buttonText);

        Drawable iconDrawable = a.getDrawable(R.styleable.WriteboardMessageBubble_icon);
        int iconColor = a.getColor(R.styleable.WriteboardMessageBubble_iconTint,0);


        int borderColor = a.getColor(R.styleable.WriteboardMessageBubble_borderColor,0);
        int borderWidth = a.getInt(R.styleable.WriteboardMessageBubble_borderWidth,0);

        if (alignment.equalsIgnoreCase(Alignment.LEFT))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_writeboard_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_writeboard_bubble,null);

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
        icon(iconDrawable);
        iconTint(iconColor);
        buttonText(buttonText);
        borderColor(borderColor);
        borderWidth(borderWidth);

        cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });


//        setColorFilter(baseMessage,cvMessageView);



    }

    private void initView(View view) {
        addView(view);

        icon = view.findViewById(R.id.icon);
        title = view.findViewById(R.id.title);
        joinBtn = view.findViewById(R.id.join_button);

        tvUser= view.findViewById(R.id.tv_user);
        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);
        txtTime = view.findViewById(R.id.time);
        messageReceipt = view.findViewById(R.id.receiptsIcon);
        receiptLayout = view.findViewById(R.id.receipt_layout);
        ivUser = view.findViewById(R.id.iv_user);
        rlMessageBubble = view.findViewById(R.id.rl_message);
        tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
        reactionLayout = view.findViewById(R.id.reactions_group);
        reactionLayout.setReactionEventListener(new CometChatMessageReaction.OnReactionClickListener() {
            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                messageBubbleListener.onReactionClick(reaction,baseMessageID);
            }
        });
        //CustomView
        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.document);
        if(messageTemplate!=null)
            layoutId = messageTemplate.getView();
//        dataView = messageTemplate.getDataView();
        if (layoutId != 0) {
            View customView = LayoutInflater.from(context).inflate(layoutId, null);
            cvMessageBubbleLayout.setVisibility(View.GONE);
            if (customView.getParent() != null)
                ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
            cvMessageBubble.addView(customView);
            joinBtn = customView.findViewById(R.id.join_button);
            customView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    messageBubbleListener.onLongCLick(baseMessage);
                    return true;
                }
            });
        }

    }

    public void borderWidth(int width) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setStrokeWidth(width);
    }

    public void borderColor(@ColorInt int color) {
        if (color!=0 && cvMessageBubble!=null) {
            cvMessageBubble.setStrokeColor(color);
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

    public void buttonText(String str) {
        if (joinBtn!=null && str!=null)
            joinBtn.setText(str);
    }

    public void buttonTextColor(@ColorInt int color) {
        if (color!=0 && joinBtn!=null)
            joinBtn.setTextColor(color);
    }

    public void buttonTextFont(String font) {
        if (font!=null && joinBtn!=null)
            joinBtn.setTypeface(fontUtils.getTypeFace(font));
    }

    public void buttonIcon(Drawable icon) {
        if (joinBtn!=null && icon!=null)
            joinBtn.setIcon(icon);
    }

    public void buttonIconGravity(int gravity) {
        if (joinBtn!=null)
            joinBtn.setIconGravity(gravity);
    }

    public void setButtonIconTint(@ColorInt int color) {
        if (joinBtn!=null)
            joinBtn.setIconTint(ColorStateList.valueOf(color));
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

    public void setReactionBorderColor(@ColorInt int strokeColor) {
        this.reactionStrokeColor = strokeColor;
        reactionLayout.setBorderColor(strokeColor);
    }
    public void messageAlignment(@Alignment.MessageAlignment String mAlignment) {
        if (mAlignment!=null && mAlignment== Alignment.LEFT)
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_writeboard_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_writeboard_bubble,null);

        removeAllViewsInLayout();
        initView(view);
    }

    public void messageReceiptIcon(CometChatMessageReceipt messageReceipt) {
        if (messageReceipt!=null) {
            messageReceipt.messageDeliveredIcon(messageReceipt.getDeliveredIcon());
            messageReceipt.messageReadIcon(messageReceipt.getReadIcon());
            messageReceipt.messageSentIcon(messageReceipt.getSentIcon());
            messageReceipt.messageErrorIcon(messageReceipt.getErrorIcon());
            messageReceipt.messageProgressIcon(messageReceipt.getProgressIcon());
        }
    }

    public void replyCount(int count) {
        if (count!=0) {
            tvThreadReplyCount.setVisibility(View.VISIBLE);
            tvThreadReplyCount.setText(baseMessage.getReplyCount()+" "+context.getResources().getString(R.string.replies));
        } else {
            tvThreadReplyCount.setVisibility(View.GONE);
        }
    }

    public void setReplyCountColor(@ColorInt int color) {
        if (tvThreadReplyCount!=null)
            tvThreadReplyCount.setTextColor(color);
    }

    public void messageTimeAlignment(TimeAlignment timeAlignment) {
        if (timeAlignment == TimeAlignment.TOP) {
            LayoutParams params = (LayoutParams) receiptLayout.getLayoutParams();
            params.addRule(RelativeLayout.END_OF, R.id.tv_user);
            params.addRule(RelativeLayout.ALIGN_START,0);
            params.addRule(RelativeLayout.BELOW, 0);
            params.topMargin = 0;
            params.leftMargin = 8;

            LayoutParams messageBubbleParam = (LayoutParams)cvMessageBubble.getLayoutParams();
            messageBubbleParam.topMargin = 8;
            messageBubbleParam.bottomMargin = 8;
//            receiptLayout.setLayoutParams(params);
        } else {
            LayoutParams params = (LayoutParams) receiptLayout.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.thread_reply_count);
//            receiptLayout.setLayoutParams(params);
        }
    }

    public void documentUrl(String url) {
        if (url!=null) {
            this.url = url;
            baseMessage = new BaseMessage();
            try {
                JSONObject injectedObject = new JSONObject();//"@injected");
                JSONObject extensionsObject = new JSONObject();//"extensions");
                JSONObject whiteBoardData = new JSONObject();//"whiteboard");
                JSONObject boardUrl = new JSONObject();
                boardUrl.put("document_url", url);
                whiteBoardData.put("document",boardUrl);
                extensionsObject.put("extensions",whiteBoardData);
                injectedObject.put("@injected",extensionsObject);
                baseMessage.setMetadata(injectedObject);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            if (joinBtn!=null) {
                joinBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageBubbleListener.onClick(baseMessage);
                    }
                });
            }
        }
    }
    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
        messageReceipt.messageObject(baseMessage);
        txtTime.setDate(baseMessage.getSentAt(),"hh:mm a");
        txtTime.setTransparentBackground(true);

        if (tvUser!=null)
            tvUser.setText(baseMessage.getSender().getName());
        if (ivUser!=null)
            ivUser.setAvatar(baseMessage.getSender().getAvatar());
        reactionLayout.setMessage(baseMessage);

        if (joinBtn!=null) {
            joinBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageBubbleListener.onClick(baseMessage);
                }
            });
        }

        cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });
    }

    public void title(String str) {
        title.setText(str);
    }
    public void titleColor(@ColorInt int color) {
        if (title!=null && color!=0)
        title.setTextColor(color);
    }
    public void titleFont(String font) {
        title.setTypeface(fontUtils.getTypeFace(font));
    }


    public void setMessageBubbleListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }
}
