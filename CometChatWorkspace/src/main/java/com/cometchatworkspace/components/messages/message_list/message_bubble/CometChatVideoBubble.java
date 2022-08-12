package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate.CometChatDate;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatMessageReaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;

public class CometChatVideoBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;
    private ShapeableImageView videoMessage;

    private ImageView playBtn;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;


    private RelativeLayout rlMessageBubble;

    private RelativeLayout sensitiveLayout;

    private String alignment = Alignment.RIGHT;

    private final String TAG = "VideoMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private Palette palette;
    private Typography typography;

    private boolean isImageNotSafe;

    private int borderColor = 0;

    private int borderWidth = 0;

    private String url;

    private int layoutId;

    public CometChatVideoBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatVideoBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatVideoBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatVideoBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                R.styleable.VideoMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.VideoMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.VideoMessageBubble_backgroundColor,0);
        Drawable messageAvatar = a.getDrawable(R.styleable.VideoMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.VideoMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.VideoMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.VideoMessageBubble_userName);
        int color = a.getColor(R.styleable.VideoMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.VideoMessageBubble_messageAlignment,0));

        int borderColor = a.getColor(R.styleable.VideoMessageBubble_borderColor,0);
        int borderWidth = a.getInt(R.styleable.VideoMessageBubble_borderWidth,0);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_video_bubble,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        borderColor(borderColor);
        borderWidth(borderWidth);

//        setColorFilter(baseMessage,cvMessageView);



    }

    private void initView(View view) {
        addView(view);
        videoMessage = view.findViewById(R.id.video_message);
        playBtn = view.findViewById(R.id.playBtn);
        cvMessageBubble = view.findViewById(R.id.cv_image_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);
        rlMessageBubble = view.findViewById(R.id.rl_message);
        sensitiveLayout = view.findViewById(R.id.sensitive_layout);


        //CustomView
//        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
//                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.video);
//        if(messageTemplate!=null)
//            layoutId = messageTemplate.getView();
////        dataView = messageTemplate.getDataView();
//        if (layoutId != 0) {
//            View customView = LayoutInflater.from(context).inflate(layoutId, null);
//            cvMessageBubbleLayout.removeAllViewsInLayout();
//            if (customView.getParent() != null)
//                ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
//            cvMessageBubbleLayout.addView(customView);
//            videoMessage = customView.findViewById(R.id.video_message);
//            customView.setOnLongClickListener(new OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    messageBubbleListener.onLongCLick(baseMessage);
//                    return true;
//                }
//            });
//            customView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    messageBubbleListener.onClick(baseMessage);
//                }
//            });
//        }
    }

    private void checkForExtensions(BaseMessage baseMessage) {
        isImageNotSafe = Extensions.getImageModeration(context,baseMessage);
        String smallUrl = Extensions.getThumbnailGeneration(context,baseMessage);
        Attachment attachment = ((MediaMessage)baseMessage).getAttachment();
        if (attachment!=null) {
            if (smallUrl!=null) {
                 setImageDrawable(smallUrl);
            } else {
               setImageDrawable(((MediaMessage)baseMessage).getAttachment().getFileUrl());
            }
        }
        if (isImageNotSafe) {
            sensitiveLayout.setVisibility(View.VISIBLE);
        } else {
            sensitiveLayout.setVisibility(View.GONE);
        }
    }
    private void setImageDrawable(String url) {
        Glide.with(context).load(url).into(videoMessage);
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

        if (videoMessage !=null)
            videoMessage.setShapeAppearanceModel(videoMessage.getShapeAppearanceModel()
                .toBuilder()
                .setBottomLeftCornerSize(bottomLeft)
                .setBottomRightCornerSize(bottomRight)
                .setTopLeftCornerSize(topLeft)
                .setTopRightCornerSize(topRight).build());
    }
    public void cornerRadius(float radius) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setRadius(radius);
        if (videoMessage !=null)
            videoMessage.setShapeAppearanceModel(videoMessage.getShapeAppearanceModel()
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

    public void setVideoUrl(String url) {
        if (videoMessage !=null)
            setImageDrawable(url);
    }

    public void setPadding(int padding) {
        if (videoMessage !=null) {
            cvMessageBubble.setContentPadding(padding,padding,padding,padding);
        }
    }

    public void setPadding(int left,int right,int top,int bottom) {
        if (cvMessageBubble !=null)
            cvMessageBubble.setContentPadding(left,top,right,bottom);
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

    public void loadVideoFromUrl(String url) {
        if (url!=null && !url.isEmpty()) {
            this.url = url;
            baseMessage = new BaseMessage();
            Attachment attachment = new Attachment();
            attachment.setFileName("Video");
            attachment.setFileExtension("mp4");
            attachment.setFileMimeType("video/*");
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
        if (baseMessage.getMetadata()!=null) {
            try {
                if (baseMessage.getMetadata().has("path")) {
                    String filePath = baseMessage.getMetadata().getString("path");
                    setVideoUrl(filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        checkForExtensions(baseMessage);
        rlMessageBubble.setOnClickListener(view -> {
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

        cvMessageBubble.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                messageBubbleListener.onClick(baseMessage);
            }
        });

        cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });
    }

    public void setEventListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }
}
