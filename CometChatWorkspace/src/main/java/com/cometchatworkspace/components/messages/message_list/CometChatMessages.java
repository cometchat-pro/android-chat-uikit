package com.cometchatworkspace.components.messages.message_list;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TransientMessage;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.messages.CometChatMessageEvents;
import com.cometchatworkspace.components.messages.MessageStatus;
import com.cometchatworkspace.components.messages.common.extensions.ExtensionResponseListener;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.header.CometChatMessagesHeader;
import com.cometchatworkspace.components.messages.message_list.message_bubble.utils.MessageInputData;
import com.cometchatworkspace.components.messages.message_list.utils.LocationUtils;
import com.cometchatworkspace.components.messages.message_list.utils.polls.PollsUtils;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ComposerConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.HeaderConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageListConfiguration;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatOptions.CometChatOptions;
import com.cometchatworkspace.components.messages.template.TemplateUtils;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.CometChatSoundManager;
import com.cometchatworkspace.components.shared.primaryComponents.soundManager.Sound;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionSheet;
import com.cometchatworkspace.components.messages.composer.CometChatComposer;
import com.cometchatworkspace.components.messages.composer.listener.Events;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.CometChatReactionDialog;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.listener.OnReactionClickListener;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStickers.StickerView;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStickers.listener.StickerClickListener;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStickers.model.Sticker;

import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.CometChatError;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.MediaUtils;
import com.cometchatworkspace.resources.utils.Utils;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * Purpose - CometChatMessageScreen class is a fragment used to display list of messages and perform certain action on click of message.
 * It also provide search bar to perform search operation on the list of messages. User can send text,images,video and file as messages
 * to each other and in groups. User can also perform actions like edit message,delete message and forward messages to other user and groups.
 *
 * @see CometChat
 * @see User
 * @see Group
 * @see TextMessage
 * @see MediaMessage
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 16th January 2020
 */


public class CometChatMessages extends Fragment implements View.OnClickListener {

    private static final String TAG = "CometChatMessageScreen";

    private Context context;

    private String Id;
    private String name = "";
    private String status = "";
    private String avatarUrl;
    private String profileLink;
    private String type;
    private int memberCount;
    private String groupDesc;
    private String groupPassword;
    private String groupType;
    private String groupOwnerId;
    private boolean isBlockedByMe;
    private String loggedInUserScope;
    private final User loggedInUser = CometChat.getLoggedInUser();

    private CometChatMessagesHeader cometchatMessageHeader;

    private static CometChatMessageList cometChatMessageList;

    private LinearLayout bottomLayout;
    private CometChatComposer composeBox;

    private LinearLayout blockUserLayout;
    private MaterialButton unblockBtn;
    private TextView blockedUserName;

    private BaseMessage baseMessage;


    private View view;

    String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private StickerView stickersView;
    private RelativeLayout stickerLayout;
    private ImageView closeStickerView;

    private int resultIntentCode;

    private ImageView imageView;
    private FrameLayout container;
    public ObjectAnimator animation;

    boolean hideDeleteMessage;

    private BaseMessage repliedMessage;
    private CometChatSoundManager soundManager;
    private LocationUtils locationUtils;
    private int count = 0;

    private User user;
    private Group group;
    private boolean isGroupActionMessagesVisible;

    private Palette palette;
    private Typography typography;

    //Configurations
    private boolean userChatEnabled = true;
    private boolean groupChatEnabled = true;
    private CometChatConfigurations config;

    public CometChatMessages() {
        // Required empty public constructor
    }

    public static void updateList(BaseMessage message) {
        if (cometChatMessageList != null)
            cometChatMessageList.onMessageReceived(message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        palette = Palette.getInstance(context);
        typography = Typography.getInstance();
        handleArguments();
    }

    public void setUser(User user) {
        this.user = user;
        this.Id = user.getUid();
        this.type = CometChatConstants.RECEIVER_TYPE_USER;
        getUser();
    }

    public void setUser(String uid) {
        this.Id = uid;
        this.type = CometChatConstants.RECEIVER_TYPE_USER;
    }

    public void setGroup(Group group) {
        this.group = group;
        this.Id = group.getGuid();
        this.type = CometChatConstants.RECEIVER_TYPE_GROUP;
        this.groupType = group.getGroupType();
        getGroup();
    }

    public void setGroup(String guid) {
        this.Id = guid;
        this.type = CometChatConstants.RECEIVER_TYPE_GROUP;
    }

    public void setReplyMessage(String json) {
        try {
            JSONObject repliedMessageJSON = new JSONObject(json);
            repliedMessage = CometChatHelper.processMessage(repliedMessageJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to handle arguments passed to this fragment.
     */
    private void handleArguments() {
        if (getArguments() != null) {
            Id = getArguments().getString(UIKitConstants.IntentStrings.UID);
            avatarUrl = getArguments().getString(UIKitConstants.IntentStrings.AVATAR);
            status = getArguments().getString(UIKitConstants.IntentStrings.STATUS);
            name = getArguments().getString(UIKitConstants.IntentStrings.NAME);
            profileLink = getArguments().getString(UIKitConstants.IntentStrings.LINK);
            type = getArguments().getString(UIKitConstants.IntentStrings.TYPE);
            if (type != null && type.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                Id = getArguments().getString(UIKitConstants.IntentStrings.GUID);
                memberCount = getArguments().getInt(UIKitConstants.IntentStrings.MEMBER_COUNT);
                groupDesc = getArguments().getString(UIKitConstants.IntentStrings.GROUP_DESC);
                groupPassword = getArguments().getString(UIKitConstants.IntentStrings.GROUP_PASSWORD);
                groupType = getArguments().getString(UIKitConstants.IntentStrings.GROUP_TYPE);
            }

            String message = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE);
            if (message != null) {
                try {
                    JSONObject repliedMessageJSON = new JSONObject(message);
                    repliedMessage = CometChatHelper.processMessage(repliedMessageJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cometchat_messagelist, container, false);
        initViewComponent(view);
        return view;
    }


    /**
     * This is a main method which is used to initialize the view for this fragment.
     *
     * @param view
     */
    private void initViewComponent(View view) {
        soundManager = new CometChatSoundManager(getContext());
        setHasOptionsMenu(true);
        locationUtils = new LocationUtils();
        CometChatError.init(getContext());

        bottomLayout = view.findViewById(R.id.bottom_layout);
        composeBox = view.findViewById(R.id.message_box);
        composeBox.background(palette.getAccent100());
        composeBox.placeHolderColor(palette.getAccent600());
        composeBox.hideMicrophone(true);
        cometChatMessageList = view.findViewById(R.id.message_list);
        RelativeLayout relativeLayout = view.findViewById(R.id.message_parent);
        if (palette.getGradientBackground() != null)
            relativeLayout.setBackground(palette.getGradientBackground());
        else
            relativeLayout.setBackgroundColor(palette.getBackground());

        if (user != null)
            composeBox.setUser(user);
        if (group != null)
            composeBox.setGroup(group);

        composeBox.addKeyboardSupport(getActivity(), cometChatMessageList.getParent());

        setComposeBoxListener();

        container = view.findViewById(R.id.reactions_container);

        stickersView = view.findViewById(R.id.stickersView);
        stickerLayout = view.findViewById(R.id.sticker_layout);
        closeStickerView = view.findViewById(R.id.close_sticker_layout);

        closeStickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerLayout.setVisibility(GONE);
            }
        });

        stickersView.setStickerClickListener(new StickerClickListener() {
            @Override
            public void onClickListener(Sticker sticker) {
                JSONObject stickerData = new JSONObject();
                try {
                    stickerData.put("sticker_url", sticker.getUrl());
                    stickerData.put("sticker_name", sticker.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCustomMessage(UIKitConstants.IntentStrings.STICKERS, stickerData);
                stickerLayout.setVisibility(GONE);
            }
        });


        if (repliedMessage != null) {
            baseMessage = repliedMessage;
            composeBox.reply(baseMessage);
            if (cometChatMessageList != null) {
                cometChatMessageList.highLightMessage(baseMessage.getId());
            }
        }

//        cometChatMessageList.avatar(avatarUrl);
        cometChatMessageList.name(name);
        cometChatMessageList.type(type);
        if (CometChatConstants.RECEIVER_TYPE_GROUP.equalsIgnoreCase(type)) {
            cometChatMessageList.guid(Id);
//            cometChatMessageList.groupType(groupType);
//            cometChatMessageList.groupDescription(groupDesc);
//            cometChatMessageList.groupPassword(groupPassword);
            cometChatMessageList.loggedInUserScope(loggedInUserScope);
//            cometChatMessageList.membersCount(memberCount);
        } else {
            cometChatMessageList.uid(Id);
            cometChatMessageList.status(status);
            cometChatMessageList.isBlockedByMe(isBlockedByMe);
        }

        cometChatMessageList.actionSheetMode(ActionSheet.LayoutMode.gridMode);
//        rvChatListView = view.findViewById(R.id.rv_message_list);
        MaterialButton unblockUserBtn = view.findViewById(R.id.btn_unblock_user);
        unblockUserBtn.setOnClickListener(this);
        blockedUserName = view.findViewById(R.id.tv_blocked_user_name);
        unblockBtn = view.findViewById(R.id.btn_unblock_user);
        blockUserLayout = view.findViewById(R.id.blocked_user_layout);

        cometchatMessageHeader = view.findViewById(R.id.chatList_toolbar);
//        List<CometChatOptions> options = new ArrayList();
//        options.add(new CometChatOptions(
//                UIKitConstants.UserOptions.audioCall,
//                null,
//                R.drawable.ic_audiocall));
//        options.add(new CometChatOptions(
//                UIKitConstants.UserOptions.videoCall,
//                null,
//                R.drawable.ic_videocall));

//        options.add(new CometChatOptions(
//                UIKitConstants.UserOptions.viewDetails,
//                null,
//                R.drawable.ic_info));
//        cometchatMessageHeader.options(options);

        if (CometChatConstants.RECEIVER_TYPE_GROUP.equals(type)) {
            if (group != null)
                cometchatMessageHeader.group(group);
//            cometchatMessageHeader.hideAudioCallOption(true);
//            cometchatMessageHeader.hideVideoCallOption(false);
        } else {
            if (user != null)
                cometchatMessageHeader.user(user);
//            cometchatMessageHeader.hideAudioCallOption(false);
//            cometchatMessageHeader.hideVideoCallOption(false);
        }
        cometchatMessageHeader.titleFont(FontUtils.robotoMedium);
        cometchatMessageHeader.addListener("CometChatMessage", new CometChatMessagesHeader.OnEventListener() {
            @Override
            public void onBackPressed() {
                getActivity().onBackPressed();
            }
        });
//        rvChatListView.setLayoutManager(linearLayoutManager);


        CometChatMessageEvents.addListener("CometChatMessage", new CometChatMessageEvents() {

            @Override
            public void onMessageSent(BaseMessage message, int status) {
                baseMessage = message;
                if (status == MessageStatus.IN_PROGRESS) {
                    if (cometChatMessageList != null) {
                        soundManager.play(Sound.outgoingMessage);
                        cometChatMessageList.add(message);
                        cometChatMessageList.scrollToBottom();
                    }
                } else if (status == MessageStatus.SUCCESS) {
                    if (cometChatMessageList != null) {
                        cometChatMessageList.updateOptimisticMessage(message);
                        cometChatMessageList.scrollToBottom();
                    }
                }
            }


            @Override
            public void onMessageError(CometChatException e) {
                if (e.getCode().equalsIgnoreCase("ERROR_INTERNET_UNAVAILABLE")) {
                    CometChatSnackBar.show(context, cometChatMessageList,
                            context.getString(R.string.no_internet_available), CometChatSnackBar.ERROR);
                } else if (!e.getCode().equalsIgnoreCase("ERR_BLOCKED_BY_EXTENSION")) {
                    if (cometChatMessageList == null) {
                    } else {
                        if (baseMessage != null) {
                            baseMessage.setSentAt(-1);
                            cometChatMessageList.updateOptimisticMessage(baseMessage);
                        }
                    }
                } else if (cometChatMessageList != null) {
                    if (baseMessage != null)
                        cometChatMessageList.remove(baseMessage);
                }
            }

            @Override
            public void onMessageEdit(BaseMessage baseMessage, @MessageStatus int status) {
                if (status == MessageStatus.IN_PROGRESS) {
                    if (baseMessage != null && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                        composeBox.edit(baseMessage);
                        if (cometChatMessageList != null) {
                            cometChatMessageList.highLightMessage(baseMessage.getId());
                        }
                    }
                } else {
                    if (cometChatMessageList != null) {
                        cometChatMessageList.updateOptimisticMessage(baseMessage);
                        cometChatMessageList.scrollToBottom();
                    }
                }
            }

            @Override
            public void onMessageForward(BaseMessage baseMessage, CometChatMessageList cometChatMessageList) {
                cometChatMessageList.forwardMessage(baseMessage);
            }

            @Override
            public void onMessageDelete(BaseMessage baseMessage) {
                cometChatMessageList.deleteMessage(baseMessage);
            }

            @Override
            public void onReplyMessagePrivately(BaseMessage baseMessage, CometChatMessageList cometChatMessageList) {
                cometChatMessageList.replyMessagePrivately(baseMessage);
            }

            @Override
            public void onStartThread(BaseMessage baseMessage, CometChatMessageList cometChatMessageList) {

            }

            @Override
            public void onMessageReply(BaseMessage baseMessage, int status) {
                if (status == MessageStatus.IN_PROGRESS) {
                    composeBox.reply(baseMessage);
                }
            }

            @Override
            public void onMessageReact(BaseMessage message, Reaction emoji) {
                baseMessage = message;
                if (emoji.getName().equals("add_emoji")) {
                    CometChatReactionDialog reactionDialog = new CometChatReactionDialog();
                    reactionDialog.setOnEmojiClick(new OnReactionClickListener() {
                        @Override
                        public void onEmojiClicked(Reaction emojicon) {
                            sendReaction(emojicon);
                            reactionDialog.dismiss();
                        }
                    });
                    reactionDialog.show(((FragmentActivity) context).getSupportFragmentManager(), "ReactionDialog");
                } else {
                    sendReaction(emoji);
                }
            }

            @Override
            public void onLiveReaction(String type, Reaction emoji) {
                sendLiveReaction(emoji.getCode());
            }

            @Override
            public void onScrollStateChanged() {
                cometchatMessageHeader.setSelected(cometChatMessageList.getRecyclerView().canScrollVertically(-1));
            }
        });
        // Uses to fetch next list of messages if rvChatListView (RecyclerView) is scrolled in downward direction.

        if (type != null) {
            if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (userChatEnabled)
                    composeBox.setVisibility(View.VISIBLE);
                else
                    composeBox.setVisibility(View.GONE);
            } else if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                if (groupChatEnabled)
                    composeBox.setVisibility(View.VISIBLE);
                else
                    composeBox.setVisibility(View.GONE);
            }
        }

    }


    private void setComposeBoxListener() {
        CometChatComposer.addListener("Messages", new Events() {
            @Override
            public void onKeyboardMediaSelected(InputContentInfoCompat inputContentInfo) {
                Log.e(TAG, "onEditTextMediaSelected: Path=" + inputContentInfo.getLinkUri().getPath()
                        + "\nHost=" + inputContentInfo.getLinkUri().getFragment());
                String messageType = inputContentInfo.getLinkUri().toString().substring(inputContentInfo.getLinkUri().toString().lastIndexOf('.'));
                MediaMessage mediaMessage = new MediaMessage(Id, CometChatConstants.MESSAGE_TYPE_IMAGE, type);
                List<Attachment> attachments= new ArrayList<>();
                Attachment attachment = new Attachment();
                attachment.setFileUrl(inputContentInfo.getLinkUri().toString());
                attachment.setFileMimeType(inputContentInfo.getDescription().getMimeType(0));
                attachment.setFileExtension(messageType);
                attachment.setFileName(inputContentInfo.getDescription().getLabel().toString());
                attachments.add(attachment);
                mediaMessage.setAttachments(attachments);
                Log.e(TAG, "onClick: " + attachment);
                sendMediaMessage(mediaMessage);
            }


            @Override
            public void onAudioActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivityForResult(MediaUtils.openAudio(getActivity()), UIKitConstants.RequestCode.AUDIO);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UIKitConstants.RequestCode.AUDIO);
                }
            }

            @Override
            public void onCameraActionClicked() {
                if (Utils.hasPermissions(getContext(), CAMERA_PERMISSION)) {
                    startActivityForResult(MediaUtils.openCamera(getContext()), UIKitConstants.RequestCode.CAMERA);
                } else {
                    requestPermissions(CAMERA_PERMISSION, UIKitConstants.RequestCode.CAMERA);
                }
            }


            @Override
            public void onGalleryActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivityForResult(MediaUtils.openGallery(getActivity()), UIKitConstants.RequestCode.GALLERY);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UIKitConstants.RequestCode.GALLERY);
                }
            }

            @Override
            public void onFileActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivityForResult(MediaUtils.getFileIntent(UIKitConstants.IntentStrings.EXTRA_MIME_DOC), UIKitConstants.RequestCode.FILE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UIKitConstants.RequestCode.FILE);
                }
            }


            @Override
            public void onVoiceNoteComplete(String string) {
                if (string != null) {
                    File audioFile = new File(string);
                    sendMediaMessage(audioFile, CometChatConstants.MESSAGE_TYPE_AUDIO);
                }
            }

            @Override
            public void onLocationActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    locationUtils.initialize(context, new ExtensionResponseListener() {
                        @Override
                        public void OnResponseSuccess(Object var) {
                            JSONObject response = (JSONObject) var;
                            sendCustomMessage(UIKitConstants.IntentStrings.LOCATION, response);
                        }

                        @Override
                        public void OnResponseFailed(CometChatException e) {
                        }
                    });
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, UIKitConstants.RequestCode.LOCATION);
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                        checkBackgroundLocationPermissionAPI30(UIKitConstants.RequestCode.LOCATION);
                    }
                }
            }

            @Override
            public void onPollActionClicked() {
                PollsUtils.createPollDialog(context, Id, type);
            }

            @Override
            public void onStickerClicked() {
                stickerLayout.setVisibility(View.VISIBLE);
                Extensions.fetchStickers(new ExtensionResponseListener() {
                    @Override
                    public void OnResponseSuccess(Object var) {
                        JSONObject stickersJSON = (JSONObject) var;
                        stickersView.setData(Id, type, Extensions.extractStickersFromJSON(stickersJSON));
                    }

                    @Override
                    public void OnResponseFailed(CometChatException e) {
                        CometChatSnackBar.show(context, stickersView, CometChatError.localized(e), CometChatSnackBar.ERROR);
                    }
                });
            }

            @Override
            public void onWhiteboardClicked() {
                Extensions.callWhiteBoardExtension(Id, type, new ExtensionResponseListener() {
                    @Override
                    public void OnResponseSuccess(Object var) {
                        JSONObject jsonObject = (JSONObject) var;
                    }

                    @Override
                    public void OnResponseFailed(CometChatException e) {
                        CometChatSnackBar.show(context, cometChatMessageList, CometChatError.localized(e), CometChatSnackBar.ERROR);
                    }
                });
            }

            @Override
            public void onDocumentClicked() {
                Extensions.callWriteBoardExtension(Id, type, new ExtensionResponseListener() {
                    @Override
                    public void OnResponseSuccess(Object var) {
                        JSONObject jsonObject = (JSONObject) var;
                    }

                    @Override
                    public void OnResponseFailed(CometChatException e) {
                        CometChatSnackBar.show(context, cometChatMessageList, CometChatError.localized(e), CometChatSnackBar.ERROR);
                    }
                });
            }

            @Override
            public void onCustomUserAction(CometChatMessageTemplate template) {
                if (template != null) {
                    if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
                        template.getClickListener().onClick(user,context);
                    else
                        template.getClickListener().onClick(group,context);
                }
            }
        });
    }

    private void sendReaction(Reaction reaction) {

        JSONObject body = new JSONObject();
        try {
            body.put("msgId", baseMessage.getId());
            body.put("emoji", reaction.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CometChat.callExtension("reactions", "POST", "/v1/react", body,
                new CometChat.CallbackListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject responseObject) {
                        // ReactionModel added successfully.
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    //Start of LocationUtils

    /**
     * This method is used to check whether Background permission for location is enabled or not.
     *
     * @param backgroundLocationRequestCode
     * @author CometChat Inc
     */
    @TargetApi(30)
    private void checkBackgroundLocationPermissionAPI30(int backgroundLocationRequestCode) {
        if (Utils.hasPermissions(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            return;
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.background_location_permission_title)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION}
                                , backgroundLocationRequestCode);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        alertDialog.show();
    }

    /**
     * This method sends custom message based on parameter received
     *
     * @param customType
     * @param customData
     */
    private void sendCustomMessage(String customType, JSONObject customData) {
        CustomMessage customMessage;

        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_USER, customType, customData);
        else
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_GROUP, customType, customData);

        String pushNotificationMessage = "";
        if (customType.equalsIgnoreCase(UIKitConstants.IntentStrings.LOCATION))
            pushNotificationMessage = getString(R.string.shared_location);
        else if (customType.equalsIgnoreCase(UIKitConstants.IntentStrings.STICKERS))
            pushNotificationMessage = getString(R.string.shared_a_sticker);
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
                if (cometChatMessageList != null) {
                    cometChatMessageList.add(customMessage);
                    cometChatMessageList.scrollToBottom();
                }
                for (CometChatMessageEvents e : CometChatMessageEvents.messageEvents.values()) {
                    e.onMessageSent(customMessage, MessageStatus.SUCCESS);
                }
            }

            @Override
            public void onError(CometChatException e) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {

            case UIKitConstants.RequestCode.CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)

                    startActivityForResult(MediaUtils.openCamera(getActivity()), UIKitConstants.RequestCode.CAMERA);
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_camera_permission));
                break;
            case UIKitConstants.RequestCode.GALLERY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivityForResult(MediaUtils.openGallery(getActivity()), UIKitConstants.RequestCode.GALLERY);
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_storage_permission));
                break;
            case UIKitConstants.RequestCode.FILE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivityForResult(MediaUtils.getFileIntent(UIKitConstants.IntentStrings.EXTRA_MIME_DOC), UIKitConstants.RequestCode.FILE);
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_storage_permission));
                break;
            case UIKitConstants.RequestCode.LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationUtils.initialize(context, new ExtensionResponseListener() {
                        @Override
                        public void OnResponseSuccess(Object var) {
                            JSONObject response = (JSONObject) var;
                            sendCustomMessage(UIKitConstants.IntentStrings.LOCATION, response);
                        }

                        @Override
                        public void OnResponseFailed(CometChatException e) {
                        }
                    });
                } else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_location_permission));
                break;
        }
    }

    private void showPermissionSnackBar(View view, String message) {
        CometChatSnackBar.show(context, view, message, CometChatSnackBar.WARNING);
    }


    /**
     * Incase if user is blocked already, then this method is used to unblock the user .
     *
     * @see CometChat#unblockUsers(List, CometChat.CallbackListener)
     */
    private void unblockUser() {
        ArrayList<String> uids = new ArrayList<>();
        uids.add(Id);
        CometChat.unblockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                CometChatSnackBar.show(context, cometChatMessageList,
                        name + " " + getResources().getString(R.string.unblocked_successfully), CometChatSnackBar.SUCCESS);
                blockUserLayout.setVisibility(GONE);
                composeBox.setVisibility(View.VISIBLE);
                isBlockedByMe = false;
//                cometChatMessageList.reset();
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(context, cometChatMessageList, e.getMessage(), CometChatSnackBar.ERROR);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UIKitConstants.RequestCode.LEAVE_GROUP:
                    ((Activity) context).onBackPressed();
                    break;
                case UIKitConstants.RequestCode.AUDIO:
                    if (data != null) {
                        resultIntentCode = UIKitConstants.RequestCode.AUDIO;
                        File file = MediaUtils.getRealPath(getContext(), data.getData(), false);
                        ContentResolver cr = getActivity().getContentResolver();
                        sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_AUDIO);
                    }
                    break;
                case UIKitConstants.RequestCode.GALLERY:
                    if (data != null) {
                        resultIntentCode = UIKitConstants.RequestCode.GALLERY;
                        File file = MediaUtils.getRealPath(getContext(), data.getData(), false);
                        ContentResolver cr = getActivity().getContentResolver();
                        String mimeType = cr.getType(data.getData());
                        if (mimeType != null && mimeType.contains("image")) {
                            if (file.exists())
                                sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_IMAGE);
                            else
                                CometChatSnackBar.show(context, cometChatMessageList, getString(R.string.file_not_exist), CometChatSnackBar.WARNING);
                        } else if (mimeType != null && mimeType.contains("video")) {
                            if (file.exists())
                                sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_VIDEO);
                            else
                                CometChatSnackBar.show(context, cometChatMessageList, getString(R.string.file_not_exist), CometChatSnackBar.WARNING);
                        } else {
                            if (file.exists())
                                sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_FILE);
                            else
                                CometChatSnackBar.show(context, cometChatMessageList, getString(R.string.file_not_exist), CometChatSnackBar.WARNING);
                        }
                    }

                    break;
                case UIKitConstants.RequestCode.CAMERA:
                    File file;
                    resultIntentCode = UIKitConstants.RequestCode.CAMERA;
                    if (Build.VERSION.SDK_INT >= 29) {
                        file = MediaUtils.getRealPath(getContext(), MediaUtils.uri, false);
                    } else {
                        file = new File(MediaUtils.pictureImagePath);
                    }
                    if (file.exists())
                        sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_IMAGE);
                    else
                        Snackbar.make(cometChatMessageList, R.string.file_not_exist, Snackbar.LENGTH_LONG).show();

                    break;
                case UIKitConstants.RequestCode.FILE:
                    if (data != null) {
                        resultIntentCode = UIKitConstants.RequestCode.FILE;
                        sendMediaMessage(MediaUtils.getRealPath(getActivity(), data.getData(), false), CometChatConstants.MESSAGE_TYPE_FILE);
                    }
                    break;
                case UIKitConstants.RequestCode.BLOCK_USER:
                    name = data.getStringExtra("");
                    break;
                case UIKitConstants.RequestCode.LOCATION:
                    locationUtils.initialize(context, new ExtensionResponseListener() {
                        @Override
                        public void OnResponseSuccess(Object var) {
                            JSONObject response = (JSONObject) var;
                            sendCustomMessage(UIKitConstants.IntentStrings.LOCATION, response);
                        }

                        @Override
                        public void OnResponseFailed(CometChatException e) {

                        }
                    });
            }
        }

    }

    /**
     * This method is used to send media messages to other users and group
     *
     * @param mediaMessage is a MediaMessageObject
     */
    private void sendMediaMessage(MediaMessage mediaMessage) {
        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                if (cometChatMessageList != null) {
                    cometChatMessageList.add(mediaMessage);
                    cometChatMessageList.scrollToBottom();
                }
                for (CometChatMessageEvents e : CometChatMessageEvents.messageEvents.values()) {
                    e.onMessageSent(mediaMessage, MessageStatus.SUCCESS);
                }
            }

            @Override
            public void onError(CometChatException e) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method is used to send media messages to other users and group.
     *
     * @param file     is an object of File which is been sent within the message.
     * @param filetype is a string which indicate a type of file been sent within the message.
     * @see CometChat#sendMediaMessage(MediaMessage, CometChat.CallbackListener)
     * @see MediaMessage
     */
    private void sendMediaMessage(File file, String filetype) {
        MediaMessage mediaMessage;

        if (CometChatConstants.RECEIVER_TYPE_USER.equalsIgnoreCase(type))
            mediaMessage = new MediaMessage(Id, file, filetype, CometChatConstants.RECEIVER_TYPE_USER);
        else
            mediaMessage = new MediaMessage(Id, file, filetype, CometChatConstants.RECEIVER_TYPE_GROUP);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("path", file.getAbsolutePath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mediaMessage.setMetadata(jsonObject);
        mediaMessage.setSentAt(System.currentTimeMillis() / 1000);
        mediaMessage.setMuid("" + System.currentTimeMillis());
        mediaMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
        mediaMessage.setSender(loggedInUser);

        if (cometChatMessageList != null) {
            cometChatMessageList.add(mediaMessage);
            cometChatMessageList.scrollToBottom();
        }
        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                Log.d(TAG, "sendMediaMessage onSuccess: " + mediaMessage.toString());
                if (cometChatMessageList != null) {
                    cometChatMessageList.updateOptimisticMessage(mediaMessage);
//                    rvSmartReply.setVisibility(GONE);
                }
                for (CometChatMessageEvents e : CometChatMessageEvents.messageEvents.values()) {
                    e.onMessageSent(mediaMessage, MessageStatus.SUCCESS);
                }
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
                if (cometChatMessageList != null) {
                    mediaMessage.setSentAt(-1);
                    cometChatMessageList.updateOptimisticMessage(mediaMessage);
                }
                if (getActivity() != null) {
                    CometChatSnackBar.show(context, cometChatMessageList, e.getMessage(), CometChatSnackBar.ERROR);
                }
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
            public void onSuccess(Group group_) {
                group = group_;
                if (group.getOwner().equals(loggedInUser.getUid()))
                    groupOwnerId = loggedInUser.getUid();
                loggedInUserScope = group.getScope();
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
            public void onSuccess(User user_) {
                user = user_;
                if (getActivity() != null) {
                    if (user.isBlockedByMe()) {
                        isBlockedByMe = true;

                        blockedUserName.setText(getString(R.string.you_ve_blocked) + user.getName());
                        unblockBtn.setVisibility(View.VISIBLE);
                        blockUserLayout.setVisibility(View.VISIBLE);
                        composeBox.setVisibility(GONE);
                    } else if (user.isHasBlockedMe()) {
                        isBlockedByMe = true;
                        blockedUserName.setText(getString(R.string.you_have_blocked_by) + user.getName());
                        blockUserLayout.setVisibility(View.VISIBLE);
                        unblockBtn.setVisibility(GONE);
                        composeBox.setVisibility(GONE);
                    } else {
                        isBlockedByMe = false;
                        composeBox.setVisibility(View.VISIBLE);
                        blockUserLayout.setVisibility(GONE);
                        avatarUrl = user.getAvatar();
                        profileLink = user.getLink();
                    }
                    name = user.getName();
                    Log.d(TAG, "onSuccess: " + user.toString());
                }

            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberLeft(Action action, User user, Group group) {
                if (user.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            }

            @Override
            public void onGroupMemberKicked(Action action, User user, User user1, Group group) {
                if (user.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User user, User user1, Group group) {
                if (user.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            }
        });
        CometChatGroupEvents.addGroupListener("messages", new CometChatGroupEvents() {
            @Override
            public void onGroupMemberLeave(User leftUser, Group leftGroup) {
                if (leftUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            }

            @Override
            public void onGroupMemberBan(User bannedUser, User bannedBy, Group group) {
                if (bannedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            }

            @Override
            public void onGroupMemberKick(User kickedUser, User kickedBy, Group group) {
                if (kickedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            }

            @Override
            public void onGroupDelete(Group group) {
                ((Activity) context).onBackPressed();
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
            public void onTransientMessageReceived(TransientMessage transientMessage) {
                if (transientMessage.getSender().getUid().equalsIgnoreCase(Id)) {
                    if (transientMessage.getData() != null) {
                        setLiveReaction(composeBox.getLiveReactionIcon());
                    }
                }
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


    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        removeMessageListener();
        removeGroupListener();
        composeBox.sendTypingIndicator(true);
    }

    private void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (!(resultIntentCode == UIKitConstants.RequestCode.GALLERY ||
                resultIntentCode == UIKitConstants.RequestCode.CAMERA ||
                resultIntentCode == UIKitConstants.RequestCode.FILE ||
                resultIntentCode == UIKitConstants.RequestCode.AUDIO)) {

//            messagesRequest = null;
//            messageAdapter = null;
//            fetchMessage();
        }

        if (composeBox != null) {
            composeBox.hideActionSheet();
        }

        if (type != null) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                addGroupListener();
//                if (isGroupActionMessagesVisible)
//                    addGroupListener();
                new Thread(this::getGroup).start();
            } else {
                new Thread(this::getUser).start();
            }
        }
        addMessageListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btn_unblock_user) {
            unblockUser();
        }

    }


    //Live Reactions
    private void sendLiveReaction(int iconId) {
        try {
            JSONObject metaData = new JSONObject();
            metaData.put("reaction", "heart");
            metaData.put("type", "live_reaction");
            TransientMessage transientMessage = new TransientMessage(Id, type, metaData);
            CometChat.sendTransientMessage(transientMessage);
            setLiveReaction(iconId);
        } catch (Exception e) {
            Log.e(TAG, "sendLiveReaction: " + e.getMessage());
        }
    }

    private void stopLiveReaction() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(container, "alpha", 0.2f);
        animator.setDuration(800);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (imageView != null)
                    imageView.clearAnimation();
                container.removeAllViews();
                count = 0;
            }
        });
    }

    private void setLiveReaction(int resId) {
        count++;
        container.setAlpha(1.0f);
        animateLiveReaction(resId);
        if (count == 1)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopLiveReaction();
                }
            }, 1500);
    }

    private void animateLiveReaction(final int resId) {
        imageView = new ImageView(getContext());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
        layoutParams.rightMargin = 16;
        imageView.setLayoutParams(layoutParams);
        container.addView(imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if (bitmap != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.2f), (int) (bitmap.getHeight() * 0.2f), false);
            imageView.setImageBitmap(scaledBitmap);
        }

        animation = ObjectAnimator.ofFloat(imageView, "translationY", -700f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setDuration(500);
        animation.start();

    }

    public void background(Drawable drawable) {
        if (view != null)
            view.setBackground(drawable);
    }

    public void background(@ColorInt int color) {
        if (view != null)
            view.setBackgroundColor(color);
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        for (CometChatConfigurations config : configurations) {
            setConfiguration(config);
        }
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        if (configuration instanceof CometChatMessagesConfigurations) {
            CometChatMessagesConfigurations messageConfigurations =
                    (CometChatMessagesConfigurations) configuration;
            /**
             * Here we will set the CometchatMessages Configurations i.e to enable or disable
             * chat for 1-1 or group chats
             */

            userChatEnabled = messageConfigurations.isUserChatEnabled();
            groupChatEnabled = messageConfigurations.isGroupChatEnabled();
            if (messageConfigurations.getBackgroundDrawable() != null)
                background(messageConfigurations.getBackgroundDrawable());
        } else if (configuration instanceof ComposerConfiguration) {
            /**
             * Here we will set the CometchatComposer Configurations
             */
            if (composeBox != null)
                composeBox.setConfiguration(configuration);

        } else if (configuration instanceof MessageListConfiguration ||
                configuration instanceof MessageBubbleConfiguration) {
            /**
             * Here we will set the CometchatMessageList Configurations
             */
            if (cometChatMessageList != null)
                cometChatMessageList.setConfiguration(configuration);
        } else if (configuration instanceof HeaderConfiguration) {
            if (cometchatMessageHeader != null)
                cometchatMessageHeader.setConfiguration(configuration);
        }
    }
}
