package com.cometchat.chatuikit.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.R;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

public class MessageActivity extends AppCompatActivity {
    private CometChatMessages cometChatMessages;

    private static User user;

    private static Group group;

    private static MessagesConfiguration messageConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        cometChatMessages = findViewById(R.id.message_component);
        if (getIntent() != null) {
            if (user != null) cometChatMessages.setUser(user);
            if (group != null) cometChatMessages.setGroup(group);
        }
        if (messageConfiguration != null) {
            cometChatMessages.disableSoundForMessages(messageConfiguration.isDisableSoundForMessages());
            cometChatMessages.disableTyping(messageConfiguration.isDisableTyping());
            cometChatMessages.hideMessageComposer(messageConfiguration.isHideMessageComposer());
            cometChatMessages.setMessageComposerConfiguration(messageConfiguration.getMessageComposerConfiguration());
            cometChatMessages.setStyle(messageConfiguration.getStyle());
            cometChatMessages.setMessageListConfiguration(messageConfiguration.getMessageListConfiguration());
            cometChatMessages.setMessageHeaderView(messageConfiguration.getMessageHeaderView());
            cometChatMessages.setMessageHeaderConfiguration(messageConfiguration.getMessageHeaderConfiguration());
            cometChatMessages.setMessageComposerView(messageConfiguration.getMessageComposerView());
            cometChatMessages.hideMessageHeader(messageConfiguration.isHideMessageHeader());
            cometChatMessages.setMessageListView(messageConfiguration.getMessageListView());
            cometChatMessages.setCustomSoundForIncomingMessages(messageConfiguration.getCustomSoundForIncomingMessages());
            cometChatMessages.setCustomSoundForOutgoingMessages(messageConfiguration.getCustomSoundForOutgoingMessages());
            cometChatMessages.setThreadedMessagesConfiguration(messageConfiguration.getThreadedMessagesConfiguration());
            cometChatMessages.setDetailsConfiguration(messageConfiguration.getDetailsConfiguration());
            cometChatMessages.hideDetails(messageConfiguration.isHideDetails());
        }
    }

    public static void launch(Context context, User user_) {
        group = null;
        user = user_;
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    public static void launch(Context context, User user_, MessagesConfiguration configurations) {
        group = null;
        user = user_;
        messageConfiguration = configurations;
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    public static void launch(Context context, Group group_) {
        user = null;
        group = group_;
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    public static void launch(Context context, Group group_, MessagesConfiguration configurations) {
        user = null;
        group = group_;
        messageConfiguration = configurations;
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        user = null;
        group = null;
        messageConfiguration = null;
    }
}