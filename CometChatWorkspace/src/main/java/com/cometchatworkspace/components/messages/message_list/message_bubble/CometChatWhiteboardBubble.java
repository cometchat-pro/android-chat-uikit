package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

public class CometChatWhiteboardBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;

    private RelativeLayout rlMessageBubble;

    private ImageView icon;
    private TextView title;
    private TextView subtitle;
    private TextView joinBtn;

    private MaterialCardView cvMessageBubble;
    private RelativeLayout cvMessageBubbleLayout;

    private final String TAG = "WhiteboardMessageBubble";

    private MessageBubbleListener messageBubbleListener;

    private Palette palette;
    private Typography typography;

    private String url;

    private int layoutId;

    public CometChatWhiteboardBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatWhiteboardBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatWhiteboardBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatWhiteboardBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                R.styleable.WhiteboardMessageBubble,
                0, 0);
        float cornerRadius  = a.getFloat(R.styleable.WhiteboardMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.WhiteboardMessageBubble_backgroundColor,0);
        Drawable messageAvatar = a.getDrawable(R.styleable.WhiteboardMessageBubble_avatar);
        int hideAvatar = a.getInt(R.styleable.WhiteboardMessageBubble_avatarVisibility,View.VISIBLE);
        int hideUserName = a.getInt(R.styleable.WhiteboardMessageBubble_userNameVisibility,View.VISIBLE);
        String userName = a.getString(R.styleable.WhiteboardMessageBubble_userName);
        int color = a.getColor(R.styleable.WhiteboardMessageBubble_userNameColor,0);

        String titleStr = a.getString(R.styleable.WhiteboardMessageBubble_title);
        int titleColor = a.getColor(R.styleable.WhiteboardMessageBubble_titleColor,0);

        String buttonText = a.getString(R.styleable.WhiteboardMessageBubble_buttonText);

        Drawable iconDrawable = a.getDrawable(R.styleable.WhiteboardMessageBubble_icon);
        int iconColor = a.getColor(R.styleable.WhiteboardMessageBubble_iconTint,0);

        int borderColor = a.getColor(R.styleable.WhiteboardMessageBubble_borderColor,0);
        int borderWidth = a.getInt(R.styleable.WhiteboardMessageBubble_borderWidth,0);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_right_whiteboard_bubble,null);

        initView(view);

        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);

        title(titleStr);
        titleColor(titleColor);
        icon(iconDrawable);
        iconTint(iconColor);
        buttonText(buttonText);
        buttonTextColor(palette.getPrimary());

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
        subtitle = view.findViewById(R.id.subtitle);
        joinBtn = view.findViewById(R.id.join_button);

        cvMessageBubble = view.findViewById(R.id.cv_message_container);
        cvMessageBubbleLayout = view.findViewById(R.id.cv_message_container_layout);

        rlMessageBubble = view.findViewById(R.id.rl_message);

        //CustomView
//        CometChatMessageTemplate messageTemplate = CometChatMessagesConfigurations
//                .getMessageTemplateById(CometChatMessageTemplate.DefaultList.document);
//        if(messageTemplate!=null)
//            layoutId = messageTemplate.getView();
////        dataView = messageTemplate.getDataView();
//        if (layoutId != 0) {
//            View customView = LayoutInflater.from(context).inflate(layoutId, null);
//            cvMessageBubbleLayout.setVisibility(View.GONE);
//            if (customView.getParent() != null)
//                ((ViewGroup) customView.getParent()).removeAllViewsInLayout();
//            cvMessageBubble.addView(customView);
//            joinBtn = customView.findViewById(R.id.join_button);
//            customView.setOnLongClickListener(new OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    messageBubbleListener.onLongCLick(baseMessage);
//                    return true;
//                }
//            });
//        }
    }

    public void borderColor(@ColorInt int color) {
        if (color!=0 && cvMessageBubble!=null)
            cvMessageBubble.setStrokeColor(color);
    }

    public void borderWidth(int width) {
        if (cvMessageBubble!=null)
            cvMessageBubble.setStrokeWidth(width);
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
        else {
            joinBtn.setText(context.getString(R.string.open_document));
        }
    }


    private void buttonTextColor(int color) {
        if (joinBtn!=null)
            joinBtn.setTextColor(color);
    }

    public void buttonIcon(Drawable icon,int gravity) {
        if (joinBtn!=null && icon!=null) {
            if (gravity== Gravity.LEFT)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            else if (gravity== Gravity.TOP)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
            else if (gravity== Gravity.RIGHT)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,icon,null);
            else if (gravity==Gravity.BOTTOM)
                joinBtn.setCompoundDrawablesWithIntrinsicBounds(null,null,null,icon);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void buttonIconTint(@ColorInt int color) {
        if (joinBtn!=null)
            joinBtn.setCompoundDrawableTintList(ColorStateList.valueOf(color));
    }

    public void buttonTextFont(String font) {
        if (font!=null && joinBtn!=null)
            joinBtn.setTypeface(fontUtils.getTypeFace(font));
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

    public void whiteboardUrl(String url) {
        if (url!=null) {
            this.url = url;
            baseMessage = new BaseMessage();
            try {
                JSONObject injectedObject = new JSONObject();//"@injected");
                JSONObject extensionsObject = new JSONObject();//"extensions");
                JSONObject whiteBoardData = new JSONObject();//"whiteboard");
                JSONObject boardUrl = new JSONObject();
                boardUrl.put("board_url", url);
                whiteBoardData.put("whiteboard",boardUrl);
                extensionsObject.put("extensions",whiteBoardData);
                injectedObject.put("@injected",extensionsObject);
                baseMessage.setMetadata(injectedObject);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            joinBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageBubbleListener.onClick(baseMessage);
                }
            });
        }
    }

    public void messageObject(BaseMessage baseMessage) {
        this.baseMessage = baseMessage;

       joinBtn.setOnClickListener(new OnClickListener() {
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

    public void subTitle(String str) {
        if (subtitle!=null)
            subtitle.setText(str);
    }
    public void title(String str) {
        if (str!=null)
            title.setText(str);
    }
    public void titleColor(@ColorInt int color) {
        if (title!=null && color!=0)
        title.setTextColor(color);
    }
    public void titleFont(String font) {
        title.setTypeface(fontUtils.getTypeFace(font));
    }


    public void setEventListener(MessageBubbleListener listener) {
        messageBubbleListener = listener;
    }
}
