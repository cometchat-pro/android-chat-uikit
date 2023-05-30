package com.cometchat.chatuikit.calls.callbutton;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.calls.outgoingcall.CometChatCallActivity;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.views.button.ButtonStyle;
import com.cometchat.chatuikit.shared.views.button.CometChatButton;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

/**
 * Custom view class for displaying call buttons in the CometChat application.
 * <p>
 * It extends MaterialCardView to provide a card-like appearance.
 * <p>
 * Use this class to display and customize the call buttons.
 */
public class CometChatCallButtons extends MaterialCardView {

    private Context context;
    private CometChatButton voiceCall, videoCall;
    private User user;
    private Group group;
    private OnClick onVideoCallClick, onVoiceCallClick;
    private CallButtonsViewModel callButtonsViewModel;
    private OnError onError;
    private boolean errorDisplayed = false;
    private CometChatTheme theme;
    LinearLayout.LayoutParams params;

    public CometChatCallButtons(Context context) {
        super(context);
        init(context);
    }

    public CometChatCallButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatCallButtons(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        callButtonsViewModel = new CallButtonsViewModel();
        theme = CometChatTheme.getInstance(context);
        View view = View.inflate(context, R.layout.cometchat_call_button, null);
        voiceCall = view.findViewById(R.id.voice_call);
        videoCall = view.findViewById(R.id.video_call);
        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(5, 5, 5, 5);
        callButtonsViewModel.getCallInitiated().observe((AppCompatActivity) context, this::callInitiated);
        callButtonsViewModel.getStartDirectCall().observe((AppCompatActivity) context, this::startDirectCall);
        callButtonsViewModel.getError().observe((AppCompatActivity) context, this::showError);
        callButtonsViewModel.getCallRejected().observe((AppCompatActivity) context, this::enableButton);
        voiceCall.setOnClickListener(view12 -> {
            if (onVoiceCallClick != null) {
                onVoiceCallClick.onClick(user, group);
            } else {
                callButtonsViewModel.initiateCall(CometChatConstants.CALL_TYPE_AUDIO);
            }
        });
        videoCall.setOnClickListener(view1 -> {
            if (onVideoCallClick != null) onVideoCallClick.onClick(user, group);
            else callButtonsViewModel.initiateCall(CometChatConstants.CALL_TYPE_VIDEO);
        });
        addView(view);
    }

    /**
     * Sets the margin between the call buttons.
     *
     * @param marginBetweenButtons The margin value to be applied between the call buttons.
     */
    public void setMarginForButtons(int marginBetweenButtons) {
        if (marginBetweenButtons > 0) {
            params.setMargins(marginBetweenButtons, marginBetweenButtons, marginBetweenButtons, marginBetweenButtons);
            videoCall.setLayoutParams(params);
            voiceCall.setLayoutParams(params);
        }
    }

    private void showError(CometChatException e) {
        if (onError != null) onError.onError(context, e);
        else {
            if (!errorDisplayed) {
                errorDisplayed = true;
                new CometChatDialog(context, 0, theme.getTypography().getText1(), theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, theme.getPalette().getAccent(), context.getResources().getString(R.string.something_went_wrong), "", getContext().getString(R.string.okay), "", "", theme.getPalette().getPrimary(), 0, 0, (alertDialog, which, popupId) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        errorDisplayed = false;
                        alertDialog.dismiss();
                    }
                }, 0, false);
            }
        }
    }

    private void startDirectCall(BaseMessage baseMessage) {
        CometChatCallActivity.launch(context, baseMessage, baseMessage.getReceiverType());
    }

    private void callInitiated(Call call) {
        Log.e("", "callInitiated: " + call.getSessionId());
        CometChatCallActivity.launch(context, call, user, call.getReceiverType());
    }

    /**
     * Enables the call buttons.
     *
     * @param call The call object associated with the buttons.
     */
    public void enableButton(Call call) {
        videoCall.setEnabled(true);
        voiceCall.setEnabled(true);
    }

    /**
     * Disables the call buttons.
     *
     * @param call The call object associated with the buttons.
     */
    public void disableButton(Call call) {
        videoCall.setEnabled(false);
        voiceCall.setEnabled(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (CometChat.getActiveCall() != null) {
            disableButton(CometChat.getActiveCall());
        } else {
            enableButton(null);
        }
        callButtonsViewModel.addListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        callButtonsViewModel.removeListener();
    }

    /**
     * Hides or shows the voice call button.
     *
     * @param hide True to hide the voice call button, false to show it.
     */
    public void hideVoiceCall(boolean hide) {
        voiceCall.setVisibility(hide ? GONE : VISIBLE);
    }

    /**
     * Hides or shows the video call button.
     *
     * @param hide True to hide the video call button, false to show it.
     */
    public void hideVideoCall(boolean hide) {
        videoCall.setVisibility(hide ? GONE : VISIBLE);
    }

    /**
     * Sets the user associated with the call buttons.
     *
     * @param user The User object representing the user associated with the buttons.
     */
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            hideVoiceCall(false);
            hideVideoCall(false);
            callButtonsViewModel.setUser(user);
        }
    }

    /**
     * Sets the style for the call buttons.
     *
     * @param style The ButtonStyle object representing the style to be applied to the buttons.
     */
    public void setButtonStyle(ButtonStyle style) {
        if (style != null) {
            videoCall.setStyle(style);
            voiceCall.setStyle(style);
        }
    }

    /**
     * Sets the overall style for the call buttons view.
     *
     * @param style The CallButtonsStyle object representing the style to be applied to the view.
     */
    public void setStyle(CallButtonsStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets the group associated with the call buttons.
     *
     * @param group The Group object representing the group associated with the buttons.
     */
    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            hideVoiceCall(true);
            callButtonsViewModel.setGroup(group);
        }
    }

    /**
     * Sets the icon for the voice call button.
     *
     * @param voiceCallIcon The resource ID of the drawable representing the icon for the voice call button.
     */
    public void setVoiceCallIcon(@DrawableRes int voiceCallIcon) {
        if (voiceCallIcon != 0) voiceCall.setButtonIcon(voiceCallIcon);
    }

    /**
     * Sets the icon for the video call button.
     *
     * @param videoCallIcon The resource ID of the drawable representing the icon for the video call button.
     */
    public void setVideoCallIcon(@DrawableRes int videoCallIcon) {
        if (videoCallIcon != 0) videoCall.setButtonIcon(videoCallIcon);
    }

    /**
     * Sets the text for the voice call button.
     *
     * @param voiceCallText The text to be displayed on the voice call button.
     */
    public void setVoiceButtonText(String voiceCallText) {
        if (voiceCallText != null && !voiceCallText.isEmpty()) {
            voiceCall.setVisibility(VISIBLE);
            voiceCall.setButtonText(voiceCallText);
        }
    }

    /**
     * Sets the text for the video call button.
     *
     * @param videoCallText The text to be displayed on the video call button.
     */
    public void setVideoButtonText(String videoCallText) {
        if (videoCallText != null && !videoCallText.isEmpty()) {
            videoCall.setVisibility(VISIBLE);
            videoCall.setButtonText(videoCallText);
        }
    }

    /**
     * Hides or shows the text on the call buttons.
     *
     * @param hide True to hide the text on the buttons, false to show it.
     */
    public void hideButtonText(boolean hide) {
        voiceCall.hideButtonText(hide);
        videoCall.hideButtonText(hide);
    }

    /**
     * Hides or shows the icon on the call buttons.
     *
     * @param hide True to hide the icon on the buttons, false to show it.
     */
    public void hideButtonIcon(boolean hide) {
        videoCall.hideButtonIcon(hide);
        voiceCall.hideButtonIcon(hide);
    }

    /**
     * Sets the click listener for the video call button.
     *
     * @param onVideoCallClick The OnClick listener to be invoked when the video call button is clicked.
     */
    public void setOnVideoCallClick(OnClick onVideoCallClick) {
        if (onVideoCallClick != null) this.onVideoCallClick = onVideoCallClick;
    }

    /**
     * Sets the click listener for the voice call button.
     *
     * @param onVoiceCallClick The OnClick listener to be invoked when the voice call button is clicked.
     */

    public void setOnVoiceCallClick(OnClick onVoiceCallClick) {
        if (onVoiceCallClick != null) this.onVoiceCallClick = onVoiceCallClick;
    }

    public CometChatButton getVoiceCallButton() {
        return voiceCall;
    }

    public CometChatButton getVideoCallButton() {
        return videoCall;
    }

    /**
     * Sets the error listener for the call buttons.
     *
     * @param onError The OnError listener to be invoked when an error occurs in the call buttons.
     */
    public void setOnError(OnError onError) {
        if (onError != null) this.onError = onError;
    }

    public interface OnClick {
        void onClick(User user, Group group);
    }
}
