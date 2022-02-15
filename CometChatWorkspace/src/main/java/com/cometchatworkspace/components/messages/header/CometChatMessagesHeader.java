package com.cometchatworkspace.components.messages.header;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.Toolbar;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.components.calls.CallUtils;
import com.cometchatworkspace.resources.utils.CometChatError;
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
    private MaterialCardView toolbarCard;
    private Toolbar toolbar;
    private ImageView backIcon;
    private ImageView infoAction;
    private ImageView audioCallAction;
    private ImageView videoCallAction;
    private CometChatAvatar userAvatar;
    private TextView title;
    private TextView subtitle;

    private RelativeLayout onGoingCallView;
    private TextView onGoingCallTxt;
    private ImageView onGoingCallClose;

    private String type;
    private String Id;
    private String status;
    private final String TAG = "CometChatMessageToolbar";

    private HashMap<String,OnEventListener> onEventListeners = new HashMap<>();
    private final User loggedInUser = CometChat.getLoggedInUser();

    public CometChatMessagesHeader(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context,null);
    }

    public CometChatMessagesHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context,attrs);
    }

    public CometChatMessagesHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context,attrs);
    }

    private void initViewComponent(Context context,AttributeSet attrs) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.CometChatMessagesHeader, 0, 0);

        String titleStr = a.getString(R.styleable.CometChatMessagesHeader_title);
        int titleColor = a.getColor(R.styleable.CometChatMessagesHeader_titleColor,0);
        String subtitleStr = a.getString(R.styleable.CometChatMessagesHeader_subtitle);
        int subtitleColor = a.getColor(R.styleable.CometChatMessagesHeader_subtitleColor,0);
        Drawable audioIcon = a.getDrawable(R.styleable.CometChatMessagesHeader_audioCallIcon);
        int audioIconTint = a.getColor(R.styleable.CometChatMessagesHeader_audioCallIconTint,0);
        Drawable videoIcon = a.getDrawable(R.styleable.CometChatMessagesHeader_videoCallIcon);
        int videoIconTint = a.getColor(R.styleable.CometChatMessagesHeader_videoCallIconTint,0);
        Drawable viewDetailIcon = a.getDrawable(R.styleable.CometChatMessagesHeader_viewDetailsIcon);
        int viewDetailsIconTint = a.getColor(R.styleable.CometChatMessagesHeader_viewDetailsIconTint,0);
        int backIconTint = a.getColor(R.styleable.CometChatMessagesHeader_backIconTint,0);

        view = LayoutInflater.from(context).inflate(R.layout.cometchat_messagelist_toolbar,null);
        toolbarCard = view.findViewById(R.id.toolbar_card);
        toolbar = view.findViewById(R.id.toolbar);
        title = view.findViewById(R.id.tv_name);
        subtitle = view.findViewById(R.id.tv_status);
        userAvatar = view.findViewById(R.id.iv_chat_avatar);
        backIcon = view.findViewById(R.id.back_action);
        infoAction = view.findViewById(R.id.info_action);
        audioCallAction = view.findViewById(R.id.audio_call_action);
        videoCallAction = view.findViewById(R.id.video_call_action);
        if (Utils.isDarkMode(context)) {
            setBackgroundColor(getResources().getColor(R.color.grey));
            titleColor(getResources().getColor(R.color.textColorWhite));
        } else {
            setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            titleColor(getResources().getColor(R.color.primaryTextColor));
        }

        title(titleStr);
        if (titleColor!=0)
            titleColor(titleColor);
        subTitle(subtitleStr);
        if (subtitleColor!=0)
            subTitleColor(subtitleColor);
        audioCallButtonIcon(audioIcon);
        audioCallButtonIconTint(audioIconTint);
        videoCallButtonIcon(videoIcon);
        videoCallButtonIconTint(videoIconTint);
        viewDetailsButtonIcon(viewDetailIcon);
        viewDetailsButtonIconTint(viewDetailsIconTint);
        backButtonIconTint(backIconTint);

        addView(view);
        backIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OnEventListener eventListener : onEventListeners.values())
                    eventListener.onBackPressed();
            }
        });
        infoAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OnEventListener eventListener : onEventListeners.values())
                    eventListener.onInfoIconClicked();
            }
        });
        audioCallAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    JSONObject customData = new JSONObject();
                    try {
                        customData.put("callType", CometChatConstants.CALL_TYPE_AUDIO);
                        customData.put("sessionID",Id+"_audio");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sendCustomCallMessage(UIKitConstants.IntentStrings.GROUP_CALL,customData);
                } else {
                    checkOnGoingCall(CometChatConstants.CALL_TYPE_AUDIO);
                }
            }
        });
        videoCallAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                    JSONObject customData = new JSONObject();
                    try {
                        customData.put("callType", CometChatConstants.CALL_TYPE_VIDEO);
                        customData.put("sessionID",Id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sendCustomCallMessage(UIKitConstants.IntentStrings.GROUP_CALL,customData);
                } else {
                    checkOnGoingCall(CometChatConstants.CALL_TYPE_VIDEO);
                }
            }
        });

        //Check Ongoing Call
        onGoingCallView = view.findViewById(R.id.ongoing_call_view);
        onGoingCallClose = view.findViewById(R.id.close_ongoing_view);
        onGoingCallTxt = view.findViewById(R.id.ongoing_call);
        checkOnGoingCall();
    }


    public void title(String name) {
        title.setText(name);
    }

    public void titleColor(@ColorInt int color) {
        title.setTextColor(color);
    }

    public void titleFont(String font){
        title.setTypeface(fontUtils.getTypeFace(font));
    }

    public void subTitle(String str) {
        status = str;
        subtitle.setText(str);
    }

    public String getSubTitle() {
        return status;
    }

    public void subTitleColor(@ColorInt int color) {
        subtitle.setTextColor(color);
    }

    public void hideSubTitle(boolean isHidden) {
        if (isHidden)
            subtitle.setVisibility(View.GONE);
        else
            subtitle.setVisibility(View.VISIBLE);
    }

    public void subTitleFont(String font){
        subtitle.setTypeface(fontUtils.getTypeFace(font));
    }

    public void cornerRadius(float radius) {
        if (toolbarCard!=null)
            toolbarCard.setRadius(radius);
    }

    public void backgroundColor(@ColorInt int color) {
        if (toolbarCard!=null && toolbar!=null) {
            toolbarCard.setCardBackgroundColor(color);
            toolbar.setBackgroundColor(color);
        }
    }

    public void borderWidth(int width) {
        if (toolbarCard!=null)
            toolbarCard.setStrokeWidth(width);
    }

    public void borderColor(@ColorInt int color) {
        if (toolbarCard!=null)
            toolbarCard.setStrokeColor(color);
    }

    public void audioCallButtonIcon(Drawable drawable) {
        if (audioCallAction!=null && drawable!=null)
            audioCallAction.setImageDrawable(drawable);
    }

    public void audioCallButtonIconTint(@ColorInt int color){
        if (audioCallAction!=null && color!=0)
            audioCallAction.setImageTintList(ColorStateList.valueOf(color));
    }

    public void videoCallButtonIcon(Drawable drawable) {
        if (videoCallAction!=null && drawable!=null)
            videoCallAction.setImageDrawable(drawable);
    }

    public void videoCallButtonIconTint(@ColorInt int color) {
        if (videoCallAction!=null && color!=0)
            videoCallAction.setImageTintList(ColorStateList.valueOf(color));
    }

    public void viewDetailsButtonIcon(Drawable drawable) {

    }
    public void viewDetailsButtonIconTint(@ColorInt int color) {

    }

    public void backButtonIconTint(@ColorInt int color) {

    }
    /**
     * This method is used to check if the app has ongoing call or not and based on it show the view
     * through which user can join ongoing call.
     *
     */
    private void checkOnGoingCall() {
        if (CometChat.getActiveCall() != null
                && (CometChat.getActiveCall().getReceiverUid().equalsIgnoreCase(Id) ||
                CometChat.getActiveCall().getReceiverUid()
                        .equalsIgnoreCase(loggedInUser.getUid()))
                && CometChat.getActiveCall().getCallStatus().
                equals(CometChatConstants.CALL_STATUS_ONGOING)
                && CometChat.getActiveCall().getSessionId() != null) {
            audioCallAction.setEnabled(false);
            videoCallAction.setEnabled(false);
            if (onGoingCallView != null)
                onGoingCallView.setVisibility(View.VISIBLE);
            if (onGoingCallTxt != null) {
                onGoingCallTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGoingCallView.setVisibility(View.GONE);
                        if (CometChat.getActiveCall() != null)
                            CallUtils.joinOnGoingCall(context, CometChat.getActiveCall());
                        else {
                            Toast.makeText(context, context.getString(R.string.call_ended), Toast.LENGTH_LONG).show();
                            onGoingCallView.setVisibility(View.GONE);
                        }
                    }
                });
            }
            if (onGoingCallClose != null) {
                onGoingCallClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onGoingCallView.setVisibility(GONE);
                    }
                });
            }
        } else if (CometChat.getActiveCall() != null) {
            if (onGoingCallView != null)
                onGoingCallView.setVisibility(GONE);
            Log.e(TAG, "checkOnGoingCall: " + CometChat.getActiveCall().toString());
        } else {
            audioCallAction.setEnabled(true);
            videoCallAction.setEnabled(true);
        }
    }
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
            Call call = null;
            if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP))
                call = new Call(Id, CometChatConstants.RECEIVER_TYPE_GROUP, callType);
            else
                call = new Call(Id, CometChatConstants.RECEIVER_TYPE_USER, callType);
            CometChat.initiateCall(call, new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    for (OnEventListener eventListener : onEventListeners.values())
                        eventListener.onDefaultCall(call);
//                    CallUtils.startCallIntent(context,
//                            ((User) call.getCallReceiver()), call.getType(), true,
//                            call.getSessionId());
                }

                @Override
                public void onError(CometChatException e) {
                    audioCallAction.setClickable(true);
                    videoCallAction.setClickable(true);
                    Log.e(TAG, "onError: " + e.getMessage());
                    if (title !=null)
                        CometChatSnackBar.show(context,
                                title,
                            CometChatError.localized(e), CometChatSnackBar.ERROR);
                }
            });
//        }
    }

    /**
     * This method sends custom message based on parameter received
     * @param customType
     * @param customData
     */
    private void sendCustomCallMessage(String customType, JSONObject customData) {
        CustomMessage customMessage;

        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_USER, customType, customData);
        else
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_GROUP, customType, customData);

        String pushNotificationMessage= context.getString(R.string.has_shared_group_call);
        try {
            JSONObject jsonObject = customMessage.getMetadata();
            if (jsonObject==null) {
                jsonObject = new JSONObject();
                jsonObject.put("incrementUnreadCount", true);
                jsonObject.put("pushNotification",pushNotificationMessage);
            } else {
                jsonObject.accumulate("incrementUnreadCount", true);
            }
            customMessage.setMetadata(jsonObject);
        } catch(Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
            @Override
            public void onSuccess(CustomMessage customMessage) {
                Log.e(TAG, "onSuccessCustomMesage: "+customMessage.toString());
                for (OnEventListener eventListener : onEventListeners.values()) {
                    eventListener.onCustomCallMessageSent(customMessage);
                }
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used to get Group Members and display names of group member.
     *
     * @see GroupMember
     * @see GroupMembersRequest
     */
    private void getMember() {
        GroupMembersRequest groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(Id).setLimit(30).build();

        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> list) {
                String[] s = new String[0];
                if (list != null && list.size() != 0) {
                    s = new String[list.size()];
                    for (int j = 0; j < list.size(); j++) {

                        s[j] = list.get(j).getName();
                    }

                }
                if (s.length != 0) {
                    StringBuilder stringBuilder = new StringBuilder();

                    for (String user : s) {
                        stringBuilder.append(user).append(",");
                    }

                    String memberNames = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

                    subTitle(memberNames);
                }

            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "Group Member list fetching failed with exception: " + e.getMessage());
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
                String name = group.getName();
                String avatarUrl = group.getIcon();

                title(name);
                setAvatar(avatarUrl,name);
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
                    hideSubTitle(true);
                    setSelected(false);
                } else if (user.isHasBlockedMe()) {
                    hideSubTitle(true);
                    setSelected(false);
                } else {
                    hideSubTitle(false);
                    if (user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE)) {
                        subTitleColor(getResources().getColor(R.color.colorPrimary));
                    }

                    status = user.getStatus();
                    subTitle(status);

                    setAvatar(user.getAvatar(),user.getName());
                }
                String name = user.getName();
                title(name);
                Log.d(TAG, "onSuccess: " + user.toString());
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof CometChatMessagesConfigurations) {
            CometChatMessagesConfigurations messagesConfigurations =
                    (CometChatMessagesConfigurations) configuration;
            AvatarConfiguration avatarConfiguration = messagesConfigurations.getHeaderAvatarConfiguration();
            setAvatarConfig(avatarConfiguration);
        }
        if (configuration instanceof AvatarConfiguration) {
            setAvatarConfig((AvatarConfiguration) configuration);
        }
    }

    private void setAvatarConfig(AvatarConfiguration avatarConfiguration) {
        if (avatarConfiguration!=null) {
            if (avatarConfiguration.getCornerRadius()!=-1)
                userAvatar.setCornerRadius(avatarConfiguration.getCornerRadius());
            if (avatarConfiguration.getBorderWidth()!=-1)
                userAvatar.setBorderWidth(avatarConfiguration.getBorderWidth());
            if (avatarConfiguration.getWidth()!=-1 && avatarConfiguration.getHeight()!=-1) {
                int avWidth = (int) Utils.dpToPx(context,avatarConfiguration.getWidth());
                int avHeight = (int) Utils.dpToPx(context,avatarConfiguration.getHeight());
                RelativeLayout.LayoutParams layoutParams =
                        new RelativeLayout.LayoutParams(avWidth,avHeight);
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
     * This method is used to display typing status to user.
     *
     * @param show is boolean, If it is true then <b>is Typing</b> will be shown to user
     *             If it is false then it will show user status i.e online or offline.
     */
    private void typingIndicator(TypingIndicator typingIndicator, boolean show) {
        if (show) {
            if (typingIndicator.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                subtitle.setText(context.getString(R.string.is_typing));
            } else {
                subtitle.setText(typingIndicator.getSender().getName() + context.getString(R.string.is_typing));
            }
        } else {
            subtitle.setText(status);
        }
    }

    //Methods
    public void hideAudioCallOption(boolean isHidden) {
        if (isHidden)
            audioCallAction.setVisibility(View.GONE);
        else
            audioCallAction.setVisibility(View.VISIBLE);
    }

    public void hideVideoCallOption(boolean isHidden) {
        if (isHidden)
            audioCallAction.setVisibility(View.GONE);
        else
            audioCallAction.setVisibility(View.VISIBLE);
    }
    public void chatReceiver(String Id,String type) {
        this.type = type;
        this.Id = Id;
      //  fetchSettings();
    }

    public void setAvatar(String url,String initials) {
        userAvatar.setInitials(initials);
        if (url!=null)
            userAvatar.setAvatar(url);
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
                        subTitle(context.getString(R.string.online));
                        subTitleColor(getResources().getColor(R.color.colorPrimary));
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
                        subTitle(context.getString(R.string.offline));
                    }
                }
            });
        }
    }



    public void setTypingIndicator(TypingIndicator typingIndicator,boolean isShow) {
        if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
            Log.e(TAG, "onTypingStarted: " + typingIndicator);
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getSender().getUid())) {
                typingIndicator(typingIndicator, isShow);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getReceiverId()))
                typingIndicator(typingIndicator, isShow);
        }
    }



    public void onResume() {
        checkOnGoingCall();
        audioCallAction.setClickable(true);
        videoCallAction.setClickable(true);

        if (type != null) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                addUserListener();
                new Thread(this::getUser).start();
            } else {
                new Thread(this::getGroup).start();
                new Thread(this::getMember).start();
            }
        }
    }

    public void onPause() {
        removeUserListener();
    }

   public void addListener(String TAG,OnEventListener messageToolbarListener) {
        onEventListeners.put(TAG,messageToolbarListener);
   }

   public abstract static class OnEventListener {
       public void onCustomCallMessageSent(CustomMessage customMessage) {

       }

       public void onDefaultCall(Call call) {

       }
       public abstract void onBackPressed();

       public void onInfoIconClicked() {

       }

   }
}
