package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;

public class HeaderConfiguration extends CometChatConfigurations {

    private InputData inputData;
    private AvatarConfiguration avatarConfig;
    private Style style;

    public HeaderConfiguration setAvatarConfiguration(AvatarConfiguration configuration) {
        avatarConfig = configuration;
        return this;
    }
    public AvatarConfiguration getAvatarConfiguration() {
        return avatarConfig;
    }

    public HeaderConfiguration setInputData(InputData data) {
        inputData = data;
        return this;
    }

    public HeaderConfiguration setStyle(Style style) {
        this.style = style;
        return this;
    }

    public Style getStyle() {
        return style;
    }
    public InputData getInputData() {
        return inputData;
    }
}
