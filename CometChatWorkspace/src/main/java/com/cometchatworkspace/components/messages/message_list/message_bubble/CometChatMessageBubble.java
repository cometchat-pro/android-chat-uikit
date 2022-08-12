package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.BR;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageInputData;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.TimeAlignment;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageReceiptConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.ShapeAppearanceModel;

import org.json.JSONException;

public class CometChatMessageBubble extends RelativeLayout implements MessageBubbleListener {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private MaterialCardView bubble;
    private RelativeLayout cvMessageBubble;

    private CometChatAvatar ivUser;
    private TextView tvUser;

    private CometChatDate txtTime;
    private CometChatMessageReceipt messageReceipt;
    private View receiptLayout;

    private TextView divider;
    private TextView tvThreadReplyCount;

    private CometChatMessageReaction reactionLayout;

    private String alignment = Alignment.RIGHT;

    private final String TAG = "MessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private Palette palette;
    private Typography typography;

    private int borderColor = 0;

    private int borderWidth = 0;

    private String url;

    private View childView;

    private int layoutId=0;

    public CometChatMessageBubble(Context context) {
        super(context);
        initComponent(context, null);
    }

    public CometChatMessageBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs);
    }

    public CometChatMessageBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context, attrs);
    }

    public CometChatMessageBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context, attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography= Typography.getInstance();
        fontUtils = FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CometChatMessageBubble,
                0, 0);
        float cornerRadius = a.getFloat(R.styleable.CometChatMessageBubble_corner_radius, 0);
        int backgroundColor = a.getColor(R.styleable.CometChatMessageBubble_backgroundColor, 0);
        Drawable messageAvatar = a.getDrawable(R.styleable.CometChatMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.CometChatMessageBubble_avatarVisibility, View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.CometChatMessageBubble_userNameVisibility, View.VISIBLE);
        String userName = a.getString(R.styleable.CometChatMessageBubble_userName);
        int color = a.getColor(R.styleable.CometChatMessageBubble_userNameColor, 0);
        alignment = Alignment.getValue(a.getInt(R.styleable.CometChatMessageBubble_messageAlignment, 0));
        borderColor = a.getColor(R.styleable.CometChatMessageBubble_borderColor, 0);
        borderWidth = a.getInt(R.styleable.CometChatMessageBubble_borderWidth, 0);

        if (alignment.equalsIgnoreCase(Alignment.LEFT))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_bubble, null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_bubble, null);

        initView(view);

        avatar(messageAvatar);
        avatarVisibility(hideAvatar);
        backgroundColor(backgroundColor);
        borderColor(borderColor);
        borderWidth(borderWidth);
        cornerRadius(cornerRadius);
        userName(userName);
        userNameVisibility(hideUserName);
        userNameColor(color);

//        setColorFilter(baseMessage,cvMessageView);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childView = getChildAt(1);
    }

    private void initView(View view) {
        addView(view);
        tvUser = view.findViewById(R.id.tv_user);
        bubble = view.findViewById(R.id.bubble_container);
        bubble.setCardElevation(0);
        cvMessageBubble = view.findViewById(R.id.message_container);
        txtTime = view.findViewById(R.id.time);
        messageReceipt = view.findViewById(R.id.receiptsIcon);
        receiptLayout = view.findViewById(R.id.receipt_layout);
        ivUser = view.findViewById(R.id.iv_user);
        ivUser.setBackgroundColor(palette.getAccent700());
        ivUser.setTextColor(palette.getAccent900());
        tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
        divider = view.findViewById(R.id.divider);
        reactionLayout = view.findViewById(R.id.reactions_group);
        reactionLayout.setReactionEventListener(new CometChatMessageReaction.OnReactionClickListener() {
            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                if (messageBubbleListener != null)
                    messageBubbleListener.onReactionClick(reaction, baseMessageID);
            }
        });
    }

    public void messageInputData(MessageInputData messageInputData) {
        if (messageInputData != null) {
            boolean thumbnail = messageInputData.isThumbnail();
            avatarVisibility(thumbnail ? VISIBLE : GONE);
            boolean name = messageInputData.isTitle();
            userNameVisibility(name ? VISIBLE : GONE);
            boolean timestamp = messageInputData.isTimeStamp();
            timeStampVisibility(timestamp ? VISIBLE : GONE);
            boolean readReceipt = messageInputData.isReadReceipts();
            receiptVisibility(readReceipt ? VISIBLE : GONE);
        }
    }

    public void cornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (bubble != null)
            bubble.setShapeAppearanceModel(bubble.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopLeftCornerSize(topLeft)
                    .setTopRightCornerSize(topRight)
                    .setBottomLeftCornerSize(bottomLeft)
                    .setBottomRightCornerSize(bottomRight)
                    .build());

    }

    public void cornerRadius(float radius) {
        if (bubble != null)
            bubble.setRadius(radius);
    }

    public void cornerRadius(ShapeAppearanceModel shapeAppearanceModel) {
        if (bubble!=null)
            bubble.setShapeAppearanceModel(shapeAppearanceModel);
    }

    public View getBubbleView() {
        return bubble;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (bubble != null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
            gd.setCornerRadius(bubble.getRadius());
            bubble.setBackgroundDrawable(gd);
        }
    }

    public void backgroundColor(@ColorInt int bgColor) {
        if (bubble != null) {
            bubble.setCardBackgroundColor(bgColor);
        }
    }

    public void avatar(Drawable avatarDrawable) {
        if (ivUser != null)
            ivUser.setDrawable(avatarDrawable);
    }

    public void avatar(String url, String initials) {
        if (ivUser != null) {
            ivUser.setInitials(initials);
            if (url != null)
                ivUser.setAvatar(url);
        }
    }

    public void avatarVisibility(int visibility) {
        if (ivUser != null) {
            ivUser.setVisibility(visibility);
        }
    }

    public void userName(String username) {
        if (tvUser != null)
            tvUser.setText(username);
    }

    public void userNameFont(String font) {
        if (tvUser != null)
            tvUser.setTypeface(fontUtils.getTypeFace(font));
    }

    public void userNameColor(@ColorInt int color) {
        if (tvUser != null && color != 0)
            tvUser.setTextColor(color);
    }

    public void userNameVisibility(int visibility) {
        if (tvUser != null) {
            tvUser.setVisibility(visibility);
        }
    }

    public void setReactionBorderColor(@ColorInt int strokeColor) {
        if (strokeColor != 0)
            reactionLayout.setBorderColor(strokeColor);
    }

    public void messageAlignment(@Alignment.MessageAlignment String mAlignment) {
        if (mAlignment != null && mAlignment.equalsIgnoreCase(Alignment.LEFT))
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_bubble, null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_bubble, null);

        removeAllViewsInLayout();
        initView(view);
    }

    public void borderColor(@ColorInt int color) {
        borderColor = color;
        if (bubble != null) {
            if (color != 0)
                bubble.setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        borderWidth = width;
        if (bubble != null)
            bubble.setStrokeWidth(width);
    }

    public void setPadding(int padding) {
        if (bubble != null) {
            bubble.setContentPadding(padding, padding, padding, padding);
        }
    }

    public void messageReceiptIcon(CometChatMessageReceipt messageReceipt) {
        if (messageReceipt != null) {
            messageReceipt.messageDeliveredIcon(messageReceipt.getDeliveredIcon());
            messageReceipt.messageReadIcon(messageReceipt.getReadIcon());
            messageReceipt.messageSentIcon(messageReceipt.getSentIcon());
            messageReceipt.messageErrorIcon(messageReceipt.getErrorIcon());
            messageReceipt.messageProgressIcon(messageReceipt.getProgressIcon());
        }
    }

    public void replyCount(int count) {
        if (count != 0) {
            divider.setVisibility(View.VISIBLE);
            tvThreadReplyCount.setVisibility(View.VISIBLE);
            tvThreadReplyCount.setText(baseMessage.getReplyCount() + " " + context.getResources().getString(R.string.replies));
        } else {
            divider.setVisibility(View.GONE);
            tvThreadReplyCount.setVisibility(View.GONE);
        }
    }

    public void setReplyCountColor(@ColorInt int color) {
        if (tvThreadReplyCount != null)
            tvThreadReplyCount.setTextColor(color);
    }

    public void messageTimeAlignment(TimeAlignment timeAlignment) {
        if (timeAlignment == TimeAlignment.TOP) {
            LayoutParams params = (LayoutParams) receiptLayout.getLayoutParams();
            params.addRule(RelativeLayout.END_OF, R.id.tv_user);
            params.addRule(RelativeLayout.ALIGN_START, 0);
            params.addRule(RelativeLayout.BELOW, 0);
            params.topMargin = 0;
            params.leftMargin = 8;

            LayoutParams messageBubbleParam = (LayoutParams) cvMessageBubble.getLayoutParams();
            messageBubbleParam.topMargin = 8;
            messageBubbleParam.bottomMargin = 8;
//            receiptLayout.setLayoutParams(params);
        } else {
            LayoutParams params = (LayoutParams) receiptLayout.getLayoutParams();
            params.addRule(RelativeLayout.BELOW, R.id.thread_reply_count);
//            receiptLayout.setLayoutParams(params);
        }
    }

    public void timeStampVisibility(int visible) {
        if (txtTime != null)
            txtTime.setVisibility(visible);
    }

    public void receiptVisibility(int visible) {
        if (messageReceipt != null)
            messageReceipt.setVisibility(visible);
    }

    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
        messageReceipt.messageObject(baseMessage);
        txtTime.setDate(baseMessage.getSentAt(), "hh:mm a");
        txtTime.setTransparentBackground(true);
        if (childView != null && cvMessageBubble != null) {
            if (childView.getParent() != null)
                ((ViewGroup) childView.getParent()).removeView(childView);
            //CustomView
            CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
                    .getMessageTemplateById(baseMessage.getType());
            if(messageTemplate!=null)
                layoutId = messageTemplate.getView();
//        dataView = messageTemplate.getDataView();
            if (layoutId != 0) {
                LayoutInflater inflater = LayoutInflater.from(context);
                ViewDataBinding binding = DataBindingUtil.inflate(inflater,layoutId,null,false);
                if(messageTemplate !=null && messageTemplate.getActivity()!=null){
                    binding.setVariable(BR.activity,messageTemplate.getActivity());
                }
                if (baseMessage instanceof MediaMessage)
                    binding.setVariable(BR.message, baseMessage);
                else if (baseMessage instanceof TextMessage)
                    binding.setVariable(BR.message, baseMessage);
                else if (baseMessage instanceof CustomMessage)
                    binding.setVariable(BR.message, baseMessage);
                View customView = binding.getRoot();
//            View customView = LayoutInflater.from(context).inflate(layoutId, null);
                cvMessageBubble.removeAllViewsInLayout();
                if (customView.getParent() != null)
                    ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
                cvMessageBubble.addView(customView);
                customView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(messageBubbleListener !=null) {
                            messageBubbleListener.onLongCLick(baseMessage);
                        }
                        return true;
                    }
                });
                customView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(messageBubbleListener !=null) {
                            messageBubbleListener.onClick(baseMessage);
                        }
                    }
                });
            } else {

                cvMessageBubble.addView(childView);
                if (childView instanceof CometChatTextBubble && baseMessage instanceof TextMessage) {
                    CometChatTextBubble textBubble = (CometChatTextBubble) childView;
                    textBubble.messageObject(baseMessage);
                    textBubble.setEventListener(this);
                } else if (childView instanceof CometChatAudioBubble) {
                    CometChatAudioBubble audioBubble = (CometChatAudioBubble) childView;
                    audioBubble.messageObject(baseMessage);
                    audioBubble.setEventListener(this);
                } else if (childView instanceof CometChatStickerBubble) {
                    CometChatStickerBubble stickerBubble = (CometChatStickerBubble) childView;
                    stickerBubble.setEventListener(this);
                    stickerBubble.messageObject(baseMessage);
                } else if (childView instanceof CometChatImageBubble) {
                    CometChatImageBubble imageBubble = (CometChatImageBubble) childView;
                    imageBubble.messageObject(baseMessage);
                    imageBubble.setEventListener(this);
                } else if (childView instanceof CometChatVideoBubble) {
                    CometChatVideoBubble videoBubble = (CometChatVideoBubble) childView;
                    videoBubble.messageObject(baseMessage);
                    videoBubble.setEventListener(this);
                } else if (childView instanceof CometChatFileBubble) {
                    CometChatFileBubble fileBubble = (CometChatFileBubble) childView;
                    fileBubble.messageObject(baseMessage);
                    fileBubble.setEventListener(this);
                } else if (childView instanceof CometChatLocationBubble) {
                    CometChatLocationBubble locationBubble = (CometChatLocationBubble) childView;
                    locationBubble.messageObject(baseMessage);
                    locationBubble.setEventListener(this);
                } else if (childView instanceof CometChatPollBubble) {
                    CometChatPollBubble pollBubble = (CometChatPollBubble) childView;
                    pollBubble.messageObject(baseMessage);
                    pollBubble.setEventListener(this);
                } else if (childView instanceof CometChatWhiteboardBubble) {
                    CometChatWhiteboardBubble whiteboardBubble = (CometChatWhiteboardBubble) childView;
                    whiteboardBubble.messageObject(baseMessage);
                    whiteboardBubble.setEventListener(this);
                } else if (childView instanceof CometChatDocumentBubble) {
                    CometChatDocumentBubble cometChatDocumentBubble = (CometChatDocumentBubble) childView;
                    cometChatDocumentBubble.messageObject(baseMessage);
                    cometChatDocumentBubble.setEventListener(this);
                }
            }
        }
        if (tvUser != null)
            tvUser.setText(baseMessage.getSender().getName());
        if (ivUser != null)
            ivUser.setAvatar(baseMessage.getSender());
        reactionLayout.setMessage(baseMessage);
        if (cvMessageBubble != null) {
            cvMessageBubble.setOnClickListener(view -> {
                if (messageBubbleListener != null) {
                    messageBubbleListener.onClick(baseMessage);
                }
            });

            cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (messageBubbleListener != null)
                        messageBubbleListener.onLongCLick(baseMessage);
                    return true;
                }
            });
        }

    }

    public void setMessageBubbleListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }

    @Override
    public void onLongCLick(BaseMessage baseMessage) {
        if (messageBubbleListener != null)
            messageBubbleListener.onLongCLick(baseMessage);
    }

    @Override
    public void onClick(BaseMessage baseMessage) {
        if (messageBubbleListener != null)
            messageBubbleListener.onClick(baseMessage);
    }

    @Override
    public void onReactionClick(Reaction reaction, int baseMessageID) {

    }

    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof AvatarConfiguration) {
            AvatarConfiguration config = (AvatarConfiguration)configuration;
            setAvatarConfig(config);
        } else if (configuration instanceof MessageReceiptConfiguration) {
            MessageReceiptConfiguration config = (MessageReceiptConfiguration) configuration;
            setMessageReceiptConfig(config);
        }
    }

    private void setMessageReceiptConfig(MessageReceiptConfiguration config) {
        if (config.getDeliveredIcon()!=null)
            messageReceipt.messageDeliveredIcon(config.getDeliveredIcon());
        if (config.getSentIcon()!=null)
            messageReceipt.messageSentIcon(config.getSentIcon());
        if (config.getReadIcon()!=null)
            messageReceipt.messageReadIcon(config.getReadIcon());
        if (config.getInProgressIcon()!=null)
            messageReceipt.messageProgressIcon(config.getInProgressIcon());
        if (config.getWidth()!=-1 && config.getHeight()!=-1) {
            LayoutParams params = new RelativeLayout.LayoutParams(
                    (int)config.getWidth(),
                    (int)config.getHeight());
            messageReceipt.setLayoutParams(params);
        }
    }

    private void setAvatarConfig(AvatarConfiguration config) {
        if (config.getCornerRadius()!=-1)
            ivUser.setCornerRadius(config.getCornerRadius());
        if (config.getBorderWidth()!=-1)
            ivUser.setBorderWidth(config.getBorderWidth());
        if (config.getOuterViewWidth()!=-1)
            ivUser.setOuterViewSpacing(config.getOuterViewWidth());
        if (config.getHeight()!=-1 && config.getWidth()!=-1) {
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                    (int)config.getWidth(),
                    (int)config.getHeight());
            ivUser.setLayoutParams(param);
        }
    }
}
