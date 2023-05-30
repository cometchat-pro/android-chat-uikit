package com.cometchat.chatuikit.extensions.polls;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.shared.models.BaseStyle;

public class CreatePollsStyle extends BaseStyle {

    private @ColorInt
    int answerTextColor;
    private @ColorInt
    int optionHintColor;
    private @ColorInt
    int optionTextColor;
    private @StyleRes
    int optionTextAppearance;
    private @StyleRes
    int answerTextAppearance;
    private @ColorInt
    int titleColor;
    private @StyleRes
    int titleAppearance;
    private @ColorInt
    int questionTextColor;
    private @ColorInt
    int questionHintColor;
    private @StyleRes
    int questionTextAppearance;
    private @ColorInt
    int separatorColor;
    private @ColorInt
    int closeIconTint;
    private @ColorInt
    int createIconTint;
    private @ColorInt
    int addOptionColor;
    private @StyleRes
    int addOptionAppearance;

    @Override
    public CreatePollsStyle setBackground(int background) {
        super.setBackground(background);
        return this;
    }

    @Override
    public CreatePollsStyle setBackground(Drawable drawableBackground) {
        super.setBackground(drawableBackground);
        return this;
    }

    @Override
    public CreatePollsStyle setCornerRadius(float cornerRadius) {
        super.setCornerRadius(cornerRadius);
        return this;
    }

    @Override
    public CreatePollsStyle setBorderWidth(int borderWidth) {
        super.setBorderWidth(borderWidth);
        return this;
    }

    @Override
    public CreatePollsStyle setBorderColor(int borderColor) {
        super.setBorderColor(borderColor);
        return this;
    }

    @Override
    public CreatePollsStyle setActiveBackground(int activeBackground) {
        super.setActiveBackground(activeBackground);
        return this;
    }

    public CreatePollsStyle setAnswerTextColor(int answerTextColor) {
        this.answerTextColor = answerTextColor;
        return this;
    }

    public CreatePollsStyle setOptionHintColor(int optionHintColor) {
        this.optionHintColor = optionHintColor;
        return this;
    }

    public CreatePollsStyle setAnswerTextAppearance(int answerTextAppearance) {
        this.answerTextAppearance = answerTextAppearance;
        return this;
    }

    public CreatePollsStyle setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public CreatePollsStyle setTitleAppearance(int titleAppearance) {
        this.titleAppearance = titleAppearance;
        return this;
    }

    public CreatePollsStyle setQuestionTextColor(int questionTextColor) {
        this.questionTextColor = questionTextColor;
        return this;
    }

    public CreatePollsStyle setQuestionTextAppearance(int questionTextAppearance) {
        this.questionTextAppearance = questionTextAppearance;
        return this;
    }

    public CreatePollsStyle setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        return this;
    }

    public CreatePollsStyle setCloseIconTint(int closeIconTint) {
        this.closeIconTint = closeIconTint;
        return this;
    }

    public CreatePollsStyle setCreateIconTint(int createIconTint) {
        this.createIconTint = createIconTint;
        return this;
    }

    public CreatePollsStyle setOptionTextColor(int optionTextColor) {
        this.optionTextColor = optionTextColor;
        return this;
    }

    public CreatePollsStyle setQuestionHintColor(int questionHintColor) {
        this.questionHintColor = questionHintColor;
        return this;
    }

    public CreatePollsStyle setOptionTextAppearance(int optionTextAppearance) {
        this.optionTextAppearance = optionTextAppearance;
        return this;
    }

    public CreatePollsStyle setAddOptionColor(int addOptionColor) {
        this.addOptionColor = addOptionColor;
        return this;
    }

    public CreatePollsStyle setAddOptionAppearance(int addOptionAppearance) {
        this.addOptionAppearance = addOptionAppearance;
        return this;
    }

    public int getOptionTextColor() {
        return optionTextColor;
    }

    public int getOptionTextAppearance() {
        return optionTextAppearance;
    }

    public int getAnswerTextColor() {
        return answerTextColor;
    }

    public int getOptionHintColor() {
        return optionHintColor;
    }

    public int getAnswerTextAppearance() {
        return answerTextAppearance;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getQuestionTextColor() {
        return questionTextColor;
    }

    public int getQuestionTextAppearance() {
        return questionTextAppearance;
    }

    public int getSeparatorColor() {
        return separatorColor;
    }

    public int getCloseIconTint() {
        return closeIconTint;
    }

    public int getCreateIconTint() {
        return createIconTint;
    }

    public int getAddOptionColor() {
        return addOptionColor;
    }

    public int getQuestionHintColor() {
        return questionHintColor;
    }

    public int getAddOptionAppearance() {
        return addOptionAppearance;
    }
}
