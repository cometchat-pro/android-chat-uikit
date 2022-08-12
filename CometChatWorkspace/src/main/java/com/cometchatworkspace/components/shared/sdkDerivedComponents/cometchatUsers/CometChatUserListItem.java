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

import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

public class CometChatUserListItem extends MaterialCardView {

    FontUtils fontUtils;

    Context context;
    User user;
    TextView userListItemTitle;
    TextView userListItemSubTitle;
    CometChatAvatar userListItemAvatar;
    CometChatStatusIndicator userListItemUserPresence;
    TextView tvSeperator;
    boolean isRole = false, isStatus = true;
    RelativeLayout parentLayout;
    int titleColor;

    private Palette palette;
    private Typography typography;

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
        if (isStatus)
            setSubTitle(user.getStatus());
        else if (isRole)
            setSubTitle(user.getRole());


//        hideStatusIndicator(!user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE));
        setTitle(user.getName());
//        setSubTitleFont(CometChatTheme.Typography.robotoMedium);

        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            getAvatar().setInitials(user.getName());
        } else {
            getAvatar().setAvatar(user.getAvatar());
        }
    }

    private void initViewComponent(Context context, AttributeSet attrs, int i) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        View view = View.inflate(context, R.layout.cometchat_user_item, null);
        addView(view);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatUserListItem,
                0, 0);

        boolean avatarHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideAvatar, false);
        boolean titleHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideTitle, false);
        titleColor = a.getColor(R.styleable
                .CometChatUserListItem_userListItem_titleColor, palette.getAccent());
        boolean subTitleHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideSubTitle, true);
        int subTitleColor = a.getColor(R.styleable
                .CometChatUserListItem_userListItem_subTitleColor, palette.getAccent600());
        boolean userPresenceHidden = a.getBoolean(R.styleable
                .CometChatUserListItem_userListItem_hideUserPresence, false);

        parentLayout = view.findViewById(R.id.view_foreground);
        userListItemTitle = view.findViewById(R.id.userListItem_title);
        userListItemSubTitle = view.findViewById(R.id.userListItem_subTitle);
        userListItemAvatar = view.findViewById(R.id.userListItem_avatar);
        userListItemUserPresence = view.findViewById(R.id.userListItem_userPresence);
        tvSeperator = view.findViewById(R.id.tvSeperator);

        hideAvatar(avatarHidden);
        hideTitle(titleHidden);
        setTitleColor(titleColor);
        setTitleTextAppearance(typography.getName());
        setSubTitleTextAppearance(typography.getSubtitle1());
        setSubTitleColor(subTitleColor);
//        setTitleFont(CometChatTheme.Typography.robotoMedium);
        hideSubTitle(subTitleHidden);
        hideStatusIndicator(userPresenceHidden);
        setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setAvatarBackgroundColor(palette.getAccent700());
        setAvatarTextColor(palette.getAccent900());
        setAvatarTextAppearance(typography.getName());
    }

    public void setAvatarBackgroundColor(int color) {
        if (color != 0)
            getAvatar().setBackgroundColor(color);
    }

    public void setAvatarTextColor(int color) {
        if (color != 0)
            getAvatar().setTextColor(color);
    }

    public void setAvatarTextAppearance(int appearance) {
        if (appearance != 0)
            getAvatar().setTextAppearance(appearance);
    }


    /**
     * @InputData is a class which is helpful to set data into the view and control visibility
     * as per value passed in constructor .
     * i.e we can control the visibility of the component inside the CometChatUserListItem,
     * and also decide what value i need to show in that particular view
     */

    public void inputData(InputData inputData) {

        setSubTitle(inputData.getSubTitle(user).toString());
        hideSubTitle(false);

        hideTitle(!inputData.isTitle());

        hideAvatar(!inputData.isThumbnail());
        hideStatusIndicator(!inputData.isStatus());


//        if (data.getThumbnail() == null || data.getThumbnail().isEmpty()) {
//            hideAvatar(true);
//        } else {
//            getAvatar().setAvatar(data.getThumbnail());
//        }
//        if (data.getTitle() != null) {
//            setTitle(data.getTitle());
//        } else {
//            hideTitle(true);
//        }
//        if (data.getSubTitle() != null) {
//            setSubTitle(data.getSubTitle());
//        } else {
//            hideSubTitle(true);
//        }
//        if (data.getStatus() != null) {
//            setStatusIndicator(data.getStatus());
//        } else {
//            hideStatusIndicator(true);
//        }


    }

    public void setStyle(Style style) {

        if (style.getBorder() > 0) {
            setStrokeWidth(style.getBorder());
        }
        if (style.getCornerRadius() > 0) {
            setRadius(style.getCornerRadius());
        }
        if (style.getSubTitleColor() != 0) {
            setTitleColor(style.getTitleColor());
        }
        if (style.getSubTitleColor() != 0) {
            setSubTitleColor(style.getSubTitleColor());
        }
        if (style.getTitleFont() != null && !style.getTitleFont().isEmpty()) {
            setTitleFont(style.getTitleFont());
        }
        if (style.getSubTitleFont() != null && !style.getSubTitleFont().isEmpty()) {
            setSubTitleFont(style.getSubTitleFont());
        }
        if (style.getBackground() != 0) {
            setBackgroundColor(style.getBackground());
        }
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
        if (fonts != null && !fonts.isEmpty())
            userListItemTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }


    public void setTitleColor(int color) {
        if (color != 0)
            userListItemTitle.setTextColor(color);
        else
            userListItemSubTitle.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
    }

    public void setSubTitle(String subTitleStr) {
        userListItemSubTitle.setText(subTitleStr);
    }

    public void setSubTitleColor(int color) {
        if (color != 0)
            userListItemSubTitle.setTextColor(color);
    }


    public void setSubTitleFont(String fonts) {
        if (fonts != null && !fonts.isEmpty())
            userListItemSubTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }

    public void setTitleTextAppearance(int appearance) {
        if (userListItemTitle != null && appearance != 0)
            userListItemTitle.setTextAppearance(context, appearance);
    }

    public void setSubTitleTextAppearance(int appearance) {
        if (userListItemSubTitle != null && appearance != 0)
            userListItemSubTitle.setTextAppearance(context, appearance);
    }

    public void hideSubTitle(boolean subTitleHidden) {
        if (subTitleHidden)
            userListItemSubTitle.setVisibility(View.GONE);
        else
            userListItemSubTitle.setVisibility(View.VISIBLE);
    }

    public void setStatusIndicator(@CometChatStatusIndicator.STATUS String userPresenceStr) {
        userListItemUserPresence.status(userPresenceStr);
    }

    public void hideStatusIndicator(boolean isHidden) {
        if (isHidden)
            userListItemUserPresence.setVisibility(View.GONE);
        else
            userListItemUserPresence.setVisibility(View.VISIBLE);
    }

    public void setCornerRadius(int radius) {
        if (parentLayout != null) {
            setRadius(radius);
        }
    }

    public void setBorderColor(@ColorInt int color) {
        if (color != 0 && parentLayout != null)
            setStrokeColor(color);
    }

    public void setBorderWidth(int width) {
        if (parentLayout != null) {
            setStrokeWidth(width);
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
        if (url == null)
            userListItemAvatar.setInitials(initials);
    }

    public void setAvatar(Drawable drawable) {
        userListItemAvatar.setDrawable(drawable);
    }

    public CometChatAvatar getAvatar() {
        return userListItemAvatar;
    }

    public void hideSeparator(boolean isHidden) {
        if (isHidden)
            tvSeperator.setVisibility(View.GONE);
        else
            tvSeperator.setVisibility(View.VISIBLE);
    }

    public void setSeparatorColor(int color) {
        tvSeperator.setBackgroundColor(color);
    }

}