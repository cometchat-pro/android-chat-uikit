package com.cometchatworkspace.components.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.models.Group;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.DataItemConfiguration;


public class Groups extends Fragment {
    CometChatGroupsWithMessages cometChatGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        cometChatGroups = view.findViewById(R.id.group_list);
//        cometChatGroups.addListener("Groups", new CometChatGroups.Events<Group>() {
//            @Override
//            public void OnItemClick(Group var, int position) {
//
////                if (getContext() != null && var != null)ƒ
////                    CometChatMessagesActivity.launch(getContext(), var);
//
//            }
//
//            @Override
//            public void onCreateIconClick() {
//                Log.e("create", "onCreateIconClick: ");
//            }
//        });
        return view;
    }


}