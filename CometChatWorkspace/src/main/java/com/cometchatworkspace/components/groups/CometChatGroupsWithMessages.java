package com.cometchatworkspace.components.groups;

import android.content.Context;
import android.util.AttributeSet;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.components.messages.message_list.CometChatMessagesActivity;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatMessagesConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.ComposerConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.HeaderConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageBubbleConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.MessageListConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CometChatGroupsWithMessages extends CometChatGroups {

    private Context context;
    private List<CometChatConfigurations> messageConfig = new ArrayList<>();

    public CometChatGroupsWithMessages(Context context) {
        super(context);
        initView(context);
    }

    public CometChatGroupsWithMessages(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public CometChatGroupsWithMessages(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    private void initView(Context context) {
        this.context = context;


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        CometChatGroupEvents.addGroupListener("CometChatGroupsWithMessages", new CometChatGroupEvents() {
            @Override
            public void onItemClick(Group group, int position) {
                if (context != null && group.isJoined())
                    CometChatMessagesActivity.launch(context, group,messageConfig);
            }

            @Override
            public void onGroupCreate(Group group) {
                if (context != null)
                    CometChatMessagesActivity.launch(context, group,messageConfig);
            }
            @Override
            public void onGroupMemberJoin(User joinedUser, Group joinedGroup) {
                if (context != null)
                    CometChatMessagesActivity.launch(context, joinedGroup,messageConfig);

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CometChatGroupEvents.removeListener();
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
