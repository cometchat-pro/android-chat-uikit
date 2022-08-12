package com.cometchatworkspace.components.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.groups.CometChatGroupActivity;
import com.cometchatworkspace.components.groups.CometChatJoinProtectedGroup;
import com.cometchatworkspace.components.groups.createGroup.CometChatCreateGroup;
import com.cometchatworkspace.components.groups.members.CometChatGroupMembers;
import com.cometchatworkspace.components.shared.primaryComponents.InputData;
import com.cometchatworkspace.components.shared.primaryComponents.configurations.DataItemConfiguration;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatDetails.CometChatDetails;

public class CometChatUserActivity extends AppCompatActivity {

    private static User user;
    private static Class component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);
        RelativeLayout root = findViewById(R.id.root);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (component== CometChatDetails.class) {
            CometChatDetails cometChatDetails = new CometChatDetails(this);
            cometChatDetails.setLayoutParams(params);
            root.addView(cometChatDetails);
            if (user != null)
                cometChatDetails.setUser(user);
        }
    }

    public static void launch(Context context, Class className) {
        component = className;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    public static void launch(Context context,Class className, User u) {
        user = u;
        component = className;
        context.startActivity(new Intent(context,CometChatUserActivity.class));
    }
}
