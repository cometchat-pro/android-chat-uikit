package com.cometchat.chatuikit.users;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.events.CometChatUserEvents;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    public UsersRequest.UsersRequestBuilder usersRequestBuilder;

    public UsersRequest.UsersRequestBuilder searchUsersRequestBuilder;

    private UsersRequest usersRequest;

    public String LISTENERS_TAG;

    public MutableLiveData<List<User>> mutableUsersList;

    public MutableLiveData<Integer> insertAtTop;

    public MutableLiveData<Integer> moveToTop;

    public List<User> userArrayList;

    public MutableLiveData<Integer> updateUser;

    public MutableLiveData<Integer> removeUser;

    public MutableLiveData<CometChatException> cometChatException;

    public MutableLiveData<UIKitConstants.States> states;

    public int limit = 30;

    public boolean hasMore = true;

    public UsersViewModel() {
        mutableUsersList = new MutableLiveData<>();
        insertAtTop = new MutableLiveData<>();
        moveToTop = new MutableLiveData<>();
        userArrayList = new ArrayList<>();
        updateUser = new MutableLiveData<>();
        removeUser = new MutableLiveData<>();
        cometChatException = new MutableLiveData<>();
        states = new MutableLiveData<>();
        usersRequestBuilder = new UsersRequest.UsersRequestBuilder().setLimit(limit);
        searchUsersRequestBuilder = new UsersRequest.UsersRequestBuilder();
        usersRequest = usersRequestBuilder.build();
    }

    public MutableLiveData<List<User>> getMutableUsersList() {
        return mutableUsersList;
    }

    public MutableLiveData<Integer> insertAtTop() {
        return insertAtTop;
    }

    public MutableLiveData<Integer> moveToTop() {
        return moveToTop;
    }

    public List<User> getUserArrayList() {
        return userArrayList;
    }

    public MutableLiveData<Integer> updateUser() {
        return updateUser;
    }

    public MutableLiveData<Integer> removeUser() {
        return removeUser;
    }

    public MutableLiveData<CometChatException> getCometChatException() {
        return cometChatException;
    }

    public MutableLiveData<UIKitConstants.States> getStates() {
        return states;
    }

    public void addListeners() {
        LISTENERS_TAG = System.currentTimeMillis() + "";
        CometChat.addUserListener(LISTENERS_TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                moveToTop(user);
            }

            @Override
            public void onUserOffline(User user) {
                updateUser(user);
            }
        });
        CometChatUserEvents.addUserListener(LISTENERS_TAG, new CometChatUserEvents() {
            @Override
            public void ccUserBlocked(User user) {
                updateUser(user);
            }

            @Override
            public void ccUserUnblocked(User user) {
                updateUser(user);
            }
        });
    }

    public void removeListeners() {
        CometChat.removeUserListener(LISTENERS_TAG);
        CometChatUserEvents.removeListener(LISTENERS_TAG);
    }

    public void updateUser(User user) {
        if (userArrayList.contains(user)) {
            userArrayList.set(userArrayList.indexOf(user), user);
            updateUser.setValue(userArrayList.indexOf(user));
        }
    }

    public void moveToTop(User user) {
        if (userArrayList.contains(user)) {
            int oldIndex = userArrayList.indexOf(user);
            userArrayList.remove(user);
            userArrayList.add(0, user);
            moveToTop.setValue(oldIndex);
        }
    }

    public void addToTop(User user) {
        if (user != null && !userArrayList.contains(user)) {
            userArrayList.add(0, user);
            insertAtTop.setValue(0);
        }
    }

    public void removeUser(User user) {
        if (userArrayList.contains(user)) {
            int index = userArrayList.indexOf(user);
            this.userArrayList.remove(user);
            removeUser.setValue(index);
            states.setValue(checkIsEmpty(userArrayList));
        }
    }

    public void fetchUser() {
        if (userArrayList.size() == 0)
            states.setValue(UIKitConstants.States.LOADING);
        if (hasMore) {
            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    hasMore = users.size() != 0;
                    if (hasMore)
                        addList(users);
                    states.setValue(UIKitConstants.States.LOADED);
                    states.setValue(checkIsEmpty(userArrayList));
                }

                @Override
                public void onError(CometChatException e) {
                    cometChatException.setValue(e);
                    states.setValue(UIKitConstants.States.ERROR);
                }
            });
        }
    }

    public void searchUsers(String search) {
        clear();
        hasMore = true;
        if (search != null)
            usersRequest = searchUsersRequestBuilder.setSearchKeyword(search).build();
        else
            usersRequest = usersRequestBuilder.build();
        fetchUser();
    }

    public void addList(List<User> userList) {
        for (User user : userList) {
            if (userArrayList.contains(user)) {
                int index = userArrayList.indexOf(user);
                userArrayList.remove(index);
                userArrayList.add(index, user);
            } else {
                userArrayList.add(user);
            }
        }
        mutableUsersList.setValue(userArrayList);
    }

    private UIKitConstants.States checkIsEmpty(List<User> users) {
        if (users.isEmpty()) return UIKitConstants.States.EMPTY;
        return UIKitConstants.States.NON_EMPTY;
    }

    public void setUsersRequestBuilder(UsersRequest.UsersRequestBuilder usersRequest) {
        if (usersRequest != null) {
            this.usersRequestBuilder = usersRequest;
            this.usersRequest = usersRequestBuilder.build();
        }
    }

    public void setSearchRequestBuilder(UsersRequest.UsersRequestBuilder usersRequestBuilder) {
        if (usersRequestBuilder != null)
            this.searchUsersRequestBuilder = usersRequestBuilder;
    }

    public void clear() {
        userArrayList.clear();
        mutableUsersList.setValue(userArrayList);
    }

}
