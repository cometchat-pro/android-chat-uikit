package com.cometchat.chatuikit.extensions.polls;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

public class PollBubbleStyle extends BaseStyle {

    private @ColorInt
    int questionColor;
    private @ColorInt
    int optionTextColor;
    private @ColorInt
    int percentageTextColor;
    private @ColorInt
    int optionBackgroundColor;
    private @ColorInt
    int selectedOptionBackgroundColor;
    private @ColorInt
    int optionDefaultBackgroundColor;
    private @ColorInt
    int votesTextColor;
    private @StyleRes
    int questionTextAppearance;
    private @StyleRes
    int optionTextAppearance;
    private @StyleRes
    int percentageTextAppearance;

    @Override
    public PollBubbleStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public PollBubbleStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public PollBubbleStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public PollBubbleStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public PollBubbleStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public PollBubbleStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    public PollBubbleStyle setOptionDefaultBackgroundColor(int optionDefaultBackgroundColor) {
        this.optionDefaultBackgroundColor = optionDefaultBackgroundColor;
        return this;
    }

    public PollBubbleStyle setQuestionColor(int questionColor) {
        this.questionColor = questionColor;
        return this;
    }

    public PollBubbleStyle setOptionTextColor(int optionTextColor) {
        this.optionTextColor = optionTextColor;
        return this;
    }

    public PollBubbleStyle setPercentageTextColor(int percentageTextColor) {
        this.percentageTextColor = percentageTextColor;
        return this;
    }

    public PollBubbleStyle setOptionBackgroundColor(int optionBackgroundColor) {
        this.optionBackgroundColor = optionBackgroundColor;
        return this;
    }

    public PollBubbleStyle setSelectedOptionBackgroundColor(int selectedOptionBackgroundColor) {
        this.selectedOptionBackgroundColor = selectedOptionBackgroundColor;
        return this;
    }

    public PollBubbleStyle setVotesTextColor(int votesTextColor) {
        this.votesTextColor = votesTextColor;
        return this;
    }

    public PollBubbleStyle setQuestionTextAppearance(int questionTextAppearance) {
        this.questionTextAppearance = questionTextAppearance;
        return this;
    }

    public PollBubbleStyle setOptionTextAppearance(int optionTextAppearance) {
        this.optionTextAppearance = optionTextAppearance;
        return this;
    }

    public PollBubbleStyle setPercentageTextAppearance(int percentageTextAppearance) {
        this.percentageTextAppearance = percentageTextAppearance;
        return this;
    }

    public int getQuestionColor() {
        return questionColor;
    }

    public int getOptionTextColor() {
        return optionTextColor;
    }

    public int getPercentageTextColor() {
        return percentageTextColor;
    }

    public int getOptionBackgroundColor() {
        return optionBackgroundColor;
    }

    public int getSelectedOptionBackgroundColor() {
        return selectedOptionBackgroundColor;
    }

    public int getVotesTextColor() {
        return votesTextColor;
    }

    public int getQuestionTextAppearance() {
        return questionTextAppearance;
    }

    public int getOptionTextAppearance() {
        return optionTextAppearance;
    }

    public int getOptionDefaultBackgroundColor() {
        return optionDefaultBackgroundColor;
    }

    public int getPercentageTextAppearance() {
        return percentageTextAppearance;
    }
}
