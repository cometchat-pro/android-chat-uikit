package com.cometchatworkspace.components.chats;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ConversationListConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList.CometChatConversationList;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.components.users.CometChatUserEvents;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.custom_alertDialog.CustomAlertDialogHelper;
import com.cometchatworkspace.resources.utils.custom_alertDialog.OnAlertDialogButtonClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerViewSwipeListener;

import java.util.HashMap;
import java.util.List;

/**
 * Purpose - CometChatConversationList class is a fragment used to display list of conversations and perform certain action on click of item.
 * It also provide search bar to perform search operation on the list of conversations.User can search by username, groupname, last message of conversation.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 4th February 2022
 *
 * @author CometChat Team
 * Copyright &copy; 2021 CometChat Inc.
 */
public class CometChatConversations extends CometChatListBase {

    public CometChatConversationList cometchatConversationList;    //Uses to display list of conversations.

    private String conversationListType; // Used to set Conversation Type i.e Group or User

    private static final HashMap<String, CometChatConversationEvents> events = CometChatConversationEvents.conversationEvents; //Used to handle events given by CometChatConversations. Refer https://cometchat.com/docs/android-chat-uikit/CometChatConversations#Events

    private static final String TAG = "ConversationList";

    private View view;

    private Context context;

    private FontUtils fontUtils;

    private Palette palette;

    private Typography typography;
    private final User loggedInUser = CometChat.getLoggedInUser();

    private String errorFont = null;
    private int errorColor = 0;
    private boolean hideStartConversation, hideDeleteConversation = false;
    private Drawable startConversationIcon;
    private int startConversationIconColor;
    private ImageView icon;

    private ProgressDialog progressDialog;

    private RecyclerViewSwipeListener swipeHelper;

    /**
     * Instantiates a new Comet chat conversations.
     *
     * @param context the context
     */
    public CometChatConversations(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context, null, -1);
    }

    /**
     * Instantiates a new Comet chat conversations.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CometChatConversations(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs, -1);
    }

    /**
     * Instantiates a new Comet chat conversations.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CometChatConversations(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * This method is used to initialize the component available in CometChatConversations
     *
     * @param context      - It is reference object of Context
     * @param attributeSet - It is object of AttributeSet which is used to handle attributes passed from xml
     * @param defStyleAttr
     * @author CometChat Team
     * Copyright &copy;  2021 CometChat Inc.
     */
    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {
        // Inflate the layout for this fragment
        fontUtils = FontUtils.getInstance(context);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        this.context = context;
        view = View.inflate(context, R.layout.cometchat_conversations, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.Conversations,
                0, 0);

        //Start of Handling Attributes
        conversationListType = a.getString(R.styleable.Conversations_conversationType);
        String title = a.getString(R.styleable.Conversations_title);

        int titleColor = a.getColor(R.styleable.
                Conversations_titleColor, palette.getAccent());

        boolean hideStartConversation = a.getBoolean(
                R.styleable.Conversations_hideStartConversation, true);
        startConversationIconColor = a.getColor(R.styleable.
                Conversations_startConversationIconColor, palette.getPrimary());
        startConversationIcon = a.getDrawable(R.styleable.Conversations_startConversationIcon);
        boolean hideSearch = a.getBoolean(R.styleable.
                Conversations_hideSearch, true);
        float searchBoxRadius = a.getDimension(R.styleable.
                Conversations_searchCornerRadius, 0f);
        int searchBoxColor = a.getColor(R.styleable.
                Conversations_searchBackgroundColor, palette.getAccent50());
        int searchTextColor = a.getColor(R.styleable.
                Conversations_searchTextColor, palette.getAccent600());
        int searchBorderWidth = (int) a.getDimension(R.styleable.Conversations_searchBorderWidth, 0f);
        int searchBorderColor = a.getColor(R.styleable.Conversations_searchBorderColor, 0);
        boolean showBackButton = a.getBoolean(R.styleable.Conversations_showBackButton, false);
        int listBackgroundColor = a.getColor(R.styleable.
                Conversations_listBackgroundColor, getResources().getColor(android.R.color.transparent));
        String searchPlaceHolder = a.getString(R.styleable.
                Conversations_searchHint);

        Drawable backButtonIcon = a.getDrawable(R.styleable.Conversations_backButtonIcon);
        int backgroundColor = a.getColor(R.styleable.Conversations_backgroundColor, palette.getBackground());
        //End of Handling Attributes

        //Below method will set color of StatusBar.

        setStatusColor(palette.getBackground());

        cometchatConversationList = view.findViewById(R.id.cometchat_conversation_list);

        super.showBackButton(showBackButton);
        super.setTitle(title);
        super.backIcon(backButtonIcon);
        setConversationType(conversationListType);
        super.setSearchTextAppearance(typography.getText1());
        super.setTitleAppearance(typography.getHeading());
        emptyStateTextAppearance(typography.getHeading());
        emptyStateTextColor(palette.getAccent400());
        super.addSearchViewPlaceHolder(searchPlaceHolder);
        super.setTitleColor(titleColor);
        super.setSearchTextColor(searchTextColor);
        super.setSearchBorderColor(searchBorderColor);
        super.setSearchBorderWidth(searchBorderWidth);
        super.setSearchPlaceHolderColor(searchTextColor);
        setListBackgroundColor(listBackgroundColor);
        super.setSearchBackground(searchBoxColor);
        super.hideSearch(hideSearch);
        super.setSearchCornerRadius(searchBoxRadius);
        if (palette.getGradientBackground() != null) {
            setBackground(palette.getGradientBackground());
        } else {
            setBackgroundColor(backgroundColor);
        }
        super.addListView(view);
//        hideStartConversation(hideStartConversation);
        CometChatError.init(getContext());


        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {
                if (state.equals(SearchState.Filter)) {
                    progressDialog = ProgressDialog.show(getContext(), "", context.getString(R.string.search));
                    cometchatConversationList.refreshConversation(new CometChat.CallbackListener<List<Conversation>>() {
                        @Override
                        public void onSuccess(List<Conversation> conversationList) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            cometchatConversationList.searchConversation(text);
                        }

                        @Override
                        public void onError(CometChatException e) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            CometChatSnackBar.show(getContext(), cometchatConversationList,
                                    CometChatError.localized(e), CometChatSnackBar.ERROR);
                        }
                    });
                } else if (state.equals(SearchState.TextChange)) {
                    if (text.length() == 0) {
//                    // if searchEdit is empty then fetch all conversations.
                        cometchatConversationList.refreshList();
                    } else {
//                    // Search conversation based on text in searchEdit field.
                        cometchatConversationList.searchConversation(text);
                    }
                }
            }

            @Override
            public void onBack() {
                ((Activity) context).onBackPressed();
            }
        });

        swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                if (!hideDeleteConversation) {
                    Bitmap deleteBitmap = Utils.drawableToBitmap(getResources().getDrawable(R.drawable.ic_delete_conversation));
                    underlayButtons.add(new UnderlayButton(
                            "Delete",
                            deleteBitmap,
                            getResources().getColor(R.color.red),
                            new UnderlayButtonClickListener() {
                                @Override
                                public void onClick(final int pos) {
                                    Conversation conversation = cometchatConversationList.getConversation(pos);
                                    handleDeleteConversation(conversation);
                                }
                            }
                    ));
                }
            }
        };
        swipeHelper.attachToRecyclerView(cometchatConversationList.getRecyclerView());
        hideDeleteConversation(hideDeleteConversation);

        CometChatGroupEvents.addGroupListener("conversations", new CometChatGroupEvents() {
            @Override
            public void onItemClick(Group group, int position) {

            }

            @Override
            public void onGroupCreate(Group group) {

            }

            @Override
            public void onError(CometChatException error) {

            }

            @Override
            public void onGroupMemberLeave(User leftUser, Group leftGroup) {
                if (leftUser.equals(loggedInUser) && cometchatConversationList != null)
                    cometchatConversationList.removeGroupConversation(leftGroup);
            }

            @Override
            public void onGroupMemberChangeScope(User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {

            }

            @Override
            public void onGroupMemberBan(User bannedUser, User bannedBy, Group bannedFrom) {
                if (cometchatConversationList != null && bannedFrom != null) {
                    cometchatConversationList.updateGroupConversation(bannedFrom);
                }
            }

            @Override
            public void onGroupMemberAdd(User addedBy, List<User> usersAdded, Group group) {
                if (cometchatConversationList != null && group != null) {
                    cometchatConversationList.updateGroupConversation(group);
                }
            }

            @Override
            public void onGroupMemberKick(User kickedUser, User kickedBy, Group kickedFrom) {
                if (cometchatConversationList != null && kickedFrom != null) {
                    cometchatConversationList.updateGroupConversation(kickedFrom);
                }

            }

            @Override
            public void onGroupMemberUnban(User unbannedUser, User unbannedBy, Group unBannedFrom) {

            }

            @Override
            public void onGroupMemberJoin(User joinedUser, Group joinedGroup) {
                if (cometchatConversationList != null && joinedGroup != null) {
                    cometchatConversationList.updateGroupConversation(joinedGroup);
                }
            }

            @Override
            public void onOwnershipChange(Group group, GroupMember member) {

            }

            @Override
            public void onGroupDelete(Group group) {
                if (cometchatConversationList != null && group != null)
                    cometchatConversationList.removeGroupConversation(group);
            }
        });
        CometChatUserEvents.addUserListener("CometChatConversation", new CometChatUserEvents() {
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
                if (cometchatConversationList != null)
                    cometchatConversationList.removeUserConversation(user);
            }

            @Override
            public void onUserUnblock(User user) {

            }
        });
    }

    public void setStatusColor(int color) {
        Utils.setStatusBarColor(context, color);

    }

    public void emptyStateTextFont(String font) {
        if (cometchatConversationList != null && font != null)
            cometchatConversationList.emptyStateTextFont(font);

    }

    public void emptyStateTextColor(@ColorInt int color) {
        if (cometchatConversationList != null && color != 0)
            cometchatConversationList.emptyStateTextColor(color);
    }

    public void emptyStateTextAppearance(int appearance) {
        if (cometchatConversationList != null && appearance != 0)
            cometchatConversationList.emptyStateTextAppearance(appearance);
    }

    /**
     * @param res this method is exposed for the user to change the backIcon as per there preference
     */
    public void setBackIcon(int res) {
        super.backIcon(res);
    }

    /**
     * @param res this method is exposed for the user to change the search Icon as per there preference
     */
    public void setSearchBoxSearchIcon(int res) {
        super.setSearchBoxStartIcon(res);
    }

    /**
     * @param style
     * @method setStyle is use to customize the inner components as per user need
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

    public void hideDeleteConversation(boolean hideDeleteConversation) {
        this.hideDeleteConversation = hideDeleteConversation;
    }

    /**
     * This method is used to display Delete Conversation Confirmation Dialog and based on it.
     * It will perform certain actions. It performs <code>CometChat.deleteConversation()</code>
     * method whenever user click "Yes" and no it dismiss the dialog.
     *
     * @param conversation is a specific Conversation object that will be deleted.
     * @author CometChat Team
     * Copyright &copy;  2021 CometChat Inc.
     * @see com.cometchat.pro.core.CometChat#deleteConversation(String, String, CometChat.CallbackListener)
     * @see com.cometchatworkspace.components.chats.CometChatConversations
     */

    private void handleDeleteConversation(Conversation conversation) {
        if (conversation != null) {
            String conversationUid = "";
            String type = "";
            if (conversation.getConversationType()
                    .equalsIgnoreCase(CometChatConstants.CONVERSATION_TYPE_GROUP)) {
                conversationUid = ((Group) conversation.getConversationWith()).getGuid();
                type = CometChatConstants.CONVERSATION_TYPE_GROUP;
            } else {
                conversationUid = ((User) conversation.getConversationWith()).getUid();
                type = CometChatConstants.CONVERSATION_TYPE_USER;
            }
            String finalConversationUid = conversationUid;
            String finalType = type;
            new CustomAlertDialogHelper(getContext(), errorFont, errorColor,
                    context.getString(R.string.delete_conversation_message),
                    null,
                    context.getString(R.string.yes),
                    "",
                    context.getString(R.string.no),
                    new OnAlertDialogButtonClickListener() {
                        @Override
                        public void onButtonClick(AlertDialog alertDialog,
                                                  View v,
                                                  int which,
                                                  int popupId) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                ProgressDialog progressDialog = ProgressDialog.show(getContext(),
                                        null, context.getString(R.string.deleting_conversation));
                                CometChat.deleteConversation(
                                        finalConversationUid, finalType,
                                        new CometChat.CallbackListener<String>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        alertDialog.dismiss();
                                                        progressDialog.dismiss();
                                                    }
                                                }, 1500);
                                                removeConversation(conversation);
                                            }

                                            @Override
                                            public void onError(CometChatException e) {
                                                throwError(e);
                                                progressDialog.dismiss();
                                                e.printStackTrace();
                                            }
                                        });
                            } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                                alertDialog.dismiss();
                            }
                        }
                    }, 1, true);
            for (CometChatConversationEvents e : events.values()) {
                e.onDeleteConversation(conversation);
            }
        }
    }

    private void throwError(CometChatException e) {
        for (CometChatConversationEvents events : events.values()) {
            events.onError(e);
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
     * This method is used to set background color of CometChatConversationList.
     *
     * @param listBackgroundColor - It is color object i.e R.color.white
     * @author CometChat Team
     * Copyright &copy; 2021 CometChat Inc.
     * @see com.cometchatworkspace.components.chats.CometChatConversations#setListBackgroundColor(int)
     */
    public void setListBackgroundColor(@ColorInt int listBackgroundColor) {
        if (cometchatConversationList != null) {
            if (listBackgroundColor != 0)
                cometchatConversationList.setCardBackgroundColor(listBackgroundColor);
            else {
                cometchatConversationList.setCardBackgroundColor(Color.TRANSPARENT);
                cometchatConversationList.setCardElevation(0);
                cometchatConversationList.setRadius(0);
            }
        }
    }


    /**
     * This method is used to set the type of ConversationList i.e User or Group
     *
     * @param conversationListType -  It is a String object and here we can pass                             <b>CometChatConstant.CONVERSATION_TYPE_USER</b> or                             <b>CometChatConstant.CONVERSATION_TYPE_GROUP</b>
     */
    public void setConversationType(String conversationListType) {
        this.conversationListType = conversationListType;
        if (cometchatConversationList != null)
            cometchatConversationList.setConversationType(conversationListType);
    }

    /**
     * This method is used to remove the particular Conversation from CometChatConversationList
     *
     * @param conversation is Converastion Object which will be removed.
     * @see CometChatConversations#removeConversation(Conversation)
     * @see Conversation
     */
    private void removeConversation(Conversation conversation) {
        cometchatConversationList.remove(conversation);
    }

    /**
     * This method is used to hide/show <b>Start Conversation</b> Icon shown in CometChatConversations.
     *
     * @param b is a boolean object. If b=true show "StartConversation" else hide "StartConversation"
     */
    private void hideStartConversation(boolean b) {
        hideStartConversation = b;
        if (icon == null) {
            icon = new ImageView(context);
            icon.setImageResource(R.drawable.ic_create);
            icon.setTag(R.string.tap_to_start_conversation, icon);
            super.addMenuIcon(icon);
            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CometChatConversationEvents e : events.values()) {
                        e.onStartConversation();
                    }
                }
            });
            startConversationIcon(startConversationIcon);
            startConversationIconTint(startConversationIconColor);
        }
        super.hideMenuIcon(b);
    }

    /**
     * This method is used to hide/show group actions shown as conversation list.
     */
    //TODO(Hide group actions)

    /**
     * This method is used to <b>set the Icon Color of StartConversation</b> option shown in CometChatConversations
     *
     * @param color is Color Object i.e R.color.blue
     */
    public void startConversationIconTint(@ColorInt int color) {
        if (color != 0) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon != null)
                icon.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    /**
     * This method is used to <b>set the Icon of StartConversation</b> option shown in CometChatConversations
     *
     * @param iconDrawable the icon drawable
     */
    public void startConversationIcon(Drawable iconDrawable) {
        if (iconDrawable != null) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon != null)
                icon.setImageDrawable(iconDrawable);
        }
    }


    /**
     * This method is used to <b>set the Icon of StartConversation</b> option shown in CometChatConversations
     *
     * @param iconDrawable the icon drawable
     */
    public void startConversationIcon(@DrawableRes int iconDrawable) {
        if (iconDrawable != 0) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon != null)
                icon.setImageResource(iconDrawable);
        }
    }


    /**
     * Sets configuration.
     *
     * @param configuration the configuration
     */
    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof ConversationListConfiguration) {
            cometchatConversationList.setEmptyView(((ConversationListConfiguration) configuration).getEmptyView());
            cometchatConversationList.setErrorView(((ConversationListConfiguration) configuration).getErrorView());
            cometchatConversationList.errorStateText(((ConversationListConfiguration) configuration).getErrorText());
            cometchatConversationList.emptyStateText(((ConversationListConfiguration) configuration).getEmptyText());
            cometchatConversationList.setHideError(((ConversationListConfiguration) configuration).isHideError());
            cometchatConversationList.withUserAndGroupTags(((ConversationListConfiguration) configuration).isUserAndGroupTags());
            cometchatConversationList.setTags(((ConversationListConfiguration) configuration).getTags());
            cometchatConversationList.setUserTags(((ConversationListConfiguration)configuration).getUserTags());
            cometchatConversationList.setGroupTags(((ConversationListConfiguration)configuration).getGroupTags());
            cometchatConversationList.setConversationType(((ConversationListConfiguration) configuration).getConversationType());
            cometchatConversationList.setLimit(((ConversationListConfiguration) configuration).getLimit());

        } else {
            cometchatConversationList.setConfiguration(configuration);
        }
    }

    /**
     * Sets configuration.
     *
     * @param configurations the configurations
     */
    public void setConfiguration(List<CometChatConfigurations> configurations) {
        for (CometChatConfigurations cometChatConfigurations : configurations) {
            if (cometChatConfigurations instanceof ConversationListConfiguration) {
                setConfiguration(cometChatConfigurations);
            }
        }
        cometchatConversationList.setConfiguration(configurations);
    }


}
