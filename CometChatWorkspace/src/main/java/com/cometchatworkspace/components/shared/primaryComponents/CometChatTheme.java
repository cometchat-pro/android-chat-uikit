package com.cometchatworkspace.components.shared.primaryComponents;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionSheet;

public class CometChatTheme {
    public static String primaryColor = "#3399FF";
    public static int urlColor = R.color.dark_blue;
    public static int phoneNumberColor = R.color.dark_blue;
    public static int emailColor = R.color.dark_blue;

    public static final String PERSIAN = "PERSIAN";
    public static final String PERSIAN_DARK = "PERSIAN_DARk";
    public static final String MOUNTAIN_MEADOW = "MOUNTAIN_MEADOW";
    public static final String MOUNTAIN_MEADOW_DARK = "MOUNTAIN_MEADOW_DARK";
    public static final String AZURE_RADIANCE = "AZURE_RADIANCE";
    public static final String AZURE_RADIANCE_DARK = "AZURE_RADIANCE_DARK";
    public static final String DEFAULT_LIGHT = "DEFAULT_LIGHT";
    public static final String DEFAULT_DARK = "DEFAULT_DARK";

    public static final String DARK = "DARK";
    public static final String LIGHT = "LIGHT";
    private static String MODE = "";


    @SuppressLint("ResourceAsColor")
    private static CometChatPalette primary = new CometChatPalette(R.color.primary, R.color.primary_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette background = new CometChatPalette(R.color.background, R.color.background_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette error = new CometChatPalette(R.color.error, R.color.error_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent = new CometChatPalette(R.color.accent, R.color.accent_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent50 = new CometChatPalette(R.color.accent50, R.color.accent50_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent100 = new CometChatPalette(R.color.accent100, R.color.accent100_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent200 = new CometChatPalette(R.color.accent200, R.color.accent200_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent300 = new CometChatPalette(R.color.accent300, R.color.accent300_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent400 = new CometChatPalette(R.color.accent400, R.color.accent400_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent500 = new CometChatPalette(R.color.accent500, R.color.accent500_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent600 = new CometChatPalette(R.color.accent600, R.color.accent600_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent700 = new CometChatPalette(R.color.accent700, R.color.accent700_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent800 = new CometChatPalette(R.color.accent800, R.color.accent800_dark);
    @SuppressLint("ResourceAsColor")
    private static CometChatPalette accent900 = new CometChatPalette(R.color.accent900, R.color.accent900_dark);


    public static int getPrimary() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return primary.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return primary.getDark();
        } else {
            return primary.getLight();
        }

    }

    public static int getBackground() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return background.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return background.getDark();
        } else {
            return background.getLight();
        }

    }

    public static int getError() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return error.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return error.getDark();
        } else {
            return error.getLight();
        }
    }

    public static int getAccent() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent.getDark();
        } else {
            return accent.getLight();
        }
    }

    public static int getAccent50() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent50.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent50.getDark();
        } else {
            return accent50.getLight();
        }
    }

    public static int getAccent100() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent100.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent100.getDark();
        } else {
            return accent100.getLight();
        }
    }

    public static int getAccent200() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent200.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent200.getDark();
        } else {
            return accent200.getLight();
        }
    }

    public static int getAccent300() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent300.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent300.getDark();
        } else {
            return accent300.getLight();
        }
    }

    public static int getAccent400() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent400.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent400.getDark();
        } else {
            return accent400.getLight();
        }
    }

    public static int getAccent500() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent500.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent500.getDark();
        } else {
            return accent500.getLight();
        }
    }

    public static int getAccent600() {
        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent600.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent600.getDark();
        } else {
            return accent600.getLight();
        }
    }

    public static int getAccent700() {

        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent700.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent700.getDark();
        } else {
            return accent700.getLight();
        }

    }

    public static int getAccent800() {

        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent800.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent800.getDark();
        } else {
            return accent800.getLight();
        }
    }

    public static int getAccent900() {

        if (MODE.equalsIgnoreCase(CometChatTheme.LIGHT)) {
            return accent900.getLight();
        } else if (MODE.equalsIgnoreCase(CometChatTheme.DARK)) {
            return accent900.getDark();
        } else {
            return accent900.getLight();
        }

    }


    public void Primary(@NonNull CometChatPalette primary) {
        this.primary = primary != null ? primary : this.primary;
        setCustomTheme();
    }

    public void Background(@NonNull CometChatPalette background) {
        this.background = background != null ? background : this.background;
        setCustomTheme();
    }

    public void Error(CometChatPalette error) {
        this.error = error != null ? error : this.error;
        setCustomTheme();
    }

    public void Accent(CometChatPalette accent) {
        this.accent = accent != null ? accent : this.accent;
        setCustomTheme();
    }

    public void Accent50(CometChatPalette accent50) {
        this.accent50 = accent50 != null ? accent50 : this.accent50;
        setCustomTheme();
    }

    public void Accent100(CometChatPalette accent100) {
        this.accent100 = accent100 != null ? accent100 : this.accent100;
        setCustomTheme();
    }

    public void Accent200(CometChatPalette accent200) {
        this.accent200 = accent200 != null ? accent200 : this.accent200;
        setCustomTheme();
    }

    public void Accent300(CometChatPalette accent300) {
        this.accent300 = accent300 != null ? accent300 : this.accent300;
        setCustomTheme();
    }

    public void Accent400(CometChatPalette accent400) {
        this.accent500 = accent500 != null ? accent500 : this.accent500;
        setCustomTheme();
    }

    public void Accent500(CometChatPalette accent500) {
        this.accent500 = accent500 != null ? accent500 : this.accent500;
        setCustomTheme();
    }

    public void Accent600(CometChatPalette accent600) {
        this.accent600 = accent600 != null ? accent600 : this.accent600;
        setCustomTheme();
    }

    public void Accent700(CometChatPalette accent700) {
        this.accent700 = accent700 != null ? accent700 : this.accent700;
        setCustomTheme();
    }

    public void Accent800(CometChatPalette accent800) {
        this.accent800 = accent800 != null ? accent800 : this.accent800;
        setCustomTheme();
    }

    public void Accent900(CometChatPalette accent900) {
        this.accent900 = accent900 != null ? accent900 : this.accent900;
        setCustomTheme();
    }

    public CometChatTheme() {
        setTheme(DEFAULT_LIGHT);
    }

    public CometChatTheme(String CometChatTheme) {
        setTheme(CometChatTheme);
    }

    public CometChatTheme(String mode, CometChatPalette primary_color,
                          CometChatPalette background_color,
                          CometChatPalette error_color,
                          CometChatPalette accent_color,
                          CometChatPalette accent50_color,
                          CometChatPalette accent100_color,
                          CometChatPalette accent200_color,
                          CometChatPalette accent300_color,
                          CometChatPalette accent400_color,
                          CometChatPalette accent500_color,
                          CometChatPalette accent600_color,
                          CometChatPalette accent700_color,
                          CometChatPalette accent800_color,
                          CometChatPalette accent900_color) {
        MODE = mode;
        primary = primary_color != null ? primary_color : primary;
        background = background_color != null ? background_color : background;
        error = error_color != null ? primary_color : error;
        accent = accent_color != null ? primary_color : accent;
        accent50 = accent50_color != null ? accent50_color : accent50;
        accent100 = accent100_color != null ? accent100_color : accent100;
        accent200 = accent200_color != null ? accent200_color : accent200;
        accent300 = accent300_color != null ? accent300_color : accent300;
        accent400 = accent400_color != null ? accent400_color : accent400;
        accent500 = accent500_color != null ? accent500_color : accent500;
        accent600 = accent600_color != null ? accent600_color : accent600;
        accent700 = accent700_color != null ? accent700_color : accent700;
        accent800 = accent800_color != null ? accent800_color : accent800;
        accent900 = accent900_color != null ? accent900_color : accent900;
        setTheme(MODE);
    }


    public static class Avatar {
        public static int cornerRadius = 16;
        public static int backgroundColor = R.color.primary;
        public static int borderColor = R.color.colorPrimaryDark;
        public static float borderWidth = 1f;
        public static int emptyAvatarNameColor = android.R.color.black;
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

    public static class Conversations {
        public static int baseBackGroundColor = android.R.color.white;
        public static int listBackGroundColor = android.R.color.transparent;
        public static int baseTitleColor = R.color.accent900;
        public static int searchBoxBackgroundColor = R.color.searchBackground;
        public static int searchBoxTextColor = R.color.accent600;
    }

    public static class ConversationListItem {
        public static int item_background = android.R.color.white;
        public static int titleTextColor = R.color.accent900;
        public static int subTitleTextColor = R.color.accent600;
        public static int timeColor = R.color.accent600;
    }


    public static class Users {
        public static int baseBackGroundColor = android.R.color.transparent;
        public static int listBackGroundColor = android.R.color.transparent;
        public static int baseTitleColor = R.color.accent900;
        public static int backgroundColor = android.R.color.transparent;
        public static int searchBoxBackgroundColor = R.color.searchBackground;
        public static int searchBoxTextColor = R.color.accent600;
    }

    public static class UserListItem {
        public static int item_background = android.R.color.transparent;
        public static int titleTextColor = R.color.accent900;
        public static int subTitleTextColor = R.color.colorPrimary;
    }

    public static class Groups {
        public static int baseBackGroundColor = android.R.color.transparent;
        public static int listBackGroundColor = android.R.color.transparent;
        public static int baseTitleColor = R.color.primaryTextColor;
        public static int backgroundColor = android.R.color.transparent;
        public static int searchBoxBackgroundColor = R.color.light_grey;
        public static int searchBoxTextColor = R.color.primaryTextColor;
    }

    public static class GroupListItem {
        public static int item_background = android.R.color.transparent;
        public static int titleTextColor = R.color.primaryTextColor;
        public static int subTitleTextColor = R.color.colorPrimary;
    }

    public static class Typography {

        public static final String robotoMedium = "Roboto-Medium.ttf";

        public static final String robotoRegular = "Roboto-Regular.ttf";

        public static final String robotoLight = "Roboto-Light.ttf";

    }

    public static class MessageList {
        public static int backgroundColor = android.R.color.transparent;
        public static String actionSheetMode = ActionSheet.LayoutMode.listMode;
        public static String messageAlignment = CometChatMessageList.STANDARD;
    }

    public static class Composer {
        public static int sendIconTint = R.color.colorPrimary;
    }


    public static void setTheme(String theme) {
        switch (theme) {
            case DEFAULT_LIGHT:
                CometChatTheme.setDefaultLight();
                break;
            case DEFAULT_DARK:
                CometChatTheme.setDefaultDark();
                break;
            case PERSIAN:
                CometChatTheme.setPersianTheme();
                break;
            case PERSIAN_DARK:
                CometChatTheme.setPersianThemeDark();
                break;
            case MOUNTAIN_MEADOW:
                CometChatTheme.setMountainMeadow();
                break;
            case MOUNTAIN_MEADOW_DARK:
                CometChatTheme.setMountainMeadowDark();
                break;
            case AZURE_RADIANCE:
                CometChatTheme.setAzureRadiance();
                break;
            case AZURE_RADIANCE_DARK:
                CometChatTheme.setAzureRadianceDark();
                break;
            case DARK:
            case LIGHT:
                CometChatTheme.setCustomTheme();
                break;
        }
    }

    private static void setCustomTheme() {
        Avatar.backgroundColor = CometChatTheme.getPrimary();
        Avatar.borderColor = R.color.colorPrimaryDark;
        BadgeCount.textColor = R.color.textColorWhite;
        BadgeCount.backgroundColor = R.color.colorPrimary;


        Conversations.baseBackGroundColor = CometChatTheme.getBackground();
        Conversations.listBackGroundColor = CometChatTheme.getBackground();
        Conversations.baseTitleColor = CometChatTheme.getAccent900();
        Conversations.searchBoxBackgroundColor = CometChatTheme.getAccent600();
        Conversations.searchBoxTextColor = CometChatTheme.getAccent900();
        ConversationListItem.item_background = CometChatTheme.getBackground();
        ConversationListItem.titleTextColor = CometChatTheme.getAccent900();
        ConversationListItem.subTitleTextColor = CometChatTheme.getAccent600();
        ConversationListItem.timeColor = CometChatTheme.getAccent600();

        Users.baseBackGroundColor = CometChatTheme.getBackground();
        Users.listBackGroundColor = CometChatTheme.getBackground();
        Users.baseTitleColor = CometChatTheme.getAccent900();
        Users.searchBoxBackgroundColor = CometChatTheme.getAccent600();
        Users.searchBoxTextColor = CometChatTheme.getAccent900();
        UserListItem.item_background = CometChatTheme.getBackground();
        UserListItem.titleTextColor = CometChatTheme.getAccent900();
        UserListItem.subTitleTextColor = CometChatTheme.getAccent600();


        Groups.baseBackGroundColor = CometChatTheme.getBackground();
        Groups.listBackGroundColor = CometChatTheme.getBackground();
        Groups.baseTitleColor = CometChatTheme.getAccent900();
        Groups.searchBoxBackgroundColor = CometChatTheme.getAccent600();
        Groups.searchBoxTextColor = CometChatTheme.getAccent900();
        GroupListItem.item_background = CometChatTheme.getBackground();
        GroupListItem.titleTextColor = CometChatTheme.getAccent900();
        GroupListItem.subTitleTextColor = CometChatTheme.getAccent600();

    }

    private static void setAzureRadiance() {

        Avatar.borderColor = R.color.azure_radiance_base_bg;
        Avatar.backgroundColor = R.color.textColorWhite;

        BadgeCount.textColor = R.color.azure_radiance_badge_text;
        BadgeCount.backgroundColor = R.color.azure_radiance_badge_bg;

        Conversations.searchBoxBackgroundColor = R.color.azure_radiance_search_bg;
        Conversations.searchBoxTextColor = R.color.azure_radiance_search_text;
        Conversations.baseBackGroundColor = R.color.azure_radiance_base_bg;
        Conversations.listBackGroundColor = R.color.azure_radiance_list_base_bg;
        Conversations.baseTitleColor = R.color.azure_radiance_base_title;

        ConversationListItem.item_background = R.color.azure_radiance_list_item_bg;
        ConversationListItem.titleTextColor = R.color.azure_radiance_title_text;
        ConversationListItem.subTitleTextColor = R.color.azure_radiance_subtitle_text;
        ConversationListItem.timeColor = R.color.azure_radiance_time;

        // setting User Theme
        Users.searchBoxBackgroundColor = R.color.azure_radiance_search_bg;
        Users.searchBoxTextColor = R.color.azure_radiance_search_text;
        Users.baseBackGroundColor = R.color.azure_radiance_base_bg;
        Users.listBackGroundColor = R.color.azure_radiance_list_base_bg;
        Users.baseTitleColor = R.color.azure_radiance_base_title;

        UserListItem.item_background = R.color.azure_radiance_list_item_bg;
        UserListItem.titleTextColor = R.color.azure_radiance_title_text;
        UserListItem.subTitleTextColor = R.color.azure_radiance_subtitle_text;

        // setting Group Theme
        Groups.searchBoxBackgroundColor = R.color.azure_radiance_search_bg;
        Groups.searchBoxTextColor = R.color.azure_radiance_search_text;
        Groups.baseBackGroundColor = R.color.azure_radiance_base_bg;
        Groups.listBackGroundColor = R.color.azure_radiance_list_base_bg;
        Groups.baseTitleColor = R.color.azure_radiance_base_title;

        GroupListItem.item_background = R.color.azure_radiance_list_item_bg;
        GroupListItem.titleTextColor = R.color.azure_radiance_title_text;
        GroupListItem.subTitleTextColor = R.color.azure_radiance_subtitle_text;
    }

    private static void setAzureRadianceDark() {

        Avatar.borderColor = R.color.azure_radiance_base_bg;
        Avatar.backgroundColor = R.color.textColorWhite;

        BadgeCount.textColor = R.color.azure_radiance_badge_text_dark;
        BadgeCount.backgroundColor = R.color.azure_radiance_badge_bg_dark;

        Conversations.searchBoxBackgroundColor = R.color.azure_radiance_search_bg_dark;
        Conversations.searchBoxTextColor = R.color.azure_radiance_search_text_dark;
        Conversations.baseBackGroundColor = R.color.azure_radiance_base_bg_dark;
        Conversations.listBackGroundColor = R.color.azure_radiance_list_base_bg_dark;
        Conversations.baseTitleColor = R.color.azure_radiance_base_title_dark;

        ConversationListItem.item_background = R.color.azure_radiance_list_item_bg_dark;
        ConversationListItem.titleTextColor = R.color.azure_radiance_title_text_dark;
        ConversationListItem.subTitleTextColor = R.color.azure_radiance_subtitle_text_dark;
        ConversationListItem.timeColor = R.color.azure_radiance_time_dark;

        // setting User Theme
        Users.searchBoxBackgroundColor = R.color.azure_radiance_search_bg_dark;
        Users.searchBoxTextColor = R.color.azure_radiance_search_text_dark;
        Users.baseBackGroundColor = R.color.azure_radiance_base_bg_dark;
        Users.listBackGroundColor = R.color.azure_radiance_list_base_bg_dark;
        Users.baseTitleColor = R.color.azure_radiance_base_title_dark;

        UserListItem.item_background = R.color.azure_radiance_list_item_bg_dark;
        UserListItem.titleTextColor = R.color.azure_radiance_title_text_dark;
        UserListItem.subTitleTextColor = R.color.azure_radiance_subtitle_text_dark;

        // setting Group Theme
        Groups.searchBoxBackgroundColor = R.color.azure_radiance_search_bg_dark;
        Groups.searchBoxTextColor = R.color.azure_radiance_search_text_dark;
        Groups.baseBackGroundColor = R.color.azure_radiance_base_bg_dark;
        Groups.listBackGroundColor = R.color.azure_radiance_list_base_bg_dark;
        Groups.baseTitleColor = R.color.azure_radiance_base_title_dark;

        GroupListItem.item_background = R.color.azure_radiance_list_item_bg_dark;
        GroupListItem.titleTextColor = R.color.azure_radiance_title_text_dark;
        GroupListItem.subTitleTextColor = R.color.azure_radiance_subtitle_text_dark;
    }

    private static void setPersianTheme() {

        Avatar.borderColor = R.color.colorPrimary;
        Avatar.backgroundColor = R.color.textColorWhite;

        BadgeCount.textColor = R.color.persian_theme_badge_text;
        BadgeCount.backgroundColor = R.color.persian_theme_badge_bg;

        Conversations.searchBoxBackgroundColor = R.color.persian_theme_search_bg;
        Conversations.searchBoxTextColor = R.color.persian_theme_search_text;
        Conversations.baseBackGroundColor = R.color.persian_theme_base_bg;
        Conversations.baseTitleColor = R.color.persian_theme_base_title;
        Conversations.listBackGroundColor = R.color.persian_theme_list_base_bg;

        ConversationListItem.item_background = R.color.persian_theme_list_item_bg;
        ConversationListItem.titleTextColor = R.color.persian_theme_title_text;
        ConversationListItem.subTitleTextColor = R.color.persian_theme_subtitle_text;
        ConversationListItem.timeColor = R.color.persian_theme_time;

        // setting User Theme
        Users.searchBoxBackgroundColor = R.color.persian_theme_search_bg;
        Users.searchBoxTextColor = R.color.persian_theme_search_text;
        Users.baseBackGroundColor = R.color.persian_theme_base_bg;
        Users.baseTitleColor = R.color.persian_theme_base_title;
        Users.listBackGroundColor = R.color.persian_theme_list_base_bg;

        UserListItem.item_background = R.color.persian_theme_list_item_bg;
        UserListItem.titleTextColor = R.color.persian_theme_title_text;
        UserListItem.subTitleTextColor = R.color.persian_theme_subtitle_text;

        // setting Group Theme
        Groups.searchBoxBackgroundColor = R.color.persian_theme_search_bg;
        Groups.searchBoxTextColor = R.color.persian_theme_search_text;
        Groups.baseBackGroundColor = R.color.persian_theme_base_bg;
        Groups.baseTitleColor = R.color.persian_theme_base_title;
        Groups.listBackGroundColor = R.color.persian_theme_list_base_bg;

        GroupListItem.item_background = R.color.persian_theme_list_item_bg;
        GroupListItem.titleTextColor = R.color.persian_theme_title_text;
        GroupListItem.subTitleTextColor = R.color.persian_theme_subtitle_text;


    }

    private static void setPersianThemeDark() {


        Avatar.borderColor = R.color.colorPrimary;
        Avatar.backgroundColor = R.color.textColorWhite;

        BadgeCount.textColor = R.color.persian_theme_badge_text_dark;
        BadgeCount.backgroundColor = R.color.persian_theme_badge_bg_dark;

        Conversations.searchBoxBackgroundColor = R.color.persian_theme_search_bg_dark;
        Conversations.searchBoxTextColor = R.color.persian_theme_search_text_dark;
        Conversations.baseBackGroundColor = R.color.persian_theme_base_bg_dark;
        Conversations.baseTitleColor = R.color.persian_theme_base_title_dark;
        Conversations.listBackGroundColor = R.color.persian_theme_list_base_bg_dark;

        ConversationListItem.item_background = R.color.persian_theme_list_item_bg_dark;
        ConversationListItem.titleTextColor = R.color.persian_theme_title_text_dark;
        ConversationListItem.subTitleTextColor = R.color.persian_theme_subtitle_text_dark;
        ConversationListItem.timeColor = R.color.persian_theme_time_dark;

        // setting User Theme
        Users.searchBoxBackgroundColor = R.color.persian_theme_search_bg_dark;
        Users.searchBoxTextColor = R.color.persian_theme_search_text_dark;
        Users.baseBackGroundColor = R.color.persian_theme_base_bg_dark;
        Users.baseTitleColor = R.color.persian_theme_base_title_dark;
        Users.listBackGroundColor = R.color.persian_theme_list_base_bg_dark;

        UserListItem.item_background = R.color.persian_theme_list_item_bg_dark;
        UserListItem.titleTextColor = R.color.persian_theme_title_text_dark;
        UserListItem.subTitleTextColor = R.color.persian_theme_subtitle_text_dark;

        // setting Group Theme
        Groups.searchBoxBackgroundColor = R.color.persian_theme_search_bg_dark;
        Groups.searchBoxTextColor = R.color.persian_theme_search_text_dark;
        Groups.baseBackGroundColor = R.color.persian_theme_base_bg_dark;
        Groups.baseTitleColor = R.color.persian_theme_base_title_dark;
        Groups.listBackGroundColor = R.color.persian_theme_list_base_bg_dark;

        GroupListItem.item_background = R.color.persian_theme_list_item_bg_dark;
        GroupListItem.titleTextColor = R.color.persian_theme_title_text_dark;
        GroupListItem.subTitleTextColor = R.color.persian_theme_subtitle_text_dark;
    }

    private static void setMountainMeadow() {
        Avatar.borderColor = R.color.colorPrimary;
        Avatar.backgroundColor = R.color.textColorWhite;

        BadgeCount.textColor = R.color.MountainMeadow_badge_text;
        BadgeCount.backgroundColor = R.color.MountainMeadow_badge_bg;

        Conversations.searchBoxBackgroundColor = R.color.MountainMeadow_search_bg;
        Conversations.searchBoxTextColor = R.color.MountainMeadow_search_text;
        Conversations.baseBackGroundColor = R.color.MountainMeadow_base_bg;
        Conversations.baseTitleColor = R.color.MountainMeadow_base_title;
        Conversations.listBackGroundColor = R.color.MountainMeadow_list_base_bg;

        ConversationListItem.item_background = R.color.MountainMeadow_list_item_bg;
        ConversationListItem.titleTextColor = R.color.MountainMeadow_title_text;
        ConversationListItem.subTitleTextColor = R.color.MountainMeadow_subtitle_text;
        ConversationListItem.timeColor = R.color.MountainMeadow_time;

        Users.searchBoxBackgroundColor = R.color.MountainMeadow_search_bg;
        Users.searchBoxTextColor = R.color.MountainMeadow_search_text;
        Users.baseBackGroundColor = R.color.MountainMeadow_base_bg;
        Users.baseTitleColor = R.color.MountainMeadow_base_title;
        Users.listBackGroundColor = R.color.MountainMeadow_list_base_bg;

        UserListItem.item_background = R.color.MountainMeadow_list_item_bg;
        UserListItem.titleTextColor = R.color.MountainMeadow_title_text;
        UserListItem.subTitleTextColor = R.color.MountainMeadow_subtitle_text;

        Groups.searchBoxBackgroundColor = R.color.MountainMeadow_search_bg;
        Groups.searchBoxTextColor = R.color.MountainMeadow_search_text;
        Groups.baseBackGroundColor = R.color.MountainMeadow_base_bg;
        Groups.baseTitleColor = R.color.MountainMeadow_base_title;
        Groups.listBackGroundColor = R.color.MountainMeadow_list_base_bg;

        GroupListItem.item_background = R.color.MountainMeadow_list_item_bg;
        GroupListItem.titleTextColor = R.color.MountainMeadow_title_text;
        GroupListItem.subTitleTextColor = R.color.MountainMeadow_subtitle_text;


    }

    private static void setMountainMeadowDark() {
        Avatar.borderColor = R.color.colorPrimary;
        Avatar.backgroundColor = android.R.color.black;

        BadgeCount.textColor = R.color.MountainMeadow_badge_text;
        BadgeCount.backgroundColor = R.color.MountainMeadow_badge_bg;

        Conversations.searchBoxBackgroundColor = R.color.MountainMeadow_search_bg_dark;
        Conversations.searchBoxTextColor = R.color.MountainMeadow_search_text_dark;
        Conversations.baseBackGroundColor = R.color.MountainMeadow_base_bg_dark;
        Conversations.baseTitleColor = R.color.MountainMeadow_base_title_dark;
        Conversations.listBackGroundColor = R.color.MountainMeadow_list_base_bg_dark;

        ConversationListItem.item_background = R.color.MountainMeadow_list_item_bg_dark;
        ConversationListItem.titleTextColor = R.color.MountainMeadow_title_text_dark;
        ConversationListItem.subTitleTextColor = R.color.MountainMeadow_subtitle_text_dark;
        ConversationListItem.timeColor = R.color.MountainMeadow_time_dark;

        Users.searchBoxBackgroundColor = R.color.MountainMeadow_search_bg_dark;
        Users.searchBoxTextColor = R.color.MountainMeadow_search_text_dark;
        Users.baseBackGroundColor = R.color.MountainMeadow_base_bg_dark;
        Users.baseTitleColor = R.color.MountainMeadow_base_title_dark;
        Users.listBackGroundColor = R.color.MountainMeadow_list_base_bg_dark;

        UserListItem.item_background = R.color.MountainMeadow_list_item_bg_dark;
        UserListItem.titleTextColor = R.color.MountainMeadow_title_text_dark;
        UserListItem.subTitleTextColor = R.color.MountainMeadow_subtitle_text_dark;

        Groups.searchBoxBackgroundColor = R.color.MountainMeadow_search_bg_dark;
        Groups.searchBoxTextColor = R.color.MountainMeadow_search_text_dark;
        Groups.baseBackGroundColor = R.color.MountainMeadow_base_bg_dark;
        Groups.baseTitleColor = R.color.MountainMeadow_base_title_dark;
        Groups.listBackGroundColor = R.color.MountainMeadow_list_base_bg_dark;

        GroupListItem.item_background = R.color.MountainMeadow_list_item_bg_dark;
        GroupListItem.titleTextColor = R.color.MountainMeadow_title_text_dark;
        GroupListItem.subTitleTextColor = R.color.MountainMeadow_subtitle_text_dark;


    }

    private static void setDefaultLight() {
        Avatar.backgroundColor = R.color.colorPrimary;
        Avatar.borderColor = R.color.colorPrimaryDark;
        BadgeCount.textColor = R.color.textColorWhite;
        BadgeCount.backgroundColor = R.color.colorPrimary;

        Conversations.baseBackGroundColor = android.R.color.white;
        Conversations.listBackGroundColor = android.R.color.white;
        Conversations.baseTitleColor = R.color.accent900;
        Conversations.searchBoxBackgroundColor = R.color.searchBackground;
        Conversations.searchBoxTextColor = R.color.accent600;
        ConversationListItem.item_background = android.R.color.white;
        ConversationListItem.titleTextColor = R.color.accent900;
        ConversationListItem.subTitleTextColor = R.color.accent600;
        ConversationListItem.timeColor = R.color.accent600;

        Users.baseBackGroundColor = android.R.color.white;
        Users.listBackGroundColor = android.R.color.transparent;
        Users.baseTitleColor = R.color.accent900;
        Users.backgroundColor = android.R.color.transparent;
        Users.searchBoxBackgroundColor = R.color.searchBackground;
        Users.searchBoxTextColor = R.color.accent600;
        UserListItem.item_background = android.R.color.white;
        UserListItem.titleTextColor = R.color.accent900;
        UserListItem.subTitleTextColor = R.color.accent600;


        Groups.baseBackGroundColor = android.R.color.white;
        Groups.listBackGroundColor = android.R.color.transparent;
        Groups.baseTitleColor = R.color.accent900;
        Groups.backgroundColor = android.R.color.transparent;
        Groups.searchBoxBackgroundColor = R.color.searchBackground;
        Groups.searchBoxTextColor = R.color.accent600;
        GroupListItem.item_background = android.R.color.white;
        GroupListItem.titleTextColor = R.color.accent900;
        GroupListItem.subTitleTextColor = R.color.accent600;


    }

    private static void setDefaultDark() {
        Avatar.backgroundColor = R.color.background_dark;
        Avatar.borderColor = R.color.colorPrimaryDark;
        BadgeCount.textColor = R.color.accent900;
        BadgeCount.backgroundColor = android.R.color.white;

        Conversations.baseBackGroundColor = R.color.background_dark;
        Conversations.listBackGroundColor = R.color.background_dark;
        Conversations.baseTitleColor = R.color.accent900_dark;
        Conversations.searchBoxBackgroundColor = R.color.searchBackground;
        Conversations.searchBoxTextColor = R.color.accent600_dark;
        ConversationListItem.item_background = R.color.background_dark;
        ConversationListItem.titleTextColor = R.color.accent900_dark;
        ConversationListItem.subTitleTextColor = R.color.accent600_dark;
        ConversationListItem.timeColor = R.color.accent600_dark;

        Users.baseBackGroundColor = R.color.background_dark;
        Users.listBackGroundColor = R.color.background_dark;
        Users.baseTitleColor = R.color.accent900_dark;
        Users.searchBoxBackgroundColor = R.color.searchBackground;
        Users.searchBoxTextColor = R.color.accent600_dark;
        UserListItem.item_background = R.color.background_dark;
        UserListItem.titleTextColor = R.color.accent900_dark;
        UserListItem.subTitleTextColor = R.color.accent600_dark;


        Groups.baseBackGroundColor = R.color.background_dark;
        Groups.listBackGroundColor = R.color.background_dark;
        Groups.baseTitleColor = R.color.accent900_dark;
        Groups.searchBoxBackgroundColor = R.color.searchBackground;
        Groups.searchBoxTextColor = R.color.accent600_dark;
        GroupListItem.item_background = R.color.background_dark;
        GroupListItem.titleTextColor = R.color.accent900_dark;
        GroupListItem.subTitleTextColor = R.color.accent600_dark;
    }


}
