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

import java.util.List;

public class CometChatUsersWithMessages extends CometChatUsers {

    private Context context;

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
                    CometChatMessagesActivity.launch(context, user);
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
}
