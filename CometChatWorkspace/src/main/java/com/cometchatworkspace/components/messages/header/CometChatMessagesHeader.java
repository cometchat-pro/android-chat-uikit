package com.cometchatworkspace.components.messages.header;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.AppEntity;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupActivity;
import com.cometchatworkspace.components.messages.CometChatMessageEvents;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.DataItemConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.HeaderConfiguration;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.CometChatOptions;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDetails.CometChatDetails;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.components.users.CometChatUserActivity;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CometChatMessagesHeader extends RelativeLayout {

    private View view;
    private Context context;
    private FontUtils fontUtils;
    private Palette palette;
    private Typography typography;
    private MaterialCardView toolbarCard;
    private Toolbar toolbar;
    private CometChatDataItem headerDataItem;
    private ImageView backIcon;

    private RelativeLayout onGoingCallView;
    private TextView onGoingCallTxt;
    private ImageView onGoingCallClose;

    private AppEntity receiver;
    private User user;
    private Group group;

    private String type;
    private String Id;
    private String status;
    private final String TAG = "CometChatMessageToolbar";

    private final HashMap<String, OnEventListener> onEventListeners = new HashMap<>();
    private final User loggedInUser = CometChat.getLoggedInUser();

    public CometChatMessagesHeader(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context, null);
    }

    public CometChatMessagesHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context, attrs);
    }

    public CometChatMessagesHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context, attrs);
    }

    private void initViewComponent(Context context, AttributeSet attrs) {
        this.context = context;
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        fontUtils = FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.CometChatMessagesHeader, 0, 0);

        String titleStr = a.getString(R.styleable.CometChatMessagesHeader_title);
        int titleColor = a.getColor(R.styleable.CometChatMessagesHeader_titleColor, palette.getAccent());
        String subtitleStr = a.getString(R.styleable.CometChatMessagesHeader_subtitle);
        int subtitleColor = a.getColor(R.styleable.CometChatMessagesHeader_subtitleColor, 0);
        int backIconTint = a.getColor(R.styleable.CometChatMessagesHeader_backIconTint, 0);

        view = LayoutInflater.from(context).inflate(R.layout.cometchat_messagelist_toolbar, null);
        toolbarCard = view.findViewById(R.id.toolbar_card);
        toolbar = view.findViewById(R.id.toolbar);
        headerDataItem = view.findViewById(R.id.headerDataItem);
        backIcon = view.findViewById(R.id.back_action);

        if (titleColor != 0)
            titleColor(titleColor);
        if (subtitleColor != 0)
            subTitleColor(subtitleColor);

        backgroundColor(Color.TRANSPARENT);
        headerElevation(0);
        backButtonIconTint(backIconTint);
        headerDataItem.getAvatar().setBackgroundColor(palette.getAccent700());
        addView(view);
        backIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OnEventListener eventListener : onEventListeners.values())
                    eventListener.onBackPressed();
            }
        });

        CometChatMessageEvents.addListener(TAG, new CometChatMessageEvents() {
            @Override
            public void onVoiceCall(AppEntity user_or_group) {
                if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    JSONObject customData = new JSONObject();
                    try {
                        customData.put("callType", CometChatConstants.CALL_TYPE_AUDIO);
                        customData.put("sessionID", Id + "_audio");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    directCallMessage(UIKitConstants.IntentStrings.GROUP_CALL, customData, CometChatConstants.CALL_TYPE_AUDIO);
                } else {
//                    checkOnGoingCall(CometChatConstants.CALL_TYPE_AUDIO);
                }
            }

            @Override
            public void onVideoCall(AppEntity appEntity) {
                if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    JSONObject customData = new JSONObject();
                    try {
                        customData.put("callType", CometChatConstants.CALL_TYPE_VIDEO);
                        customData.put("sessionID", Id + "_audio");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    directCallMessage(UIKitConstants.IntentStrings.GROUP_CALL, customData, CometChatConstants.CALL_TYPE_VIDEO);
                } else {
//                    checkOnGoingCall(CometChatConstants.CALL_TYPE_VIDEO);
                }
            }

            @Override
            public void onViewInformation(AppEntity appEntity) {
                if (appEntity instanceof User && receiver instanceof User) {
                    CometChatUserActivity.launch(context, CometChatDetails.class, (User) receiver);
                } else {
                    CometChatGroupActivity.launch(context, CometChatDetails.class, (Group) receiver);
                }
            }
        });

        //Check Ongoing Call
        onGoingCallView = view.findViewById(R.id.ongoing_call_view);
        onGoingCallClose = view.findViewById(R.id.close_ongoing_view);
        onGoingCallTxt = view.findViewById(R.id.ongoing_call);
//        checkOnGoingCall();
    }

    public void options(List<CometChatOptions> options) {
        LinearLayout layoutView = new LinearLayout(context);
        for (CometChatOptions eachOption : options) {
            TextView titleView;
            ImageView imageView;
            int id = -1;
            if (eachOption.getIcon() != 0) {
                imageView = new ImageView(context);
                imageView.setPadding(16, 0, 16, 0);
                imageView.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.colorPrimary)));
                imageView.setId(id);
                imageView.setImageResource(eachOption.getIcon());
                imageView.setElevation(32f);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (CometChatMessageEvents events : CometChatMessageEvents.messageEvents.values()) {
                            if (eachOption.getId().equals(UIKitConstants.UserOptions.audioCall))
                                events.onVoiceCall(receiver);
                            else if (eachOption.getId().equals(UIKitConstants.UserOptions.videoCall))
                                events.onVideoCall(receiver);
                            else if (eachOption.getId().equals(UIKitConstants.UserOptions.viewDetails))
                                events.onViewInformation(receiver);
                        }
                    }
                });
                layoutView.addView(imageView);
            } else if (eachOption.getTitle() != null) {
                titleView = new TextView(context);
                titleView.setId(id);
                titleView.setText(eachOption.getTitle());
                layoutView.addView(titleView);
            }
        }
        headerDataItem.setTailView(layoutView);
    }


    public void titleColor(@ColorInt int color) {
        headerDataItem.titleColor(color);
    }

    public void titleFont(String font) {
        headerDataItem.titleFont(font);
    }

    public String getSubTitle() {
        return status;
    }

    public void subTitleColor(@ColorInt int color) {
        headerDataItem.subTitleColor(color);
    }

    public void hideSubTitle(boolean isHidden) {
        headerDataItem.hideSubTitle(isHidden);
    }

    public void subTitleFont(String font) {
        headerDataItem.subTitleFont(font);
    }

    public void cornerRadius(float radius) {
        if (toolbarCard != null)
            toolbarCard.setRadius(radius);
    }

    public void background(Drawable drawable) {
        if (toolbarCard!=null && toolbar!=null) {
            toolbar.setBackground(drawable);
            toolbarCard.setBackgroundDrawable(drawable);
        }
    }
    public void backgroundColor(@ColorInt int color) {
        if (toolbarCard != null && toolbar != null) {
            toolbarCard.setCardBackgroundColor(color);
            toolbar.setBackgroundColor(color);
        }
    }

    public void headerElevation(int elevation) {
        if (toolbarCard!=null)
            toolbarCard.setCardElevation(elevation);
    }

    public void borderWidth(int width) {
        if (toolbarCard != null)
            toolbarCard.setStrokeWidth(width);
    }

    public void borderColor(@ColorInt int color) {
        if (toolbarCard != null)
            toolbarCard.setStrokeColor(color);
    }

    public void setStyle(Style style) {
        titleColor(style.getTitleColor());
        subTitleColor(style.getSubTitleColor());
        backButtonIconTint(style.getBackIconTint());
        cornerRadius(style.getCornerRadius());
        borderWidth(style.getBorder());
        borderColor(style.getBorderColor());
        backgroundColor(style.getBackground());
        background(style.getGradientBackground());
    }

    public void backButtonIconTint(@ColorInt int color) {
        if (backIcon != null && color != 0)
            backIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    //    /**
//     * This method is used to check if the app has ongoing call or not and based on it show the view
//     * through which user can join ongoing call.
//     *
//     */
//    private void checkOnGoingCall() {
//        if (CometChat.getActiveCall() != null
//                && (CometChat.getActiveCall().getReceiverUid().equalsIgnoreCase(Id) ||
//                CometChat.getActiveCall().getReceiverUid()
//                        .equalsIgnoreCase(loggedInUser.getUid()))
//                && CometChat.getActiveCall().getCallStatus().
//                equals(CometChatConstants.CALL_STATUS_ONGOING)
//                && CometChat.getActiveCall().getSessionId() != null) {
//            audioCallAction.setEnabled(false);
//            videoCallAction.setEnabled(false);
//            if (onGoingCallView != null)
//                onGoingCallView.setVisibility(View.VISIBLE);
//            if (onGoingCallTxt != null) {
//                onGoingCallTxt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onGoingCallView.setVisibility(View.GONE);
//                        if (CometChat.getActiveCall() != null)
//                            CallUtils.joinOnGoingCall(context, CometChat.getActiveCall());
//                        else {
//                            Toast.makeText(context, context.getString(R.string.call_ended), Toast.LENGTH_LONG).show();
//                            onGoingCallView.setVisibility(View.GONE);
//                        }
//                    }
//                });
//            }
//            if (onGoingCallClose != null) {
//                onGoingCallClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onGoingCallView.setVisibility(GONE);
//                    }
//                });
//            }
//        } else if (CometChat.getActiveCall() != null) {
//            if (onGoingCallView != null)
//                onGoingCallView.setVisibility(GONE);
//            Log.e(TAG, "checkOnGoingCall: " + CometChat.getActiveCall().toString());
//        } else {
//            audioCallAction.setEnabled(true);
//            videoCallAction.setEnabled(true);
//        }
//    }
    private void checkOnGoingCall(String callType) {
//        if (CometChat.getActiveCall() != null && CometChat.getActiveCall().getCallStatus().equals(CometChatConstants.CALL_STATUS_ONGOING) && CometChat.getActiveCall().getSessionId() != null) {
//            AlertDialog.Builder alert = new AlertDialog.Builder(context);
//            alert.setTitle(getResources().getString(R.string.ongoing_call))
//                    .setMessage(getResources().getString(R.string.ongoing_call_message))
//                    .setPositiveButton(getResources().getString(R.string.join), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            CallUtils.joinOnGoingCall(context, CometChat.getActiveCall());
//                        }
//                    }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    audioCallAction.setClickable(true);
//                    videoCallAction.setClickable(true);
//                }
//            }).create().show();
//        } else {
//            Call call = null;
//            if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP))
//                call = new Call(Id, CometChatConstants.RECEIVER_TYPE_GROUP, callType);
//            else
//                call = new Call(Id, CometChatConstants.RECEIVER_TYPE_USER, callType);
//            CometChat.initiateCall(call, new CometChat.CallbackListener<Call>() {
//                @Override
//                public void onSuccess(Call call) {
//                    for (OnEventListener eventListener : onEventListeners.values())
//                        eventListener.onVoiceCall(call);
//                    CallUtils.startCallIntent(context,
//                            ((User) call.getCallReceiver()), call.getType(), true,
//                            call.getSessionId());
//                }
//
//                @Override
//                public void onError(CometChatException e) {
//                    audioCallAction.setClickable(true);
//                    videoCallAction.setClickable(true);
//                    Log.e(TAG, "onError: " + e.getMessage());
//                    for (CometChatMessageEvents events : CometChatMessageEvents.messageEvents.values()) {
//                        events.onMessageError(e);
//                    }
//                }
//            });
//        }
    }

    /**
     * This method sends custom message based on parameter received
     *
     * @param customType
     * @param customData
     */
    private void directCallMessage(String customType, JSONObject customData, String callType) {
        CustomMessage customMessage;

        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_USER, customType, customData);
        else
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_GROUP, customType, customData);

        String pushNotificationMessage = context.getString(R.string.has_shared_group_call);
        try {
            JSONObject jsonObject = customMessage.getMetadata();
            if (jsonObject == null) {
                jsonObject = new JSONObject();
                jsonObject.put("incrementUnreadCount", true);
                jsonObject.put("pushNotification", pushNotificationMessage);
            } else {
                jsonObject.accumulate("incrementUnreadCount", true);
            }
            customMessage.setMetadata(jsonObject);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
            @Override
            public void onSuccess(CustomMessage customMessage) {
                Call call = new Call(Id, type, callType);
                for (OnEventListener eventListener : onEventListeners.values()) {
                    if (callType.equalsIgnoreCase(CometChatConstants.CALL_TYPE_AUDIO))
                        eventListener.onVoiceCall(call);
                    else
                        eventListener.onVideoCall(call);
                }
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used to get Group Details.
     *
     * @see CometChat#getGroup(String, CometChat.CallbackListener)
     */
    private void getGroup() {

        CometChat.getGroup(Id, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                if (headerDataItem != null) {
                    headerDataItem.group(group);
                    headerDataItem.hideStatusIndicator(true);
                    if (group.getMembersCount() > 1)
                       status = group.getMembersCount() + " " + context.getString(R.string.members);
                    else
                        status = group.getMembersCount() + " " + context.getString(R.string.member);
                }

            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method is used to get details of reciever.
     *
     * @see CometChat#getUser(String, CometChat.CallbackListener)
     */
    private void getUser() {

        CometChat.getUser(Id, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                if (user.isBlockedByMe()) {
                    headerDataItem.hideSubTitle(true);
                    setSelected(false);
                } else if (user.isHasBlockedMe()) {
                    headerDataItem.hideSubTitle(true);
                    setSelected(false);
                } else {
                    if (user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE)) {
                        headerDataItem.subTitleColor(getResources().getColor(R.color.colorPrimary));
                    }

                    status = user.getStatus();
                    headerDataItem.user(user);
                    headerDataItem.hideSubTitle(false);
                    headerDataItem.subTitle(status + "");
                }
                Log.d(TAG, "onSuccess: " + user);
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof HeaderConfiguration) {
            HeaderConfiguration headerConfiguration =
                    (HeaderConfiguration) configuration;
            AvatarConfiguration avatarConfiguration = headerConfiguration.getAvatarConfiguration();
            if (avatarConfiguration != null)
                setAvatarConfig(avatarConfiguration);
            if (headerConfiguration.getInputData() != null)
                headerDataItem.inputData(headerConfiguration.getInputData());
            if (headerConfiguration.getStyle()!=null)
                setStyle(headerConfiguration.getStyle());
        } else if (configuration instanceof DataItemConfiguration) {
            DataItemConfiguration dataItemConfiguration = (DataItemConfiguration) configuration;
            if (dataItemConfiguration.getInputData() != null)
                headerDataItem.inputData(dataItemConfiguration.getInputData());
        } else if (configuration instanceof AvatarConfiguration) {
            setAvatarConfig((AvatarConfiguration) configuration);
        }
    }

    private void setAvatarConfig(AvatarConfiguration avatarConfiguration) {
        if (avatarConfiguration != null) {
            CometChatAvatar userAvatar = headerDataItem.getAvatar();
            if (avatarConfiguration.getCornerRadius() != -1)
                userAvatar.setCornerRadius(avatarConfiguration.getCornerRadius());
            if (avatarConfiguration.getBorderWidth() != -1)
                userAvatar.setBorderWidth(avatarConfiguration.getBorderWidth());
            if (avatarConfiguration.getWidth() != -1 && avatarConfiguration.getHeight() != -1) {
                int avWidth = (int) Utils.dpToPx(context, avatarConfiguration.getWidth());
                int avHeight = (int) Utils.dpToPx(context, avatarConfiguration.getHeight());
                RelativeLayout.LayoutParams layoutParams =
                        new RelativeLayout.LayoutParams(avWidth, avHeight);
                userAvatar.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * This method is used to remove user presence listener
     *
     * @see CometChat#removeUserListener(String)
     */
    private void removeUserListener() {
        CometChat.removeUserListener(TAG);
    }

    /**
     * This method is used to remove group listener
     *
     * @see CometChat#removeGroupListener(String)
     */
    private void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    /**
     * This method is used to display typing status to user.
     *
     * @param show is boolean, If it is true then <b>is Typing</b> will be shown to user
     *             If it is false then it will show user status i.e online or offline.
     */
    private void typingIndicator(TypingIndicator typingIndicator, boolean show) {
        if (show) {
            if (typingIndicator.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                headerDataItem.subTitle(context.getString(R.string.is_typing));
            } else {
                headerDataItem.subTitle(typingIndicator.getSender().getName() +" "+ context.getString(R.string.is_typing));
            }
        } else {
            headerDataItem.subTitle(status);
        }
    }

    public void user(User user) {
        type = CometChatConstants.RECEIVER_TYPE_USER;
        Id = user.getUid();
        receiver = user;
        headerDataItem.user(user);
        headerDataItem.hideSubTitle(false);
    }

    public void group(Group group) {
        type = CometChatConstants.RECEIVER_TYPE_GROUP;
        Id = group.getGuid();
        receiver = group;
        headerDataItem.group(group);
        headerDataItem.hideStatusIndicator(true);

    }

    /**
     * This method is used to get real time user status i.e user is online or offline.
     *
     * @see CometChat#addUserListener(String, CometChat.UserListener)
     */
    public void addUserListener() {
        if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            CometChat.addUserListener(TAG, new CometChat.UserListener() {
                @Override
                public void onUserOnline(User user) {
                    Log.d(TAG, "onUserOnline: " + user.toString());
                    if (user.getUid().equals(Id)) {
                        headerDataItem.subTitle(context.getString(R.string.online));
                        subTitleColor(getResources().getColor(R.color.colorPrimary));
                        headerDataItem.statusIndicator(CometChatStatusIndicator.STATUS.ONLINE);

                    }
                }

                @Override
                public void onUserOffline(User user) {
                    Log.d(TAG, "onUserOffline: " + user.toString());
                    if (user.getUid().equals(Id)) {
                        if (Utils.isDarkMode(getContext()))
                            subTitleColor(getResources().getColor(R.color.textColorWhite));
                        else
                            subTitleColor(getResources().getColor(android.R.color.black));
                        headerDataItem.subTitle(context.getString(R.string.offline));
                        headerDataItem.statusIndicator(CometChatStatusIndicator.STATUS.OFFLINE);
                    }
                }
            });
        }
    }


    public void setTypingIndicator(TypingIndicator typingIndicator, boolean isShow) {
        if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getSender().getUid())) {
                typingIndicator(typingIndicator, isShow);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getReceiverId()))
                typingIndicator(typingIndicator, isShow);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if (visibility == VISIBLE)
            onResume();
        else
            onPause();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onPause();
    }

    private void onResume() {
//        checkOnGoingCall();
//        audioCallAction.setClickable(true);
//        videoCallAction.setClickable(true);

        addMessageListener();
        if (type != null) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                addUserListener();
                new Thread(this::getUser).start();
            } else {
                addGroupListener();
                new Thread(this::getGroup).start();
            }
        }
    }

    private void onPause() {
        removeMessageListener();
        removeGroupListener();
        removeUserListener();
    }

    /**
     * This method is used to recieve real time group events like onMemberAddedToGroup, onGroupMemberJoined,
     * onGroupMemberKicked, onGroupMemberLeft, onGroupMemberBanned, onGroupMemberUnbanned,
     * onGroupMemberScopeChanged.
     *
     * @see CometChat#addGroupListener(String, CometChat.GroupListener)
     */
    private void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                super.onGroupMemberJoined(action, joinedUser, joinedGroup);
                if (joinedGroup.getGuid().equals(Id)) {
                    if (joinedGroup.getMembersCount() > 1)
                        headerDataItem.subTitle(joinedGroup.getMembersCount() + " " + context.getString(R.string.members));
                    else
                        headerDataItem.subTitle(joinedGroup.getMembersCount() + " " + context.getString(R.string.member));
                }
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                if (leftGroup.getGuid().equals(Id)) {
                    if (leftGroup.getMembersCount() > 1)
                        headerDataItem.subTitle(leftGroup.getMembersCount() + " " + context.getString(R.string.members));
                    else
                        headerDataItem.subTitle(leftGroup.getMembersCount() + " " + context.getString(R.string.member));
                }
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                if (kickedFrom.getGuid().equals(Id)) {
                    if (kickedFrom.getMembersCount() > 1)
                        headerDataItem.subTitle(kickedFrom.getMembersCount() + " " + context.getString(R.string.members));
                    else
                        headerDataItem.subTitle(kickedFrom.getMembersCount() + " " + context.getString(R.string.member));
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom.getGuid().equals(Id)) {
                    if (bannedFrom.getMembersCount() > 1)
                        headerDataItem.subTitle(bannedFrom.getMembersCount() + " " + context.getString(R.string.members));
                    else
                        headerDataItem.subTitle(bannedFrom.getMembersCount() + " " + context.getString(R.string.member));
                }
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                if (addedTo.getGuid().equals(Id)) {
                    if (addedTo.getMembersCount() > 1)
                        headerDataItem.subTitle(addedTo.getMembersCount() + " " + context.getString(R.string.members));
                    else
                        headerDataItem.subTitle(addedTo.getMembersCount() + " " + context.getString(R.string.member));
                }
            }
        });
    }

    /**
     * This method is used to add message listener to recieve real time messages between users &
     * groups. It also give real time events for typing indicators, edit message, delete message,
     * message being read & delivered.
     *
     * @see CometChat#addMessageListener(String, CometChat.MessageListener)
     */
    private void addMessageListener() {

        CometChat.addMessageListener(TAG, new CometChat.MessageListener() {
            @Override
            public void onTypingStarted(TypingIndicator typingIndicator) {
                setTypingIndicator(typingIndicator, true);
            }

            @Override
            public void onTypingEnded(TypingIndicator typingIndicator) {
                Log.d(TAG, "onTypingEnded: " + typingIndicator.toString());
                setTypingIndicator(typingIndicator, false);
            }
        });
    }

    /**
     * This method is used to remove message listener
     *
     * @see CometChat#removeMessageListener(String)
     */
    private void removeMessageListener() {
        CometChat.removeMessageListener(TAG);
    }

    public void addListener(String TAG, OnEventListener messageToolbarListener) {
        onEventListeners.put(TAG, messageToolbarListener);
    }

    public abstract static class OnEventListener extends CometChatMessageEvents {
        public abstract void onBackPressed();
    }
}
