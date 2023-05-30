package com.cometchat.chatuikit.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.details.CometChatDetails;
import com.cometchat.chatuikit.details.DetailsConfiguration;
import com.cometchat.chatuikit.groups.CometChatGroupActivity;
import com.cometchat.pro.models.User;

public class CometChatUserActivity extends AppCompatActivity {

    private static User user;
    private static Class component;
    private static DetailsConfiguration detailsConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);
        RelativeLayout root = findViewById(R.id.root);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (component == CometChatDetails.class) {
            CometChatDetails cometChatDetails = new CometChatDetails(this);
            cometChatDetails.setLayoutParams(params);
            root.addView(cometChatDetails);
            if (user != null) cometChatDetails.setUser(user);
            if (detailsConfiguration != null) {
                cometChatDetails.showCloseButton(detailsConfiguration.isShowCloseButton());
                cometChatDetails.setCloseButtonIcon(detailsConfiguration.getCloseButtonIcon());
                cometChatDetails.hideProfile(detailsConfiguration.isHideProfile());
                cometChatDetails.setSubtitleView(detailsConfiguration.getSubtitleView());
                cometChatDetails.setCustomProfileView(detailsConfiguration.getCustomProfileView());
                cometChatDetails.setData(detailsConfiguration.getData());
                cometChatDetails.setMenu(detailsConfiguration.getMenu());
                cometChatDetails.disableUserPresence(detailsConfiguration.isDisableUsersPresence());
                cometChatDetails.setProtectedGroupIcon(detailsConfiguration.getProtectedGroupIcon());
                cometChatDetails.setPrivateGroupIcon(detailsConfiguration.getPrivateGroupIcon());
                cometChatDetails.setStyle(detailsConfiguration.getStyle());
                cometChatDetails.setListItemStyle(detailsConfiguration.getListItemStyle());
                cometChatDetails.setAvatarStyle(detailsConfiguration.getAvatarStyle());
                cometChatDetails.setStatusIndicatorStyle(detailsConfiguration.getStatusIndicatorStyle());
            }
        }
    }

    public static void launch(Context context, Class className) {
        component = className;
        context.startActivity(new Intent(context, CometChatGroupActivity.class));
    }

    public static void launch(Context context, Class className, User u, DetailsConfiguration detailsConfiguration1) {
        user = u;
        component = className;
        detailsConfiguration = detailsConfiguration1;
        context.startActivity(new Intent(context, CometChatUserActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        user = null;
        component = null;
        detailsConfiguration = null;
    }
}
