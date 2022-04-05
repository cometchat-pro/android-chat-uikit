package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

import com.cometchatworkspace.components.shared.primaryComponents.InputData;

public class GroupListItemConfiguration extends CometChatConfigurations {

    InputData inputData;

    public InputData get() {
        return inputData;
    }

    public void set(InputData inputData) {
        this.inputData = inputData;
    }
}
