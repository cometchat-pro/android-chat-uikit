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
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
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
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

public class CometChatAudioBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;
    private TextView subtitleTv;
    private TextView titleTv;
    private ImageView button;
    private TextView tvUser;
    private CometChatAvatar ivUser;
    private CometChatDate txtTime;
    private CometChatMessageReceipt messageReceipt;
    private TextView tvThreadReplyCount;

    private CometChatMessageReaction reactionLayout;

    private View receiptLayout;

    private String alignment = Alignment.RIGHT;

    private final String TAG = "AudioMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private int reactionStrokeColor = Color.parseColor(CometChatTheme.primaryColor);

    private int borderWidth = 0;

    private int borderColor = 0;

    private String url;

    private int layoutId;

    public CometChatAudioBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatAudioBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.AudioMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.AudioMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.AudioMessageBubble_backgroundColor,0);
        Drawable audioMessageAvatar = a.getDrawable(R.styleable.AudioMessageBubble_avatar);
        int avatarVisibility = a.getInt(R.styleable.AudioMessageBubble_avatarVisibility,View.VISIBLE);
        int userNameVisibility = a.getInt(R.styleable.AudioMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.AudioMessageBubble_userName);
        int color = a.getColor(R.styleable.AudioMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.AudioMessageBubble_messageAlignment,0));

        int borderWidth = a.getInt(R.styleable.AudioMessageBubble_borderWidth,0);
        int borderColor = a.getColor(R.styleable.AudioMessageBubble_borderColor,0);

        String title = a.getString(R.styleable.AudioMessageBubble_title);
        int titleColor = a.getColor(R.styleable.AudioMessageBubble_titleColor,0);
        String subtitle = a.getString(R.styleable.AudioMessageBubble_subtitle);
        int subtitleColor = a.getColor(R.styleable.AudioMessageBubble_subtitleColor,0);

        Drawable icon = a.getDrawable(R.styleable.AudioMessageBubble_icon);
        int iconVisibility = a.getInt(R.styleable.AudioMessageBubble_iconVisibility,View.VISIBLE);

        if (alignment== Alignment.LEFT)
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_audio_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_audio_bubble,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        avatar(audioMessageAvatar);
        avatarVisibility(avatarVisibility);
        userName(userName);
        userNameVisibility(userNameVisibility);
        userNameColor(color);
        title(title);
        titleColor(titleColor);
        subtitle(subtitle);
        subtitleColor(subtitleColor);
        icon(icon);
        iconVisibility(iconVisibility);
        borderColor(borderColor);
        borderWidth(borderWidth);
        cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });
    }

    private void initView(View view) {
        addView(view);
        tvUser = view.findViewById(R.id.tv_user);
        txtTime = view.findViewById(R.id.time);
        messageReceipt = view.findViewById(R.id.receiptsIcon);
        ivUser = view.findViewById(R.id.iv_user);

        titleTv = view.findViewById(R.id.title);
        subtitleTv = view.findViewById(R.id.audiolength_tv);
        button = view.findViewById(R.id.playBtn);
        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);
        receiptLayout = view.findViewById(R.id.receipt_layout);
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
                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.audio);
        if(messageTemplate!=null)
            layoutId = messageTemplate.getView();
//        dataView = messageTemplate.getDataView();
        if (layoutId != 0) {
            View customView = LayoutInflater.from(context).inflate(layoutId,null);
            cvMessageBubbleLayout.setVisibility(View.GONE);
            if (customView.getParent()!=null)
                ((ViewGroup)customView.getParent()).removeAllViewsInLayout();
            cvMessageBubble.addView(customView);
            button = customView.findViewById(R.id.playBtn);
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


    public void iconVisibility(int buttonVisibility) {
        if (button!=null)
            button.setVisibility(buttonVisibility);
    }

    public void icon(Drawable buttonDrawable) {
        if (button!=null && buttonDrawable!=null)
            button.setImageDrawable(buttonDrawable);
    }

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setShapeAppearanceModel(cvMessageBubble.getShapeAppearanceModel()
                .toBuilder()
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight).build());
    }
    public void cornerRadius(float radius) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setRadius(radius);
    }

    public MaterialCardView getBubbleView() {
        return cvMessageBubble;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (cvMessageBubble!=null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
            gd.setCornerRadius(cvMessageBubble.getRadius());
            cvMessageBubble.setBackgroundDrawable(gd);
        }
    }
    public void backgroundColor(@ColorInt int bgColor) {
        if (cvMessageBubble!=null) {
            if (bgColor != 0)
                cvMessageBubble.setCardBackgroundColor(bgColor);
        }
    }

    public void audioURL(String url) {
        if (url!=null) {
            this.url = url;
            baseMessage = new BaseMessage();
            Attachment attachment = new Attachment();
            if (titleTv.getText().toString().isEmpty())
                attachment.setFileName("Audio File");
            else
                attachment.setFileName(titleTv.getText().toString());
            attachment.setFileExtension("mp3");
            attachment.setFileMimeType("audio/mpeg");
            attachment.setFileSize(0);
            attachment.setFileUrl(url);
            ((MediaMessage) baseMessage).setAttachment(attachment);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageBubbleListener.onClick(baseMessage);
                }
            });
        }
    }

    public void borderColor(@ColorInt int color) {
        borderColor = color;
        if (cvMessageBubble!=null) {
            if (color!=0)
                cvMessageBubble.setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        borderWidth = width;
        if (cvMessageBubble!=null)
            cvMessageBubble.setStrokeWidth(width);
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

    public void title(String title) {
        if (titleTv!=null && title!=null && !title.isEmpty())
            titleTv.setText(title);
    }

    public void titleColor(@ColorInt int color) {
        if (color!=0 && titleTv!=null)
            titleTv.setTextColor(color);
    }

    public void subtitle(String title) {
        if (subtitleTv!=null)
            subtitleTv.setText(title);
    }

    public void subtitleColor(@ColorInt int color) {
        if (color!=0 && subtitleTv!=null)
            subtitleTv.setTextColor(color);
    }

    public void setReactionBorderColor(@ColorInt int strokeColor) {
        this.reactionStrokeColor = strokeColor;
        reactionLayout.setBorderColor(strokeColor);
    }
    public void messageAlignment(@Alignment.MessageAlignment String mAlignment) {
        if (alignment!=null && alignment== Alignment.LEFT)
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_audio_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_audio_bubble,null);

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

        if (((MediaMessage)baseMessage).getAttachment()!=null) {
            subtitleTv.setText(Utils.getFileSize(((MediaMessage) baseMessage).getAttachment().getFileSize()));
            button.setVisibility(View.VISIBLE);
        } else {
            subtitleTv.setText("-");
            button.setVisibility(View.GONE);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               messageBubbleListener.onClick(baseMessage);
            }
        });
    }

    public void setMessageBubbleListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }

    public void buttonBackgroundTint(int color) {
        if (button!=null && color!=0)
            button.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public void buttonTint(int color) {
        if (button!=null && color!=0)
            button.setImageTintList(ColorStateList.valueOf(color));
    }
}
