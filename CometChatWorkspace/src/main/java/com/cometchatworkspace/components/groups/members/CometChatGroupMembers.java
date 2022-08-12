package com.cometchatworkspace.components.groups.members;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroupMemberList.CometChatGroupMemberList;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;

import java.util.HashMap;
import java.util.List;

public class CometChatGroupMembers extends CometChatListBase {

    private CometChatGroupMemberList cometchatMemberList;
    private Context context;
    private FontUtils fontUtils;
    private View view;
    private Palette palette;
    private Typography typography;
    private Group group;
    private static final HashMap<String, Events> events = new HashMap<>();


    public CometChatGroupMembers(Context context) {
        super(context);
        initViewComponent(context, null, -1);

    }

    public CometChatGroupMembers(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs, -1);
    }

    public CometChatGroupMembers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs, defStyleAttr);
    }

    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {

        fontUtils = FontUtils.getInstance(context);
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();

        view = View.inflate(context, R.layout.cometchat_members, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CometChatGroupMembers,
                0, 0);
        cometchatMemberList = view.findViewById(R.id.cometchatMemberList);

        String title = a.getString(R.styleable.CometChatGroupMembers_title) == null ? getResources().getString(R.string.members) : a.getString(R.styleable.CometChatGroupMembers_title);

        int titleColor = a.getColor(R.styleable.
                CometChatGroupMembers_titleColor, palette.getAccent());
        boolean hideSearchBox = a.getBoolean(R.styleable.
                CometChatGroupMembers_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.
                CometChatGroupMembers_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.
                CometChatGroupMembers_searchBackgroundColor, palette.getAccent50());
        int searchTextColor = a.getColor(R.styleable.
                CometChatGroupMembers_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.CometChatGroupMembers_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.CometChatGroupMembers_searchBorderColor, 0);
        int backgroundColor = a.getColor(R.styleable.CometChatGroupMembers_backgroundColor, palette.getBackground());
        boolean showBackButton = a.getBoolean(R.styleable.CometChatGroupMembers_showBackButton, true);

        Drawable backButtonIcon = a.getDrawable(R.styleable.CometChatGroupMembers_backButtonIcon) != null ? a.getDrawable(R.styleable.CometChatGroupMembers_backButtonIcon) : getResources().getDrawable(R.drawable.ic_arrow_back);
        int listBackgroundColor = a.getColor(R.styleable.
                CometChatGroupMembers_listBackgroundColor, getResources().getColor(android.R.color.transparent));
        String searchPlaceholder = a.getString(R.styleable.CometChatGroupMembers_searchPlaceholder);
        int backIconTint = a.getColor(R.styleable.CometChatGroupMembers_backIconTint, palette.getPrimary());
        int limit = a.getColor(R.styleable.CometChatGroupMembers_limit, 30);
        String searchKeyword = a.getString(R.styleable.CometChatGroupMembers_searchKeyword);
        //End of Handling Attributes

        //Below method will set color of StatusBar.
        setStatusColor(palette.getBackground());

        super.showBackButton(showBackButton);
        super.backIcon(backButtonIcon);
        super.backIconTint(backIconTint);
        super.addSearchViewPlaceHolder(searchPlaceholder);
        super.setTitle(title);
        super.setSearchTextAppearance(typography.getText1());
        super.setTitleAppearance(typography.getHeading());
        emptyStateTextAppearance(typography.getHeading());
        emptyStateTextColor(palette.getAccent400());
        super.setTitleColor(titleColor);
        super.setSearchTextColor(searchTextColor);
        super.setSearchBorderColor(searchBorderColor);
        super.setSearchBorderWidth(searchBorderWidth);
        super.setSearchPlaceHolderColor(searchTextColor);
        setListBackgroundColor(listBackgroundColor);
        super.setSearchBackground(searchBoxColor);
        super.hideSearch(hideSearchBox);
        super.setSearchCornerRadius(searchBoxRadius);
        setLimit(limit);
        setSearchKeyWord(searchKeyword);

        if (palette.getGradientBackground() != null)
            setBackground(palette.getGradientBackground());
        else
            setBackgroundColor(backgroundColor);

        super.addListView(view);
        CometChatError.init(getContext());
        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {
                if (state.equals(SearchState.Filter)) {
                    cometchatMemberList.searchGroups(text);
                } else if (state.equals(SearchState.TextChange)) {
                    if (text.length() == 0) {
//                    // if searchEdit is empty then fetch all groups.
                        cometchatMemberList.clearList();
                    } else {
//                    // Search groups based on text in searchEdit field.
                        cometchatMemberList.searchGroups(text);
                    }
                }
            }

            @Override
            public void onBack() {
                ((Activity) context).onBackPressed();
            }
        });

        cometchatMemberList.setItemClickListener(new OnItemClickListener<GroupMember>() {
            @Override
            public void OnItemClick(GroupMember groupMember, int position) {
                if (events != null) {
                    for (Events e : events.values()) {
                        e.OnItemClick(groupMember, position);
                    }
                }
            }
        });
    }

    public CometChatGroupMembers setLimit(int limit) {
        cometchatMemberList.setLimit(limit);
        return this;
    }

    public CometChatGroupMembers setScope(List<String> scopes) {
        cometchatMemberList.setScopes(scopes);
        return this;
    }

    public CometChatGroupMembers setSearchKeyWord(String searchKeyWord) {
        cometchatMemberList.setSearchKeyword(searchKeyWord);
        return this;
    }

    public CometChatGroupMembers allowKickMembers(boolean allowKickMembers) {
        cometchatMemberList.allowKickMembers(allowKickMembers);
        return this;
    }

    public CometChatGroupMembers allowBanUnbanMembers(boolean allowBanUnbanMembers) {
        cometchatMemberList.allowBanUnbanMembers(allowBanUnbanMembers);
        return this;
    }


    public CometChatGroupMembers allowPromoteDemoteMembers(boolean allowPromoteDemoteMembers) {
        cometchatMemberList.allowPromoteDemoteMembers(allowPromoteDemoteMembers);
        return this;
    }

    public CometChatGroupMembers setGroup(Group group) {
        this.group = group;
        cometchatMemberList.setGroup(group);
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }


    /**
     * This method is used to change the color of create group icon shown at the top.
     *
     * @param color
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setCreateGroupIconTint(int color) {
        if (color != 0) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon != null)
                icon.setImageTintList(ColorStateList.valueOf(color));
        }
    }
    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context,color);

    }

    /**
     * This method is used to change the text color of Empty State Text
     *
     * @param color
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatGroupMembers emptyStateTextColor(@ColorInt int color) {
        if (cometchatMemberList != null && color != 0)
            cometchatMemberList.emptyTextColor(color);
        return this;
    }

    /**
     * This method is used to change the text Appearance of Empty State Text
     *
     * @param appearance
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatGroupMembers emptyStateTextAppearance(int appearance) {
        if (cometchatMemberList != null && appearance != 0)
            cometchatMemberList.emptyStateTextAppearance(appearance);
        return this;
    }

    /**
     * This method is used to change the font of Empty State Text.
     *
     * @param font
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatGroupMembers emptyStateTextFont(String font) {
        if (cometchatMemberList != null && font != null)
            cometchatMemberList.emptyStateTextFont(font);
        return this;
    }

    public static void addListener(String TAG, Events<GroupMember> onEventListener) {
        events.put(TAG, onEventListener);
    }

    private void setStatusBarColor() {
        int backgroundColor = palette.getPrimary();

        if (backgroundColor != 0)
            ((Activity) context).getWindow().setStatusBarColor(backgroundColor);

    }

    public CometChatGroupMembers setListBackgroundColor(@ColorInt int listBackgroundColor) {
        if (cometchatMemberList != null) {
            if (listBackgroundColor != 0)
                cometchatMemberList.setCardBackgroundColor(listBackgroundColor);
            else {
                cometchatMemberList.setCardBackgroundColor(Color.TRANSPARENT);
                cometchatMemberList.setCardElevation(0);
                cometchatMemberList.setRadius(0);
            }
        }
        return this;
    }

    /**
     * This method is used to set the configuration in CometChatGroups.
     *
     * @param configuration the configuration
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setConfiguration(CometChatConfigurations configuration) {
        cometchatMemberList.setConfigurations(configuration);
    }

    /**
     * This method is used to set the multiple configurations in CometChatGroups
     *
     * @param configurations the configurations
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setConfigurations(List<CometChatConfigurations> configurations) {
        cometchatMemberList.setConfigurations(configurations);
    }


    public abstract static class Events<T> {

        /**
         * It is triggered whenever any item from cometchatMemberList is clicked
         *
         * @param var      the var
         * @param position the position
         * @author CometChat Team
         * Copyright &copy; 2021 CometChat Inc.
         */
        public abstract void OnItemClick(T var, int position);

        /**
         * It is triggered whenever a long press action is performed
         * on the item from cometchatMemberList
         *
         * @param var      the var
         * @param position the position
         * @author CometChat Team
         * Copyright &copy; 2021 CometChat Inc.
         */
        public void OnItemLongClick(T var, int position) {

        }
    }

}
