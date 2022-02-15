package com.cometchatworkspace.components.messages.composer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;
import com.cometchatworkspace.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.secondaryComponents.CometChatSnackBar;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.CometChatActionSheet;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.components.messages.composer.listener.Events;
import com.cometchatworkspace.components.messages.composer.audioVisualizer.AudioRecordView;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchatworkspace.resources.utils.keyboard_utils.KeyBoardUtils;
import com.cometchatworkspace.resources.utils.pattern_utils.PatternUtils;
import com.cometchatworkspace.resources.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class CometChatComposer extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = CometChatComposer.class.getName();

    private final User loggedInUser = CometChat.getLoggedInUser();

    private boolean isSendEmoji = true;

    private boolean isMicrophoneHidden = false;

    private OnItemClickListener liveReactionClick;
    private ActionItem galleryActionItem,cameraActionItem,audioActionItem,fileActionItem,
            pollActionItem,stickerActionItem,documentActionItem,whiteboardActionItem,locationActionItem;
    private boolean typingIndicatorEnabled = true;


    @IntDef({MessageStatus.IN_PROGRESS, MessageStatus.SUCCESS})

    public @interface MessageStatus {
        int IN_PROGRESS = 0;
        int SUCCESS = 1;
    }

    private AudioRecordView audioRecordView;

    private MediaRecorder mediaRecorder;

    private MediaPlayer mediaPlayer;

    private Runnable timerRunnable;

    private final Handler seekHandler = new Handler(Looper.getMainLooper());

    private Timer timer = new Timer();

    private CometChatActionSheet cometChatActionSheet;

    private String audioFileNameWithPath;

    private boolean isOpen,isRecording,isPlaying,voiceMessage;

    private ImageView ivSend, ivAttachment, ivMicrophone,ivDelete;

    private SeekBar voiceSeekbar;

    private Chronometer recordTime;

    private CometChatEditText etComposeBox;

    private RelativeLayout composeBox;

    private MaterialCardView flBox;

    private RelativeLayout voiceMessageLayout;

    private RelativeLayout editMessageLayout;
    private TextView editMessageTitle,editMessageSubTitle;
    private ImageView editMessageCloseIv;

    private static final HashMap<String, Events> composeListener = new HashMap<>();

    private RelativeLayout replyMessageLayout;
    private TextView replyTitle;
    private TextView replyMessage;
    private ImageView replyMedia;
    private ImageView replyClose;

    private Context context;

    private String placeHolder;

    private Drawable sendIcon, attachmentIcon, microphoneIcon;
    private int sendIconColor, attachmentIconColor, microphoneIconColor, background,
            placeHolderTextColor;

    private float cornerRadius;

    public ImageView liveReactionBtn;

    private String Id,type;

    private boolean isEdit;

    private boolean isReply;

    private BaseMessage linkedMessage = null;

    public boolean isGalleryVisible,isAudioVisible,isCameraVisible,
            isFileVisible,isLocationVisible,isPollVisible,isStickerVisible,
            isWhiteBoardVisible, isWriteBoardVisible, isGroupCallVisible;

    public FontUtils fontUtils;

    private Timer typingTimer = new Timer();

    private int liveReactionIcon;

    public CometChatComposer(Context context) {
        super(context);
        if (!isInEditMode())
            initViewComponent(context,null,-1,-1);
    }

    public CometChatComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            initViewComponent(context,attrs,-1,-1);
    }

    public CometChatComposer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            initViewComponent(context,attrs,defStyleAttr,-1);
    }

    private void initViewComponent(Context context,AttributeSet attributeSet,int defStyleAttr,int defStyleRes){
        fontUtils = FontUtils.getInstance(context);
        View view =View.inflate(context, R.layout.cometchat_compose_box,null);
        liveReactionIcon = R.drawable.heart_reaction;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.Composer, 0, 0);
        sendIcon = a.getDrawable(R.styleable.Composer_sendIcon);
        sendIconColor = a.getColor(R.styleable.Composer_sendIconTint,getResources().getColor(R.color.colorPrimary));
        attachmentIcon = a.getDrawable(R.styleable.Composer_attachmentIcon);
        attachmentIconColor = a.getColor(R.styleable.Composer_attachmentIconTint,getResources().getColor(R.color.grey));
        placeHolder = a.getString(R.styleable.Composer_placeholder);
        microphoneIcon = a.getDrawable(R.styleable.Composer_microphoneIcon);
        microphoneIconColor = a.getColor(R.styleable.Composer_microphoneIconTint,getResources().getColor(R.color.grey));
        background = a.getColor(R.styleable.Composer_composerBackground,0);
        cornerRadius = a.getDimension(R.styleable.Composer_corner_radius,24f);
        placeHolderTextColor = a.getColor(R.styleable.Composer_placeholderColor,0);
        addView(view);

        this.context=context;

        ViewGroup viewGroup=(ViewGroup)view.getParent();
        viewGroup.setClipChildren(false);

        mediaPlayer = new MediaPlayer();
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.isMusicActive())
        {
            audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        stopRecording(true);
                    }
                }
            }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        liveReactionBtn = view.findViewById(R.id.live_reaction_btn);
        composeBox=this.findViewById(R.id.message_box);
        flBox=this.findViewById(R.id.flBox);
        ivMicrophone =this.findViewById(R.id.ivMic);
        ivDelete=this.findViewById(R.id.ivDelete);
        audioRecordView=this.findViewById(R.id.record_audio_visualizer);
        voiceMessageLayout=this.findViewById(R.id.voiceMessageLayout);
        recordTime=this.findViewById(R.id.record_time);
        voiceSeekbar=this.findViewById(R.id.voice_message_seekbar);
        ivAttachment =this.findViewById(R.id.ivArrow);
        etComposeBox=this.findViewById(R.id.etComposeBox);
        ivSend=this.findViewById(R.id.ivSend);

        //EditMessageLayout
        editMessageLayout = view.findViewById(R.id.editMessageLayout);
        editMessageTitle = view.findViewById(R.id.tv_message_layout_title);
        editMessageSubTitle = view.findViewById(R.id.tv_message_layout_subtitle);
        editMessageCloseIv = view.findViewById(R.id.iv_message_close);
        editMessageCloseIv.setOnClickListener(this);

        //ReplyMessageLayout
        replyMessageLayout = view.findViewById(R.id.replyMessageLayout);
        replyTitle = view.findViewById(R.id.tv_reply_layout_title);
        replyMessage = view.findViewById(R.id.tv_reply_layout_subtitle);
        replyMedia = view.findViewById(R.id.iv_reply_media);
        replyClose = view.findViewById(R.id.iv_reply_close);
        replyClose.setOnClickListener(this);

        attachmentIcon(attachmentIcon);
        attachmentIconTint(attachmentIconColor);
        sendIcon(sendIcon);
        sendIconTint(sendIconColor);
        placeHolder(placeHolder);
        placeHolderColor(placeHolderTextColor);
        textColor(placeHolderTextColor);
        background(background);
        cornerRadius(cornerRadius);
        microphoneIcon(microphoneIcon);
        microphoneIconTint(microphoneIconColor);
        ivAttachment.setOnClickListener(this);
        ivSend.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivMicrophone.setOnClickListener(this);


        cometChatActionSheet = new CometChatActionSheet();
        cometChatActionSheet.setEventListener(new CometChatActionSheet.CometChatActionSheetListener() {
            @Override
            public void onActionItemClick(ActionItem item) {
                    if (item.getTitle().equalsIgnoreCase(context.getString(R.string.create_poll)))
                        for (Events listener : composeListener.values())
                            listener.onPollActionClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.photo_video_library)))
                        for (Events listener : composeListener.values())
                            listener.onGalleryActionClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.take_a_photo)))
                        for (Events listener : composeListener.values())
                            listener.onCameraActionClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.send_files)))
                        for (Events listener : composeListener.values())
                            listener.onFileActionClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.send_audio_files)))
                        for (Events listener : composeListener.values())
                            listener.onAudioActionClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.share_location)))
                        for (Events listener : composeListener.values())
                            listener.onLocationActionClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.share_whiteboard)))
                        for (Events listener : composeListener.values())
                            listener.onWhiteboardClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.share_writeboard)))
                        for (Events listener : composeListener.values())
                            listener.onDocumentClicked();
                    else if (item.getTitle().equalsIgnoreCase(context.getString(R.string.send_sticker)))
                        for (Events listener : composeListener.values())
                            listener.onStickerClicked();
                    else {
                        CometChatMessageTemplate template =
                                CometChatMessagesConfigurations.getMessageTemplateById(item.getId());
                        for (Events listener : composeListener.values())
                            listener.onCustomUserAction(template);
                    }
                    hideActionSheet();
            }

        });

        etComposeBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (typingIndicatorEnabled)
                    sendTypingIndicator(charSequence.length() <= 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    hideSend(false);
                    ivMicrophone.setVisibility(View.GONE);
                } else {
                    hideSend(true);
                    if (!isMicrophoneHidden)
                        ivMicrophone.setVisibility(View.VISIBLE);
                }
                if (typingTimer == null) {
                    typingTimer = new Timer();
                }
                endTypingTimer();
            }
        });

        etComposeBox.setMediaSelected(new CometChatEditText.OnEditTextMediaListener() {
            @Override
            public void OnMediaSelected(InputContentInfoCompat i) {
                for (Events composeActionListener : composeListener.values())
                    composeActionListener.onEditTextMediaSelected(i);
            }
        });
//        InputConnection ic = etComposeBox.onCreateInputConnection(new EditorInfo());
//        EditorInfoCompat.setContentMimeTypes(new EditorInfo(),
//                new String [] {"image/png","image/gif"});

//        if (Utils.isDarkMode(context)) {
//            composeBox.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
//            ivMicrophone.setImageDrawable(getResources().getDrawable(R.drawable.ic_microphone_white_selected));
//            editMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border_dark));
//            replyMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border_dark));
//            flBox.setCardBackgroundColor(getResources().getColor(R.color.textColorWhite));
//            etComposeBox.setTextColor(getResources().getColor(R.color.textColorWhite));
//            ivAttachment.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
//            ivSend.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
//        } else {
//            composeBox.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
//            ivMicrophone.setImageDrawable(getResources().getDrawable(R.drawable.ic_microphone_grey_selected));
//            editMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border));
//            replyMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border));
//            etComposeBox.setTextColor(getResources().getColor(R.color.primaryTextColor));
//            ivSend.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
//            flBox.setCardBackgroundColor(getResources().getColor(R.color.light_grey));
//            ivAttachment.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
//        }
        if (CometChatTheme.Composer.sendIconTint!=0) {
            int settingsColor = CometChatTheme.Composer.sendIconTint;
            ivSend.setImageTintList(ColorStateList.valueOf(getResources().getColor(settingsColor)));
        }


        liveReactionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setTag(R.string.message,liveReactionIcon);
                liveReactionClick.OnItemClick(liveReactionIcon,0);
            }
        });

        galleryActionItem = new ActionItem(context.getString(R.string.photo_video_library),
                R.drawable.ic_image_library);
        cameraActionItem = new ActionItem(context.getString(R.string.take_a_photo),
                R.drawable.ic_camera);
        audioActionItem = new ActionItem(context.getString(R.string.send_audio_files),
                R.drawable.ic_audio);
        fileActionItem = new ActionItem(context.getString(R.string.send_files),
                R.drawable.ic_file_upload);
        locationActionItem = new ActionItem(context.getString(R.string.share_location),
                R.drawable.ic_location);
        whiteboardActionItem = new ActionItem(context.getString(R.string.share_whiteboard),
                R.drawable.ic_collaborative_whiteboard);
        documentActionItem = new ActionItem(context.getString(R.string.share_writeboard),
                R.drawable.ic_collaborative_document);
        pollActionItem = new ActionItem(context.getString(R.string.create_poll),
                R.drawable.ic_polls);
        stickerActionItem = new ActionItem(context.getString(R.string.send_sticker),
                R.drawable.ic_sticker);

        fetchMessageFilterConfiguration();
        checkIfAttachmentIsVisible();
        a.recycle();
    }

    private void checkIfAttachmentIsVisible() {
        if (!isGroupCallVisible &&
                !isPollVisible &&
                !isFileVisible &&
                !isGalleryVisible &&
                !isAudioVisible &&
                !isLocationVisible &&
                !isStickerVisible &&
                !isWhiteBoardVisible && !isWriteBoardVisible) {
            ivAttachment.setVisibility(GONE);
        }
    }

    public void attachmentIcon(Drawable icon) {
        if (icon!=null && ivAttachment!=null)
            ivAttachment.setImageDrawable(icon);
    }

    public void attachmentIcon(int icon) {
        if (icon!=0 && ivAttachment !=null)
            ivAttachment.setImageResource(icon);
    }

    public void attachmentIconTint(@ColorInt int color) {
        if (color!=0 && ivAttachment !=null)
            ivAttachment.setImageTintList(ColorStateList.valueOf(color));
    }

    public void hideAttachment(boolean b) {
        if (ivAttachment !=null) {
            if (b)
                ivAttachment.setVisibility(View.GONE);
            else
                ivAttachment.setVisibility(View.VISIBLE);
        }
    }

    public void sendIcon(Drawable sendIcon) {
        if (sendIcon!=null && ivSend!=null)
            ivSend.setImageDrawable(sendIcon);
    }

    public void sendIcon(int sendIcon) {
        if (sendIcon!=0 && ivSend!=null)
            ivSend.setImageResource(sendIcon);
    }

    public void sendIconTint(@ColorInt int color) {
        if (color!=0 && ivSend!=null)
            ivSend.setImageTintList(ColorStateList.valueOf(color));
    }

    private void hideSend(boolean b) {
        if (ivSend!=null) {
            if (b) {
                ivSend.setVisibility(GONE);
            } else
                ivSend.setVisibility(VISIBLE);
        }
    }

    public void microphoneIcon(Drawable micIcon) {
        if (micIcon!=null && ivMicrophone!=null)
            ivMicrophone.setImageDrawable(micIcon);
    }
    public void microphoneIcon(int micIcon) {
        if (micIcon!=0 && ivMicrophone !=null)
            ivMicrophone.setImageResource(micIcon);
    }

    public void microphoneIconTint(@ColorInt int color) {
        if (color!=0 && ivMicrophone !=null)
            ivMicrophone.setImageTintList(ColorStateList.valueOf(color));
    }

    public void hideMicrophone(boolean b) {
        isMicrophoneHidden = b;
        if (b) {
            ivMicrophone.setVisibility(GONE);
        } else {
            ivMicrophone.setVisibility(VISIBLE);
        }
    }

    public void placeHolder(String placeHolder) {
        if (etComposeBox!=null && placeHolder!=null)
            etComposeBox.setHint(placeHolder);
    }

    public void placeHolderColor(@ColorInt int color) {
        if (etComposeBox!=null && color!=0)
            etComposeBox.setHintTextColor(color);
    }

    public void placeHolderFont(String font) {
        if (etComposeBox!=null)
            etComposeBox.setTypeface(fontUtils.getTypeFace(font));
    }
    public void textColor(@ColorInt int color) {
        if (color!=0 && etComposeBox!=null)
            etComposeBox.setTextColor(color);
    }

    public void cornerRadius(float radius) {
        flBox.setRadius(radius);
    }

    public void background(@ColorInt int color) {
        if (color!=0 && flBox!=null) {
            flBox.setCardBackgroundColor(color);
        }
    }

    public void liveReactionIcon(int icon) {
        liveReactionIcon = icon;
        if (liveReactionBtn!=null && icon!=0) {
            liveReactionBtn.setImageResource(icon);
        }
    }

    public int getLiveReactionIcon() {
        return liveReactionIcon;
    }
    public void onLiveReactionClick(OnItemClickListener clickListener) {
        liveReactionClick = clickListener;
    }

    public void hideLiveReaction(boolean b) {
        if (liveReactionBtn!=null) {
            if (b)
                liveReactionBtn.setVisibility(View.GONE);
            else
                liveReactionBtn.setVisibility(View.VISIBLE);
        }
    }

    //End of Customization Method

    //Data Method
    public void setUser(User user) {
        this.Id = user.getUid();
        this.type = CometChatConstants.RECEIVER_TYPE_USER;
    }

    public void setGroup(Group group) {
        this.Id = group.getGuid();
        this.type = CometChatConstants.RECEIVER_TYPE_GROUP;
    }

    public String getText() {
        return etComposeBox.getText().toString().trim();
    }

    public void draft(String text) {
        etComposeBox.setText(text);
    }

    /**
     * This method is used to show Reply Message View within ComposeBox which includes previous
     * baseMessage passed within it.
     *
     * @param baseMessage is a replied message on which message will be sent.
     */

    public void reply(BaseMessage baseMessage) {
        if (baseMessage != null) {
            linkedMessage = baseMessage;
            isReply = true;
            replyTitle.setText(baseMessage.getSender().getName());
            replyMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            replyMedia.setVisibility(View.VISIBLE);
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
//                boolean isSentimentNegative = Extensions.checkSentiment(baseMessage);
//                if (isSentimentNegative) {
//                    replyMessage.setBackgroundColor(getResources().getColor(R.color.red));
//                    replyMessage.setText(getString(R.string.sentiment_content));
//                }
//                else {
//                    replyMessage.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                replyMessage.setText(((TextMessage) baseMessage).getText());
//                }
                replyMedia.setVisibility(GONE);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                replyMessage.setText(getResources().getString(R.string.shared_a_image));
//                boolean isImageSafe = Extensions.getImageModeration(context,baseMessage);
//                if (isImageSafe)
                Glide.with(context).load(((MediaMessage) baseMessage).getAttachment().getFileUrl()).into(replyMedia);
//                else {
//                    replyMedia.setImageResource(R.drawable.ic_privacy);
//                }
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
                String messageStr = String.format(getResources().getString(R.string.shared_a_audio),
                        Utils.getFileSize(((MediaMessage) baseMessage).getAttachment().getFileSize()));
                replyMessage.setText(messageStr);
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_audio, 0, 0, 0);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                replyMessage.setText(getResources().getString(R.string.shared_a_video));
                Glide.with(context).load(((MediaMessage) baseMessage).getAttachment().getFileUrl()).into(replyMedia);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                String messageStr = String.format(getResources().getString(R.string.shared_a_file),
                        Utils.getFileSize(((MediaMessage) baseMessage).getAttachment().getFileSize()));
                replyMessage.setText(messageStr);
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_upload, 0, 0, 0);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
                try {
                    JSONObject jsonObject = ((CustomMessage) baseMessage).getCustomData();
                    String messageStr = String.format(context.getString(R.string.shared_location),
                            Utils.getAddress(context, jsonObject.getDouble("latitude"),
                                    jsonObject.getDouble("longitude")));
                    replyMessage.setText(messageStr);
                }catch (Exception e) {
                    Log.e(TAG, "replyMessageError: "+e.getMessage() );
                }
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.STICKERS)) {
                replyMessage.setText(getResources().getString(R.string.shared_a_sticker));
                try {
                    Glide.with(context).load(((CustomMessage) baseMessage).getCustomData().getString("url")).into(replyMedia);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.POLLS)) {
                try {
                    JSONObject jsonObject = ((CustomMessage) baseMessage).getCustomData();
                    String messageStr = String.format(context.getString(R.string.shared_a_polls),jsonObject.getString("question"));
                    replyMessage.setText(messageStr);
                }catch (Exception e) {
                    Log.e(TAG, "replyMessageError: "+e.getMessage() );
                }
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                replyMessage.setText(context.getString(R.string.shared_a_whiteboard));
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collaborative_whiteboard, 0, 0, 0);

            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                replyMessage.setText(context.getString(R.string.shared_a_writeboard));
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collaborative_document, 0, 0, 0);

            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.GROUP_CALL)) {
                replyMessage.setText(context.getString(R.string.has_shared_group_call));
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_video_call_selected,0,0,0);
            }
            ivMicrophone.setVisibility(GONE);
            ivSend.setVisibility(View.VISIBLE);
            replyMessageLayout.setVisibility(View.VISIBLE);

        }
    }

    /**
     * This method is used to show Edit message View within ComposeBox which includes the
     * original baseMessage passed within parameter of method.
     *
     * @param baseMessage is a original message which will be edited.
     */
    public void edit(BaseMessage baseMessage) {
        linkedMessage = baseMessage;
        isEdit = true;
        editMessageTitle.setText(getResources().getString(R.string.edit_message));
        editMessageSubTitle.setText(((TextMessage) baseMessage).getText());
        ivMicrophone.setVisibility(GONE);
        ivSend.setVisibility(View.VISIBLE);
        editMessageLayout.setVisibility(View.VISIBLE);
        etComposeBox.setText(((TextMessage) baseMessage).getText());
    }

    public static void addListener(String id, Events composeActionListener) {
        composeListener.put(id,composeActionListener);
    }

    public void hideActionSheet() {
        if(cometChatActionSheet!=null && cometChatActionSheet.getFragmentManager()!=null)
            cometChatActionSheet.dismiss();
    }


    /**
     * This method is used to send typing indicator to other users and groups.
     *
     * @param isEnd is boolean which is used to differentiate between startTyping &amp; endTyping Indicators.
     * @see com.cometchat.pro.core.CometChat#startTyping(TypingIndicator)
     * @see com.cometchat.pro.core.CometChat#endTyping(TypingIndicator)
     */
    public void sendTypingIndicator(boolean isEnd) {
        if (isEnd) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                CometChat.endTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_USER));
            } else {
                CometChat.endTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_GROUP));
            }
        } else {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                CometChat.startTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_USER));
            } else {
                CometChat.startTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_GROUP));
            }
        }
    }

    private void endTypingTimer() {
        if (typingTimer!=null) {
            typingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendTypingIndicator(true);
                }
            }, 2000);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.ivDelete) {
            stopRecording(true);
            stopPlayingAudio();
            voiceMessageLayout.setVisibility(GONE);
            etComposeBox.setVisibility(View.VISIBLE);
            ivAttachment.setVisibility(View.VISIBLE);
            if (!isMicrophoneHidden)
                ivMicrophone.setVisibility(View.VISIBLE);
            ivMicrophone.setImageDrawable(getResources().getDrawable(R.drawable.ic_microphone_grey_selected));
            isPlaying = false;
            isRecording = false;
            voiceMessage = false;
            ivDelete.setVisibility(GONE);
            ivSend.setVisibility(View.GONE);
        }
        if (view.getId()==R.id.ivSend){
            if (!voiceMessage) {
                sendTextMessage(linkedMessage);
            } else {
                for (Events composeActionListener : composeListener.values()) {
                    composeActionListener.onVoiceNoteComplete(audioFileNameWithPath);
                }
                audioFileNameWithPath = "";
                voiceMessageLayout.setVisibility(GONE);
                etComposeBox.setVisibility(View.VISIBLE);
                ivDelete.setVisibility(GONE);
                ivSend.setVisibility(GONE);
                ivAttachment.setVisibility(View.VISIBLE);
                if (!isMicrophoneHidden)
                    ivMicrophone.setVisibility(View.VISIBLE);
                isRecording = false;
                isPlaying = false;
                voiceMessage = false;
                ivMicrophone.setImageResource(R.drawable.ic_microphone_grey_selected);
            }

        }
        if(view.getId()==R.id.ivArrow) {
//            if (isOpen) {
//               closeActionContainer();
//            } else {
//                openActionContainer();
//            }

            FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            cometChatActionSheet.setLayoutMode(CometChatTheme.ActionSheetLayout.mode);
            cometChatActionSheet.show(fm, cometChatActionSheet.getTag());
        }
        if (view.getId()==R.id.ivMic) {
            if (Utils.hasPermissions(context, Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                if (isOpen) {
//                    closeActionContainer();
                }
                if (!isRecording) {
                    startRecord();
                    ivMicrophone.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop_24dp));
                    isRecording = true;
                    isPlaying = false;
                } else {
                    if (isRecording && !isPlaying) {
                        isPlaying = true;
                        stopRecording(false);
                        recordTime.stop();
                    }
                    ivMicrophone.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_24dp));
                    audioRecordView.setVisibility(GONE);
                    ivSend.setVisibility(View.VISIBLE);
                    ivDelete.setVisibility(View.VISIBLE);
                    voiceSeekbar.setVisibility(View.VISIBLE);
                    voiceMessage = true;
                    if (audioFileNameWithPath != null)
                        startPlayingAudio(audioFileNameWithPath);
                    else
                        Toast.makeText(getContext(), "No File Found. Please", Toast.LENGTH_LONG).show();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Activity)context).requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            UIKitConstants.RequestCode.RECORD);
                }
            }
        }
        if (view.getId()==R.id.iv_message_close) {
            isEdit = false;
            editMessageLayout.setVisibility(GONE);
            for (Events composeActionListener : composeListener.values())
                composeActionListener.onViewClosed();
        }

        if (view.getId()==R.id.iv_reply_close) {
            for (Events composeActionListener : composeListener.values())
                composeActionListener.onViewClosed();
//            if (cometChatMessageList !=null) {
//                cometChatMessageList.clearLongClickSelectedItem();
//            }
            isReply = false;
//            baseMessage = null;
            replyMessageLayout.setVisibility(GONE);
        }
    }


//    public void openActionContainer() {
//        ivArrow.setRotation(45f);
//        isOpen = true;
//        Animation rightAnimate = AnimationUtils.loadAnimation(getContext(), R.anim.animate_right_slide);
//        rlActionContainer.startAnimation(rightAnimate);
//        rlActionContainer.setVisibility(View.VISIBLE);
//    }
//
//    public void closeActionContainer() {
//        ivArrow.setRotation(0);
//        isOpen = false;
//        Animation leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.animate_left_slide);
//        rlActionContainer.startAnimation(leftAnim);
//        rlActionContainer.setVisibility(GONE);
//    }

    public void emojiSupport(boolean isSupported) {
        isSendEmoji = isSupported;
    }

    private void sendTextMessage(BaseMessage linkedMessage) {
        String message = etComposeBox.getText().toString().trim();
        if (!message.isEmpty()) {
            String filterEmojiMessage = PatternUtils.removeEmojiAndSymbol(message);
            etComposeBox.setText("");
            etComposeBox.setHint(context.getString(R.string.message));
            if (!isSendEmoji) {
                if (filterEmojiMessage.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Emoji Support is not enabled",
                                Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (isEdit) {
                editMessage(linkedMessage, message);
                editMessageLayout.setVisibility(GONE);
            } else if (isReply) {
                sendMessage(linkedMessage, message, true);
                replyMessageLayout.setVisibility(GONE);
            } else if (!message.isEmpty()) {
                sendMessage(null, message, false);
            }
        }
    }

    /**
     * This method is used to edit the message. This methods takes old message and change text of old
     * message with new message i.e String and update it.
     *
     * @param baseMessage is an object of BaseMessage, It is a old message which is going to be edited.
     * @param message     is String, It is a new message which will be replaced with text of old message.
     * @see TextMessage
     * @see BaseMessage
     * @see CometChat#editMessage(BaseMessage, CometChat.CallbackListener)
     */
    private void editMessage(BaseMessage baseMessage, String message) {

        isEdit = false;

        TextMessage textMessage;
        if (baseMessage.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            textMessage = new TextMessage(baseMessage.getReceiverUid(), message, CometChatConstants.RECEIVER_TYPE_USER);
        else
            textMessage = new TextMessage(baseMessage.getReceiverUid(), message, CometChatConstants.RECEIVER_TYPE_GROUP);
        textMessage.setId(baseMessage.getId());
        CometChat.editMessage(textMessage, new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage message) {
                for (Events composeActionListener : composeListener.values())
                    composeActionListener.onMessageSent(message,MessageStatus.SUCCESS);
//                if (cometChatMessageList != null) {
//                    Log.e(TAG, "onSuccess: " + message.toString());
//                    cometChatMessageList.updateMessage(message);
//                }
            }

            @Override
            public void onError(CometChatException e) {
                for (Events composeActionListener : composeListener.values())
                    composeActionListener.onMessageError(e);
            }
        });

    }

    /**
     * This method is used to send reply message by link previous message with new message.
     * It can also used to send new message by setting baseMessage as null and isReplyMessage as false.
     * @param baseMessage is a linked message
     * @param message is a String. It will be new message sent as reply.
     * @param isReplyMessage is a boolean used to identify if the message is reply message or not.
     */
    private void sendMessage(BaseMessage baseMessage, String message,boolean isReplyMessage) {
        isReply = false;
        try {
            TextMessage textMessage;
            if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
                textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_USER);
            else
                textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_GROUP);
            if (isReplyMessage) {
                JSONObject replyObject = new JSONObject();
                replyObject.put("reply-message", baseMessage.getRawMessage());
                textMessage.setMetadata(replyObject);
            }
            textMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
            textMessage.setSender(loggedInUser);
            textMessage.setMuid(System.currentTimeMillis()+"");

            for (Events listener : composeListener.values()) {
                listener.onMessageSent(textMessage,MessageStatus.IN_PROGRESS);
            }
//            if (cometChatMessageList != null) {
//                MediaUtils.playSendSound(context, R.raw.outgoing_message);
//                cometChatMessageList.add(textMessage);
//                cometChatMessageList.scrollToBottom();
//            }
//            isSmartReplyClicked=false;
//            rvSmartReply.setVisibility(GONE);
            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    for (Events listener : composeListener.values()) {
                        listener.onMessageSent(textMessage, MessageStatus.SUCCESS);
                    }
//                    if (cometChatMessageList != null) {
//                        MediaUtils.playSendSound(context, R.raw.outgoing_message);
//                        cometChatMessageList.updateOptimisticMessage(textMessage);
//                        cometChatMessageList.scrollToBottom();
//                    }
                }

                @Override
                public void onError(CometChatException e) {
                    if (e.getCode().equalsIgnoreCase("ERROR_INTERNET_UNAVAILABLE")) {
                        CometChatSnackBar.show(context, composeBox,
                                context.getString(R.string.no_internet_available), CometChatSnackBar.ERROR);
                    }
                    e.printStackTrace();
                    for (Events listener : composeListener.values()) {
                        listener.onMessageError(e);
                    }
//                    else if (!e.getCode().equalsIgnoreCase("ERR_BLOCKED_BY_EXTENSION")) {
//                        if (cometChatMessageList == null) {
//                            Log.e(TAG, "onError: MessageAdapter is null");
//                        } else {
//                            textMessage.setSentAt(-1);
//                            cometChatMessageList.updateOptimisticMessage(textMessage);
//                        }
//                    } else if (cometChatMessageList != null) {
//                        cometChatMessageList.remove(textMessage);
//                    }
                }
            });
        }catch (Exception e) {
            Log.e(TAG, "replyMessage: "+e.getMessage());
        }
    }

    private void startRecord() {
            etComposeBox.setVisibility(GONE);
            recordTime.setBase(SystemClock.elapsedRealtime());
            recordTime.start();
            ivAttachment.setVisibility(GONE);
            voiceSeekbar.setVisibility(GONE);
            voiceMessageLayout.setVisibility(View.VISIBLE);
            audioRecordView.recreate();
            audioRecordView.setVisibility(View.VISIBLE);
            startRecording();
    }

    private void startPlayingAudio(String path) {
        try {

            if (timerRunnable != null) {
                seekHandler.removeCallbacks(timerRunnable);
                timerRunnable = null;
            }

            mediaPlayer.reset();
            if (Utils.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } else {
                ((Activity)context).requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        UIKitConstants.RequestCode.READ_STORAGE);
            }

            final int duration = mediaPlayer.getDuration();
            voiceSeekbar.setMax(duration);
            recordTime.setBase(SystemClock.elapsedRealtime());
            recordTime.start();
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    int pos = mediaPlayer.getCurrentPosition();
                    voiceSeekbar.setProgress(pos);

                    if (mediaPlayer.isPlaying() && pos < duration) {
//                        audioLength.setText(Utils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                        seekHandler.postDelayed(this, 100);
                    } else {
                        seekHandler
                                .removeCallbacks(timerRunnable);
                        timerRunnable = null;
                    }
                }

            };
            seekHandler.postDelayed(timerRunnable, 100);
            mediaPlayer.setOnCompletionListener(mp -> {
                seekHandler
                        .removeCallbacks(timerRunnable);
                timerRunnable = null;
                mp.stop();
                recordTime.stop();
//                audioLength.setText(Utils.convertTimeStampToDurationTime(duration));
                voiceSeekbar.setProgress(0);
//                playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            });

        } catch (Exception e) {
            Log.e( "playAudioError: ",e.getMessage());
            stopPlayingAudio();
        }
    }


    private void stopPlayingAudio() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }
    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            audioFileNameWithPath = Utils.getOutputMediaFile(getContext());
            mediaRecorder.setOutputFile(audioFileNameWithPath);
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int currentMaxAmp = 0;
                    try {
                        currentMaxAmp = mediaRecorder != null ? mediaRecorder.getMaxAmplitude() : 0;
                        audioRecordView.update(currentMaxAmp);
                        if (mediaRecorder==null)
                            timer = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }}, 0, 100);
            mediaRecorder.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording(boolean isCancel) {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                if (isCancel) {
                    new File(audioFileNameWithPath).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addKeyboardSupport(Activity activity, ViewParent parentView) {
        KeyBoardUtils.setKeyboardVisibilityListener(activity, (View) parentView, keyboardVisible -> {
            if (keyboardVisible) {
//                cometChatMessageList.scrollToBottom();
                ivMicrophone.setVisibility(View.GONE);
                hideSend(false);
            } else {
                if (isEdit || isReply || !getText().isEmpty()) {
                    ivMicrophone.setVisibility(GONE);
                    hideSend(false);
                } else {
                   if (!isMicrophoneHidden)
                       ivMicrophone.setVisibility(View.VISIBLE);
                   hideSend(true);
                }
            }
        });
    }

    public void typingIndicator(boolean isEnabled) {
        typingIndicatorEnabled = isEnabled;
    }

    public void fetchMessageFilterConfiguration() {
        List<CometChatMessageTemplate> list = CometChatMessagesConfigurations.getMessageFilterList();
        List<ActionItem> listOfItems = new ArrayList<>();
        for (CometChatMessageTemplate template : list) {
            if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.video)) {
                isGalleryVisible = true;
                if (template.getName()!=null)
                    galleryActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    galleryActionItem.setIcon(template.getIcon());
                }
               listOfItems.add(galleryActionItem);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.image)) {
                isCameraVisible = true;
                if (template.getName()!=null)
                    cameraActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    cameraActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(cameraActionItem);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.file)) {
                isFileVisible = true;
                if (template.getName()!=null)
                    fileActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    fileActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(fileActionItem);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.audio)) {
                isAudioVisible = true;
                if (template.getName()!=null)
                    audioActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    audioActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(audioActionItem);
            } else if (template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.location)) {
                isLocationVisible = true;
                if (template.getName()!=null)
                    locationActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    locationActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(locationActionItem);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.whiteboard)) {
                isWhiteBoardVisible = true;
                if (template.getName()!=null)
                    whiteboardActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    whiteboardActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(whiteboardActionItem);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.document)) {
                isWriteBoardVisible = true;
                if (template.getName()!=null)
                    documentActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    documentActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(documentActionItem);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.poll)) {
                isPollVisible = true;
                if (template.getName()!=null)
                    pollActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    pollActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(pollActionItem);
            } else if(template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.sticker)) {
                isStickerVisible = true;
                if (template.getName()!=null)
                    stickerActionItem.setTitle(template.getName());
                if (template.getIcon()!=0) {
                    stickerActionItem.setIcon(template.getIcon());
                }
                listOfItems.add(stickerActionItem);
            } else if(!template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.text)
                    &&!template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.callAction)
                    && !template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.groupCall)
                    && !template.getId().equalsIgnoreCase(CometChatMessageTemplate.DefaultList.groupAction)) {
                ActionItem customItem = new ActionItem(template.getName(),template.getIcon());
                customItem.setId(template.getId());
                listOfItems.add(customItem);
            }
        }
        cometChatActionSheet.setList(listOfItems);
    }
}
