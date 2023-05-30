package com.cometchat.chatuikit.calls.callbutton;

import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.shared.Interfaces.OnError;
import com.cometchat.chatuikit.shared.views.button.ButtonStyle;
/**

 Configuration class for customizing the appearance and behavior of the call buttons.

 Use this class to set various options and styles for the call buttons.
 */
public class CallButtonsConfiguration {
    private CometChatCallButtons.OnClick onVideoCallClick, onVoiceCallClick;
    private OnError onError;
    private @DrawableRes
    int voiceCallIcon, videoCallIcon;
    private CallButtonsStyle style;
    private ButtonStyle buttonStyle;
    private boolean hideButtonText, hideButtonIcon;
    private String voiceCallText, videoCallText;
    /**

     Sets the click listener for the video call button.
     @param onVideoCallClick The callback to be invoked when the video call button is clicked.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setOnVideoCallClick(CometChatCallButtons.OnClick onVideoCallClick) {
        this.onVideoCallClick = onVideoCallClick;
        return this;
    }
    /**

     Sets the click listener for the voice call button.
     @param onVoiceCallClick The callback to be invoked when the voice call button is clicked.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setOnVoiceCallClick(CometChatCallButtons.OnClick onVoiceCallClick) {
        this.onVoiceCallClick = onVoiceCallClick;
        return this;
    }
    /**

     Sets the style for the call buttons.
     @param buttonStyle The style to be applied to the call buttons.
     */
    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle = buttonStyle;
    }
    /**

     Sets the error handler for the call buttons.
     @param onError The error handler to be invoked when an error occurs.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setOnError(OnError onError) {
        this.onError = onError;
        return this;
    }
    /**

     Sets the icon resource for the voice call button.
     @param voiceCallIcon The icon resource to be displayed on the voice call button.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setVoiceCallIcon(int voiceCallIcon) {
        this.voiceCallIcon = voiceCallIcon;
        return this;
    }
    /**

     Sets the icon resource for the video call button.
     @param videoCallIcon The icon resource to be displayed on the video call button.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setVideoCallIcon(int videoCallIcon) {
        this.videoCallIcon = videoCallIcon;
        return this;
    }
    /**

     Sets the style for the call buttons.
     @param style The style to be applied to the call buttons.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setStyle(CallButtonsStyle style) {
        this.style = style;
        return this;
    }
    /**

     Sets whether to hide the text on the call buttons.
     @param hideVoiceCall True to hide the text on the voice call button, false otherwise.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration hideButtonText(boolean hideVoiceCall) {
        this.hideButtonText = hideVoiceCall;
        return this;
    }
    /**

     Sets whether to hide the icon on the call buttons.
     @param hideVideoCall True to hide the icon on the video call button, false otherwise.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration hideButtonIcon(boolean hideVideoCall) {
        this.hideButtonIcon = hideVideoCall;
        return this;
    }
    /**

     Sets the text for the video call button.
     @param voiceCallText The text to be displayed on the voice call button.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setVideoButtonText(String voiceCallText) {
        this.voiceCallText = voiceCallText;
        return this;
    }
    /**

     Sets the text for the voice call button.
     @param videoCallText The text to be displayed on the video call button.
     @return The updated CallButtonsConfiguration object.
     */
    public CallButtonsConfiguration setVoiceButtonText(String videoCallText) {
        this.videoCallText = videoCallText;
        return this;
    }

    public CometChatCallButtons.OnClick getOnVideoCallClick() {
        return onVideoCallClick;
    }

    public CometChatCallButtons.OnClick getOnVoiceCallClick() {
        return onVoiceCallClick;
    }

    public OnError getOnError() {
        return onError;
    }

    public int getVoiceCallIcon() {
        return voiceCallIcon;
    }

    public int getVideoCallIcon() {
        return videoCallIcon;
    }

    public CallButtonsStyle getStyle() {
        return style;
    }

    public boolean isHideButtonText() {
        return hideButtonText;
    }

    public boolean isHideButtonIcon() {
        return hideButtonIcon;
    }

    public ButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    public String getVoiceCallText() {
        return voiceCallText;
    }

    public String getVideoCallText() {
        return videoCallText;
    }
}
