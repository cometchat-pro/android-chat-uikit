package com.cometchatworkspace.components.users;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.UsersListConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatUsers.CometChatUserList;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose - CometChatUsers class is a component used to display list of users and perform certain action on click of item.
 * It also provide search bar to perform search operation on the list of users.User can search by username, uid.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 4th February 2022
 *
 * @author CometChat Team
 * Copyright &copy; 2021 CometChat Inc.
 */
public class CometChatUsers extends CometChatListBase {

    /**
     * Uses to display list of users.
     */
    public CometChatUserList cometchatUserList;

    /**
     * Used to check if user is friend
     */
    private boolean isFriends;

    private static final String TAG = "UserList";

    private View view;

    private Context context;

    private FontUtils fontUtils;

    private ImageView icon;

    private ProgressDialog progressDialog;

    private RecyclerViewSwipeListener swipeHelper;

    private Palette palette;
    private Typography typography;

    private final List<String> uids = new ArrayList<>();
    private final List<GroupMember> groupMembers = new ArrayList<>();
    private OnAddMemberClick onAddMemberClick;

    /**
     * Constructor used to initiate a CometChatUsers.
     *
     * @param context
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatUsers(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context, null, -1);
    }

    /**
     * Constructor used to initiate a CometChatUsers.
     *
     * @param context
     * @param attrs
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatUsers(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs, -1);
    }

    /**
     * Constructor used to initiate a CometChatUsers.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatUsers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * This method is used to initialize the component available in CometChatUsers
     *
     * @param context      - It is reference object of Context
     * @param attributeSet - It is object of AttributeSet which is used to handle attributes passed from xml
     * @param defStyleAttr
     * @author CometChat Team
     * Copyright &copy;  2022 CometChat Inc.
     */
    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {
        // Inflate the layout for this fragment
        fontUtils = FontUtils.getInstance(context);
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        view = View.inflate(context, R.layout.cometchat_users, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.Users,
                0, 0);

        //Start of Handling Attributes
        String title = a.getString(R.styleable.Users_title) != null ? a.getString(R.styleable.Users_title) : getResources().getString(R.string.users);

        int titleColor = a.getColor(R.styleable.
                Users_titleColor, palette.getAccent());

        Drawable backButtonIcon = a.getDrawable(R.styleable.Users_backButtonIcon);

        boolean showBackButton = a.getBoolean(
                R.styleable.Users_showBackButton, false);
        boolean hideTags = a.getBoolean(R.styleable.Users_hideTags, true);
        int filterIconColor = a.getColor(R.styleable.
                Users_filterIconColor, 0);
        Drawable filterIcon = a.getDrawable(R.styleable.Users_filterIcon);
        boolean searchBoxVisible = a.getBoolean(R.styleable.
                Users_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.
                Users_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.
                Users_searchBackgroundColor, palette.getAccent50());
        int searchTextColor = a.getColor(R.styleable.
                Users_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.Users_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.Users_searchBorderColor, 0);

        int listBackgroundColor = a.getColor(R.styleable.
                Users_listBackgroundColor, getResources().getColor(android.R.color.transparent));
        String searchHint = a.getString(R.styleable.
                Users_searchHint);
        int backgroundColor = a.getColor(R.styleable.Users_backgroundColor, palette.getBackground());

        //End of Handling Attributes

        //Below method will set color of StatusBar.
        setStatusColor(palette.getBackground());
        cometchatUserList = view.findViewById(R.id.cometchat_user_list);

        /**
         * setList as per Uid List
         */
        super.addSearchViewPlaceHolder(searchHint);
        super.setTitle(title);
        super.backIcon(backButtonIcon);
        super.showBackButton(showBackButton);
        super.setSearchTextAppearance(typography.getText1());
//        super.setTitleAppearance(typography.getHeading());
        emptyStateTextAppearance(typography.getHeading());
        emptyStateTextColor(palette.getAccent400());
        super.setTitleColor(titleColor);
        super.setSearchTextColor(searchTextColor);
        super.setSearchBorderColor(searchBorderColor);
        super.setSearchBorderWidth(searchBorderWidth);
        super.setSearchPlaceHolderColor(searchTextColor);
        setListBackgroundColor(listBackgroundColor);
        super.setSearchBackground(searchBoxColor);
        super.hideSearch(searchBoxVisible);
        super.setSearchCornerRadius(searchBoxRadius);
        if (palette.getGradientBackground() != null)
            setBackground(palette.getGradientBackground());
        else
            setBackgroundColor(backgroundColor);

        super.addListView(view);

        CometChatError.init(getContext());
//        if (!hideTags) {
//            icon = new ImageView(context);
//            icon.setImageResource(R.drawable.ic_list_bulleted_white_24dp);
//            icon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
//            super.addMenuIcon(icon);
//            icon.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    for (Events e : events.values()) {
////                        e.onMenuIconClick("tags");
////                    }
//                }
//            });
//        }

        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {
                if (state.equals(SearchState.Filter)) {
                    cometchatUserList.searchUser(text);
                    cometchatUserList.setSearchKeyword(text);
                } else if (state.equals(SearchState.TextChange)) {
                    if (text.length() == 0) {
//                    // if searchEdit is empty then fetch all users.
                        cometchatUserList.setSearchKeyword(null);
                        cometchatUserList.refreshList();
                    } else {
//                    // Search users based on text in searchEdit field.
                        cometchatUserList.searchUser(text);
                    }
                }
            }

            @Override
            public void onBack() {
                ((Activity) context).onBackPressed();
            }
        });

        CometChatUserEvents.addUserListener("CometChatUsers", new CometChatUserEvents() {
            @Override
            public void onError(CometChatException error) {

            }

            @Override
            public void onItemClick(User user, int position) {

            }

            @Override
            public void onItemLongClick(User user, int position) {

            }

            @Override
            public void onUserBlock(User user) {
                if (cometchatUserList != null) {
                    if (cometchatUserList.isBlockedUsersHidden()) {
                        cometchatUserList.remove(user);
                    } else {
                        cometchatUserList.update(user);
                    }
                }
            }

            @Override
            public void onUserUnblock(User user) {
                if (cometchatUserList != null) {
                    if (cometchatUserList.isBlockedUsersHidden()) {
                        cometchatUserList.add(user);
                    } else {
                        cometchatUserList.update(user);
                    }
                }
            }
        });

    }

    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context, color);

    }


    public void emptyStateTextColor(@ColorInt int color) {
        if (cometchatUserList != null && color != 0)
            cometchatUserList.emptyStateTextColor(color);
    }

    public void emptyStateTextAppearance(int appearance) {
        if (cometchatUserList != null && appearance != 0)
            cometchatUserList.emptyStateTextAppearance(appearance);
    }

    public void emptyStateTextFont(String font) {
        if (cometchatUserList != null && font != null)
            cometchatUserList.emptyStateTextFont(font);

    }

    /**
     * This method is used to change the backIcon
     *
     * @param res
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setBackIcon(int res) {
        super.backIcon(res);
    }

    /**
     * This method is exposed for the user to change the search Icon
     *
     * @param res
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setSearchBoxSearchIcon(int res) {
        super.setSearchBoxStartIcon(res);
    }

    /**
     * This method is used to customize the inner components of CometChatUsers
     *
     * @param style
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setStyle(Style style) {
        if (style.getBackground() != 0) {
            super.setBackground(style.getBackground());
        }

        if (style.getBorder() != 0) {
            super.setStrokeWidth(style.getBorder());
        }

        if (style.getCornerRadius() != 0) {
            super.setRadius(style.getCornerRadius());
        }

        if (style.getTitleFont() != null) {
            super.setTitleFont(style.getTitleFont());
        }
        if (style.getTitleColor() != 0) {
            super.setTitleColor(style.getTitleColor());
        }
        if (style.getBackIconTint() != 0) {
            super.backIconTint(style.getBackIconTint());
        }
        if (style.getSearchBorder() != 0) {
            super.setSearchBorderWidth(style.getSearchBorder());
        }
        if (style.getSearchBackground() != 0) {
            super.setSearchBackground(style.getSearchBackground());
        }
        if (style.getSearchTextFont() != null) {
            super.setSearchTextFont(style.getSearchTextFont());
        }
        if (style.getSearchTextColor() != 0) {
            super.setSearchTextColor(style.getSearchTextColor());
        }
        if (style.getSearchIconTint() != 0) {
            super.setStartIconTint(style.getSearchIconTint());
        }

    }


    /**
     * This method is used to set background color of cometchatUserList.
     *
     * @param listBackgroundColor - It is color object i.e R.color.white
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     * @see CometChatUsers#setListBackgroundColor(int)
     */
    public void setListBackgroundColor(@ColorInt int listBackgroundColor) {
        if (cometchatUserList != null) {
            if (listBackgroundColor != 0)
                cometchatUserList.setCardBackgroundColor(listBackgroundColor);
            else {
                cometchatUserList.setCardBackgroundColor(Color.TRANSPARENT);
                cometchatUserList.setCardElevation(0);
                cometchatUserList.setRadius(0);
            }
        }
    }

    public void setSelectedUser(User user) {
        if (cometchatUserList != null && user != null) {
            GroupMember groupMember = new GroupMember(user.getUid(), CometChatConstants.SCOPE_PARTICIPANT);
            if (!groupMembers.contains(groupMember))
                groupMembers.add(groupMember);
            else
                groupMembers.remove(groupMember);
            cometchatUserList.setSelectedUser(user);
        }
        setAddMemberIcon(groupMembers.size() > 0);
        if (groupMembers.size() > 0 && icon != null)
            super.hideMenuIcon(false);

    }

    private void setAddMemberIcon(boolean value) {
        if (value && icon == null) {
            icon = new ImageView(context);
            icon.setImageResource(R.drawable.ic_check_primary);
            icon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            super.addMenuIcon(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddMemberClick != null) {
                        if (groupMembers.size() > 0)
                            onAddMemberClick.onAddMemberClick(groupMembers);
                    }
                }
            });
        } else if (!value) {
            super.hideMenuIcon(true);
        }
    }


    /**
     * Sets configuration.
     *
     * @param configuration the configuration
     */
    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof UsersListConfiguration) {
            cometchatUserList.showHeader(((UsersListConfiguration) configuration).isShowHeader());
            cometchatUserList.setHideError(((UsersListConfiguration) configuration).isHideError());
            cometchatUserList.isFriendsOnly(((UsersListConfiguration) configuration).isFriendOnly());
            cometchatUserList.isHideBlockUsers(((UsersListConfiguration) configuration).isHideBlockedUsers());
            setSearchText(((UsersListConfiguration) configuration).getSearchKeyword());
            cometchatUserList.setSearchKeyword(((UsersListConfiguration) configuration).getSearchKeyword());
            cometchatUserList.setStatus(((UsersListConfiguration) configuration).getStatus());
            cometchatUserList.setLimit(((UsersListConfiguration) configuration).getLimit());
            cometchatUserList.setTags(((UsersListConfiguration) configuration).getTags());
            cometchatUserList.setUidS(((UsersListConfiguration) configuration).getUidS());
            cometchatUserList.setRoles(((UsersListConfiguration) configuration).getRoles());
            cometchatUserList.setEmptyView(((UsersListConfiguration) configuration).getEmptyView());
            cometchatUserList.setErrorView(((UsersListConfiguration) configuration).getErrorView());
            cometchatUserList.errorStateText(((UsersListConfiguration) configuration).getErrorText());
            cometchatUserList.emptyStateText(((UsersListConfiguration) configuration).getEmptyText());
        } else {
            cometchatUserList.setConfiguration(configuration);
        }

    }

    /**
     * Sets configuration.
     *
     * @param configurations the configurations
     */
    public void setConfiguration(List<CometChatConfigurations> configurations) {
        if (configurations != null && !configurations.isEmpty()) {
            for (CometChatConfigurations cometChatConfigurations : configurations) {
                if (cometChatConfigurations instanceof UsersListConfiguration) {
                    setConfiguration(cometChatConfigurations);
                }
            }
        }
        cometchatUserList.setConfiguration(configurations);
    }


    public void onAddMemberClick(OnAddMemberClick addMemberClick) {
        this.onAddMemberClick = addMemberClick;
    }

    public void clearList() {
        if (cometchatUserList != null)
            cometchatUserList.clearList();
    }

    public void setUserList(List<User> users) {
        if (cometchatUserList != null)
            cometchatUserList.setUserList(users);
    }

    public interface OnAddMemberClick {
        void onAddMemberClick(List<GroupMember> groupMembers);
    }
}
