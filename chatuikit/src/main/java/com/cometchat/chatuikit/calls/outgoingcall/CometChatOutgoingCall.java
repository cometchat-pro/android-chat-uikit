package com.cometchat.chatuikit.calls.outgoingcall;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.calls.ongoingcall.CometChatOngoingCall;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.resources.soundManager.CometChatSoundManager;
import com.cometchat.chatuikit.shared.resources.soundManager.Sound;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.views.button.ButtonStyle;
import com.cometchat.chatuikit.shared.views.button.CometChatButton;
import com.cometchat.chatuikit.shared.views.card.CometChatCard;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

/**
 * CometChatOutgoingCall is a custom view that represents the UI for an outgoing call.
 * <p>
 * It extends the MaterialCardView class and provides methods for setting call-related properties and styles.
 */
public class CometChatOutgoingCall extends MaterialCardView {
    private Context context;
    private CometChatTheme cometChatTheme;
    private CometChatCard cometChatCard;
    private CometChatOngoingCall ongoingCall;
    private OutgoingViewModel viewModel;
    private CometChatButton button;
    private Call call;
    private User user;
    private OnError onError;
    private TextView subtitle;
    private OnClick onDeclineCallClick;
    private CometChatSoundManager soundManager;
    private @RawRes
    int customSoundForCalls;
    private boolean disableSoundForCall;

    /**
     * Constructs a new CometChatOutgoingCall with the given context.
     *
     * @param context The context in which the view is created.
     */
    public CometChatOutgoingCall(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new CometChatOutgoingCall with the given context and attribute set.
     *
     * @param context The context in which the view is created.
     * @param attrs   The attribute set for the view.
     */
    public CometChatOutgoingCall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new CometChatOutgoingCall with the given context, attribute set, and default style.
     *
     * @param context      The context in which the view is created.
     * @param attrs        The attribute set for the view.
     * @param defStyleAttr The default style resource.
     */
    public CometChatOutgoingCall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the view.
     *
     * @param context The context in which the view is created.
     */
    private void init(Context context) {
        this.context = context;
        cometChatTheme = CometChatTheme.getInstance(context);
        View view = View.inflate(context, R.layout.comet_chat_outgoing_call_layout, null);
        soundManager = new CometChatSoundManager(context);
        cometChatCard = view.findViewById(R.id.outgoing_card);
        ongoingCall = view.findViewById(R.id.ongoing_call);
        viewModel = new OutgoingViewModel();
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(viewModel.getClass());
        viewModel.getAcceptedCall().observe((AppCompatActivity) context, this::acceptedCall);
        viewModel.getRejectCall().observe((AppCompatActivity) context, this::rejectedCall);
        viewModel.getException().observe((AppCompatActivity) context, this::showError);
        addView(view);
        setDeclineButton();
    }

    private void showError(CometChatException e) {
        if (onError != null) {
            String errorMessage = getContext().getString(R.string.something_went_wrong);
            if (getContext() != null) {
                new CometChatDialog(context, 0, cometChatTheme.getTypography().getText1(), cometChatTheme.getTypography().getText3(), cometChatTheme.getPalette().getAccent900(), 0, cometChatTheme.getPalette().getAccent900(), errorMessage, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", cometChatTheme.getPalette().getPrimary(), cometChatTheme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        alertDialog.dismiss();
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        alertDialog.dismiss();
                    }
                }, 0, false);
            }
        }
    }

    private void setDeclineButton() {
        View view = View.inflate(context, R.layout.decline_button_view, null);
        button = view.findViewById(R.id.button);
        button.setStyle(new ButtonStyle().setButtonBackgroundColor(cometChatTheme.getPalette().getError()).setButtonIconTint(cometChatTheme.getPalette().getAccent900()).setButtonTextColor(cometChatTheme.getPalette().getAccent()).setButtonTextAppearance(cometChatTheme.getTypography().getText1()));
        button.setButtonText(getResources().getString(R.string.cancel));
        button.setOnClickListener(view1 -> {
            if (onDeclineCallClick == null) viewModel.rejectCall(call.getSessionId());
            else onDeclineCallClick.onClick();
        });
        cometChatCard.setBottomView(view);
    }

    /**
     * Sets the call associated with the outgoing call view.
     *
     * @param call The Call object representing the outgoing call.
     */
    public void setCall(Call call) {
        if (call != null) {
            this.call = call;
        }
    }

    /**
     * Sets the user associated with the outgoing call.
     *
     * @param user The User object representing the user making the outgoing call.
     */
    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            cometChatCard.setTitle(user.getName() + "");
            cometChatCard.setTitleTextAppearance(R.style.Headline);
            cometChatCard.setTitleColor(cometChatTheme.getPalette().getAccent());
            cometChatCard.setAvatar(user.getAvatar(), user.getName());
            subtitle = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            subtitle.setText(getResources().getString(R.string.calling));
            subtitle.setTextColor(cometChatTheme.getPalette().getAccent600());
            subtitle.setTextAppearance(context, cometChatTheme.getTypography().getSubtitle1());
            subtitle.setLayoutParams(params);
            subtitle.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            cometChatCard.setSubtitleView(subtitle);
        }
    }

    private void playSound() {
        if (!disableSoundForCall) soundManager.play(Sound.outgoingCall, customSoundForCalls);
    }

    /**
     * Sets the text to be displayed on the decline button.
     *
     * @param text The text to be displayed on the decline button.
     */
    public void setDeclineButtonText(String text) {
        if (text != null && !text.isEmpty()) button.setButtonText(text);
    }

    /**
     * Sets the icon for the decline button.
     *
     * @param icon The icon resource ID for the decline button.
     */
    public void setDeclineButtonIcon(@DrawableRes int icon) {
        if (icon != 0) button.setButtonIcon(icon);
    }

    /**
     * Sets the style for the decline button.
     *
     * @param buttonStyle The ButtonStyle object representing the style for the decline button.
     */
    public void setDeclineButtonStyle(ButtonStyle buttonStyle) {
        if (buttonStyle != null) {
            button.setStyle(buttonStyle);

        }
    }

    /**
     * Sets the style for the outgoing call view.
     *
     * @param callStyle The OutgoingCallStyle object representing the style for the outgoing call view.
     */
    public void setStyle(OutgoingCallStyle callStyle) {
        if (callStyle != null) {
            cometChatCard.setStyle(callStyle);
            setSubtitleTextAppearance(callStyle.getSubTitleAppearance());
            setSubtitleTextColor(callStyle.getSubTitleColor());
        }
    }

    public void rejectedCall(Call call) {
        ((Activity) context).onBackPressed();
    }

    public void setSubtitleTextColor(@ColorInt int color) {
        if (color != 0 && subtitle != null) subtitle.setTextColor(color);
    }

    public void setSubtitleTextAppearance(@StyleRes int appearance) {
        if (appearance != 0 && subtitle != null) subtitle.setTextAppearance(context, appearance);
    }

    public void acceptedCall(Call call) {
        launchOnGoingScreen(call.getSessionId(), call.getReceiverType());
    }

    public void launchOnGoingScreen(String sessionId, String receiverType) {
        cometChatCard.setVisibility(GONE);
        ongoingCall.setType(receiverType);
        ongoingCall.setSessionId(sessionId);
        ongoingCall.startCall();
        ongoingCall.setVisibility(VISIBLE);
        soundManager.pause();
    }

    public void disableSoundForCall(boolean disableSoundForCall) {
        this.disableSoundForCall = disableSoundForCall;
    }

    private void setCustomSoundForCalls(@RawRes int customSoundForCalls) {
        if (customSoundForCalls != 0)
            this.customSoundForCalls = customSoundForCalls;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        viewModel.addListeners();
        if (user != null)
            playSound();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        viewModel.removeListeners();
        soundManager.pause();
    }

    public void setOnDeclineCallClick(OnClick click) {
        if (click != null) this.onDeclineCallClick = click;
    }

    public void setOnError(OnError onError) {
        if (onError != null) this.onError = onError;
    }
}
