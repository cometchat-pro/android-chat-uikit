package com.cometchat.chatuikit.extensions.imagemoderation;

import androidx.annotation.DrawableRes;

public class ImageModerationConfiguration {

    private @DrawableRes
    int sensitiveContentBackgroundImage;
    private @DrawableRes
    int warningImageIcon;
    private String warningText;
    private ImageModerationStyle style;
    private String alertText;
    private String positiveButtonText;
    private String negativeButtonText;

    public ImageModerationConfiguration setSensitiveContentBackgroundImage(int sensitiveContentBackgroundImage) {
        this.sensitiveContentBackgroundImage = sensitiveContentBackgroundImage;
        return this;
    }

    public ImageModerationConfiguration setAlertText(String alertText) {
        this.alertText = alertText;
        return this;
    }

    public ImageModerationConfiguration setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    public ImageModerationConfiguration setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    public ImageModerationConfiguration setWarningImageIcon(int warningImageIcon) {
        this.warningImageIcon = warningImageIcon;
        return this;
    }

    public ImageModerationConfiguration setWarningText(String warningText) {
        this.warningText = warningText;
        return this;
    }

    public ImageModerationConfiguration setStyle(ImageModerationStyle style) {
        this.style = style;
        return this;
    }

    public int getSensitiveContentBackgroundImage() {
        return sensitiveContentBackgroundImage;
    }

    public int getWarningImageIcon() {
        return warningImageIcon;
    }

    public String getWarningText() {
        return warningText;
    }

    public ImageModerationStyle getStyle() {
        return style;
    }

    public String getAlertText() {
        return alertText;
    }

    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public String getNegativeButtonText() {
        return negativeButtonText;
    }
}
