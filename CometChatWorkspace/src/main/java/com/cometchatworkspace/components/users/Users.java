package com.cometchatworkspace.components.users;

import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.CometChatConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.DataItemConfiguration;

import java.util.ArrayList;
import java.util.List;


/**
 * Purpose - Users class is a fragment used to display list of conversations and perform certain action on click of item.
 * It also provide search bar to perform search operation on the list of conversations.User can search by username, groupname, last message of conversation.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 23rd March 2020
 **/

public class Users extends Fragment {


    static CometChatUsersWithMessages cometChatUsers;

    View view;

    public Users() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cometchat_userlist, container, false);
        cometChatUsers = view.findViewById(R.id.userList);
        return view;
    }

}
