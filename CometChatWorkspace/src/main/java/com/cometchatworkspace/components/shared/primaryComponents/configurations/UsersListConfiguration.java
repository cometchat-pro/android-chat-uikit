package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class UsersListConfiguration extends CometChatConfigurations {

    private boolean showHeader;

    private boolean isFriendOnly;

    private boolean hideBlockedUsers;
    
    private boolean hideError;

    private String searchKeyword = "";

    private String status = null;

    private int limit=30;

    private List<String> tags = new ArrayList<>();

    private List<String> roles = new ArrayList<>();
    private List<String> uidS = new ArrayList<>();
    
    private View emptyView = null;


    private final Context context;

    public UsersListConfiguration(Context context) {
        this.context = context;
    }


    public boolean isShowHeader() {
        return showHeader;
    }

    public UsersListConfiguration setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
        return this;
    }

    public boolean isFriendOnly() {
        return isFriendOnly;
    }

    public UsersListConfiguration setFriendOnly(boolean friendOnly) {
        isFriendOnly = friendOnly;
        return this;

    }

    public boolean isHideBlockedUsers() {
        return hideBlockedUsers;
    }

    public UsersListConfiguration setHideBlockedUsers(boolean hideBlockedUsers) {
        this.hideBlockedUsers = hideBlockedUsers;
        return this;

    }

    public boolean isHideError() {
        return hideError;
    }

    public UsersListConfiguration setHideError(boolean hideError) {
        this.hideError = hideError;
        return this;

    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public UsersListConfiguration setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        return this;

    }

    public String getStatus() {
        return status;
    }

    public UsersListConfiguration setStatus(String status) {
        this.status = status;
        return this;

    }

    public int getLimit() {
        return limit;

    }

    public UsersListConfiguration setLimit(int limit) {
        this.limit = limit;
        return this;

    }

    public List<String> getTags() {
        return tags;

    }

    public UsersListConfiguration setTags(List<String> tags) {
        this.tags = tags;
        return this;

    }

    public List<String> getRoles() {
        return roles;

    }

    public UsersListConfiguration setRoles(List<String> roles) {
        this.roles = roles;
        return this;

    }

    public List<String> getUidS() {
        return uidS;
    }

    public UsersListConfiguration setUidS(List<String> uidS) {
        this.uidS = uidS;
        return this;

    }

    public View getEmptyView() {
        return emptyView;
    }

    public UsersListConfiguration setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        return this;
    }


}
