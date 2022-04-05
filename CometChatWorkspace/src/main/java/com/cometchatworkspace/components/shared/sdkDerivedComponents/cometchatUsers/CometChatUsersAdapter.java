package com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatUsers;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.UserListItemConfigurations;
import com.cometchatworkspace.databinding.CometchatUserListRowBinding;
import com.cometchatworkspace.resources.utils.sticker_header.StickyHeaderAdapter;
import com.cometchatworkspace.resources.utils.FontUtils;

/**
 * Purpose - UserListAdapter is a subclass of RecyclerView Adapter which is used to display
 * the list of users. It helps to organize the users in recyclerView.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 23rd March 2022
 */

public class CometChatUsersAdapter extends RecyclerView.Adapter<CometChatUsersAdapter.UserViewHolder>
        implements StickyHeaderAdapter<CometChatUsersAdapter.InitialHolder> {

    private final Context context;

    private List<User> userArrayList = new ArrayList<>();

    private static final String TAG = "UserListAdapter";

    private final FontUtils fontUtils;

    private int headerColor = 0;
    private int headerAppearance = 0;
    private boolean isAvatarHidden, isHideUserPresenceListItem, isTitleHidden, isSubtitleHidden;
    private int titleColor, subTitleColor, backgroundColor;
    private float cornerRadius;
    private CometChatConfigurations configuration;
    List<CometChatConfigurations> configurations = new ArrayList<>();

    /**
     * It is a contructor which is used to initialize wherever we needed.
     *
     * @param context is a object of Context.
     */
    public CometChatUsersAdapter(Context context) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    /**
     * It is constructor which takes userArrayList as parameter and bind it with userArrayList in adapter.
     *
     * @param context       is a object of Context.
     * @param userArrayList is a list of users used in this adapter.
     */
    public CometChatUsersAdapter(Context context, List<User> userArrayList) {
        this.userArrayList = userArrayList;
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        CometchatUserListRowBinding userListRowBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.cometchat_user_list_row, parent, false);

        return new UserViewHolder(userListRowBinding);
    }

    /**
     * This method is used to bind the UserViewHolder contents with user at given
     * position. It set username userAvatar in respective UserViewHolder content.
     *
     * @param userViewHolder is a object of UserViewHolder.
     * @param i              is a position of item in recyclerView.
     * @see User
     * @see CometChatUserListItem
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {

        final User user = userArrayList.get(i);
        User user1 = i + 1 < userArrayList.size() ? userArrayList.get(i + 1) : null;

        userViewHolder.userListRowBinding.userListItem.hideSeperator(user1 != null && user.getName().toLowerCase().substring(0, 1).toCharArray()[0] == user1.getName().substring(0, 1).toLowerCase().toCharArray()[0]);
        userViewHolder.userListRowBinding.executePendingBindings();
        userViewHolder.userListRowBinding.userListItem.setTag(R.string.user, user);

        userViewHolder.userListRowBinding.userListItem.setUser(user);

        /**
         * @InputData is a class which is helpful to set data into the view and control visibility
         * as per value passed in constructor .
         * i.e we can control the visibility of the component inside the CometChatUserListItem,
         * and also decide what value i need to show in that particular view
         */
//        InputData inputData=new InputData(user.getUid(),user.getAvatar(),user.getName(),user.getStatus(),user.getStatus());

//        userViewHolder.userListRowBinding.userListItem.inputData(inputData);


        if (configuration instanceof UserListItemConfigurations) {
            userViewHolder.userListRowBinding.userListItem.inputData(((UserListItemConfigurations) configuration).get());
        }

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    /**
     * This method is used to update the users of userArrayList in adapter.
     *
     * @param users is a list of updated user.
     */
    public void updateList(List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            if (userArrayList.contains(users.get(i))) {
                int index = userArrayList.indexOf(users.get(i));
                userArrayList.remove(index);
                userArrayList.add(index, users.get(i));
            } else {
                userArrayList.add(users.get(i));
            }
        }
        notifyDataSetChanged();
    }


    /**
     * This method is used to update particular user in userArrayList of adapter.
     *
     * @param user is a object of User which will updated in userArrayList.
     * @see User
     */
    public void updateUser(User user) {
        if (userArrayList.contains(user)) {
            int index = userArrayList.indexOf(user);
            userArrayList.remove(index);
            userArrayList.add(index, user);
            notifyItemChanged(index);
        } else {
            userArrayList.add(user);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    /**
     * This method is used to remove particular user from userArrayList of adapter.
     *
     * @param user is a object of user which will be removed from userArrayList.
     * @see User
     */
    public void removeUser(User user) {
        if (userArrayList.contains(user)) {
            int index = userArrayList.indexOf(user);
            this.userArrayList.remove(user);
            notifyItemRemoved(index);
        }

    }

    @Override
    public long getHeaderId(int var1) {
        User user = this.userArrayList.get(var1);
        char name = user.getName() != null && !user.getName().isEmpty() ? user.getName().substring(0, 1).toUpperCase().toCharArray()[0] : '#';
        return (int) name;

    }

    @Override
    public InitialHolder onCreateHeaderViewHolder(ViewGroup var1) {
        return new InitialHolder(LayoutInflater.from(var1.getContext())
                .inflate(R.layout.cometchat_userlist_header, var1, false));
    }

    @Override
    public void onBindHeaderViewHolder(InitialHolder var1, int var2, long var3) {
        User user = userArrayList.get(var2);
        char name = user.getName() != null && !user.getName().isEmpty() ? user.getName().substring(0, 1).toCharArray()[0] : '#';
        if (headerColor != 0)
            var1.textView.setTextColor(headerColor);
        if (headerAppearance != 0)
            var1.textView.setTextAppearance(context, headerAppearance);
        var1.textView.setText(String.valueOf(name));
    }

    /**
     * This method is used to set list of search user with a userArrayList in adapter.
     *
     * @param users is a list of searched users.
     */
    public void searchUser(List<User> users) {
        this.userArrayList = users;
        notifyDataSetChanged();
    }

    /**
     * This method is used to add a user in userArrayList.
     *
     * @param user is a object of user which will be added in userArrayList.
     * @see User
     */
    public void add(User user) {
        updateUser(user);
    }

    /**
     * This method is used to add a user at particular position in userArrayList of adapter.
     *
     * @param index is a postion where user will be addded.
     * @param user  is a object of User which will be added.
     * @see User
     */
    public void add(int index, User user) {
        userArrayList.add(index, user);
        notifyItemInserted(index);

    }

    /**
     * This method is used to update a user of particular position in userArrayList.
     *
     * @param index is a position of user.
     * @param user  is a object of User which will be updated at given position in userArrayList.
     * @see User
     */
    public void updateUser(int index, User user) {
        if (userArrayList.contains(user)) {
            userArrayList.remove(user);
            userArrayList.add(index, user);
            notifyDataSetChanged();
        }
    }

    /**
     * This method is used to remove user from particular position in userArrayList.
     *
     * @param index is position of user which will be removed.
     */
    public void removeUser(int index) {
        if (userArrayList.size() < index) {
            userArrayList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clear() {
        userArrayList.clear();
        notifyDataSetChanged();
    }

    public void setHeaderColor(int color) {
        headerColor = color;
        notifyDataSetChanged();
    }

    public void setHeaderAppearance(int appearance) {
        headerAppearance = appearance;
        notifyDataSetChanged();
    }

    public User getItemAtPosition(int position) {
        return userArrayList.get(position);
    }

    public void setUserListItemProperty(boolean hideAvatar, boolean hideUserPresenceListItem, boolean hideTitleListItem, int titleColorListItem, boolean hideSubtitleListItem, int subTitleColorListItem, int backgroundColorListItem, float cornerRadiusListItem) {
        isAvatarHidden = hideAvatar;
        isHideUserPresenceListItem = hideUserPresenceListItem;
        isTitleHidden = hideTitleListItem;
        titleColor = titleColorListItem;
        isSubtitleHidden = hideSubtitleListItem;
        subTitleColor = subTitleColorListItem;
        backgroundColor = backgroundColorListItem;
        cornerRadius = cornerRadiusListItem;
        notifyDataSetChanged();

    }


    public void setConfiguration(CometChatConfigurations configuration) {
        this.configuration = configuration;
    }

    public void setConfiguration(List<CometChatConfigurations> configurations) {
        this.configurations = configurations;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        CometchatUserListRowBinding userListRowBinding;

        UserViewHolder(CometchatUserListRowBinding userListRowBinding) {
            super(userListRowBinding.getRoot());
            this.userListRowBinding = userListRowBinding;

        }

    }

    class InitialHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        InitialHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_char);
        }
    }
}
