package com.cometchat.chatuikit.calls.callhistory;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

 class CallHistoryStyle extends BaseStyle {

    public final String TAG = CallHistoryStyle.class.getName();

    private String titleFont;
    private String emptyTextFont;

    private @StyleRes
    int titleAppearance;
    private @StyleRes
    int emptyTextAppearance;
    private @StyleRes
    int errorTextAppearance;

    private @ColorInt
    int titleColor;
    private @ColorInt
    int backIconTint;

    private @ColorInt
    int loadingIconTint;
    private @ColorInt
    int emptyTextColor;
    private @ColorInt
    int errorTextColor;
    private @ColorInt
    int incomingAudioCallIconTint, incomingVideoCallIconTint, callStatusColor, missedCallTitleColor, infoIconTint;

    @Override
    public CallHistoryStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public CallHistoryStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;

    }

    @Override
    public CallHistoryStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;

    }

    @Override
    public CallHistoryStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public CallHistoryStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public CallHistoryStyle setActiveBackground(int activeBackground) {
        Log.i(TAG, "setActiveBackground can not be set");
        return this;
    }

    public CallHistoryStyle setIncomingAudioCallIconTint(int incomingAudioCallIconTint) {
        this.incomingAudioCallIconTint = incomingAudioCallIconTint;
        return this;
    }

    public CallHistoryStyle setIncomingVideoCallIconTint(int incomingVideoCallIconTint) {
        this.incomingVideoCallIconTint = incomingVideoCallIconTint;
        return this;
    }

    public CallHistoryStyle setCallStatusColor(int callStatusColor) {
        this.callStatusColor = callStatusColor;
        return this;
    }

    public CallHistoryStyle setMissedCallTitleColor(int missedCallTitleColor) {
        this.missedCallTitleColor = missedCallTitleColor;
        return this;
    }

    public CallHistoryStyle setInfoIconTint(int infoIconTint) {
        this.infoIconTint = infoIconTint;
        return this;
    }

    public CallHistoryStyle setTitleFont(String titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public CallHistoryStyle setEmptyTextFont(String emptyTextFont) {
        this.emptyTextFont = emptyTextFont;
        return this;
    }

    public CallHistoryStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;

    }

    public CallHistoryStyle setEmptyTextAppearance(int emptyTextAppearance) {
        this.emptyTextAppearance = emptyTextAppearance;
        return this;

    }

    public CallHistoryStyle setErrorTextAppearance(int errorTextAppearance) {
        this.errorTextAppearance = errorTextAppearance;
        return this;

    }

    public CallHistoryStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public CallHistoryStyle setBackIconTint(int backIconTint) {
        this.backIconTint = backIconTint;
        return this;
    }

    public CallHistoryStyle setLoadingIconTint(int loadingIconTint) {
        this.loadingIconTint = loadingIconTint;
        return this;
    }

    public CallHistoryStyle setEmptyTextColor(int emptyTextColor) {
        this.emptyTextColor = emptyTextColor;
        return this;
    }

    public CallHistoryStyle setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
        return this;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public String getEmptyTextFont() {
        return emptyTextFont;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getEmptyTextAppearance() {
        return emptyTextAppearance;
    }

    public int getErrorTextAppearance() {
        return errorTextAppearance;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getBackIconTint() {
        return backIconTint;
    }

    public int getLoadingIconTint() {
        return loadingIconTint;
    }

    public int getEmptyTextColor() {
        return emptyTextColor;
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public int getIncomingAudioCallIconTint() {
        return incomingAudioCallIconTint;
    }

    public int getIncomingVideoCallIconTint() {
        return incomingVideoCallIconTint;
    }

    public int getCallStatusColor() {
        return callStatusColor;
    }

    public int getMissedCallTitleColor() {
        return missedCallTitleColor;
    }

    public int getInfoIconTint() {
        return infoIconTint;
    }
}
