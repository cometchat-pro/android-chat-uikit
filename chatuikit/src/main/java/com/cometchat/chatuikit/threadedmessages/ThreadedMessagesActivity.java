package com.cometchat.chatuikit.threadedmessages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.CometChatMessageBubble;
import com.cometchat.pro.models.BaseMessage;

public class ThreadedMessagesActivity extends AppCompatActivity {
    private static BaseMessage baseMessage;
    private static CometChatMessageBubble messageBubble;
    private CometChatThreadedMessages threadedMessages;
    private static ThreadedMessagesConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threaded_messages);
        threadedMessages = findViewById(R.id.threaded_messages);
        threadedMessages.setMessageBubbleView((context, baseMessage) -> messageBubble);
        threadedMessages.setParentMessage(baseMessage);
        if (configuration != null) {
            threadedMessages.setCloseIcon(configuration.getCloseIcon());
            threadedMessages.setTitle(configuration.getTitle());
            threadedMessages.setMessageBubbleView(configuration.getMessageBubbleView());
            threadedMessages.setMessageActionView(configuration.getMessageActionView());
            threadedMessages.setMessageListConfiguration(configuration.getMessageListConfiguration());
            threadedMessages.setMessageComposerConfiguration(configuration.getMessageComposerConfiguration());
            threadedMessages.setStyle(configuration.getStyle());
        }
    }

    public static void launch(Context context, BaseMessage baseMessage, CometChatMessageBubble messageBubble) {
        ThreadedMessagesActivity.baseMessage = baseMessage;
        ThreadedMessagesActivity.messageBubble = messageBubble;
        context.startActivity(new Intent(context, ThreadedMessagesActivity.class));
    }

    public static void launch(Context context, BaseMessage baseMessage, CometChatMessageBubble messageBubble, ThreadedMessagesConfiguration threadedMessagesConfiguration) {
        ThreadedMessagesActivity.baseMessage = baseMessage;
        ThreadedMessagesActivity.messageBubble = messageBubble;
        ThreadedMessagesActivity.configuration = threadedMessagesConfiguration;
        context.startActivity(new Intent(context, ThreadedMessagesActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseMessage = null;
        messageBubble = null;
        threadedMessages = null;
        configuration = null;
    }
}