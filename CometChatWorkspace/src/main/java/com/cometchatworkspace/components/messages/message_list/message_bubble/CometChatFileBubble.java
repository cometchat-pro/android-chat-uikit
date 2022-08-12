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
import android.widget.LinearLayout;
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
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatMessageReceipt;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

public class CometChatFileBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;
    private TextView title;
    private TextView subtitle;
    private ImageView downloadButton;
    private String alignment = Alignment.RIGHT;

    private final String TAG = "fileMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private int reactionStrokeColor;
    private Palette palette;
    private Typography typography;

    private int borderWidth = 0;

    private int borderColor = 0;

    private String url;

    private int layoutId;

    public CometChatFileBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatFileBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatFileBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatFileBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent(context,attrs);
    }

    private void initComponent(Context context, AttributeSet attributeSet) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography= Typography.getInstance();
        reactionStrokeColor = palette.getPrimary();
        fontUtils=FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.FileMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.FileMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.FileMessageBubble_backgroundColor,0);
        Drawable fileMessageAvatar = a.getDrawable(R.styleable.FileMessageBubble_avatar);
        int avatarVisibility = a.getInt(R.styleable.FileMessageBubble_avatarVisibility,View.VISIBLE);
        int userNameVisibility = a.getInt(R.styleable.FileMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.FileMessageBubble_userName);
        int color = a.getColor(R.styleable.FileMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.FileMessageBubble_messageAlignment,0));

        String title = a.getString(R.styleable.FileMessageBubble_title);
        int titleColor = a.getColor(R.styleable.FileMessageBubble_titleColor,0);
        String subtitle = a.getString(R.styleable.FileMessageBubble_subtitle);
        int subtitleColor = a.getColor(R.styleable.FileMessageBubble_subtitleColor,0);
        String type = a.getString(R.styleable.FileMessageBubble_type);
        int typeColor = a.getColor(R.styleable.FileMessageBubble_typeColor,0);
        Drawable icon = a.getDrawable(R.styleable.FileMessageBubble_icon);
        int iconColor = a.getColor(R.styleable.FileMessageBubble_iconTint,0);
        int secondaryBackgroundColor = a.getColor(R.styleable.FileMessageBubble_secondaryBackground,0);

        int borderWidth = a.getInt(R.styleable.FileMessageBubble_borderWidth,0);
        int borderColor = a.getColor(R.styleable.FileMessageBubble_borderColor,0);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_file_bubble_,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);

        title(title);
        titleColor(titleColor);
        subtitle(subtitle);
        subtitleColor(subtitleColor);
        type(type);
        typeColor(typeColor);
        icon(icon);
        iconTint(iconColor);
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
        subtitle = view.findViewById(R.id.tvSubtitle);
        title = view.findViewById(R.id.tvTitle);
        downloadButton = view.findViewById(R.id.ivDownload);
        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);

        //CustomView
//        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
//                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.file);
//        if(messageTemplate!=null)
//            layoutId = messageTemplate.getView();
////        dataView = messageTemplate.getDataView();
//        if (layoutId != 0) {
//            View customView = LayoutInflater.from(context).inflate(layoutId, null);
//            cvMessageBubbleLayout.setVisibility(View.GONE);
//            if (customView.getParent() != null)
//                ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
//            cvMessageBubble.addView(customView);
//            subtitle = customView.findViewById(R.id.tvSubtitle);
//            downloadButton = customView.findViewById(R.id.ivDownload);
//            title = customView.findViewById(R.id.tvTitle);
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

    public void type(String secondaryText) {
        if (subtitle !=null && secondaryText!=null && !secondaryText.isEmpty())
            subtitle.setText(secondaryText);
    }

    public void typeColor(@ColorInt int color) {
        if (subtitle !=null && color!=0)
            subtitle.setTextColor(color);
    }

    public void icon(Drawable icon) {
        if (downloadButton!=null && icon!=null)
            downloadButton.setImageDrawable(icon);
    }

    public void iconTint(@ColorInt int color) {
        if (downloadButton!=null && color!=0) {
            downloadButton.setImageTintList(ColorStateList.valueOf(color));
        }
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

    public void title(String title) {
        if (this.title !=null && title!=null && !title.isEmpty())
            this.title.setText(title);
    }

    public void titleColor(@ColorInt int color) {
        if (color!=0 && title !=null)
            title.setTextColor(color);
    }

    public void subtitle(String title) {
        if (subtitle!=null)
            subtitle.setText(title);
    }

    public void subtitleColor(@ColorInt int color) {
        if (color!=0 && subtitle!=null)
            subtitle.setTextColor(color);
    }

    public void fileURL(String url) {
        if (url!=null && !url.isEmpty()) {
            this.url = url;
            baseMessage = new BaseMessage();
            Attachment attachment = new Attachment();
            if (title.getText().toString().isEmpty())
                attachment.setFileName("File");
            else
                attachment.setFileName(title.getText().toString());
            if (subtitle.getText().toString().isEmpty())
                attachment.setFileExtension("file");
            else
                attachment.setFileExtension(subtitle.getText().toString());
            attachment.setFileMimeType("application/*");
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

        if (((MediaMessage)baseMessage).getAttachment()!=null) {
            title(((MediaMessage) baseMessage).getAttachment().getFileName());
//            fileExt.setText(((MediaMessage) baseMessage).getAttachment().getFileExtension());
            int size = ((MediaMessage) baseMessage).getAttachment().getFileSize();
            subtitle(Utils.getFileSize(size));
        } else {
            title(context.getString(R.string.uploading));
            subtitle("-");
        }
        cvMessageBubble.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (messageBubbleListener!=null)
                    messageBubbleListener.onLongCLick(baseMessage);
                return true;
            }
        });
        downloadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageBubbleListener!=null)
                    messageBubbleListener.onClick(baseMessage);
            }
        });
    }

    public void setEventListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }
}
