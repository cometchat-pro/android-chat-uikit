package com.cometchat.chatuikit.shared.constants;

import com.cometchat.pro.constants.CometChatConstants;

public class UIKitConstants {

    public enum SelectionMode {
        NONE, SINGLE, MULTIPLE
    }

    public enum MessageListAlignment {
        LEFT_ALIGNED, STANDARD
    }

    public enum MessageBubbleAlignment {
        RIGHT, LEFT, CENTER
    }

    public enum TimeStampAlignment {
        TOP, BOTTOM
    }

    public enum AuxiliaryButtonAlignment {
        LEFT, RIGHT
    }

    public enum States {
        LOADING, LOADED, ERROR, EMPTY, NON_EMPTY
    }

    public static class Notification{

        public static final int ID = 101;
    }

    public static class IntentStrings {

        public static final String UID = "uid";

        public static final String NAME = "name";

        public static final String TYPE = "type";

        public static final String GUID = "guid";

        public static final String[] EXTRA_MIME_DOC = new String[]{"text/plane", "text/html", "application/pdf", "application/msword", "application/vnd.ms.excel", "application/mspowerpoint", "application/docs", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/zip"};

        public static final String ID = "id";

        public static final String SESSION_ID = "sessionId";

        public static final String SENTAT = "sent_at";

        public static final String MESSAGE_TYPE = "message_type";

        public static final String INTENT_MEDIA_MESSAGE = "intent_media_message";

        public static final String REACTION_INFO = "reaction_info";

        public static final String URL = "url";

        public static final String GROUP = "group";

        public static final String CALL_TYPE = "callType";

        public static final String MEDIA_SIZE = "media_size";
    }

    public static class MapId {
        public static String PARENT_MESSAGE_ID = "parentMessageID";

        public static String RECEIVER_ID = "receiverID";

        public static String RECEIVER_TYPE = "receiverType";
    }

    public static class GroupOption {

        public static final String VIEW_GROUP_MEMBERS = "viewMembers";
        public static final String ADD_GROUP_MEMBERS = "addMembers";
        public static final String BANNED_GROUP_MEMBERS = "bannedMembers";
        public static final String LEAVE_GROUP = "leave";
        public static final String DELETE_EXIT_GROUP = "delete";
        public static final String VOICE_CALL = "voiceCall";
        public static final String VIDEO_CALL = "videoCall";
        public static final String VIEW_INFORMATION = "viewInformation";

    }

    public enum CustomUIPosition{
        COMPOSER_TOP,
        COMPOSER_BOTTOM,
        MESSAGE_LIST_TOP,
        MESSAGE_LIST_BOTTOM
    }

    public static class GroupMemberOption {
        public static final String KICK = "kick";
        public static final String BAN = "ban";
        public static final String UNBAN = "unban";
        public static final String CHANGE_SCOPE = "changeScope";
    }

    public static class UserStatus {
        public static final String ONLINE = CometChatConstants.USER_STATUS_ONLINE;
        public static final String OFFLINE = CometChatConstants.USER_STATUS_OFFLINE;
    }

    public static class ConversationOption {
        public static final String DELETE = "delete";
    }

    public static class ConversationType {
        public static final String USERS = CometChatConstants.RECEIVER_TYPE_USER;
        public static final String GROUPS = CometChatConstants.RECEIVER_TYPE_GROUP;
        public static final String BOTH = "both";
    }

    public static class GroupType {
        public static final String PRIVATE = CometChatConstants.GROUP_TYPE_PRIVATE;
        public static final String PASSWORD = CometChatConstants.GROUP_TYPE_PASSWORD;
        public static final String PUBLIC = CometChatConstants.GROUP_TYPE_PUBLIC;
    }

    public static class GroupMemberScope {
        public static final String ADMIN = CometChatConstants.SCOPE_ADMIN;
        public static final String MODERATOR = CometChatConstants.SCOPE_MODERATOR;
        public static final String PARTICIPANTS = CometChatConstants.SCOPE_PARTICIPANT;
    }

    public static class MessageCategory {
        public static final String MESSAGE = CometChatConstants.CATEGORY_MESSAGE;
        public static final String CUSTOM = CometChatConstants.CATEGORY_CUSTOM;
        public static final String ACTION = CometChatConstants.CATEGORY_ACTION;
        public static final String CALL = CometChatConstants.CATEGORY_CALL;
    }

    public static class MessageType {
        public static final String TEXT = CometChatConstants.MESSAGE_TYPE_TEXT;
        public static final String FILE = CometChatConstants.MESSAGE_TYPE_FILE;
        public static final String IMAGE = CometChatConstants.MESSAGE_TYPE_IMAGE;
        public static final String AUDIO = CometChatConstants.MESSAGE_TYPE_AUDIO;
        public static final String VIDEO = CometChatConstants.MESSAGE_TYPE_VIDEO;
        public static final String MEETING = "meeting";
    }

    public static class MessageTemplateId {
        public static final String TEXT = CometChatConstants.CATEGORY_MESSAGE + "_" + CometChatConstants.MESSAGE_TYPE_TEXT;
        public static final String FILE = CometChatConstants.CATEGORY_MESSAGE + "_" + CometChatConstants.MESSAGE_TYPE_FILE;
        public static final String IMAGE = CometChatConstants.CATEGORY_MESSAGE + "_" + CometChatConstants.MESSAGE_TYPE_IMAGE;
        public static final String AUDIO = CometChatConstants.CATEGORY_MESSAGE + "_" + CometChatConstants.MESSAGE_TYPE_AUDIO;
        public static final String VIDEO = CometChatConstants.CATEGORY_MESSAGE + "_" + CometChatConstants.MESSAGE_TYPE_VIDEO;
        public static final String GROUP_ACTION = CometChatConstants.CATEGORY_ACTION + "_" + CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER;

        public static final String EXTENSION_POLL = "extension_poll";
        public static final String EXTENSION_STICKER = "extension_sticker";
        public static final String EXTENSION_DOCUMENT = "extension_document";
        public static final String EXTENSION_WHITEBOARD = "extension_whiteboard";
        public static final String EXTENSION_MEETING = "meeting";
        public static final String EXTENSION_LOCATION = "location";

    }

    public static class CallStatusConstants {
        public static final String INITIATED = CometChatConstants.CALL_STATUS_INITIATED;
        public static final String ONGOING = CometChatConstants.CALL_STATUS_ONGOING;
        public static final String REJECTED = CometChatConstants.CALL_STATUS_REJECTED;
        public static final String CANCELLED = CometChatConstants.CALL_STATUS_CANCELLED;
        public static final String BUSY = CometChatConstants.CALL_STATUS_BUSY;
        public static final String UNANSWERED = CometChatConstants.CALL_STATUS_UNANSWERED;
        public static final String ENDED = CometChatConstants.CALL_STATUS_ENDED;
    }

    public static class ComposerAction {
        public static final String GALLERY = "gallery";
        public static final String FILE = "file";
        public static final String AUDIO = "audio";
        public static final String CAMERA = "camera";
    }

    public static class ReceiverType {
        public static final String USER = CometChatConstants.RECEIVER_TYPE_USER;
        public static final String GROUP = CometChatConstants.RECEIVER_TYPE_GROUP;
    }

    public static class MessageOption {
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

    public static class UserOption {
        public static final String BLOCK = "block";
        public static final String UNBLOCK = "unblock";
        public static final String VIEW_PROFILE = "viewProfile";
        public static final String VOICE_CALL = "voiceCall";
        public static final String VIDEO_CALL = "videoCall";
        public static final String VIEW_INFORMATION = "viewInformation";
    }

    public static class files {
        public static final String OPEN = "open";
        public static final String SHARE = "share";
    }

    public static class DetailsTemplate {
        public static final String PRIMARY = "primary";
        public static final String SECONDARY = "secondary";
    }
}
