package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroups;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Group;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;

public class CometChatGroupListItem extends RelativeLayout {

    FontUtils fontUtils;

    Context context;

    private TextView groupListItemTitle,groupListItemSubtitle,tvSeperator;
    private CometChatAvatar groupListItemIcon;
    private int privateGroupIcon=0,publicGroupIcon=0,protectedGroupIcon=0;

    public CometChatGroupListItem(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    public CometChatGroupListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);
    }

    private void initViewComponent(Context context,AttributeSet attrs,int defaultStyle) {
        this.context = context;
        fontUtils=FontUtils.getInstance(context);
        View view = View.inflate(context, R.layout.cometchat_group_item, null);
        addView(view);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatGroupListItem,
                0, 0);

        boolean iconHidden = a.getBoolean(R.styleable
                .CometChatGroupListItem_hideIcon,false);
        boolean titleHidden = a.getBoolean(R.styleable
                .CometChatGroupListItem_groupListItem_hideTitle,false);
        int titleColor = a.getColor(R.styleable
                .CometChatGroupListItem_groupListItem_titleColor,0);
        boolean subTitleHidden = a.getBoolean(R.styleable
                .CometChatGroupListItem_groupListItem_hideSubTitle,true);
        int subTitleColor = a.getColor(R.styleable
                .CometChatGroupListItem_groupListItem_subTitleColor,0);
        int typeIconTint = a.getColor(R.styleable.CometChatGroupListItem_groupListItem_typeIconTint,0);

        groupListItemTitle = view.findViewById(R.id.groupListItem_title);
        groupListItemSubtitle = view.findViewById(R.id.groupListItem_subtitle);
        groupListItemIcon = view.findViewById(R.id.groupListItem_Icon);
        tvSeperator = view.findViewById(R.id.tvSeperator);

        hideIcon(iconHidden);
        hideTitle(titleHidden);
        setTypeIconTint(typeIconTint);
        setTitleColor(titleColor);
        setSubTitleColor(subTitleColor);
        hideSubTitle(subTitleHidden);
    }


    public void setTypeIconTint(@ColorInt int typeIconTint) {
        if (typeIconTint!=0)
            groupListItemTitle.setCompoundDrawableTintList(ColorStateList.valueOf(typeIconTint));
    }


    public void setTitle(String titleStr) {
        groupListItemTitle.setText(titleStr);
    }

    public void hideTitle(boolean isHidden) {
        if (isHidden)
            groupListItemTitle.setVisibility(View.GONE);
        else
            groupListItemTitle.setVisibility(View.VISIBLE);
    }

    public void setTitleFont(String fonts) {
        groupListItemTitle.setTypeface(fontUtils.getTypeFace(fonts));
    }


    public void setTitleColor(int color) {
        groupListItemTitle.setTextColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setTitleImageColor(int color) {
        groupListItemTitle.setCompoundDrawableTintList(ColorStateList.valueOf(color));
    }

    public void setSubTitle(String subTitleStr) {
        groupListItemSubtitle.setText(subTitleStr);
    }

    public void setSubTitleColor(int color) {
        groupListItemSubtitle.setTextColor(color);
    }

    public void setSubTitleFont(String fonts) {
        groupListItemSubtitle.setTypeface(fontUtils.getTypeFace(fonts));
    }

    public void hideSubTitle(boolean isHidden) {
        if (isHidden)
            groupListItemSubtitle.setVisibility(View.GONE);
        else
            groupListItemSubtitle.setVisibility(View.VISIBLE);
    }

    public void hideIcon(boolean iconHidden) {
        if (iconHidden)
            groupListItemIcon.setVisibility(View.GONE);
        else
            groupListItemIcon.setVisibility(View.VISIBLE);
    }
    public void setIcon(String url, String initials) {
        groupListItemIcon.setAvatar(url);
        if(url==null)
            groupListItemIcon.setInitials(initials);
    }

    public void setIcon(Drawable drawable) {
        groupListItemIcon.setDrawable(drawable);
    }

    public void setPrivateGroupIcon(@DrawableRes int res) {
        privateGroupIcon = res;
    }

    public void setPublicGroupIcon(@DrawableRes int res) {
        publicGroupIcon = res;
    }

    public void setProtectedGroupIcon(@DrawableRes int res) {
        protectedGroupIcon = res;
    }

    public void setType(String groupType) {
        if (groupType.equals(CometChatConstants.GROUP_TYPE_PRIVATE)) {
            if (privateGroupIcon!=0)
                groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, privateGroupIcon, 0);
            else
                groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_private_group, 0);
        }
        else if (groupType.equals(CometChatConstants.GROUP_TYPE_PASSWORD)) {
            if (protectedGroupIcon!=0)
                groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, protectedGroupIcon, 0);
            else
                groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_protected_group, 0);
        }
        else if (groupType.equals(CometChatConstants.GROUP_TYPE_PUBLIC)) {
            groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, publicGroupIcon, 0);
        } else
            groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

    }

    public void setGroup(Group group) {
        if (group!=null) {
            setTitle(group.getName());
            setSubTitle(context.getString(R.string.members) + ": " + group.getMembersCount());
            setType(group.getGroupType());
            setIcon(group.getIcon(), group.getName());
        }
        setSubTitleFont(FontUtils.robotoRegular);
        setTitleFont(FontUtils.robotoMedium);
        if(Utils.isDarkMode(context)) {
            setTitleImageColor(context.getResources().getColor(R.color.grey));
            setTitleColor(context.getResources().getColor(R.color.textColorWhite));
//            groupViewHolder.tvSeprator.setBackgroundColor(context.getResources().getColor(R.color.grey));
        } else {
            setTitleImageColor(context.getResources().getColor(R.color.message_bubble_grey));
            setTitleColor(context.getResources().getColor(R.color.primaryTextColor));
//            groupViewHolder.tvSeprator.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
        }

    }
}