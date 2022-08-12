package com.cometchatworkspace.components.messages.message_list.message_bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

public class CometChatGroupActionBubble extends RelativeLayout {

    FontUtils fontUtils;
    private final User loggedInUser = CometChat.getLoggedInUser();
    private BaseMessage baseMessage;
    private Context context;
    private View view;


    private TextView title;
    private Palette palette;
    private Typography typography;
    private MaterialCardView cvMessageBubble;

    private final String TAG = "GroupActionBubble";

    public CometChatGroupActionBubble(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatGroupActionBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context,attrs);
    }

    public CometChatGroupActionBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context,attrs);
    }

    public CometChatGroupActionBubble(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                R.styleable.ActionMessageBubble,
                0, 0);

        float cornerRadius  = a.getFloat(R.styleable.ActionMessageBubble_corner_radius,12);
        int backgroundColor = a.getColor(R.styleable.ActionMessageBubble_backgroundColor,0);

        String titleStr = a.getString(R.styleable.ActionMessageBubble_title);
        int titleColor = a.getColor(R.styleable.ActionMessageBubble_titleColor,0);

        view = LayoutInflater.from(getContext()).inflate(R.layout.message_group_action_bubble,null);

        initView(view);
        cornerRadius(cornerRadius);
        backgroundColor(backgroundColor);
        title(titleStr);
        titleColor(titleColor);


//        setColorFilter(baseMessage,cvMessageView);



    }

    private void initView(View view) {
        addView(view);

        title = view.findViewById(R.id.title);


        cvMessageBubble = view.findViewById(R.id.cv_message_container);
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

    public void groupActionObject(BaseMessage baseMessage) {
        String actionMessage = "";
        if (((Action) baseMessage).getAction().equals(CometChatConstants.ActionKeys.ACTION_JOINED))
            actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " " + context.getString(R.string.joined);
        else if (((Action) baseMessage).getAction().equals(CometChatConstants.ActionKeys.ACTION_MEMBER_ADDED))
            actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " "
                    + context.getString(R.string.added) + " " + ((User) ((Action) baseMessage).getActionOn()).getName();
        else if (((Action) baseMessage).getAction().equals(CometChatConstants.ActionKeys.ACTION_KICKED))
            actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " "
                    + context.getString(R.string.kicked_by) + " " + ((User) ((Action) baseMessage).getActionOn()).getName();
        else if (((Action) baseMessage).getAction().equals(CometChatConstants.ActionKeys.ACTION_BANNED))
            actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " "
                    + context.getString(R.string.ban) + " " + ((User) ((Action) baseMessage).getActionOn()).getName();
        else if (((Action) baseMessage).getAction().equals(CometChatConstants.ActionKeys.ACTION_UNBANNED))
            actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " "
                    + context.getString(R.string.unban) + " " + ((User) ((Action) baseMessage).getActionOn()).getName();
        else if (((Action) baseMessage).getAction().equals(CometChatConstants.ActionKeys.ACTION_LEFT))
            actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " " + context.getString(R.string.left);
        else if (((Action) baseMessage).getAction().equals(CometChatConstants.ActionKeys.ACTION_SCOPE_CHANGED)) {
            if (((Action) baseMessage).getNewScope().equals(CometChatConstants.SCOPE_MODERATOR)) {
                actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " " + context.getString(R.string.made) + " "
                        + ((User) ((Action) baseMessage).getActionOn()).getName() + " " + context.getString(R.string.moderator);
            } else if (((Action) baseMessage).getNewScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " " + context.getString(R.string.made) + " "
                        + ((User) ((Action) baseMessage).getActionOn()).getName() + " " + context.getString(R.string.admin);
            } else if (((Action) baseMessage).getNewScope().equals(CometChatConstants.SCOPE_PARTICIPANT)) {
                actionMessage = ((User) ((Action) baseMessage).getActionBy()).getName() + " " + context.getString(R.string.made) + " "
                        + ((User) ((Action) baseMessage).getActionOn()).getName() + " " + context.getString(R.string.participant);
            } else
                actionMessage = ((Action) baseMessage).getMessage();
        }
        title.setText(actionMessage);
    }
}
