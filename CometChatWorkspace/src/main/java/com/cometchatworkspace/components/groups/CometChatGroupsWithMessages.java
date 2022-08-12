package com.cometchatworkspace.components.groups;

import android.content.Context;
import android.util.AttributeSet;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.components.messages.message_list.CometChatMessagesActivity;

import java.util.List;

public class CometChatGroupsWithMessages extends CometChatGroups {

    private Context context;

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

            }

            @Override
            public void onGroupCreate(Group group) {
                if (context != null)
                    CometChatMessagesActivity.launch(context, group);
            }

            @Override
            public void onError(CometChatException error) {

            }

            @Override
            public void onGroupDelete(Group group) {

            }

            @Override
            public void onGroupMemberLeave(User leftUser, Group leftGroup) {

            }

            @Override
            public void onGroupMemberChangeScope(User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {

            }

            @Override
            public void onGroupMemberBan(User bannedUser, User bannedBy, Group bannedFrom) {

            }

            @Override
            public void onGroupMemberAdd(User addedBy, List<User> usersAdded, Group group) {

            }

            @Override
            public void onGroupMemberKick(User kickedUser, User kickedBy, Group kickedFrom) {

            }

            @Override
            public void onGroupMemberUnban(User unbannedUser, User unbannedBy, Group unBannedFrom) {

            }

            @Override
            public void onGroupMemberJoin(User joinedUser, Group joinedGroup) {

                if (context != null)
                    CometChatMessagesActivity.launch(context, joinedGroup);

            }

            @Override
            public void onOwnershipChange(Group group, GroupMember member) {

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CometChatGroupEvents.removeListener();
    }
}
