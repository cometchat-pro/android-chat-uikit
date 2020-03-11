package viewmodel;

import android.content.Context;

import com.cometchat.pro.uikit.CometChatUserList;
import com.cometchat.pro.models.User;

import java.util.List;

import adapter.UserListAdapter;

public class UserListViewModel {

    private static final String TAG = "UserListViewModel";

    private  Context context;

    private UserListAdapter userListAdapter;

    private CometChatUserList userListView;



    public UserListViewModel(Context context,CometChatUserList cometChatUserList){
        this.userListView=cometChatUserList;
        this.context=context;
        setUserListAdapter(cometChatUserList);
    }

    private UserListViewModel(){

    }

    private UserListAdapter getAdapter() {
        if (userListAdapter==null){
            userListAdapter=new UserListAdapter(context);
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

    private void setUserListAdapter(CometChatUserList cometChatUserList){
        userListAdapter=new UserListAdapter(context);
        cometChatUserList.setAdapter(userListAdapter);
    }

    public void setUsersList(List<User> usersList){
          getAdapter().updateList(usersList);
    }

    public void setUserListView(CometChatUserList userListView) {
        this.userListView = userListView;
    }

    public void update(int index, User user) {
        if (userListAdapter!=null)
            userListAdapter.updateUser(index,user);
    }
}

