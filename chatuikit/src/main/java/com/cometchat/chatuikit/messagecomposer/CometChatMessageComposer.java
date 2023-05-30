package com.cometchat.chatuikit.messagecomposer;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatUIEvents;
import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.models.CometChatMessageComposerAction;
import com.cometchat.chatuikit.shared.resources.soundManager.CometChatSoundManager;
import com.cometchat.chatuikit.shared.resources.soundManager.Sound;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.MediaUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.resources.utils.keyboard_utils.KeyBoardUtils;
import com.cometchat.chatuikit.shared.views.CometChatMessageInput.CometChatMessageInput;
import com.cometchat.chatuikit.shared.views.CometChatMessageInput.MessageInputStyle;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.ActionItem;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.ActionSheetStyle;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.CometChatActionSheet;
import com.cometchat.chatuikit.shared.views.cometchatActionSheet.CometChatActionSheetListener;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.jvm.functions.Function4;

/**
 * The CometChatMessageComposer class is a custom view that provides a user interface for composing and sending messages.
 * It extends the MaterialCardView class and implements various functionalities related to message composition.
 * <br>
 * Example :<br>
 * <pre>{@code
 * CometChatMessageComposer cometChatMessageComposer = findViewById(R.id.message_composer);
 * if(user!=null) cometChatMessageComposer.setUser(user);
 * else if (group!=null) cometChatMessageComposer.setGroup(group);
 * }
 * </pre>
 */
public class CometChatMessageComposer extends MaterialCardView {

    private Context context;
    private CometChatMessageInput input;
    private LinearLayout headerViewLayout, footerViewLayout, composeBoxLayout, parent, sendButtonViewLayout;
    private MessageComposerViewModel composerViewModel;
    private User user;
    private Group group;
    private boolean disableSoundForMessages;
    private @RawRes
    int customSoundForMessages;
    private boolean disableTypingEvents;
    private CometChatSoundManager soundManager;
    private @DrawableRes
    int attachmentIcon;
    private @ColorInt
    int attachmentIconTint;
    private Function4<Context, User, Group, HashMap<String, String>, List<CometChatMessageComposerAction>> cometChatMessageComposerActions;
    private int parentMessageId = -1;
    private boolean hideLiveReaction;
    private @DrawableRes
    int liveReactionIcon;
    private @DrawableRes
    int sendButtonIcon = 0;
    private @ColorInt
    int sendButtonTintColor;
    private ImageView attachmentImageView, sendImageview, liveReactionImageView;
    private SendButtonClick sendButtonClick;
    private CometChatActionSheet actionSheet;
    private String text, id, type;
    private TextMessage editMessage;
    private BaseMessage sendMessage;
    private View headerView;
    private View footerView;
    private CometChatTheme cometChatTheme;
    private View sendButtonView;
    private Timer typingTimer;
    private ActivityResultLauncher<Intent> activityResult;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Activity activity;
    private String RESULT_TO_BE_OPEN = "";
    private ActionSheetStyle actionSheetStyle;
    private OnError onError;
    private View internalBottomPanel, internalTopPanel;
    private HashMap<String, CometChatMessageComposerAction> actionHashMap = new HashMap<>();

    /**
     * The constructor for the CometChatMessageComposer class.
     *
     * @param context The context of the CometChatMessageComposer.
     */
    public CometChatMessageComposer(Context context) {
        super(context);
        init(context);
    }

    /**
     * The constructor for the CometChatMessageComposer class.
     *
     * @param context The context of the CometChatMessageComposer.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public CometChatMessageComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * The constructor for the CometChatMessageComposer class.
     *
     * @param context      The context of the CometChatMessageComposer.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource that supplies default values for the view.
     */
    public CometChatMessageComposer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the views and components of the CometChatMessageComposer.
     */
    private void init(Context context) {
        this.context = context;
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        actionSheet = new CometChatActionSheet(context);
        soundManager = new CometChatSoundManager(context);
        cometChatTheme = CometChatTheme.getInstance(context);
        composerViewModel = new MessageComposerViewModel();
        typingTimer = new Timer();
        this.sendButtonTintColor = cometChatTheme.getPalette().getPrimary();
        this.attachmentIcon = R.drawable.ic_attachment;
        this.liveReactionIcon = R.drawable.heart_reaction;
//        this.sendButtonIcon = R.drawable.cc_ic_send;
        activity = ((AppCompatActivity) context);
        View view = View.inflate(context, R.layout.message_composer, null);
        input = view.findViewById(R.id.message_input);
        headerViewLayout = view.findViewById(R.id.header_view);
        footerViewLayout = view.findViewById(R.id.footer_view);
        composeBoxLayout = view.findViewById(R.id.compose_box);
        parent = view.findViewById(R.id.parent);
        composerViewModel = ViewModelProviders.of((FragmentActivity) context).get(composerViewModel.getClass());
        composerViewModel.sentMessage().observe((AppCompatActivity) context, this::messageSentSuccess);
        composerViewModel.getException().observe((AppCompatActivity) context, this::messageSendException);
        composerViewModel.processEdit().observe((AppCompatActivity) context, this::setEditMessageView);
        composerViewModel.successEdit().observe((AppCompatActivity) context, this::onMessageEditSuccess);
        composerViewModel.closeTopPanel().observe((AppCompatActivity) context, this::closeInternalTopPanel);
        composerViewModel.closeBottomPanel().observe((AppCompatActivity) context, this::closeInternalBottomPanel);
        composerViewModel.showTopPanel().observe((AppCompatActivity) context, this::showInternalTopPanel);
        composerViewModel.showBottomPanel().observe((AppCompatActivity) context, this::showInternalBottomPanel);
        createAttachmentView();
        createSendButtonView();
        addView(view);
        setAuxiliaryButtonAlignment(UIKitConstants.AuxiliaryButtonAlignment.RIGHT);
        setPlaceHolderText(getResources().getString(R.string.message));
        Drawable drawable = getContext().getDrawable(R.drawable.cc_action_item_top_curve);
        drawable.setTint(cometChatTheme.getPalette().getAccent900());
        setStyle(new MessageComposerStyle().setBackground(android.R.color.transparent).setInputCornerRadius(16).setInputBackgroundColor(cometChatTheme.getPalette().getAccent100()).setSeparatorTint(cometChatTheme.getPalette().getAccent100()).setPlaceHolderTextColor(cometChatTheme.getPalette().getAccent600()).setTextColor(cometChatTheme.getPalette().getAccent()).setTextAppearance(cometChatTheme.getTypography().getText1()).setActionSheetSeparatorTint(cometChatTheme.getPalette().getAccent200()).setActionSheetBackgroundColor(drawable).setActionSheetTitleAppearance(cometChatTheme.getTypography().getName()).setActionSheetTitleColor(cometChatTheme.getPalette().getAccent()).setActionSheetLayoutModeIconTint(cometChatTheme.getPalette().getPrimary()));
        activityResult = ((FragmentActivity) context).registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                File file = null;
                String contentType = null;
                if (UIKitConstants.ComposerAction.CAMERA.equals(RESULT_TO_BE_OPEN)) {
                    if (Build.VERSION.SDK_INT >= 29)
                        file = MediaUtils.getRealPath(getContext(), MediaUtils.uri, false);
                    else file = new File(MediaUtils.pictureImagePath);

                    if (file.exists()) contentType = CometChatConstants.MESSAGE_TYPE_IMAGE;
                    else showWarning(getResources().getString(R.string.file_not_exist));
                } else {
                    if (result.getData() != null && result.getData().getData() != null) {
                        file = MediaUtils.getRealPath(getContext(), result.getData().getData(), false);
                        contentType = getContentType(result.getData().getData());
                    }
                }
                sendMediaMessage(file, contentType);
            }
            RESULT_TO_BE_OPEN = "";
        });

        requestPermissionLauncher = ((FragmentActivity) context).registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                if (UIKitConstants.ComposerAction.CAMERA.equals(RESULT_TO_BE_OPEN)) launchCamera();
                else openStorage();
            } else {
                if (UIKitConstants.ComposerAction.CAMERA.equals(RESULT_TO_BE_OPEN))
                    showWarning(getResources().getString(R.string.grant_camera_permission));
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) openStorage();
                    else showWarning(getResources().getString(R.string.grant_storage_permission));
                }
            }
        });

        input.setOnTextChangedListener(new CometChatMessageInput.TextChangedListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                text = charSequence.toString();
                if (sendButtonIcon == 0)
                    sendImageview.setImageTintList(ColorStateList.valueOf(charSequence.length() > 0 ? sendButtonTintColor : cometChatTheme.getPalette().getAccent400()));
                if (!disableTypingEvents) sendTypingIndicator(charSequence.length() <= 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    liveReactionImageView.setVisibility(GONE);
                    sendButtonViewLayout.setVisibility(VISIBLE);
                } else {
                    handleButtonVisibility();
                }
                if (typingTimer == null) {
                    typingTimer = new Timer();
                }
                if (!disableTypingEvents) endTypingTimer();
            }
        });

        actionSheet.setEventListener(new CometChatActionSheetListener() {
            @Override
            public void onActionItemClick(ActionItem actionItem) {
                CometChatMessageComposerAction action = actionHashMap.get(actionItem.getId());
                if (action != null) {
                    if (action.getOnClick() != null) {
                        action.getOnClick().onClick();
                        actionSheet.dismiss();
                    } else {
                        switch (actionItem.getId()) {
                            case UIKitConstants.ComposerAction.CAMERA:
                                RESULT_TO_BE_OPEN = actionItem.getId();
                                requestCameraPermission();
                                actionSheet.dismiss();
                                break;
                            case UIKitConstants.ComposerAction.GALLERY:
                            case UIKitConstants.ComposerAction.AUDIO:
                            case UIKitConstants.ComposerAction.FILE:
                                RESULT_TO_BE_OPEN = actionItem.getId();
                                requestStoragePermission();
                                actionSheet.dismiss();
                                break;
                            default:
                                RESULT_TO_BE_OPEN = "";
                                actionSheet.dismiss();
                        }
                    }
                }
            }
        });
    }

    private void openStorage() {
        if (UIKitConstants.ComposerAction.FILE.equals(RESULT_TO_BE_OPEN)) openFiles();
        else if (UIKitConstants.ComposerAction.AUDIO.equals(RESULT_TO_BE_OPEN)) openAudio();
        else if (UIKitConstants.ComposerAction.GALLERY.equals(RESULT_TO_BE_OPEN)) openGallery();
    }

    public void addKeyboardSupport(Activity activity, ViewParent parentView) {
        KeyBoardUtils.setKeyboardVisibilityListener(activity, (View) parentView, keyboardVisible -> {
            if (keyboardVisible) {
                for (CometChatUIEvents events : CometChatUIEvents.uiEvents.values()) {
                    events.hidePanel(composerViewModel.getIdMap(), UIKitConstants.CustomUIPosition.COMPOSER_BOTTOM);
                }
            }
        });
    }

    private void showInternalBottomPanel(Function1<Context, View> view) {
        if (view != null) {
            Utils.removeParentFromView(internalBottomPanel);
            if (internalBottomPanel != null) footerViewLayout.removeView(internalBottomPanel);
            this.internalBottomPanel = view.apply(context);
            footerViewLayout.addView(internalBottomPanel);
        }
    }

    private void showInternalTopPanel(Function1<Context, View> view) {
        if (view != null) {
            Utils.removeParentFromView(internalTopPanel);
            if (internalTopPanel != null) headerViewLayout.removeView(internalTopPanel);
            this.internalTopPanel = view.apply(context);
            headerViewLayout.addView(internalTopPanel);
        }
    }

    private void closeInternalTopPanel(Void avoid) {
        headerViewLayout.removeView(internalTopPanel);
        internalTopPanel = null;
    }

    private void closeInternalBottomPanel(Void avoid) {
        footerViewLayout.removeView(internalBottomPanel);
        internalBottomPanel = null;
    }

    public String getContentType(Uri uri) {
        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            String mimeType = cr.getType(uri);
            if (mimeType != null) {
                if (mimeType.contains("image")) return CometChatConstants.MESSAGE_TYPE_IMAGE;
                else if (mimeType.contains("video")) return CometChatConstants.MESSAGE_TYPE_VIDEO;
                else if (mimeType.contains("audio")) return CometChatConstants.MESSAGE_TYPE_AUDIO;
                else if (Arrays.asList(UIKitConstants.IntentStrings.EXTRA_MIME_DOC).contains(mimeType))
                    return CometChatConstants.MESSAGE_TYPE_FILE;
                else return mimeType;
            } else return null;
        } else return null;
    }

    /**

     Sets the error listener for the message composer. The error listener will be triggered
     when an error occurs during message composition or sending.
     @param onError The {@link OnError} listener to be set.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    private void setMessageInputStyle(MessageInputStyle style) {
        input.setStyle(style);
    }

    /**
     * Sets the style of the message composer.
     *
     * @param style The style to be applied to the message composer.
     */
    public void setStyle(MessageComposerStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
            setAttachmentIconTint(style.getAttachIconTint());
            setSendButtonIconTint(style.getSendIconTint());
            setMessageInputStyle(new MessageInputStyle().setCornerRadius(style.getInputCornerRadius()).setInputBackground(style.getInputBackground()).setSeparatorTint(style.getSeparatorTint()).setTextColor(style.getTextColor()).setPlaceHolderColor(style.getPlaceHolderTextColor()).setInputTextAppearance(style.getTextAppearance()).setTextFont(style.getTextFont()).setBackground(style.getInputGradient()));
            setActionSheetStyle(new ActionSheetStyle().setBackground(style.getActionSheetBackgroundColor()).setListItemSeparatorColor(style.getActionSheetSeparatorTint()).setTitleColor(style.getActionSheetTitleColor()).setTitleAppearance(style.getActionSheetTitleAppearance()).setLayoutModeIconTint(style.getActionSheetLayoutModeIconTint()).setTitleFont(style.getActionSheetTitleFont()));
        }
    }

    private void setActionSheetStyle(ActionSheetStyle actionSheetStyle) {
        this.actionSheetStyle = actionSheetStyle;
        actionSheet.setStyle(actionSheetStyle);
    }

    public void sendMediaMessage(File file, String contentType) {
        playSound();
        composerViewModel.sendMediaMessage(file, contentType, false);
    }

    public void playSound() {
        if (!disableSoundForMessages)
            soundManager.play(Sound.outgoingMessage, customSoundForMessages);
    }

    public void openGallery() {
        activityResult.launch(MediaUtils.openGallery(activity));
    }

    public void openFiles() {
        activityResult.launch(MediaUtils.getFileIntent(UIKitConstants.IntentStrings.EXTRA_MIME_DOC));
    }

    public void openAudio() {
        activityResult.launch(MediaUtils.openAudio(activity));
    }

    public void launchCamera() {
        activityResult.launch(MediaUtils.openCamera(getContext()));
    }

    public void requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    public void requestStoragePermission() {
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void endTypingTimer() {
        if (typingTimer != null) {
            typingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendTypingIndicator(true);
                }
            }, 2000);
        }
    }

    public void setSendButtonIconTint(@ColorInt int color) {
        if (sendImageview != null && color != 0) {
            this.sendButtonTintColor = color;
        }
    }

    public void setAttachmentIconTint(@ColorInt int color) {
        if (attachmentImageView != null && color != 0) {
            this.attachmentIconTint = color;
            attachmentImageView.setImageTintList(ColorStateList.valueOf(color));
        }
    }

    public void sendTypingIndicator(boolean isEnd) {
        if (isEnd) CometChat.endTyping(new TypingIndicator(id, type));
        else CometChat.startTyping(new TypingIndicator(id, type));
    }

    public void messageSentSuccess(BaseMessage baseMessage) {
        if (baseMessage != null) {
            sendMessage = null;
        }
    }

    public void messageSendException(CometChatException exception) {
        if (onError != null) onError.onError(context, exception);
    }

    private void showWarning(String warning) {
        String errorMessage = warning;
        if (getContext() != null) {
            new CometChatDialog(context, 0, cometChatTheme.getTypography().getText1(), cometChatTheme.getTypography().getText3(), cometChatTheme.getPalette().getAccent900(), 0, cometChatTheme.getPalette().getAccent700(), errorMessage, "", getContext().getString(R.string.okay), null, null, cometChatTheme.getPalette().getPrimary(), cometChatTheme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    alertDialog.dismiss();
                }
            }, 0, false);
        }
    }

    private void setEditMessageView(BaseMessage message) {
        if (message != null) {
            editMessage = (TextMessage) message;
            View view = View.inflate(context, R.layout.cometchat_edit_message_view, null);
            LinearLayout layout = view.findViewById(R.id.edit_message_layout);
            MaterialCardView verticalBar = view.findViewById(R.id.vertical_bar);
            TextView title = view.findViewById(R.id.tv_message_layout_title);
            title.setTextColor(cometChatTheme.getPalette().getAccent600());
            title.setTextAppearance(context, cometChatTheme.getTypography().getText3());
            title.setText(getResources().getString(R.string.edit_message));

            TextView content = view.findViewById(R.id.tv_message_layout_subtitle);
            content.setTextColor(cometChatTheme.getPalette().getAccent600());
            content.setTextAppearance(context, cometChatTheme.getTypography().getSubtitle2());
            content.setText(((TextMessage) message).getText());
            input.setText(((TextMessage) message).getText());
            ImageView imageView = view.findViewById(R.id.iv_message_close);
            imageView.setImageTintList(ColorStateList.valueOf(cometChatTheme.getPalette().getAccent600()));
            layout.setBackgroundColor(cometChatTheme.getPalette().getBackground());
            verticalBar.setCardBackgroundColor(cometChatTheme.getPalette().getAccent100());
            imageView.setOnClickListener(view1 -> {
                editMessage = null;
                headerViewLayout.removeAllViews();
                setHeaderView(headerView);
            });
            headerViewLayout.removeAllViews();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = Utils.convertDpToPx(context, 16);
            layoutParams.rightMargin = Utils.convertDpToPx(context, 16);
            headerViewLayout.setLayoutParams(layoutParams);
            headerViewLayout.setVisibility(VISIBLE);
            headerViewLayout.addView(view);
        }
    }

    private void onMessageEditSuccess(BaseMessage message) {
        if (message != null) {
            editMessage = null;
            headerViewLayout.removeAllViews();
            setHeaderView(headerView);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (user != null) composerViewModel.setUser(user);
        else if (group != null) composerViewModel.setGroup(group);
        composerViewModel.addListeners();
        input.setAuxiliaryButtonView(ChatConfigurator.getUtils().getAuxiliaryOption(context, user, group, composerViewModel.getIdMap()));
        cometChatMessageComposerActions = (context, user1, group, map) -> ChatConfigurator.getUtils().getAttachmentOptions(context, user1, group, composerViewModel.getIdMap());
        setComposerActions();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        composerViewModel.removeListeners();
    }

    private void createSendButtonView() {
        View view = View.inflate(context, R.layout.buttons_view, null);
        sendButtonViewLayout = view.findViewById(R.id.send_button);
        sendImageview = view.findViewById(R.id.icon1);
        liveReactionImageView = view.findViewById(R.id.icon2);
        sendImageview.setImageResource(R.drawable.cc_ic_send);
        liveReactionImageView.setImageResource(liveReactionIcon);
        sendImageview.setImageTintList(ColorStateList.valueOf(cometChatTheme.getPalette().getAccent400()));
        handleButtonVisibility();
        sendImageview.setOnClickListener(view1 -> {
            String textMessage = text;
            input.setText("");
            if (sendButtonClick != null) {
                sendButtonClick.onClick(context, composerViewModel.getTextMessage(textMessage));
            } else {
                if (editMessage != null) {
                    editMessage.setText(textMessage);
                    composerViewModel.editMessage(editMessage);
                } else {
                    playSound();
                    composerViewModel.sendTextMessage(textMessage, false);
                }
            }
        });

        liveReactionImageView.setOnClickListener(view12 -> composerViewModel.sendTransientMessage(liveReactionIcon));

        if (sendButtonView != null) {
            sendImageview = null;
            Utils.handleView(sendButtonViewLayout, sendButtonView, false);
        }
        input.setPrimaryButtonView(view);
    }

    private void handleButtonVisibility() {
        if (hideLiveReaction) {
            sendButtonViewLayout.setVisibility(VISIBLE);
            liveReactionImageView.setVisibility(GONE);
        } else {
            sendButtonViewLayout.setVisibility(GONE);
            liveReactionImageView.setVisibility(VISIBLE);
        }
    }

    private void createAttachmentView() {
        View view = View.inflate(context, R.layout.buttons_view, null);
        attachmentImageView = view.findViewById(R.id.icon1);
        attachmentImageView.setImageResource(attachmentIcon);
        attachmentImageView.setImageTintList(ColorStateList.valueOf(cometChatTheme.getPalette().getAccent700()));
        view.findViewById(R.id.icon2).setVisibility(GONE);
        attachmentImageView.setOnClickListener(view1 -> actionSheet.show());
        input.setSecondaryButtonView(view);
    }

    /**
     * Sets the user for the message receiver.
     *
     * @param user The user object to set as the receiver.
     */
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            this.id = user.getUid() + "";
            this.type = UIKitConstants.ReceiverType.USER;
        }
    }

    /**
     * Sets the group for the message receiver.
     *
     * @param group The group object to set as the receiver.
     */
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            this.id = group.getGuid() + "";
            this.type = UIKitConstants.ReceiverType.GROUP;
        }
    }

    /**
     * Sets whether to disable sound for messages.
     *
     * @param disableSoundForMessages {@code true} to disable sound for messages, {@code false} otherwise.
     */
    public void disableSoundForMessages(boolean disableSoundForMessages) {
        this.disableSoundForMessages = disableSoundForMessages;
    }

    /**
     * Sets the icon for the send button.
     *
     * @param icon The resource ID of the icon to set.
     */
    public void setSendButtonIcon(@DrawableRes int icon) {
        if (sendImageview != null && icon != 0) {
            this.sendButtonIcon = icon;
            sendImageview.setImageResource(icon);
            sendImageview.setImageTintList(null);
        }
    }

    /**
     * Sets a custom sound for messages.
     *
     * @param customSoundForMessages The resource ID of the custom sound to set for messages.
     */
    public void setCustomSoundForMessages(@RawRes int customSoundForMessages) {
        this.customSoundForMessages = customSoundForMessages;
    }

    /**
     * Sets whether to disable typing events.
     *
     * @param disableTypingEvents {@code true} to disable typing events, {@code false} otherwise.
     */
    public void disableTypingEvents(boolean disableTypingEvents) {
        this.disableTypingEvents = disableTypingEvents;
    }

    /**
     * Sets the text of the input field.
     *
     * @param text The text to set.
     */
    public void setText(String text) {
        input.setText(text);
    }

    /**
     * Sets the placeholder text of the input field.
     *
     * @param placeHolderText The placeholder text to set.
     */
    public void setPlaceHolderText(String placeHolderText) {
        input.setPlaceHolderText(placeHolderText);
    }

    /**
     * Sets the header view.
     *
     * @param headerView The header view to set.
     */
    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        Utils.handleView(headerViewLayout, headerView, false);
    }

    /**
     * Sets the footer view.
     *
     * @param footerView The footer view to set.
     */
    public void setFooterView(View footerView) {
        this.footerView = footerView;
        Utils.handleView(footerViewLayout, footerView, false);
    }

    /**
     * Sets the listener for text changed events in the message input field.
     *
     * @param textChangedListener The text changed listener to set.
     */
    public void setOnTextChangedListener(CometChatMessageInput.TextChangedListener textChangedListener) {
        if (textChangedListener != null) input.setOnTextChangedListener(textChangedListener);
    }

    /**
     * Sets the maximum number of lines for the input field.
     *
     * @param maxLine The maximum number of lines to set.
     */
    public void setMaxLine(int maxLine) {
        input.setMaxLine(maxLine);
    }

    /**
     * Sets the icon for the attachment button.
     *
     * @param attachmentIcon The drawable resource ID of the attachment icon to set.
     */
    public void setAttachmentIcon(@DrawableRes int attachmentIcon) {
        this.attachmentIcon = attachmentIcon;
        if (attachmentIcon != 0) {
            attachmentImageView.setImageResource(attachmentIcon);
        }
    }

    /**
     * Sets the attachment options for the composer.
     *
     * @param cometChatMessageComposerActions The function to retrieve the list of composer actions.
     */
    public void setAttachmentOptions(Function4<Context, User, Group, HashMap<String, String>, List<CometChatMessageComposerAction>> cometChatMessageComposerActions) {
        if (cometChatMessageComposerActions != null) {
            this.cometChatMessageComposerActions = cometChatMessageComposerActions;
            setComposerActions();
        }
    }

    private void setComposerActions() {
        List<ActionItem> actionItems = new ArrayList<>();
        for (CometChatMessageComposerAction option : cometChatMessageComposerActions.invoke(context, user, group, composerViewModel.getIdMap())) {
            if (option != null) {
                actionHashMap.put(option.getId(), option);
                actionItems.add(new ActionItem(option.getId(), option.getIcon(), option.getIconTintColor(), option.getIconBackground(), option.getTitle(), option.getTitleFont(), option.getTitleAppearance(), option.getTitleColor(), option.getBackground(), option.getCornerRadius()));
            }
        }
        actionSheet.setList(actionItems);
    }

    /**
     * Sets the secondary button view for the composer.
     *
     * @param secondaryButtonView The function to retrieve the secondary button view.
     */
    public void setSecondaryButtonView(Function4<Context, User, Group, HashMap<String, String>, View> secondaryButtonView) {
        if (secondaryButtonView != null)
            input.setSecondaryButtonView(secondaryButtonView.invoke(context, user, group, composerViewModel.getIdMap()));
    }

    /**
     * Sets the auxiliary button view for the composer.
     *
     * @param auxiliaryButtonView The function to retrieve the auxiliary button view.
     */
    public void setAuxiliaryButtonView(Function4<Context, User, Group, HashMap<String, String>, View> auxiliaryButtonView) {
        if (auxiliaryButtonView != null)
            input.setAuxiliaryButtonView(auxiliaryButtonView.invoke(context, user, group, composerViewModel.getIdMap()));
    }

    /**
     * Sets the alignment of the auxiliary button.
     *
     * @param alignment The alignment to set for the auxiliary button.
     */
    public void setAuxiliaryButtonAlignment(UIKitConstants.AuxiliaryButtonAlignment alignment) {
        input.setAuxiliaryButtonAlignment(alignment);
    }

    /**
     * Sets the send button view for the composer.
     *
     * @param sendButtonView The send button view.
     */
    public void setSendButtonView(View sendButtonView) {
        if (sendButtonView != null) {
            this.sendButtonView = sendButtonView;
            createSendButtonView();
        }
    }

    /**
     * Sets the parent message ID for the composer.
     *
     * @param parentMessageId The parent message ID.
     */
    public void setParentMessageId(int parentMessageId) {
        this.parentMessageId = parentMessageId;
        composerViewModel.setParentMessageId(parentMessageId);
    }

    /**
     * Hides or shows the live reaction view.
     *
     * @param hideLiveReaction True to hide the live reaction view, false to show.
     */
    public void hideLiveReaction(boolean hideLiveReaction) {
        this.hideLiveReaction = hideLiveReaction;
        if (hideLiveReaction) {
            liveReactionImageView.setVisibility(GONE);
            sendButtonViewLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * Sets the live reaction icon for the composer.
     *
     * @param liveReactionIcon The live reaction icon.
     */
    public void setLiveReactionIcon(@DrawableRes int liveReactionIcon) {
        if (liveReactionIcon != 0) {
            this.liveReactionIcon = liveReactionIcon;
            liveReactionImageView.setImageResource(liveReactionIcon);
        }
    }

    /**
     * Sets the listener for send button clicks.
     *
     * @param sendButtonClick The callback function to be invoked when the send button is clicked.
     */
    public void setOnSendButtonClick(SendButtonClick sendButtonClick) {
        this.sendButtonClick = sendButtonClick;
    }

    public CometChatActionSheet getActionSheet() {
        return actionSheet;
    }

    public MessageComposerViewModel getComposerViewModel() {
        return composerViewModel;
    }

    public CometChatMessageInput getMessageInput() {
        return input;
    }

    public LinearLayout getView() {
        return parent;
    }

    public @DrawableRes
    int getLiveReactionIcon() {
        return liveReactionIcon;
    }

    public interface SendButtonClick {
        void onClick(Context context, BaseMessage message);
    }

}
