package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.EmojiSpan;

import com.bumptech.glide.Glide;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.Alignment;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageBubbleListener;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.TextMessageType;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.pattern_utils.PatternUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CometChatPlaceHolderBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;

    private TextView txtMessage;            //Text Message

    private String alignment = Alignment.RIGHT;

    private int borderColor = 0;

    private int borderWidth = 0;

    private final String TAG = "CustomMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private Palette palette;
    private Typography typography;

    private int layoutId;

    public CometChatPlaceHolderBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatPlaceHolderBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatPlaceHolderBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatPlaceHolderBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                R.styleable.TextMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.TextMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.TextMessageBubble_backgroundColor,0);
        Drawable textMessageAvatar = a.getDrawable(R.styleable.TextMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.TextMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.TextMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.TextMessageBubble_userName);
        int color = a.getColor(R.styleable.TextMessageBubble_userNameColor,0);
        alignment = Alignment.getValue(a.getInt(R.styleable.TextMessageBubble_messageAlignment,0));
        String textMessage = a.getString(R.styleable.TextMessageBubble_text);
        int textMessageColor = a.getColor(R.styleable.TextMessageBubble_text_color,0);

        borderColor = a.getColor(R.styleable.TextMessageBubble_borderColor,0);
        borderWidth = a.getInt(R.styleable.TextMessageBubble_borderWidth,0);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_custom_bubble, null);

        initView(view);

        //Common Layout
        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        borderColor(borderColor);
        borderWidth(borderWidth);
        if (textMessageColor!=0)
            textColor(textMessageColor);

        //set message
        String strMessage = null;
        if(textMessage!=null)
            strMessage = textMessage;
        if (txtMessage!=null) {
            txtMessage.setText(strMessage);
            txtMessage.setTextSize(16f);
        }
    }

    private void initView(View view) {

        addView(view);
        //TextMessage
        txtMessage = view.findViewById(R.id.custom_message);


        //CustomView
//        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
//                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.text);
//        if(messageTemplate!=null)
//            layoutId = messageTemplate.getView();
////        dataView = messageTemplate.getDataView();
//        if (layoutId != 0) {
//            View customView = LayoutInflater.from(context).inflate(layoutId,null);
//            cvMessageBubbleLayout.removeAllViewsInLayout();
//            if (customView.getParent()!=null)
//                ((ViewGroup)customView.getParent()).removeAllViewsInLayout();
//            cvMessageBubbleLayout.addView(customView);
//            //TextMessage
//            txtMessage = customView.findViewById(R.id.text_message);
//
//            customView.setOnLongClickListener(new OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    if (messageBubbleListener!=null)
//                        messageBubbleListener.onLongCLick(baseMessage);
//                    return true;
//                }
//            });
//        }
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

    public void borderColor(@ColorInt int color) {
        borderColor=color;
        if (cvMessageBubble!=null) {
            if (color!=0)
                cvMessageBubble.setStrokeColor(borderColor);
        }
    }

    public void borderWidth(int width) {
        borderWidth= width;
        if (cvMessageBubble!=null) {
            cvMessageBubble.setStrokeWidth(width);
        }
    }


    public void text(String str) {
        if (txtMessage!=null)
            txtMessage.setText(str);
    }

    public void textFont(String font) {
        if (txtMessage!=null)
            txtMessage.setTypeface(fontUtils.getTypeFace(font));
    }

    public void textColor(@ColorInt int color) {
        if (txtMessage!=null) {
            if (color != 0)
                txtMessage.setTextColor(color);
        }
    }

    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;
        String strMessage = ((TextMessage) baseMessage).getText();
        String url="";

        if (txtMessage!=null) {
            txtMessage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageBubbleListener != null)
                        messageBubbleListener.onClick(baseMessage);
                }
            });


            txtMessage.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (messageBubbleListener != null)
                        messageBubbleListener.onLongCLick(baseMessage);
                    return true;
                }
            });
        }
    }

    public void setEventListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }

    public void textFontStyle(int style) {
        txtMessage.setTypeface(null,style);
    }
}
