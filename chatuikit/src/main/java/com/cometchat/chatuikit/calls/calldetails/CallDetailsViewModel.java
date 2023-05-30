package com.cometchat.chatuikit.calls.calldetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

 class CallDetailsViewModel extends ViewModel {
    public User user;
    public Group group;
    private MutableLiveData<User> userMutableLiveData;
    private String LISTENER_ID;

    public CallDetailsViewModel() {
        userMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUser(User user) {
        if (user != null)
            this.user = user;
    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
        }
    }

    public void addUserListener() {
        LISTENER_ID = System.currentTimeMillis() + "";
        CometChat.addUserListener(LISTENER_ID, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user_) {
                if (user_.getUid().equalsIgnoreCase(user.getUid()))
                    updateUserStatus(user_);
            }

            @Override
            public void onUserOffline(User user_) {
                if (user_.getUid().equalsIgnoreCase(user.getUid()))
                    updateUserStatus(user_);
            }
        });
    }

    public void removeListener() {
        CometChat.removeUserListener(LISTENER_ID);
    }

    public void updateUserStatus(User user_) {
        if (user_ != null && user != null) {
            user.setStatus(user_.getStatus());
            userMutableLiveData.setValue(user);
        }
    }

}
