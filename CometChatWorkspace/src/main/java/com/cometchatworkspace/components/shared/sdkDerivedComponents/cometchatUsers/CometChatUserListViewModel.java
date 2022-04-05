package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatUsers;

import android.content.Context;

import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.User;

import java.util.List;

import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.resources.utils.sticker_header.StickyHeaderDecoration;

public class CometChatUserListViewModel {

    private static final String TAG = "UserListViewModel";

    private  Context context;

    private CometChatUsersAdapter userListAdapter;

    private CometChatUserList userListView;



    public CometChatUserListViewModel(Context context, CometChatUserList cometChatUserList, boolean showHeader){
        this.userListView=cometChatUserList;
        this.context=context;
        setUserListAdapter(cometChatUserList,showHeader);
    }

    private CometChatUsersAdapter getAdapter() {
        if (userListAdapter==null){
            userListAdapter=new CometChatUsersAdapter(context);
        }
        return userListAdapter;
    }

    public void add(User user){
        if (userListAdapter!=null)
            userListAdapter.add(user);

    }
    public void add(int index,User user){
        if (userListAdapter!=null)
            userListAdapter.add(index,user);

    }

    public void update(User user){
        if (userListAdapter!=null)
            userListAdapter.updateUser(user);

    }

    public void remove(User user){
        if (userListAdapter!=null)
            userListAdapter.removeUser(user);

    }
    public void remove(int index){
        if (userListAdapter!=null)
            userListAdapter.removeUser(index);
    }

    public void clear()
    {
        if (userListAdapter!=null)
            userListAdapter.clear();
    }
    private void setUserListAdapter(CometChatUserList cometChatUserList, boolean showHeader){
        userListAdapter=new CometChatUsersAdapter(context);
        if(showHeader) {
            StickyHeaderDecoration stickyHeaderDecoration = new StickyHeaderDecoration(userListAdapter);
            cometChatUserList.getRecyclerView().addItemDecoration(stickyHeaderDecoration, 0);
        }
        cometChatUserList.getRecyclerView().setAdapter(userListAdapter);
    }

    public void setUsersList(List<User> usersList){
          getAdapter().updateList(usersList);
    }

    public void update(int index, User user) {
        if (userListAdapter!=null)
            userListAdapter.updateUser(index,user);
    }

    public void searchUserList(List<User> userList) {
        if (userListAdapter!=null)
            userListAdapter.searchUser(userList);
    }

    public void setHeaderColor(int color) {
        if (userListAdapter!=null && color!=0)
            userListAdapter.setHeaderColor(color);

    }
    public void setHeaderTextAppearance(int Appearance){
        if(userListAdapter!=null && Appearance!=0)
            userListAdapter.setHeaderAppearance(Appearance);
    }

    public int size() {
        if (userListAdapter!=null)
            return userListAdapter.getItemCount();
        else
            return 0;
    }

    public User getUser(int position) {
        User user = null;
        if (userListAdapter!=null)
            user = userListAdapter.getItemAtPosition(position);
        return user;
    }

    public void setUserListItemProperty(boolean hideAvatar, boolean hideUserPresenceListItem,
                                                boolean hideTitleListItem, int titleColorListItem,
                                                boolean hideSubtitleListItem, int subTitleColorListItem,
                                                int backgroundColorListItem, float cornerRadiusListItem) {
        if (userListAdapter!=null)
            userListAdapter.setUserListItemProperty(hideAvatar,
                    hideUserPresenceListItem,hideTitleListItem,titleColorListItem,
                    hideSubtitleListItem,subTitleColorListItem,
                    backgroundColorListItem,cornerRadiusListItem);
    }

    public void setConfiguration(CometChatConfigurations configuration) {
        userListAdapter.setConfiguration(configuration);
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        userListAdapter.setConfiguration(configurations);

    }
}

