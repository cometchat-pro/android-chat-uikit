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
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

public class CometChatGroupListItem extends MaterialCardView {

    FontUtils fontUtils;

    Context context;

    private TextView groupListItemTitle, groupListItemSubtitle, tvSeperator;
    private CometChatAvatar groupListItemIcon;
    private CometChatStatusIndicator groupTypeIndicator;
    private RelativeLayout parent;
    private int privateGroupIcon = 0, publicGroupIcon = 0, protectedGroupIcon = 0;
    private Palette palette;
    private Typography typography;
    private Group group;
    public CometChatGroupListItem(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    public CometChatGroupListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);
    }

    private void initViewComponent(Context context, AttributeSet attrs, int defaultStyle) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        View view = View.inflate(context, R.layout.cometchat_group_item, null);
        addView(view);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CometChatGroupListItem,
                0, 0);

        boolean iconHidden = a.getBoolean(R.styleable
                .CometChatGroupListItem_hideIcon, false);
        boolean titleHidden = a.getBoolean(R.styleable
                .CometChatGroupListItem_groupListItem_hideTitle, false);
        int titleColor = a.getColor(R.styleable
                .CometChatGroupListItem_groupListItem_titleColor, palette.getAccent());
        boolean subTitleHidden = a.getBoolean(R.styleable
                .CometChatGroupListItem_groupListItem_hideSubTitle, false);
        boolean isGroupTypeIndicatorHidden = a.getBoolean(R.styleable
                .CometChatGroupListItem_groupListItem_hideGroupTypeIndicator, true);
        int subTitleColor = a.getColor(R.styleable.CometChatGroupListItem_groupListItem_subTitleColor, palette.getAccent600());

        int typeIconTint = a.getColor(R.styleable.CometChatGroupListItem_groupListItem_typeIconTint, 0);

        groupListItemTitle = view.findViewById(R.id.groupListItem_title);
        groupListItemSubtitle = view.findViewById(R.id.groupListItem_subtitle);
        groupListItemIcon = view.findViewById(R.id.groupListItem_Icon);
        tvSeperator = view.findViewById(R.id.tvSeperator);
        parent = view.findViewById(R.id.parent);
        groupTypeIndicator = view.findViewById(R.id.groupTypeIndicator);

        hideIcon(iconHidden);
        hideGroupTypeIndicator(isGroupTypeIndicatorHidden);
        hideTitle(titleHidden);
        setTypeIconTint(typeIconTint);
        setTitleColor(titleColor);
        setTitleTextAppearance(typography.getName());
        setSubTitleTextAppearance(typography.getSubtitle1());
        setSubTitleColor(subTitleColor);
        hideSubTitle(subTitleHidden);
        setAvatarBackgroundColor(palette.getAccent700());
        setAvatarTextColor(palette.getAccent900());
        setAvatarTextAppearance(typography.getName());
        setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    public void setAvatarBackgroundColor(int color) {
        if (color != 0)
            groupListItemIcon.setBackgroundColor(color);

    }

    public void setAvatarTextColor(int color) {
        if (color != 0)
            groupListItemIcon.setTextColor(color);

    }

    public void setAvatarTextAppearance(int appearance) {
        if (appearance != 0)
            groupListItemIcon.setTextAppearance(appearance);

    }

    public void hideGroupTypeIndicator(boolean isGroupTypeIndicatorHidden) {
        if (isGroupTypeIndicatorHidden) {
            groupTypeIndicator.setVisibility(GONE);
        } else
            groupTypeIndicator.setVisibility(VISIBLE);

    }

    public void setItemBackGroundColor(@ColorInt int color) {
        parent.setBackgroundColor(color);
    }

    /**
     * @InputData is a class which is helpful to set data into the view and control visibility
     * as per value passed in constructor .
     * i.e we can control the visibility of the component inside the CometChatUserListItem,
     * and also decide what value i need to show in that particular view
     */
    public void inputData(InputData inputData) {


        setSubTitle(inputData.getSubTitle(group).toString());
        hideSubTitle(false);

        hideTitle(!inputData.isTitle());

        hideIcon(!inputData.isThumbnail());
        hideGroupTypeIndicator(!inputData.isStatus());

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
            setItemBackGroundColor(style.getBackground());
        }
    }

    public void setTypeIconTint(@ColorInt int typeIconTint) {
        if (typeIconTint != 0)
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

    public void setTitleTextAppearance(int appearance) {
        if (groupListItemTitle != null && appearance != 0)
            groupListItemTitle.setTextAppearance(context, appearance);
    }

    public void setSubTitleTextAppearance(int appearance) {
        if (groupListItemSubtitle != null && appearance != 0)
            groupListItemSubtitle.setTextAppearance(context, appearance);
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
        if (url == null)
            groupListItemIcon.setInitials(initials);
        else
            groupListItemIcon.setAvatar(url);

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
        if (groupType.equals(CometChatConstants.GROUP_TYPE_PASSWORD)) {
            if (protectedGroupIcon != 0)
                groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, protectedGroupIcon, 0);
            else
                groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_password_protected_group, 0);
        } else if (groupType.equals(CometChatConstants.GROUP_TYPE_PUBLIC)) {
            groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, publicGroupIcon, 0);
        } else
            groupListItemTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group=group;
            setTitle(group.getName());
            if (group.getMembersCount() <= 1)
                setSubTitle(group.getMembersCount() + " " + context.getString(R.string.member));
            else
                setSubTitle(group.getMembersCount() + " " + context.getString(R.string.members));

            setType(group.getGroupType());
            setIcon(group.getIcon(), group.getName());
        }
//        setSubTitleFont(CometChatTheme.Typography.robotoRegular);
//        setTitleFont(CometChatTheme.Typography.robotoMedium);

    }
}