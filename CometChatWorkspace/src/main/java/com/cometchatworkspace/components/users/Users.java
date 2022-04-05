package com.cometchatworkspace.components.users;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.message_list.CometChatMessagesActivity;
import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.UserListItemConfigurations;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.UsersListConfiguration;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatTags.CometChatTags;

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

    String TAG = "CometChatUserList";

    static CometChatUsers cometChatUsers;

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

        cometChatUsers.addListener("TAG", new CometChatUsers.Events<User>() {

            @Override
            public void OnItemClick(User var, int position) {
                CometChatMessagesActivity.launch(getContext(),var);

            }

            @Override
            public void onMenuIconClick(String id) {
                CometChatTags tags = new CometChatTags();
                List<String> activities = new ArrayList<>();
                activities.add("Baseball");
                activities.add("Golf");
                activities.add("Football");
                tags.setListOfTags("Activity", activities);

                List<String> listOfTag = new ArrayList<>();
                listOfTag.add("Adventure");
                listOfTag.add("Rookie");
                listOfTag.add("Coder");
                listOfTag.add("Creative");
                tags.setListOfTags("Hobbies", listOfTag);
                tags.show(getFragmentManager(), tags.getTag());

                tags.addListener(new CometChatTags.Event() {
                    @Override
                    public void onTagsFinalize(List<String> tags) {
                        Log.e(TAG, "onTagsFinalize: " + tags.toString());
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}
