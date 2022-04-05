package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;
import android.renderscript.ScriptGroup;

import com.cometchatworkspace.components.shared.primaryComponents.InputData;

public class UserListItemConfigurations extends CometChatConfigurations {

    InputData inputData;

    public InputData get() {
        return inputData;
    }

    public void set(InputData inputData) {
        this.inputData = inputData;
    }


}
