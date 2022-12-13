package com.cometchatworkspace.components.users;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.groups.CometChatGroups;
import com.cometchatworkspace.components.messages.message_list.CometChatMessagesActivity;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ComposerConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.HeaderConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageListConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CometChatUsersWithMessages extends CometChatUsers {

    private Context context;
    private List<CometChatConfigurations> messageConfig = new ArrayList<>();

    public CometChatUsersWithMessages(Context context) {
        super(context);
        initView(context);
    }

    public CometChatUsersWithMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public CometChatUsersWithMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    private void initView(Context context) {
        this.context = context;



    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        CometChatUserEvents.addUserListener("CometChatUsersWithMessages", new CometChatUserEvents() {
            @Override
            public void onError(CometChatException error) {

            }

            @Override
            public void onItemClick(User user, int position) {
                if (context != null)
                    CometChatMessagesActivity.launch(context, user,messageConfig);
            }

            @Override
            public void onItemLongClick(User user, int position) {

            }

            @Override
            public void onUserBlock(User user) {

            }

            @Override
            public void onUserUnblock(User user) {

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CometChatUserEvents.removeListener();

    }
    public void setConfiguration(CometChatConfigurations configuration){
        if (configuration instanceof CometChatMessagesConfigurations ||
                configuration instanceof MessageBubbleConfiguration ||
                configuration instanceof HeaderConfiguration ||
                configuration instanceof MessageListConfiguration ||
                configuration instanceof ComposerConfiguration) {
            messageConfig.add(configuration);
        } else {
            super.setConfiguration(configuration);
        }
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        for (CometChatConfigurations cometChatConfigurations : configurations) {
            setConfiguration(cometChatConfigurations);
        }
    }
}
