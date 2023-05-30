package com.cometchat.chatuikit.calls.calldetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.R;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

 class CallDetailsActivity extends AppCompatActivity {

    private CometChatCallDetails callDetails;

    private static User user;

    private static Group group;

    private static BaseMessage baseMessage;

    private static CallDetailsConfiguration callDetailsConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_details);
        callDetails = findViewById(R.id.details);
        callDetails.setCall(baseMessage);

        if (callDetailsConfiguration != null) {
            callDetails.showCloseButton(callDetailsConfiguration.isShowCloseButton());
            callDetails.setCloseButtonIcon(callDetailsConfiguration.getCloseButtonIcon());
            callDetails.hideProfile(callDetailsConfiguration.isHideProfile());
            callDetails.setSubtitleView(callDetailsConfiguration.getSubtitleView());
            callDetails.setCustomProfileView(callDetailsConfiguration.getCustomProfileView());
            callDetails.setData(callDetailsConfiguration.getData());
            callDetails.setMenu(callDetailsConfiguration.getMenu());
            callDetails.setStyle(callDetailsConfiguration.getStyle());
            callDetails.setListItemStyle(callDetailsConfiguration.getListItemStyle());
            callDetails.setAvatarStyle(callDetailsConfiguration.getAvatarStyle());
            callDetails.setTitle(callDetailsConfiguration.getTitle());
            callDetails.setOnError(callDetailsConfiguration.getOnError());
            callDetails.setCallButtonsConfiguration(callDetailsConfiguration.getCallButtonsConfiguration());
        }
    }

    public static void launch(Context context, User user_, BaseMessage baseMessage_) {
        group = null;
        user = user_;
        baseMessage = baseMessage_;
        context.startActivity(new Intent(context, CallDetailsActivity.class));
    }

    public static void launch(Context context, User user_, BaseMessage baseMessage_, CallDetailsConfiguration configurations) {
        group = null;
        user = user_;
        baseMessage = baseMessage_;
        callDetailsConfiguration = configurations;
        context.startActivity(new Intent(context, CallDetailsActivity.class));
    }

    public static void launch(Context context, Group group_, BaseMessage baseMessage_) {
        user = null;
        group = group_;
        baseMessage = baseMessage_;
        context.startActivity(new Intent(context, CallDetailsActivity.class));
    }

    public static void launch(Context context, Group group_, BaseMessage baseMessage_, CallDetailsConfiguration configurations) {
        user = null;
        group = group_;
        callDetailsConfiguration = configurations;
        baseMessage = baseMessage_;
        context.startActivity(new Intent(context, CallDetailsActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}