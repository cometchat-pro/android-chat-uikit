package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
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
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;

public class CometChatImageBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;
    private ShapeableImageView imageMessage;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;
    private ProgressBar progressBar;

    private CometChatAvatar ivUser;
    private TextView tvUser;

    private CometChatDate txtTime;
    private CometChatMessageReceipt messageReceipt;
    private View receiptLayout;

    private RelativeLayout rlMessageBubble;
    private TextView tvThreadReplyCount;

    private RelativeLayout sensitiveLayout;

    private CometChatMessageReaction reactionLayout;

    private String alignment = Alignment.RIGHT;

    private final String TAG = "ImageMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private int reactionStrokeColor = Color.parseColor(CometChatTheme.primaryColor);

    private boolean isImageNotSafe;

    private int borderColor = 0;

    private int borderWidth = 0;

    private String url;

    private int layoutId;

    public CometChatImageBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatImageBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatImageBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatImageBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.ImageMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.ImageMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.ImageMessageBubble_backgroundColor,0);
        Drawable messageAvatar = a.getDrawable(R.styleable.ImageMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.ImageMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.ImageMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.ImageMessageBubble_userName);
        int color = a.getColor(R.styleable.ImageMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.ImageMessageBubble_messageAlignment,0));

        Drawable imageDrawable = a.getDrawable(R.styleable.ImageMessageBubble_drawable);

        if (alignment== Alignment.LEFT)
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_image_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_image_bubble,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        avatar(messageAvatar);
        avatarVisibility(hideAvatar);
        userName(userName);
        userNameVisibility(hideUserName);
        userNameColor(color);

        //set message
        if(imageDrawable!=null)
            imageMessage.setImageDrawable(imageDrawable);

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
        imageMessage = view.findViewById(R.id.image_message);
        tvUser= view.findViewById(R.id.tv_user);
        cvMessageBubble = view.findViewById(R.id.cv_image_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);
        progressBar = view.findViewById(R.id.progress_bar);
        txtTime = view.findViewById(R.id.time);
        messageReceipt = view.findViewById(R.id.receiptsIcon);
        receiptLayout = view.findViewById(R.id.receipt_layout);
        ivUser = view.findViewById(R.id.iv_user);
        rlMessageBubble = view.findViewById(R.id.rl_message);
        tvThreadReplyCount = view.findViewById(R.id.thread_reply_count);
        sensitiveLayout = view.findViewById(R.id.sensitive_layout);
        reactionLayout = view.findViewById(R.id.reactions_group);
        reactionLayout.setReactionEventListener(new CometChatMessageReaction.OnReactionClickListener() {
            @Override
            public void onReactionClick(Reaction reaction, int baseMessageID) {
                messageBubbleListener.onReactionClick(reaction,baseMessageID);
            }
        });

        //CustomView
        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.image);
        if(messageTemplate!=null)
            layoutId = messageTemplate.getView();
//        dataView = messageTemplate.getDataView();
        if (layoutId != 0) {
            View customView = LayoutInflater.from(context).inflate(layoutId, null);
            cvMessageBubbleLayout.removeAllViewsInLayout();
            if (customView.getParent() != null)
                ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
            cvMessageBubbleLayout.addView(customView);
            imageMessage = customView.findViewById(R.id.image_message);
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

    private void checkForExtensions(BaseMessage baseMessage) {
        isImageNotSafe = Extensions.getImageModeration(context,baseMessage);
        String smallUrl = Extensions.getThumbnailGeneration(context,baseMessage);
        Attachment attachment = ((MediaMessage)baseMessage).getAttachment();
        if (attachment!=null && imageMessage!=null) {
            if (smallUrl!=null) {
                if (attachment.getFileExtension().equalsIgnoreCase(".gif")) {
                    setImageDrawable(smallUrl,true,false);
                } else {
                    setImageDrawable(smallUrl,false,isImageNotSafe);
                }
            } else {
                if (attachment.getFileExtension().equalsIgnoreCase(".gif"))
                    setImageDrawable(((MediaMessage)baseMessage).getAttachment().getFileUrl(),true,false);
                else
                    setImageDrawable(((MediaMessage)baseMessage).getAttachment().getFileUrl(),false,isImageNotSafe);
            }
        }
        if (sensitiveLayout!=null) {
            if (isImageNotSafe) {
                sensitiveLayout.setVisibility(View.VISIBLE);
            } else {
                sensitiveLayout.setVisibility(View.GONE);
            }
        }
    }
    private void setImageDrawable(String url, boolean gif, boolean isImageNotSafe) {
        if (gif) {
            Glide.with(context).asGif().diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).load(url).into(imageMessage);
        } else {
            Glide.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).load(url).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    if (isImageNotSafe)
                        imageMessage.setImageBitmap(Utils.blur(context, resource));
                    else
                        imageMessage.setImageBitmap(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }

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

        if (imageMessage!=null)
            imageMessage.setShapeAppearanceModel(imageMessage.getShapeAppearanceModel()
                .toBuilder()
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight).build());
    }
    public void cornerRadius(float radius) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setRadius(radius);
        if (imageMessage!=null)
            imageMessage.setShapeAppearanceModel(imageMessage.getShapeAppearanceModel()
                    .toBuilder()
                    .setBottomLeftCornerSize(radius)
                    .setBottomRightCornerSize(radius)
                    .setTopLeftCornerSize(radius)
                    .setTopRightCornerSize(radius).build());
    }

    public View getBubbleView() {
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
    public void messageAlignment(@Alignment.MessageAlignment String  mAlignment) {
        if (alignment!=null && alignment== Alignment.LEFT)
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_image_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_image_bubble,null);

        removeAllViewsInLayout();
        initView(view);
    }

    public void setDrawable(Drawable drawable) {
        if (imageMessage !=null)
            imageMessage.setImageDrawable(drawable);
    }

    public void setImageUrl(String url) {
        isImageNotSafe = false;
        if (imageMessage !=null)
            setImageDrawable(url,false,false);
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

    public void setPadding(int padding) {
        if (cvMessageBubble !=null) {
            cvMessageBubble.setContentPadding(padding,padding,padding,padding);
        }
    }

    public void setPadding(int left,int right,int top,int bottom) {
        if (imageMessage!=null)
            imageMessage.setContentPadding(left,top,right,bottom);
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

    public void imageUrl(String url) {
        if (url!=null && !url.isEmpty()) {
            this.url = url;
            baseMessage = new BaseMessage();
            Attachment attachment = new Attachment();
            attachment.setFileName("Image");
            attachment.setFileExtension("jpeg");
            attachment.setFileMimeType("image/jpeg");
            attachment.setFileSize(0);
            attachment.setFileUrl(url);
            ((MediaMessage) baseMessage).setAttachment(attachment);
            cvMessageBubble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageBubbleListener.onClick(baseMessage);
                }
            });
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
        checkForExtensions(baseMessage);

        if (baseMessage.getMetadata()!=null) {
            try {
                String filePath = baseMessage.getMetadata().getString("path");
                if (imageMessage!=null && filePath!=null)
                    Glide.with(context).load(filePath).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        cvMessageBubble.setOnClickListener(view -> {
            if (isImageNotSafe) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Unsafe Content");
                alert.setIcon(R.drawable.ic_hand);
                alert.setMessage("Are you surely want to see this unsafe content");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageBubbleListener.onClick(baseMessage);
//                        MediaUtils.openFile(((MediaMessage) baseMessage).getAttachment().getFileUrl(), context);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.create().show();
            } else {
                messageBubbleListener.onClick(baseMessage);
            }

        });

    }

    public void setMessageBubbleListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }
}
