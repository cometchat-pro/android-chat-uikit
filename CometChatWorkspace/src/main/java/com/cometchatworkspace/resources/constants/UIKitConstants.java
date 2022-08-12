package com.cometchatworkspace.resources.constants;

import com.cometchat.pro.constants.CometChatConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIKitConstants {

    public static final String SELECTED = "selected";

    public static class AppInfo {
        public static String AUTH_KEY = "";

        public static String APP_ID = "";
    }

    public static class IntentStrings {

        public static final String ALLOW_BAN_UNBAN_MEMBERS = "allowBanUnbanMembers";

        public static final String ALLOW_KICK_MEMBERS = "allowKickMembers";

        public static final String ALLOW_PROMOTE_DEMOTE_MEMBERS = "allowPromoteDemoteMembers";

        public static final String IMAGE_TYPE = "image/*";

        public static final String UID = "uid";

        public static final String AVATAR = "avatar";

        public static final String STATUS = "status";

        public static final String NAME = "name";

        public static final String TYPE = "type";

        public static final String GUID = "guid";

        public static final String tabBar = "tabBar";

        public static final String[] EXTRA_MIME_DOC = new String[]{"text/plane", "text/html", "application/pdf", "application/msword", "application/vnd.ms.excel", "application/mspowerpoint", "application/docs", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/zip"};

        public static final String MEMBER_COUNT = "member_count";

        public static final String GROUP_MEMBER = "group_members";

        public static final String GROUP_NAME = "group_name";

        public static final String MEMBER_SCOPE = "member_scope";

        public static final String GROUP_OWNER = "group_owner";

        public static final String ID = "id";

        public static final String IS_ADD_MEMBER = "is_add_member";

        public static final String IS_BLOCKED_BY_ME = "is_blocked_by_me";

        public static final String SESSION_ID = "sessionId";

        public static final String INCOMING = "incoming";

        public static final String FROM_CALL_LIST = "from_call_list";

        public static final String JOIN_ONGOING = "join_ongoing_call";

        public static final String MESSAGE_TYPE_IMAGE_NAME = "file_name";

        public static final String MESSAGE_TYPE_IMAGE_URL = "file_url";

        public static final String MESSAGE_TYPE_IMAGE_MIME_TYPE = "file_mime";

        public static final String MESSAGE_TYPE_IMAGE_EXTENSION = "file_extension";

        public static final String MESSAGE_TYPE_IMAGE_SIZE = "file_size";

        public static final String MESSAGE = "message";

        public static final String SHOW_MODERATORLIST = "is_moderator";

        public static final String GROUP_DESC = "group_description";

        public static final String GROUP_PASSWORD = "group_password";

        public static final String GROUP_TYPE = "group_type";

        public static final String TEXTMESSAGE = "text_message";

        public static final String SENTAT = "sent_at";

        public static final String MESSAGE_TYPE = "message_type";

        public static final String PARENT_ID = "parent_id";

        public static final String REPLY_COUNT = "reply_count";

        public static final String CONVERSATION_NAME = "conversation_name";

        public static final String INTENT_MEDIA_MESSAGE = "intent_media_message";

        public static final String IMAGE_MODERATION = "image_moderation";

        public static final String CUSTOM_MESSAGE = "custom_message";

        public static final String LOCATION = "location";

        public static final String LOCATION_LATITUDE = "latitude";

        public static final String LOCATION_LONGITUDE = "longitude";

        public static final String MESSAGE_CATEGORY = "message_category";

        public static final String PARENT_BASEMESSAGE = "parent_baseMessage";

        public static final String POLL_VOTE_COUNT = "poll_vote_count";

        public static final String POLLS = "extension_poll";

        public static final String TRANSFER_OWNERSHIP = "transfer_ownership";

        public static final String STICKERS = "extension_sticker";

        public static final String REACTION_INFO = "reaction_info";

        public static final String URL = "url";

        public static final String WHITEBOARD = "extension_whiteboard";

        public static final String WRITEBOARD = "extension_document";

        public static final String GROUP_CALL = "meeting";

        public static final String GROUP_CALL_TYPE = "group_call_type";

        public static final String IS_DEFAULT_CALL = "is_default_call";

        public static final String INTENT_MEDIA_IMAGE_MESSAGE = "intent_media_image_message";

        public static final String INTENT_MEDIA_VIDEO_MESSAGE = "intent_media_video_message";

        public static final String INTENT_MEDIA_AUDIO_MESSAGE = "intent_media_audio_message";

        public static final String INTENT_MEDIA_FILE_MESSAGE = "intent_media_file_message";
        public static final String LINK = "link";

        public static final String IS_TITLE_VISIBLE = "IS_TITLE_VISIBLE";

        public static final String CREATE_GROUP_VISIBLE = "IS_CREATE_GROUP_VISIBLE";

        public static final String GROUP = "group";

        public static String POLL_QUESTION = "poll_question";

        public static String POLL_OPTION = "poll_option";

        public static String POLL_RESULT = "poll_result";

        public static String POLL_ID = "poll_id";

        public static final String MEDIA_SIZE = "media_size";
    }

    public static class Tab {
        public static final String Conversation = "conversations";

        public static final String User = "users";

        public static final String Group = "groups";
    }

    public static class RequestCode {

        public static final int GALLERY = 1;

        public static final int CAMERA = 2;

        public static final int FILE = 25;

        public static final int BLOCK_USER = 7;

        public static final int DELETE_GROUP = 8;

        public static final int AUDIO = 3;

        public static final int READ_STORAGE = 001;

        public static final int RECORD = 003;

        public static final int LOCATION = 14;

        public static final int LEAVE_GROUP = 005;
    }

    public static class MapUrl {

        public static final String MAPS_URL = "https://maps.googleapis.com/maps/api/staticmap?zoom=16&size=380x220&markers=color:red|";

        public static String MAP_ACCESS_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXX";
    }


    public static class MessageRequest {

        public static List<String> messageCategoriesForGroup = new ArrayList<>(Arrays.asList(
                CometChatConstants.CATEGORY_MESSAGE,
                CometChatConstants.CATEGORY_CUSTOM,
                CometChatConstants.CATEGORY_CALL,
                CometChatConstants.CATEGORY_ACTION));


        public static List<String> messageCategoriesForUser = new ArrayList<>(Arrays.asList(
                CometChatConstants.CATEGORY_MESSAGE,
                CometChatConstants.CATEGORY_CUSTOM,
                CometChatConstants.CATEGORY_CALL));
    }

    public static class Emoji {
        public static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
        public static final String EMOJI_KEY = "emojic";
    }

    public static class GroupOptionConstants {

        public static final String VIEW_GROUP_MEMBERS = "viewMembers";
        public static final String ADD_GROUP_MEMBERS = "addMembers";
        public static final String BANNED_GROUP_MEMBERS = "bannedMembers";
        public static final String LEAVE_GROUP = "leave";
        public static final String DELETE_EXIT_GROUP = "delete";
        public static final String VOICE_CALL = "voiceCall";
        public static final String VIDEO_CALL = "videoCall";
        public static final String VIEW_INFORMATION = "viewInformation";

    }

    public static class GroupMemberOptionConstants {
        public static final String KICK = "kick";
        public static final String BAN = "ban";
        public static final String UNBAN = "unban";
        public static final String CHANGE_SCOPE = "changeScope";
    }

    public static class UserOptionConstants {
        public static final String BLOCK = "block";
        public static final String UNBLOCK = "unblock";
        public static final String VIEW_PROFILE = "viewProfile";
        public static final String VOICE_CALL = "voiceCall";
        public static final String VIDEO_CALL = "videoCall";
        public static final String VIEW_INFORMATION = "viewInformation";
    }

    public static class ConversationOptionConstants {
        public static final String DELETE = "delete";
    }

    public static class ConversationTypeConstants {
        public static final String USERS = CometChatConstants.RECEIVER_TYPE_USER;
        public static final String GROUPS = CometChatConstants.RECEIVER_TYPE_GROUP;
        public static final String BOTH = "both";
    }

    public static class GroupTypeConstants {
        public static final String PRIVATE = "private";
        public static final String PASSWORD = "password";
        public static final String PUBLIC = "public";
    }

    public static class GroupMemberScope {
        public static final String ADMIN = "admin";
        public static final String MODERATOR = "moderator";
        public static final String PARTICIPANTS = "participant";
    }

    public static class MessageCategoryConstants {
        public static final String MESSAGE = CometChatConstants.CATEGORY_MESSAGE;
        public static final String CUSTOM = CometChatConstants.CATEGORY_CUSTOM;
        public static final String ACTION = CometChatConstants.CATEGORY_ACTION;
        public static final String CALL = CometChatConstants.CATEGORY_CALL;
    }

    public static class MessageTypeConstants {
        public static final String TEXT = CometChatConstants.MESSAGE_TYPE_TEXT;
        public static final String FILE = CometChatConstants.MESSAGE_TYPE_FILE;
        public static final String IMAGE = CometChatConstants.MESSAGE_TYPE_IMAGE;
        public static final String AUDIO = CometChatConstants.MESSAGE_TYPE_AUDIO;
        public static final String VIDEO = CometChatConstants.MESSAGE_TYPE_VIDEO;

        public static final String EXTENSION_POLL = "extension_poll";
        public static final String EXTENSION_STICKER = "extension_sticker";
        public static final String EXTENSION_DOCUMENT = "extension_document";
        public static final String EXTENSION_WHITEBOARD = "extension_whiteboard";
        public static final String EXTENSION_MEETING = "meeting";
        public static final String EXTENSION_LOCATION = "location";

    }

    public static class ReceiverTypeConstants {
        public static final String USER = CometChatConstants.RECEIVER_TYPE_USER;
        public static final String GROUP = CometChatConstants.RECEIVER_TYPE_GROUP;
    }

    public static class DefaultOptions {
        public static final String EDIT = "edit";
        public static final String DELETE = "delete";
        public static final String REPLY = "reply";
        public static final String FORWARD = "forward";
        public static final String REPLY_PRIVATELY = "reply_privately";
        public static final String MESSAGE_PRIVATELY = "message_privately";
        public static final String COPY = "copy";
        public static final String TRANSLATE = "translate";
        public static final String MESSAGE_INFORMATION = "message_information";
        public static final String SHARE = "share";
        public static final String REPLY_IN_THREAD = "reply_in_thread";
    }

    public static class UserOptions {
        public static final String audioCall = "audioCall";
        public static final String videoCall = "videoCall";
        public static final String viewDetails = "viewDetails";
    }

    //remaining classes
    //MessageOptionConstants
    //MessageListAlignmentConstants
    //MessageBubbleAlignmentConstants
    //MessageTimeAlignmentConstants
    //MessageStatusConstants
    //MetadataConstants
}
