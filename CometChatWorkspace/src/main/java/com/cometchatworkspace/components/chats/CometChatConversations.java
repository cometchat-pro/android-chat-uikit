package com.cometchatworkspace.components.chats;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
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
import com.cometchat.pro.models.User;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ConversationsConfiguration;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList.CometChatConversationList;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatListBase;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.cometchatworkspace.resources.utils.custom_alertDialog.CustomAlertDialogHelper;
import com.cometchatworkspace.resources.utils.custom_alertDialog.OnAlertDialogButtonClickListener;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
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

    private CometChatConversationList cometchatConversationList;    //Uses to display list of conversations.

    private String conversationListType; // Used to set Conversation Type i.e Group or User

    private static final HashMap<String,Events> events = new HashMap<>(); //Used to handle events given by CometChatConversations. Refer https://cometchat.com/docs/android-chat-uikit/CometChatConversations#Events

    private static final String TAG = "ConversationList";

    private View view;

    private Context context;

    private FontUtils fontUtils;

    private boolean hideStartConversation, showDeleteConversation=true;
    /**
     * The Hide avatar.
     */
//CometChatConversationListItem Properties
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
    hideHelperTextListItem=true,
    /**
     * The Hide time list item.
     */
    hideTimeListItem;
    /**
     * The Title color list item.
     */
    int titleColorListItem=0,
    /**
     * The Sub title color list item.
     */
    subTitleColorListItem=0,
    /**
     * The Helper text color list item.
     */
    helperTextColorListItem=0,
    /**
     * The Time text color list item.
     */
    timeTextColorListItem=0,
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

    private  RecyclerViewSwipeListener swipeHelper;

    /**
     * Instantiates a new Comet chat conversations.
     *
     * @param context the context
     */
    public CometChatConversations(Context context) {
        super(context);
        if(!isInEditMode())
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
        if(!isInEditMode())
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
        if(!isInEditMode())
        initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * This method is used to initialize the component available in CometChatConversations
     *
     * @param context - It is reference object of Context
     * @param attributeSet - It is object of AttributeSet which is used to handle attributes passed from xml
     * @param defStyleAttr
     *
     * @author CometChat Team
     * Copyright &copy;  2021 CometChat Inc.
     */
    private void initViewComponent(Context context,AttributeSet attributeSet,int defStyleAttr) {
        // Inflate the layout for this fragment
        fontUtils = FontUtils.getInstance(context);
        this.context = context;
        view = View.inflate(context,R.layout.cometchat_conversations, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.Conversations,
                0, 0);

        //Start of Handling Attributes
        conversationListType = a.getString(R.styleable.Conversations_conversationType);
        String title = a.getString(R.styleable.Conversations_title);

        int titleColor = a.getColor(R.styleable.
                Conversations_titleColor,0);

        boolean startConversationVisible = a.getBoolean(
                R.styleable.Conversations_hideStartConversation,false);
        int startConversationIconColor = a.getColor(R.styleable.
                Conversations_startConversationIconColor,0);
        Drawable startConversationIcon = a.getDrawable(R.styleable.Conversations_startConversationIcon);
        boolean searchBoxVisible = a.getBoolean(R.styleable.
                Conversations_hideSearch,false);
        float searchBoxRadius = a.getDimension(R.styleable.
                Conversations_searchCornerRadius,0f);
        int searchBoxColor = a.getColor(R.styleable.
                Conversations_searchBackgroundColor,0);
        int searchTextColor = a.getColor(R.styleable.
                Conversations_searchTextColor,0);
        int searchBorderWidth = (int) a.getDimension(R.styleable.Conversations_searchBorderWidth,0f);
        int searchBorderColor = a.getColor(R.styleable.Conversations_searchBorderColor,0);

        int listBackgroundColor = a.getColor(R.styleable.
                Conversations_listBackgroundColor,0);

        //End of Handling Attributes

        //Below method will set color of StatusBar.
        setStatusBarColor();
        this.hideStartConversation = !startConversationVisible;


        cometchatConversationList = view.findViewById(R.id.cometchat_conversation_list);

        cometchatConversationList.setConversationType(conversationListType);

        super.setTitle(title);
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

        if (titleColor==0)
            checkDarkMode();

        CometChatError.init(getContext());
        if (!startConversationVisible) {
            icon = new ImageView(context);
            icon.setImageResource(R.drawable.ic_create);
            icon.setTag(R.string.tap_to_start_conversation, icon);
            super.addMenuIcon(icon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Events e : events.values()) {
                        e.onStartConversation();
                    }
                }
            });
            startConversationIcon(startConversationIcon);
            startConversationIconTint(startConversationIconColor);
        }


        super.addEventListener(new OnEventListener() {
            @Override
            public void onSearch(String state, String text) {
                if (state.equals(SearchState.Filter)) {
                    progressDialog = ProgressDialog.show(getContext(),"",context.getString(R.string.search));
                    cometchatConversationList.refreshConversation(new CometChat.CallbackListener<List<Conversation>>() {
                        @Override
                        public void onSuccess(List<Conversation> conversationList) {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            cometchatConversationList.searchConversation(text);
                        }

                        @Override
                        public void onError(CometChatException e) {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            CometChatSnackBar.show(getContext(), cometchatConversationList,
                                    CometChatError.localized(e),CometChatSnackBar.ERROR);
                        }
                    });
                } else if (state.equals(SearchState.TextChange)) {
                    if (text.length() == 0) {
//                    // if searchEdit is empty then fetch all conversations.
                        cometchatConversationList.clearList();
                    } else {
//                    // Search conversation based on text in searchEdit field.
                        cometchatConversationList.searchConversation(text);
                    }
                }
            }

            @Override
            public void onBack() {
                ((Activity)context).onBackPressed();
            }
        });


        // Used to trigger event on click of conversation item in rvConversationList (RecyclerView)
        cometchatConversationList.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation conversation, int position) {
                if (events!=null) {
                    for (Events e : events.values()) {
                        e.OnItemClick(conversation, position);
                    }
                }
            }
        });

       swipeHelper = new RecyclerViewSwipeListener(getContext()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

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

        };
        if (showDeleteConversation) {
            swipeHelper.attachToRecyclerView(cometchatConversationList.getRecyclerView());
        } else {
            swipeHelper.attachToRecyclerView(null);
        }
    }

    /**
     * This method is used to display Delete Conversation Confirmation Dialog and based on it.
     * It will perform certain actions. It performs <code>CometChat.deleteConversation()</code>
     * method whenever user click "Yes" and no it dismiss the dialog.
     *
     * @param conversation is a specific Conversation object that will be deleted.
     *
     * @see com.cometchat.pro.core.CometChat#deleteConversation(String, String, CometChat.CallbackListener)
     * @see com.cometchatworkspace.components.chats.CometChatConversations
     *
     * @author CometChat Team
     * Copyright &copy;  2021 CometChat Inc.
     */
    private void handleDeleteConversation(Conversation conversation) {
        if (conversation!=null) {
            String conversationUid = "";
            String type = "";
            if (conversation.getConversationType()
                    .equalsIgnoreCase(CometChatConstants.CONVERSATION_TYPE_GROUP)) {
                conversationUid = ((Group)conversation.getConversationWith()).getGuid();
                type = CometChatConstants.CONVERSATION_TYPE_GROUP;
            } else {
                conversationUid = ((User)conversation.getConversationWith()).getUid();
                type = CometChatConstants.CONVERSATION_TYPE_USER;
            }
            String finalConversationUid = conversationUid;
            String finalType = type;
            new CustomAlertDialogHelper(getContext(),
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
                            if (which== DialogInterface.BUTTON_POSITIVE) {
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
                                                progressDialog.dismiss();
                                                e.printStackTrace();
                                            }
                                        });
                            } else if (which==DialogInterface.BUTTON_NEGATIVE) {
                                alertDialog.dismiss();
                            }
                        }
                    }, 1, true);
            for (Events e : events.values()) {
                e.onDeleteConversation(conversation);
            }
        }
    }

    /**
     * This method is used to set Color of StatusBar
     *
     * @author CometChat Team
     * Copyright &copy;  2021 CometChat Inc.
     */

    private void setStatusBarColor() {
        ColorDrawable viewColor = (ColorDrawable) getBackground();
        int backgroundColor = getResources().getColor(R.color.colorPrimaryDark);
        if (viewColor!=null)
            backgroundColor = viewColor.getColor();
        if (backgroundColor!=0)
            ((Activity)context).getWindow().setStatusBarColor(backgroundColor);

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
        if (cometchatConversationList!=null) {
            if (listBackgroundColor!=0)
                cometchatConversationList.setCardBackgroundColor(listBackgroundColor);
            else {
                cometchatConversationList.setCardBackgroundColor(Color.TRANSPARENT);
                cometchatConversationList.setCardElevation(0);
                cometchatConversationList.setRadius(0);
            }
        }
    }

    private void checkDarkMode() {
        if(Utils.isDarkMode(getContext())) {
            setTitleColor(getResources().getColor(R.color.textColorWhite));
        } else {
            setTitleColor(getResources().getColor(R.color.primaryTextColor));
        }
    }

    /**
     * This method is used to set the type of ConversationList i.e User or Group
     *
     * @param conversationListType -  It is a String object and here we can pass                             <b>CometChatConstant.CONVERSATION_TYPE_USER</b> or                             <b>CometChatConstant.CONVERSATION_TYPE_GROUP</b>
     */
    public void setConversationType(String conversationListType) {
        this.conversationListType = conversationListType;
        if (cometchatConversationList!=null)
            cometchatConversationList.setConversationType(conversationListType);
    }


    /**
     * Add listener.
     *
     * @param TAG             the tag
     * @param onEventListener An object of <code>OnItemClickListener&lt;T&gt;</code> abstract class helps to initialize with events
     * to perform onItemClick &amp; onItemLongClick.
     * @see Events
     */
    public static void addListener(String TAG, Events<Conversation> onEventListener) {
        events.put(TAG, onEventListener);
    }


    /**
     * This method is used to remove the particular Conversation from CometChatConversationList
     *
     * @param conversation is Converastion Object which will be removed.
     * @see CometChatConversations#removeConversation(Conversation)
     * @see Conversation
     */
    public void removeConversation(Conversation conversation) {
        cometchatConversationList.remove(conversation);
    }

    /**
     * This method is used to hide/show <b>Start Conversation</b> Icon shown in CometChatConversations.
     *
     * @param b is a boolean object. If b=true show "StartConversation" else hide "StartConversation"
     */
    public void hideStartConversation(boolean b) {
        hideStartConversation = !b;
    }

    /**
     * This method is used to hide/show <b>Delete Conversation</b> option shown on swipe of each Conversation.
     *
     * @param b is a boolean object. If b=true show "DeleteConversation" else hide "DeleteConversation"
     */
    public void showDeleteConversation(boolean b) {
        showDeleteConversation = b;
        invalidate();
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
        if (color!=0) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon!=null)
                icon.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    /**
     * This method is used to <b>set the Icon of StartConversation</b> option shown in CometChatConversations
     *
     * @param iconDrawable the icon drawable
     */
    public void startConversationIcon(Drawable iconDrawable) {
        if (iconDrawable!=null) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon!=null)
                icon.setImageDrawable(iconDrawable);
        }
    }


    /**
     * This method is used to <b>set the Icon of StartConversation</b> option shown in CometChatConversations
     *
     * @param iconDrawable the icon drawable
     */
    public void startConversationIcon(@DrawableRes int iconDrawable) {
        if (iconDrawable!=0) {
            ImageView icon = (ImageView) super.getIconAt(0);
            if (icon!=null)
                icon.setImageResource(iconDrawable);
        }
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
        hideHelperTextListItem = cometChatListItemProperty.isHelperTextHidden;
        hideTimeListItem = cometChatListItemProperty.isTimeHidden;
        titleColorListItem = cometChatListItemProperty.titleColor;
        subTitleColorListItem = cometChatListItemProperty.subTitleColor;
        helperTextColorListItem = cometChatListItemProperty.helperTextColor;
        timeTextColorListItem = cometChatListItemProperty.timeTextColor;
        backgroundColorListItem = cometChatListItemProperty.backgroundColor;
        backgroundColorListItemPosition = cometChatListItemProperty.backgroundColorAtPosition;
        cornerRadiusListItem = cometChatListItemProperty.cornerRadius;
        typingIndicatorColorListItem = cometChatListItemProperty.typingIndicatorColor;
        if (cometchatConversationList !=null)
            cometchatConversationList.setConversationListItemProperty(hideAvatar,hideUserPresenceListItem,
                    hideTitleListItem,titleColorListItem,
                    hideSubtitleListItem,subTitleColorListItem,
                    hideHelperTextListItem,helperTextColorListItem,
                    hideTimeListItem,timeTextColorListItem,
                    backgroundColorListItem,backgroundColorListItemPosition,
                    cornerRadiusListItem,typingIndicatorColorListItem);

    }

    /**
     * Sets configuration.
     *
     * @param configuration the configuration
     */
    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof ConversationsConfiguration) {
           showDeleteConversation = ((ConversationsConfiguration) configuration)
                   .isDeleteConversationHidden();
           hideStartConversation = ((ConversationsConfiguration) configuration)
                   .isStartConversationHidden();
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
        cometchatConversationList.setConfiguration(configurations);
    }

    /**
     * The type Comet chat list item property.
     */
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
        private int helperTextColor;
        private int timeTextColor;
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
        public CometChatListItemProperty setBackgroundColor(@ColorInt int color,int position) {
            backgroundColor = color;
            backgroundColorAtPosition = position;
            return this;
        }

        /**
         * Sets time text color.
         *
         * @param color the color
         * @return the time text color
         */
        public CometChatListItemProperty setTimeTextColor(@ColorInt int color) {
            timeTextColor = color;
            return this;
        }

        /**
         * Sets helper text color.
         *
         * @param color the color
         * @return the helper text color
         */
        public CometChatListItemProperty setHelperTextColor(@ColorInt int color) {
            helperTextColor = color;
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
     * The type Events.
     *
     * @param <T> the type parameter
     */
    public abstract static class Events<T> {

        /**
         * On item click.
         *
         * @param var      the var
         * @param position the position
         */
        public abstract void OnItemClick(T var, int position);

        /**
         * On delete conversation.
         *
         * @param var the var
         */
        public void onDeleteConversation   (T var) {}

        /**
         * On start conversation.
         */
        public void onStartConversation() {}

        /**
         * On item long click.
         *
         * @param var      the var
         * @param position the position
         */
        public void OnItemLongClick(T var,int position) {}
    }
}
