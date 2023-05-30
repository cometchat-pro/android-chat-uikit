package com.cometchat.chatuikit.extensions.collaborative;

public class CollaborativeBoardBubbleConfiguration {

    private String title;
    private String subtitle;
    private String buttonText;
    private CollaborativeBoardBubbleStyle style;

    public CollaborativeBoardBubbleConfiguration setTitle(String title) {
        this.title = title;
        return this;
    }

    public CollaborativeBoardBubbleConfiguration setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public CollaborativeBoardBubbleConfiguration setButtonText(String buttonText) {
        this.buttonText = buttonText;
        return this;
    }

    public CollaborativeBoardBubbleConfiguration setStyle(CollaborativeBoardBubbleStyle style) {
        this.style = style;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public CollaborativeBoardBubbleStyle getStyle() {
        return style;
    }
}
