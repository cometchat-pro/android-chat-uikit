package com.cometchat.chatuikit.calls.ongoingcall;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CallSettings;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.chatuikit.R;
import com.google.android.material.card.MaterialCardView;

/**
 * CometChatOngoingCall is a custom view that represents an ongoing call. It extends MaterialCardView
 * <p>
 * and provides methods to set the session ID, call type, error listener, and style for the ongoing call view.
 */
public class CometChatOngoingCall extends MaterialCardView {

    private Context context;

    private RelativeLayout mainView;

    private OngoingCallViewModel viewModel;

    private String sessionId;

    private String type;

    private CallSettings.CallSettingsBuilder callSettingsBuilder;

    private OnError onError;

    private CometChatTheme theme;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    private String request;

    private CometChatTheme cometChatTheme;

    public CometChatOngoingCall(Context context) {
        super(context);
        init(context);
    }

    public CometChatOngoingCall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CometChatOngoingCall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        cometChatTheme = CometChatTheme.getInstance(context);
        View view = View.inflate(context, R.layout.comet_chat_ongoing_call_screen, null);
        theme = CometChatTheme.getInstance(context);
        mainView = view.findViewById(R.id.call_view);
        viewModel = new OngoingCallViewModel();
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(viewModel.getClass());
        viewModel.getEndCall().observe((AppCompatActivity) context, this::endCall);
        viewModel.getException().observe((AppCompatActivity) context, this::showError);
        Utils.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 101);
        addView(view);
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

    public void showError(CometChatException exception) {
        if (onError == null) {
            showError();
        } else {
            onError.onError(context, exception);
        }
    }

    private void showError() {
        String errorMessage = getContext().getString(R.string.something_went_wrong);

        if (getContext() != null) {
            new CometChatDialog(context, 0, theme.getTypography().getText1(), theme.getTypography().getText3(), theme.getPalette().getAccent900(), 0, theme.getPalette().getAccent900(), errorMessage, "", getContext().getString(R.string.try_again), getResources().getString(R.string.cancel), "", theme.getPalette().getPrimary(), theme.getPalette().getPrimary(), 0, (alertDialog, which, popupId) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    alertDialog.dismiss();
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    alertDialog.dismiss();
                }
            }, 0, false);
        }
    }

    public void endCall(Call call) {
        ((Activity) context).onBackPressed();
    }

    /**
     * Sets the session ID for the ongoing call.
     *
     * @param sessionId The ID of the ongoing call session.
     */
    public void setSessionId(String sessionId) {
        if (sessionId != null) {
            viewModel.setSessionId(sessionId);
            this.sessionId = sessionId;
            if (type != null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
                callSettingsBuilder = new CallSettings.CallSettingsBuilder((Activity) context, mainView).setSessionId(sessionId).setMode(CallSettings.CALL_MODE_DEFAULT);
            } else
                callSettingsBuilder = new CallSettings.CallSettingsBuilder((Activity) context, mainView).setSessionId(sessionId);
            setCallSettingsBuilder(callSettingsBuilder);
        }
    }

    /**
     * Sets the type of the ongoing call.
     *
     * @param type The type of the ongoing call.
     */
    public void setType(String type) {
        this.type = type;
    }

    private void setCallSettingsBuilder(CallSettings.CallSettingsBuilder callSettingsBuilder) {
        if (callSettingsBuilder != null) viewModel.setCallSettingsBuilder(callSettingsBuilder);
    }

    /**
     * Initiates the ongoing call.
     * This method triggers the start of the ongoing call. It delegates the call initiation logic to the underlying
     * view model responsible for handling the ongoing call functionality.
     */
    public void startCall() {
        viewModel.startCall();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * Sets the error listener for the ongoing call.
     *
     * @param onError The OnError listener to be invoked when an error occurs in the ongoing call.
     */
    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    /**
     * Sets the style for the ongoing call view.
     *
     * @param style The OngoingCallStyle object representing the style to be applied to the view.
     */
    public void setStyle(OngoingCallStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());
        }
    }
}
