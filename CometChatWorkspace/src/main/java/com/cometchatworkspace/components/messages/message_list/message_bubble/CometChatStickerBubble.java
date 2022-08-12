package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.TimeAlignment;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;

public class CometChatStickerBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;
    private ShapeableImageView imageMessage;
    private RelativeLayout rlMessageBubble;

    private RelativeLayout sensitiveLayout;

    private String alignment = Alignment.RIGHT;

    private final String TAG = "StickerMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private Palette palette;
    private Typography typography;

    private int layoutId;

    public CometChatStickerBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatStickerBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatStickerBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatStickerBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography= Typography.getInstance();
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.StickerMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.StickerMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.StickerMessageBubble_backgroundColor,getResources().getColor(android.R.color.transparent));
        Drawable messageAvatar = a.getDrawable(R.styleable.StickerMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.StickerMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.StickerMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.StickerMessageBubble_userName);
        int color = a.getColor(R.styleable.StickerMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.StickerMessageBubble_messageAlignment,0));
        Drawable imageDrawable = a.getDrawable(R.styleable.StickerMessageBubble_stickerDrawable);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_sticker_bubble,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);

        //set message
        if(imageDrawable!=null)
            imageMessage.setImageDrawable(imageDrawable);
    }

    private void initView(View view) {
        addView(view);
        imageMessage = view.findViewById(R.id.image_message);

        rlMessageBubble = view.findViewById(R.id.rl_message);

        sensitiveLayout = view.findViewById(R.id.sensitive_layout);
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
        if (imageMessage!=null)
            imageMessage.setShapeAppearanceModel(imageMessage.getShapeAppearanceModel()
                .toBuilder()
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight).build());
    }
    public void cornerRadius(float radius) {
        if (imageMessage!=null)
            imageMessage.setShapeAppearanceModel(imageMessage.getShapeAppearanceModel()
                    .toBuilder()
                    .setBottomLeftCornerSize(radius)
                    .setBottomRightCornerSize(radius)
                    .setTopLeftCornerSize(radius)
                    .setTopRightCornerSize(radius).build());
    }

    public View getBubbleView() {
        return imageMessage;
    }

    public void backgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (imageMessage!=null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
//            gd.setCornerRadius(imageMessage.getRadius());
            imageMessage.setBackgroundDrawable(gd);
        }
    }
    public void backgroundColor(@ColorInt int bgColor) {
        if (imageMessage!=null) {
            if (bgColor != 0)
                imageMessage.setBackgroundColor(bgColor);
        }
    }

    public void messageAlignment(@Alignment.MessageAlignment String mAlignment) {
        if (alignment!=null && alignment== Alignment.LEFT)
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_left_sticker_bubble,null);
        else
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_sticker_bubble,null);

        removeAllViewsInLayout();
        initView(view);
    }

    public void setDrawable(Drawable drawable) {
        if (imageMessage !=null)
            imageMessage.setImageDrawable(drawable);
    }

    public void setImageUrl(String url) {
        if (imageMessage !=null)
            setImageDrawable(url,false,false);
    }

    public void setPadding(int padding) {
        if (imageMessage !=null) {
            imageMessage.setPadding(padding,padding,padding,padding);
        }
    }

    public void setPadding(int left,int right,int top,int bottom) {
        if (imageMessage!=null)
            imageMessage.setPadding(left,top,right,bottom);
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

    public void messageObject(BaseMessage baseMessage) {
        try {
            Glide.with(context).load(
                    ((CustomMessage)baseMessage).getCustomData().getString("sticker_url"))
                    .into(imageMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageMessage.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (messageBubbleListener!=null)
                    messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });

    }

    public void setEventListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }
}
