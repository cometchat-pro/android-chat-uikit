package com.cometchatworkspace.components.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.models.Group;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.primaryComponents.Style;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.AvatarConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.GroupListItemConfiguration;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.GroupsListConfiguration;


public class Groups extends Fragment {
    CometChatGroups cometChatGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        cometChatGroups = view.findViewById(R.id.group_list);

        cometChatGroups.addListener("create", new CometChatGroups.Events<Group>() {
            @Override
            public void OnItemClick(Group var, int position) {

            }

            @Override
            public void onCreateIconClick() {
                Log.e("create", "onCreateIconClick: ");
            }
        });

        return view;
    }
}