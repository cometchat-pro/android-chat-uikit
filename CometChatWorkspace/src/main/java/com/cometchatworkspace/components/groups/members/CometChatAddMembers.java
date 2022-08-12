package com.cometchatworkspace.components.groups.members;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupEvents;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.users.CometChatUsers;
import com.cometchatworkspace.resources.utils.item_clickListener.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CometChatAddMembers extends CometChatUsers {

    private Palette palette;
    private static Group group;
    private Context context;

    public CometChatAddMembers(Context context) {
        super(context);
        initView(context,null);
    }

    public CometChatAddMembers(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,null);
    }

    public CometChatAddMembers(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,null);
    }

    private void initView(Context cntxt, AttributeSet attrs) {
        context = cntxt;
        setTitle(getResources().getString(R.string.add_members));
        setBackIcon(R.drawable.ic_arrow_back);
        palette = Palette.getInstance(cntxt);
        backIconTint(palette.getPrimary());
        showBackButton(true);
        cometchatUserList.setItemClickListener(new OnItemClickListener<User>() {
            @Override
            public void OnItemClick(User user, int position) {
                setSelectedUser(user);
            }
        });
        onAddMemberClick(this::addMembersToGroup);
    }

    public void setGroup(Group g) {
        group = g;
    }

    private void addMembersToGroup(List<GroupMember> groupMembers) {
        CometChat.addMembersToGroup(group.getGuid(), groupMembers, null, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> successMap) {
                int i = 0;
                for (Map.Entry<String, String> entry : successMap.entrySet()) {
                    if ("success".equals(entry.getValue()))
                        i++;
                }

                group.setMembersCount(group.getMembersCount() + i);
                List<User> members = new ArrayList<>(groupMembers);
                for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                    events.onGroupMemberAdd(CometChat.getLoggedInUser(), members, group);
                }
            }

            @Override
            public void onError(CometChatException e) {
                for (CometChatGroupEvents events : CometChatGroupEvents.groupEvents.values()) {
                    events.onError(e);
                }
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

