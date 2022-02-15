package com.cometchatworkspace.components.shared.primaryComponents;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionSheet;

public class CometChatTheme {
    public static String primaryColor = "#3399FF";
    public static int urlColor = R.color.dark_blue;
    public static int phoneNumberColor = R.color.dark_blue;
    public static int emailColor = R.color.dark_blue;

    public static class Avatar {
        public static int cornerRadius = 16;
        public static int backgroundColor = R.color.colorPrimary;
        public static int borderColor = R.color.colorPrimaryDark;
        public static float borderWidth = 1f;
    }

    public static class StatusIndicator {
//        public static int onlineColor = R.color.online_green;
//        public static int offlineColor = R.color.offline;
//        public static int borderWidth = 5;
//        public static int cornerRadius = 16;
//        public static int borderColor = R.color.light_grey;
    }
    public static class BadgeCount {
        public static int textColor = R.color.textColorWhite;
        public static int backgroundColor = R.color.colorPrimary;
        public static int borderColor = R.color.grey;
        public static int textSize = 12;
        public static int borderWidth = 1;
        public static int cornerRadius = 16;
    }

    public static class ActionSheetLayout {
        public static int columnCount = 2;
        public static String mode = ActionSheet.LayoutMode.listMode;
        public static int reactionCount = 6;
    }

    public static class ConversationList {
        public static int backgroundColor = android.R.color.transparent;
        public static int searchBoxBackgroundColor = R.color.light_grey;
        public static int searchBoxTextColor = R.color.primaryTextColor;
        public static boolean hideTitle = false;
        public static String title = "Chats";
    }

    public static class MessageList {
        public static int backgroundColor = android.R.color.transparent;
        public static String actionSheetMode = ActionSheet.LayoutMode.listMode;
        public static String messageAlignment = CometChatMessageList.STANDARD;
    }

    public static class Composer {
        public static int sendIconTint = R.color.colorPrimary;
    }
}
