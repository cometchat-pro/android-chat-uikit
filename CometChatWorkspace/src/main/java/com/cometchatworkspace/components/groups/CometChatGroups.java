package com.cometchatworkspace.components.groups;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.createGroup.CometChatCreateGroup;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.GroupsListConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroups.CometChatGroupList;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;

import java.util.List;

/**
 * Purpose - CometChatGroups class is a component used to display list of groups
 * and perform certain action on click of item. It also provide search bar to
 * perform search operation on the list of groups.
 * User can search by groupname, guid.
 *
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 29th March 2022
 *
 * @author CometChat Team
 * Copyright &copy; 2021 CometChat Inc.
 */
public class CometChatGroups extends CometChatListBase {
    public CometChatGroupList cometChatGroupList;

    private FontUtils fontUtils;
    private View view;

    private Context context;
    /**
     * The Corner radius list item.
     */
    private final User loggedInUser = CometChat.getLoggedInUser();

    private ImageView icon;

    private ProgressDialog progressDialog;

    private Palette palette;
    private Typography typography;
    private RecyclerViewSwipeListener swipeHelper;


    /**
     * Constructor used to initiate a CometChatGroup.
     *
     * @param context
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatGroups(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context, null, -1);
    }

    /**
     * Constructor used to initiate a CometChatGroup.
     *
     * @param context
     * @param attrs
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatGroups(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs, -1);
    }

    /**
     * Constructor used to initiate a CometChatGroup.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public CometChatGroups(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs, defStyleAttr);
    }

    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {
        // Inflate the layout for this fragment
        fontUtils = FontUtils.getInstance(context);
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        view = View.inflate(context, R.layout.cometchat_groups, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CometChatGroups,
                0, 0);

        //Start of Handling Attributes
        String title = a.getString(R.styleable.CometChatGroups_title) != null ? a.getString(R.styleable.CometChatGroups_title) : getResources().getString(R.string.groups);

        int titleColor = a.getColor(R.styleable.
                CometChatGroups_titleColor, palette.getAccent());
        boolean hideSearchBox = a.getBoolean(R.styleable.
                CometChatGroups_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.
                CometChatGroups_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.
                CometChatGroups_searchBackgroundColor, palette.getAccent50());
        Drawable createGroupIcon = a.getDrawable(R.styleable.CometChatGroups_createGroupIcon);
        int searchTextColor = a.getColor(R.styleable.
                CometChatGroups_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.CometChatGroups_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.CometChatGroups_searchBorderColor, 0);

        boolean showBackButton = a.getBoolean(R.styleable.CometChatGroups_showBackButton, false);

        boolean hideCreateGroup = a.getBoolean(R.styleable.
                CometChatGroups_hideCreateGroup, true);
        Drawable backButtonIcon = a.getDrawable(R.styleable.CometChatGroups_backButtonIcon);
        int listBackgroundColor = a.getColor(R.styleable.
                CometChatGroups_listBackgroundColor, getResources().getColor(android.R.color.transparent));
        String searchPlaceholder = a.getString(R.styleable.CometChatGroups_searchPlaceholder);
        int backgroundColor = a.getColor(R.styleable.CometChatGroups_backgroundColor, palette.getBackground());

        //End of Handling Attributes

        //Below method will set color of StatusBar.
        setStatusColor(palette.getBackground());

        cometChatGroupList = view.findViewById(R.id.cometchat_group_list);
        super.showBackButton(showBackButton);
        super.backIcon(backButtonIcon);
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
        if (palette.getGradientBackground() != null) {
            setBackground(palette.getGradientBackground());
        } else {
            setBackgroundColor(backgroundColor);
        }
        super.addListView(view);


        CometChatError.init(getContext());

        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {
                if (state.equals(SearchState.Filter)) {
                    cometChatGroupList.searchGroups(text);
                    cometChatGroupList.setSearchKeyWord(text);
                } else if (state.equals(SearchState.TextChange)) {
                    if (text.length() == 0) {
//                    // if searchEdit is empty then fetch all groups.
                        cometChatGroupList.setSearchKeyWord(text);
                        cometChatGroupList.clearList();
                    } else {
//                    // Search groups based on text in searchEdit field.
                        cometChatGroupList.searchGroups(text);
                    }
                }
            }

            @Override
            public void onBack() {
                ((Activity) context).onBackPressed();
            }
        });

        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @SuppressLint("ResourceType")
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                Bitmap callBitmap = Utils.drawableToBitmap(getResources().getDrawable(R.drawable.ic_audiocall));
                underlayButtons.add(new UnderlayButton(
                        "Delete",
                        callBitmap,
                        getResources().getColor(Color.WHITE),
                        new UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                Group group = cometChatGroupList.getGroup(pos);
                            }
                        }
                ));
            }


        };

        if (!hideCreateGroup) {
            icon = new ImageView(context);
            icon.setImageResource(R.drawable.ic_create);
            icon.setTag(R.string.create_group, icon);
            super.addMenuIcon(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CometChatGroupEvents e : CometChatGroupEvents.groupEvents.values()) {
                        e.onCreateGroupIconClick();
                    }
                    CometChatGroupActivity.launch(context, CometChatCreateGroup.class);
                }
            });
            setCreateGroupIcon(createGroupIcon);
            setCreateGroupIconTint(palette.getPrimary());
        }

        CometChatGroupEvents.addGroupListener("COMET_CHAT_GROUPS", new CometChatGroupEvents() {
            @Override
            public void onItemClick(Group group, int position) {

            }

            @Override
            public void onGroupCreate(Group group) {
                if (cometChatGroupList != null && group != null)
                    cometChatGroupList.add(group);
            }

            @Override
            public void onError(CometChatException error) {

            }

            @Override
            public void onGroupMemberLeave(User leftUser, Group leftGroup) {
                if (cometChatGroupList != null && leftUser.equals(loggedInUser)) {
                    if (cometChatGroupList.isJoinedOnly()) {
                        cometChatGroupList.removeGroup(leftGroup);
                    } else {
                        cometChatGroupList.updateGroup(leftGroup);
                    }
                }
            }

            @Override
            public void onGroupMemberChangeScope(User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {

            }

            @Override
            public void onGroupMemberBan(User bannedUser, User bannedBy, Group bannedFrom) {
                if (cometChatGroupList != null && bannedFrom != null) {
                    cometChatGroupList.updateGroup(bannedFrom);
                }
            }

            @Override
            public void onGroupMemberAdd(User addedBy, List<User> usersAdded, Group group) {
                if (cometChatGroupList != null && group != null) {
                    cometChatGroupList.updateGroup(group);
                }
            }

            @Override
            public void onGroupMemberKick(User kickedUser, User kickedBy, Group kickedFrom) {
                if (cometChatGroupList != null && kickedFrom != null) {
                    cometChatGroupList.updateGroup(kickedFrom);
                }
            }

            @Override
            public void onGroupMemberUnban(User unbannedUser, User unbannedBy, Group unBannedFrom) {

            }

            @Override
            public void onGroupMemberJoin(User joinedUser, Group joinedGroup) {
                if (cometChatGroupList != null && joinedGroup != null) {
                    cometChatGroupList.updateGroup(joinedGroup);
                }
            }

            @Override
            public void onOwnershipChange(Group group, GroupMember member) {

            }

            @Override
            public void onGroupDelete(Group group) {
                if (cometChatGroupList != null) {
                    cometChatGroupList.removeGroup(group);
                }
            }
        });


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

    /**
     * This method is used to set the create group icon.
     *
     * @param iconDrawable
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setCreateGroupIcon(Drawable iconDrawable) {
        if (iconDrawable != null) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon != null)
                icon.setImageDrawable(iconDrawable);

        }
    }

    /**
     * This method is used to set the icon for back button.
     *
     * @param res this method is exposed for the user to change the backIcon as per there preference
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setBackIcon(int res) {
        if (res != 0)
            super.backIcon(res);
    }


    /**
     * This method is used to set the searchBox icon color
     *
     * @param res this method is exposed for the user to change the search Icon as per there preference
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setSearchBoxSearchIcon(int res) {
        if (res != 0)
            super.setSearchBoxStartIcon(res);
    }

    /**
     * This method is used to change the text color of Empty State Text
     *
     * @param color
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void emptyStateTextColor(@ColorInt int color) {
        if (cometChatGroupList != null && color != 0)
            cometChatGroupList.emptyTextColor(color);
    }

    /**
     * This method is used to change the text Appearance of Empty State Text
     *
     * @param appearance
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void emptyStateTextAppearance(int appearance) {
        if (cometChatGroupList != null && appearance != 0)
            cometChatGroupList.emptyStateTextAppearance(appearance);
    }

    /**
     * This method is used to change the font of Empty State Text.
     *
     * @param font
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void emptyStateTextFont(String font) {
        if (cometChatGroupList != null && font != null)
            cometChatGroupList.emptyStateTextFont(font);

    }

    /**
     * This method is used to customize the inner component of CometChatGroups.
     *
     * @param style
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */

    //TODO(Add EmptyState Styles)
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
     * This method is used to set Color of StatusBar
     *
     * @author CometChat Team
     * Copyright &copy;  2021 CometChat Inc.
     */

    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context, color);

    }

    /**
     * This method is used to set background color of CometChatGroupList.
     *
     * @param listBackgroundColor - It is color object i.e R.color.white
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     * @see CometChatGroups#setListBackgroundColor(int)
     */
    public void setListBackgroundColor(@ColorInt int listBackgroundColor) {
        if (cometChatGroupList != null) {
            if (listBackgroundColor != 0)
                cometChatGroupList.setCardBackgroundColor(listBackgroundColor);
            else {
                cometChatGroupList.setCardBackgroundColor(Color.TRANSPARENT);
                cometChatGroupList.setCardElevation(0);
                cometChatGroupList.setRadius(0);
            }
        }
    }


    /**
     * This method is used to set the configuration in CometChatGroups.
     *
     * @param configuration the configuration
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof GroupsListConfiguration) {
            setSearchText(((GroupsListConfiguration) configuration).getSearchKeyWord());
            cometChatGroupList.setHideError(((GroupsListConfiguration) configuration).isHideError());
            cometChatGroupList.setJoinedOnly(((GroupsListConfiguration) configuration).isJoinedOnly());
            cometChatGroupList.setSearchKeyWord(((GroupsListConfiguration) configuration).getSearchKeyWord());
            cometChatGroupList.setTags(((GroupsListConfiguration) configuration).getTags());
            cometChatGroupList.setEmptyView(((GroupsListConfiguration) configuration).getEmptyView());
            cometChatGroupList.setLimit(((GroupsListConfiguration) configuration).getLimit());
            cometChatGroupList.setErrorView(((GroupsListConfiguration) configuration).getErrorView());
            cometChatGroupList.errorStateText(((GroupsListConfiguration) configuration).getErrorText());
            cometChatGroupList.emptyStateText(((GroupsListConfiguration) configuration).getEmptyText());
        } else {
            cometChatGroupList.setConfiguration(configuration);
        }
    }

    /**
     * This method is used to set the multiple configurations in CometChatGroups
     *
     * @param configurations the configurations
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setConfiguration(List<CometChatConfigurations> configurations) {
        if (configurations != null && !configurations.isEmpty()) {
            for (CometChatConfigurations cometChatConfigurations : configurations) {
                if (cometChatConfigurations instanceof GroupsListConfiguration) {
                    setConfiguration(cometChatConfigurations);
                }
            }
        }
        cometChatGroupList.setConfiguration(configurations);
    }

}
