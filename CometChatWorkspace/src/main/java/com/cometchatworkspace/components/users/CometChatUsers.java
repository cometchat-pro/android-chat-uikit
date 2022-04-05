package com.cometchatworkspace.components.users;


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
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatUsers.CometChatUserList;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;

import java.util.HashMap;
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

    private static final HashMap<String, Events> events = new HashMap<>();
    /**Used to handle events given by CometChatUsers.
     * Refer https://cometchat.com/docs/android-chat-uikit/CometChatUsers#Events
     */
    private static final String TAG = "UserList";

    private View view;

    private Context context;

    private FontUtils fontUtils;

    /**
     * The Hide avatar.
     */
    //CometChatUserListItem Properties
    boolean hideAvatar,
    /**
     * The Hide user presence list item.
     */
    hideUserPresenceListItem,
    /**
     * The Hide title list item.
     */
    hideTitleListItem,
    /**
     * The Hide subtitle list item.
     */
    hideSubtitleListItem;

    /**
     * The Title color list item.
     */
    int titleColorListItem = 0,
    /**
     * The Sub title color list item.
     */
    subTitleColorListItem = 0,

    /**
     * The Background color list item.
     */
    backgroundColorListItem = 0,
    /**
     * The Typing indicator color list item.
     */
    typingIndicatorColorListItem,
    /**
     * The Background color list item position.
     */
    backgroundColorListItemPosition;
    /**
     * The Corner radius list item.
     */
    float cornerRadiusListItem = 16f;

    private ImageView icon;

    private ProgressDialog progressDialog;

    private RecyclerViewSwipeListener swipeHelper;

    private Palette palette;
    private Typography typography;

    /**
     * Constructor used to initiate a CometChatUsers.
     *
     * @param context
     *
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
     *
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
     *
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
     *
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
        String title = a.getString(R.styleable.Users_title);

        int titleColor = a.getColor(R.styleable.
                Users_titleColor, palette.getAccent());

        Drawable backButtonIcon = a.getDrawable(R.styleable.Users_backButtonIcon);

        boolean showBackButton = a.getBoolean(
                R.styleable.Users_showBackButton, false);
        boolean hideTags = a.getBoolean(R.styleable.Users_hideTags, false);
        int filterIconColor = a.getColor(R.styleable.
                Users_filterIconColor, 0);
        Drawable filterIcon = a.getDrawable(R.styleable.Users_filterIcon);
        boolean searchBoxVisible = a.getBoolean(R.styleable.
                Users_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.
                Users_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.
                Users_searchBackgroundColor, palette.getSearchBackground());
        int searchTextColor = a.getColor(R.styleable.
                Users_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.Users_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.Users_searchBorderColor, 0);

        int listBackgroundColor = a.getColor(R.styleable.
                Users_listBackgroundColor, getResources().getColor(android.R.color.transparent));
        String searchHint = a.getString(R.styleable.
                Users_searchHint);

        //End of Handling Attributes

        //Below method will set color of StatusBar.
        setStatusBarColor();

        cometchatUserList = view.findViewById(R.id.cometchat_user_list);

        /**
         * setList as per Uid List
         */
        super.addSearchViewPlaceHolder(searchHint);
        super.setTitle(title);
        super.backIcon(backButtonIcon);
        super.showBackButton(showBackButton);
        super.setSearchTextAppearance(typography.getText1());
        super.setTitleAppearance(typography.getHeading());
        super.setBaseBackGroundColor(palette.getBackground());
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
        super.addListView(view);

        CometChatError.init(getContext());
        if (!hideTags) {
            icon = new ImageView(context);
            icon.setImageResource(R.drawable.ic_list_bulleted_white_24dp);
            icon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            super.addMenuIcon(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Events e : events.values()) {
                        e.onMenuIconClick("tags");
                    }
                }
            });
        }


        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {
                if (state.equals(SearchState.Filter)) {
                    cometchatUserList.searchUser(text);
                } else if (state.equals(SearchState.TextChange)) {
                    if (text.length() == 0) {
//                    // if searchEdit is empty then fetch all users.
                        cometchatUserList.clearList();
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


        // Used to trigger event on click of Users item in cometchatUserList (RecyclerView)
        cometchatUserList.setItemClickListener(new OnItemClickListener<User>() {
            @Override
            public void OnItemClick(User user, int position) {
                if (events != null) {
                    for (Events e : events.values()) {
                        e.OnItemClick(user, position);
                    }
                }
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
                                User user = cometchatUserList.getUser(pos);
                            }
                        }
                ));
            }

        };

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
     *
     * This method is used to change the backIcon
     * @param res
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setBackIcon(int res) {
        super.backIcon(res);
    }

    /**
     * This method is exposed for the user to change the search Icon
     * @param res
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setSearchBoxSearchIcon(int res) {
        super.setSearchBoxStartIcon(res);
    }

    /**
     *
     * This method is used to customize the inner components of CometChatUsers
     * @param style
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setStyle(Style style) {
        if (style.getBackground() != 0) {
            super.setBaseBackGroundColor(style.getBackground());
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
    private void setStatusBarColor() {
        int backgroundColor = palette.getPrimary();
        if (backgroundColor != 0)
            ((Activity) context).getWindow().setStatusBarColor(backgroundColor);
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




    /**
     * Add listener.
     *
     * @param TAG             the tag
     * @param onEventListener An object of <code>OnItemClickListener&lt;T&gt;</code> abstract class helps to initialize with events
     *                        to perform onItemClick &amp; onItemLongClick.
     * @see Events
     */
    public static void addListener(String TAG, Events<User> onEventListener) {
        events.put(TAG, onEventListener);
    }


    /**
     * Sets properties.
     *
     * @param cometChatListItemProperty the comet chat list item property
     */
    public void setProperties(CometChatListItemProperty cometChatListItemProperty) {
        hideAvatar = cometChatListItemProperty.isAvatarHidden;
        hideUserPresenceListItem = cometChatListItemProperty.isUserPresenceHidden;
        hideTitleListItem = cometChatListItemProperty.isTitleHidden;
        hideSubtitleListItem = cometChatListItemProperty.isSubtitleHidden;

        titleColorListItem = cometChatListItemProperty.titleColor;
        subTitleColorListItem = cometChatListItemProperty.subTitleColor;

        backgroundColorListItem = cometChatListItemProperty.backgroundColor;
        backgroundColorListItemPosition = cometChatListItemProperty.backgroundColorAtPosition;
        cornerRadiusListItem = cometChatListItemProperty.cornerRadius;
        if (cometchatUserList != null)
            cometchatUserList.setUserListItemProperty(hideAvatar, hideUserPresenceListItem,
                    hideTitleListItem, titleColorListItem,
                    hideSubtitleListItem, subTitleColorListItem,
                    backgroundColorListItem, backgroundColorListItemPosition,
                    cornerRadiusListItem);

    }

    /**
     * Sets configuration.
     *
     * @param configuration the configuration
     */
    public void setConfiguration(CometChatConfigurations configuration) {
        cometchatUserList.setConfiguration(configuration);
    }

    /**
     * Sets configuration.
     *
     * @param configurations the configurations
     */
    public void setConfiguration(List<CometChatConfigurations> configurations) {
        cometchatUserList.setConfiguration(configurations);
    }

    /**
     * The type Comet chat list item property.
     */
    public static class CometChatListItemProperty {
        private boolean isAvatarHidden;
        private boolean isUserPresenceHidden;
        private boolean isTitleHidden;
        private boolean isSubtitleHidden;

        private int backgroundColor;
        private int backgroundColorAtPosition;
        private float cornerRadius;
        private int titleColor;
        private int subTitleColor;

        private int typingIndicatorColor;
        private final Context context;

        /**
         * Instantiates a new Comet chat list item property.
         *
         * @param context the context
         */
        public CometChatListItemProperty(Context context) {
            this.context = context;
        }

        /**
         * Sets avatar hidden.
         *
         * @param avatarHidden the avatar hidden
         * @return the avatar hidden
         */
        public CometChatListItemProperty setAvatarHidden(boolean avatarHidden) {
            isAvatarHidden = avatarHidden;
            return this;
        }

        /**
         * Sets title hidden.
         *
         * @param titleHidden the title hidden
         * @return the title hidden
         */
        public CometChatListItemProperty setTitleHidden(boolean titleHidden) {
            isTitleHidden = titleHidden;
            return this;
        }

        /**
         * Sets subtitle hidden.
         *
         * @param subtitleHidden the subtitle hidden
         * @return the subtitle hidden
         */
        public CometChatListItemProperty setSubtitleHidden(boolean subtitleHidden) {
            isSubtitleHidden = subtitleHidden;
            return this;
        }

        /**
         * Sets user presence hidden.
         *
         * @param isHidden the is hidden
         * @return the user presence hidden
         */
        public CometChatListItemProperty setUserPresenceHidden(boolean isHidden) {
            isUserPresenceHidden = isHidden;
            return this;
        }


        /**
         * Sets title color.
         *
         * @param color the color
         * @return the title color
         */
        public CometChatListItemProperty setTitleColor(@ColorInt int color) {
            titleColor = color;
            return this;
        }

        /**
         * Sets sub title color.
         *
         * @param color the color
         * @return the sub title color
         */
        public CometChatListItemProperty setSubTitleColor(@ColorInt int color) {
            subTitleColor = color;
            return this;
        }

        /**
         * Sets corner radius.
         *
         * @param radius the radius
         * @return the corner radius
         */
        public CometChatListItemProperty setCornerRadius(float radius) {
            cornerRadius = radius;
            return this;
        }


        /**
         * Sets background color.
         *
         * @param color    the color
         * @param position the position
         * @return the background color
         */
        public CometChatListItemProperty setBackgroundColor(@ColorInt int color, int position) {
            backgroundColor = color;
            backgroundColorAtPosition = position;
            return this;
        }

        /**
         * Sets typing indicator color.
         *
         * @param color the color
         * @return the typing indicator color
         */
        public CometChatListItemProperty setTypingIndicatorColor(@ColorInt int color) {
            typingIndicatorColor = color;
            return this;
        }
    }

    /**
     * Below is abstract class that includes events performed for CometChatUsers
     * @param <T> the type parameter
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     *
     */
    public abstract static class Events<T> {

        /**
         * It is triggered whenever any item from CometChatUserList is clicked
         *
         * @param var      the var
         * @param position the position
         *
         * @author CometChat Team
         * Copyright &copy; 2021 CometChat Inc.
         */
        public abstract void OnItemClick(T var, int position);


        public void onMenuIconClick(String id) {
        }

        /**
         * It is triggered whenever a long press action is performed
         * on the item from CometChatUserList
         *
         * @param var      the var
         * @param position the position
         *
         * @author CometChat Team
         * Copyright &copy; 2021 CometChat Inc.
         */
        public void OnItemLongClick(T var, int position) {
        }
    }
}
