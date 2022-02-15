package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatUsers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

public class CometChatUserListItem extends RelativeLayout {

    FontUtils fontUtils;

    Context context;

    User user;
    TextView userListItemTitle;
    TextView userListItemSubTitle;
    CometChatAvatar userListItemAvatar;
    CometChatStatusIndicator userListItemUserPresence;
    TextView tvSeperator;

    MaterialCardView parentLayout;

    public CometChatUserListItem(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    public CometChatUserListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);
    }

    public void setUser(User user) {
        setStatusIndicator(user.getStatus());
        hideStatusIndicator(!user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE));

        setTitle(user.getName());
        setSubTitleFont(FontUtils.robotoMedium);

        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            getAvatar().setInitials(user.getName());
        } else {
            getAvatar().setAvatar(user.getAvatar());
        }

        if(Utils.isDarkMode(context)) {
            setTitleColor(context.getResources().getColor(R.color.textColorWhite));
            setSeperatorColor(context.getResources().getColor(R.color.grey));
        } else {
            setTitleColor(context.getResources().getColor(R.color.primaryTextColor));
            setSeperatorColor(context.getResources().getColor(R.color.light_grey));
        }
    }
    private void initViewComponent(Context context, AttributeSet attrs, int i) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        View view = View.inflate(context, R.layout.cometchat_user_item, null);
        addView(view);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatUserListItem,
                0, 0);

        boolean avatarHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideAvatar,false);
        boolean titleHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideTitle,false);
        int titleColor = a.getColor(R.styleable
                .CometChatUserListItem_userListItem_titleColor,0);
        boolean subTitleHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideSubTitle,true);
        int subTitleColor = a.getColor(R.styleable
                .CometChatUserListItem_userListItem_subTitleColor,0);
        boolean userPresenceHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideUserPresence,false);

        parentLayout = view.findViewById(R.id.view_foreground);
        userListItemTitle = view.findViewById(R.id.userListItem_title);
        userListItemSubTitle = view.findViewById(R.id.userListItem_subTitle);
        userListItemAvatar = view.findViewById(R.id.userListItem_avatar);
        userListItemUserPresence = view.findViewById(R.id.userListItem_userPresence);
        tvSeperator = view.findViewById(R.id.tvSeperator);

        hideAvatar(avatarHidden);
        hideTitle(titleHidden);
        setTitleColor(titleColor);
        setSubTitleColor(subTitleColor);
        hideSubTitle(subTitleHidden);
        hideStatusIndicator(userPresenceHidden);
    }

    public void setTitle(String titleStr) {
        userListItemTitle.setText(titleStr);
    }

    public void hideTitle(boolean isHidden) {
        if (isHidden)
           userListItemTitle.setVisibility(View.GONE);
        else
           userListItemTitle.setVisibility(View.VISIBLE);
    }

    public void setTitleFont(String fonts) {
       userListItemTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }

    public void setTitleColor(int color) {
       userListItemTitle.setTextColor(color);
    }

    public void setSubTitle(String subTitleStr) {
       userListItemSubTitle.setText(subTitleStr);
    }

    public void setSubTitleColor(int color) {
       userListItemSubTitle.setTextColor(color);
    }

    public void setSubTitleFont(String fonts) {
       userListItemSubTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }

    public void hideSubTitle(boolean subTitleHidden) {
        if (subTitleHidden)
            userListItemSubTitle.setVisibility(View.GONE);
        else
            userListItemSubTitle.setVisibility(View.VISIBLE);
    }

    public void setStatusIndicator(@CometChatStatusIndicator.Presence String userPresenceStr) {
       userListItemUserPresence.status(userPresenceStr);
    }

    public void hideStatusIndicator(boolean isHidden) {
        if (isHidden)
           userListItemUserPresence.setVisibility(View.GONE);
        else
           userListItemUserPresence.setVisibility(View.VISIBLE);
    }

    public void setBackgroundColor(@ColorInt int color) {
        if (color!=0 && parentLayout!=null) {
            parentLayout.setCardBackgroundColor(color);
        }
    }

    public void setCornerRadius(int radius) {
        if (parentLayout!=null) {
            parentLayout.setRadius(radius);
        }
    }

    public void setBorderColor(@ColorInt int color) {
        if (color!=0 && parentLayout!=null)
            parentLayout.setStrokeColor(color);
    }

    public void setBorderWidth(int width) {
        if (parentLayout!=null) {
            parentLayout.setStrokeWidth(width);
        }
    }

    public void hideAvatar(boolean isHidden) {
        if (isHidden)
            userListItemAvatar.setVisibility(View.GONE);
        else
            userListItemAvatar.setVisibility(View.VISIBLE);
    }

    public void setAvatar(@NonNull String url, String initials) {
       userListItemAvatar.setAvatar(url);
        if (url==null)
           userListItemAvatar.setInitials(initials);
    }

    public void setAvatar(Drawable drawable) {
       userListItemAvatar.setDrawable(drawable);
    }

    public CometChatAvatar getAvatar() {
        return userListItemAvatar;
    }

    public void hideSeperator(boolean isHidden) {
        if (isHidden)
           tvSeperator.setVisibility(View.GONE);
        else
           tvSeperator.setVisibility(View.VISIBLE);
    }

    public void setSeperatorColor(int color) {
       tvSeperator.setBackgroundColor(color);
    }
}