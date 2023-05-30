package com.cometchat.chatuikit.extensions.polls;

public class PollsConfiguration {
    private PollBubbleStyle pollBubbleStyle;
    private CreatePollsStyle createPollsStyle;

    public PollsConfiguration setPollBubbleStyle(PollBubbleStyle pollBubbleStyle) {
        this.pollBubbleStyle = pollBubbleStyle;
        return this;
    }

    public PollsConfiguration setCreatePollsStyle(CreatePollsStyle createPollsStyle) {
        this.createPollsStyle = createPollsStyle;
        return this;
    }

    public PollBubbleStyle getPollBubbleStyle() {
        return pollBubbleStyle;
    }

    public CreatePollsStyle getCreatePollsStyle() {
        return createPollsStyle;
    }
}
