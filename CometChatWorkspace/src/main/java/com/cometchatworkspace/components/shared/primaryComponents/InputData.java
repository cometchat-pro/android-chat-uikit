package com.cometchatworkspace.components.shared.primaryComponents;

public class InputData {

    private final String id;
    private final String thumbnail;
    private final String title;
    private final String subTitle;
    private String status=null;
    private long time;
    private int unreadCount;

    //for UsersListItem
    public InputData(String id, String thumbnail, String title, String subTitle, String status) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.subTitle = subTitle;
        this.status = status;
    }
    //for GroupsListItem
    public InputData(String id, String thumbnail, String title, String subTitle) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.subTitle = subTitle;
    }
    //for ConversationsListItem

    public InputData(String id, String thumbnail, String title, String subTitle, String status, long time, int unreadCount) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.subTitle = subTitle;
        this.status = status;
        this.time = time;
        this.unreadCount = unreadCount;
    }

    public String getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getStatus() {
        return status;
    }

    public long getTime() {
        return time;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
