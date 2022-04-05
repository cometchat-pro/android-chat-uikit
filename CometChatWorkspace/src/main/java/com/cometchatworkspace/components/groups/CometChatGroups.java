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
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.Group;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatGroups.CometChatGroupList;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;

import java.util.HashMap;
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
    private static final HashMap<String, Events> events = new HashMap<>();

    private Context context;
    /**
     * The Corner radius list item.
     */
    float cornerRadiusListItem = 16f;

    private ImageView icon;

    private ProgressDialog progressDialog;

    private Palette palette;
    private Typography typography;
    private RecyclerViewSwipeListener swipeHelper;

    /**
     * The Hide avatar.
     */
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
    hideSubtitleListItem,
    /**
     * The Hide helper text list item.
     */
    hideHelperTextListItem = true,
    /**
     * The Hide time list item.
     */
    hideTimeListItem;
    /**
     * The Title color list item.
     */
    int titleColorListItem = 0,
    /**
     * The Sub title color list item.
     */
    subTitleColorListItem = 0,
    /**
     * The Helper text color list item.
     */
    helperTextColorListItem = 0,
    /**
     * The Time text color list item.
     */
    timeTextColorListItem = 0,
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

    /**
     * Constructor used to initiate a CometChatGroup.
     * @param context
     *
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
     * @param context
     * @param attrs
     *
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
     * @param context
     * @param attrs
     * @param defStyleAttr
     *
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
        String title = a.getString(R.styleable.CometChatGroups_title);

        int titleColor = a.getColor(R.styleable.
                CometChatGroups_titleColor, palette.getAccent());
        boolean hideSearchBox = a.getBoolean(R.styleable.
                CometChatGroups_hideSearch, false);
        float searchBoxRadius = a.getDimension(R.styleable.
                CometChatGroups_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.
                CometChatGroups_searchBackgroundColor, palette.getSearchBackground());
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
        //End of Handling Attributes

        //Below method will set color of StatusBar.
        setStatusBarColor();

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
        super.setBaseBackGroundColor(palette.getBackground());
        super.setSearchTextColor(searchTextColor);
        super.setSearchBorderColor(searchBorderColor);
        super.setSearchBorderWidth(searchBorderWidth);
        super.setSearchPlaceHolderColor(searchTextColor);
        setListBackgroundColor(listBackgroundColor);
        super.setSearchBackground(searchBoxColor);
        super.hideSearch(hideSearchBox);
        super.setSearchCornerRadius(searchBoxRadius);
        super.addListView(view);


        CometChatError.init(getContext());

        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {
                if (state.equals(SearchState.Filter)) {
                    cometChatGroupList.searchGroups(text);
                } else if (state.equals(SearchState.TextChange)) {
                    if (text.length() == 0) {
//                    // if searchEdit is empty then fetch all groups.
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


        // Used to trigger event on click of group item in cometChatGroupList (RecyclerView)
        cometChatGroupList.setItemClickListener(new OnItemClickListener<Group>() {
            @Override
            public void OnItemClick(Group group, int position) {
                if (events != null) {
                    for (Events e : events.values()) {
                        e.OnItemClick(group, position);
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
                    for (Events e : events.values()) {
                        e.onCreateIconClick();
                    }
                }
            });
            setCreateGroupIcon(createGroupIcon);
            setCreateGroupIconTint(palette.getPrimary());
        }
    }

    /**
     * This method is used to change the color of create group icon shown at the top.
     * @param color
     *
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
     * @param iconDrawable
     *
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
     * @param res this method is exposed for the user to change the backIcon as per there preference
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setBackIcon(int res) {
        if (res != 0)
            super.backIcon(res);
    }


    /**
     * This method is used to set the searchBox icon color
     * @param res this method is exposed for the user to change the search Icon as per there preference
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setSearchBoxSearchIcon(int res) {
        if (res != 0)
            super.setSearchBoxStartIcon(res);
    }

    /**
     * This method is used to change the text color of Empty State Text
     * @param color
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void emptyStateTextColor(@ColorInt int color){
        if (cometChatGroupList != null && color!=0)
            cometChatGroupList.emptyTextColor(color);
    }

    /**
     * This method is used to change the text Appearance of Empty State Text
     * @param appearance
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void emptyStateTextAppearance(int appearance){
        if (cometChatGroupList != null && appearance!=0)
            cometChatGroupList.emptyStateTextAppearance(appearance);
    }

    /**
     * This method is used to change the font of Empty State Text.
     * @param font
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void emptyStateTextFont(String font){
        if (cometChatGroupList != null && font!=null)
            cometChatGroupList.emptyStateTextFont(font);

    }
    /**
     * 
     * This method is used to customize the inner component of CometChatGroups.
     *
     * @param style
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    
    //TODO(Add EmptyState Styles)
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
     * Below method is used to handle the events performed by/under CometChatGroups.
     *
     * @param TAG             the tag
     * @param onEventListener An object of <code>OnItemClickListener&lt;T&gt;</code> abstract class helps to initialize with events
     *                        to perform onItemClick &amp; onItemLongClick.
     * @see CometChatGroups.Events
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public static void addListener(String TAG, Events<Group> onEventListener) {
        events.put(TAG, onEventListener);
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


    public void setProperties(CometChatListItemProperty cometChatListItemProperty) {
        hideAvatar = cometChatListItemProperty.isAvatarHidden;
        hideTitleListItem = cometChatListItemProperty.isTitleHidden;
        hideSubtitleListItem = cometChatListItemProperty.isSubtitleHidden;
        titleColorListItem = cometChatListItemProperty.titleColor;
        subTitleColorListItem = cometChatListItemProperty.subTitleColor;
        backgroundColorListItem = cometChatListItemProperty.backgroundColor;
        backgroundColorListItemPosition = cometChatListItemProperty.backgroundColorAtPosition;
        cornerRadiusListItem = cometChatListItemProperty.cornerRadius;
        if (cometChatGroupList != null)
            cometChatGroupList.setGroupListItemProperty(hideAvatar,
                    hideTitleListItem, titleColorListItem,
                    hideSubtitleListItem, subTitleColorListItem,
                    backgroundColorListItem, backgroundColorListItemPosition,
                    cornerRadiusListItem);

    }

    /**
     * This method is used to set the configuration in CometChatGroups.
     *
     * @param configuration the configuration
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setConfiguration(CometChatConfigurations configuration) {
        cometChatGroupList.setConfiguration(configuration);
    }

    /**
     * This method is used to set the multiple configurations in CometChatGroups
     *
     * @param configurations the configurations
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public void setConfiguration(List<CometChatConfigurations> configurations) {
        cometChatGroupList.setConfiguration(configurations);
    }

    public static class CometChatListItemProperty {
        private boolean isAvatarHidden;
        private boolean isUserPresenceHidden;
        private boolean isTitleHidden;
        private boolean isSubtitleHidden;
        private boolean isTimeHidden;
        private boolean isHelperTextHidden = true;
        private int backgroundColor;
        private int backgroundColorAtPosition;
        private float cornerRadius;
        private int titleColor;
        private int subTitleColor;
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
         * Sets helper text hidden.
         *
         * @param isHidden the is hidden
         * @return the helper text hidden
         */
        public CometChatListItemProperty setHelperTextHidden(boolean isHidden) {
            isHelperTextHidden = isHidden;
            return this;
        }

        /**
         * Sets message time hidden.
         *
         * @param isHidden the is hidden
         * @return the message time hidden
         */
        public CometChatListItemProperty setMessageTimeHidden(boolean isHidden) {
            isTimeHidden = isHidden;
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

    }

    /**
     * Below is abstract class that includes events performed for CometChatGroups
     *
     * @param <T> the type parameter
     *
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     */
    public abstract static class Events<T> {

        /**
         * It is triggered whenever any item from CometChatGroupList is clicked
         *
         * @param var      the var
         * @param position the position
         *
         * @author CometChat Team
         * Copyright &copy; 2021 CometChat Inc.
         */
        public abstract void OnItemClick(T var, int position);


        /**
         * It is triggered whenever a createGroup icon on the topBar is clicked.
         *
         * @author CometChat Team
         * Copyright &copy; 2021 CometChat Inc.
         */
        public void onCreateIconClick() {
        }

        /**
         * It is triggered whenever a long press action is performed
         * on the item from CometChatGroupList
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
