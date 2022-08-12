package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.view.View;

import androidx.annotation.LayoutRes;

import com.cometchatworkspace.components.shared.primaryComponents.InputData;

public class DataItemConfiguration extends CometChatConfigurations {

    private InputData inputData;
    private int viewLayout;
    private View view;
    public InputData getInputData() {
        return inputData;
    }

    public DataItemConfiguration setInputData(InputData inputData) {
        this.inputData = inputData;
        return this;
    }

    public DataItemConfiguration setView(@LayoutRes int viewLayout) {
        this.viewLayout = viewLayout;
        return this;
    } public DataItemConfiguration setViewComponent(View viewLayout) {
        this.view = viewLayout;
        return this;
    }

    public View getViewLayout() {
        return view;
    }

    public @LayoutRes int getView() {
        return viewLayout;
    }
}
