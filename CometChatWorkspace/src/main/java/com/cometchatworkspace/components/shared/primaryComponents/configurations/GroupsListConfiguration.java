package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GroupsListConfiguration extends CometChatConfigurations {
    private boolean hideStartGroup;
    private final Context context;
    private View emptyView = null;
    private boolean isHideError, isJoinedOnly;

    private String searchKeyWord = "";

    private List<String> tags = new ArrayList<>();
    private int limit=30;


    public GroupsListConfiguration(Context context) {
        this.context = context;
    }


    public GroupsListConfiguration hideStartGroup(boolean isHidden) {
        this.hideStartGroup = isHidden;
        return this;
    }

    public boolean isStartGroupHidden() {
        return hideStartGroup;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public GroupsListConfiguration setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    public boolean isHideError() {
        return isHideError;
    }

    public GroupsListConfiguration setHideError(boolean hideError) {
        isHideError = hideError;
        return this;

    }

    public boolean isJoinedOnly() {
        return isJoinedOnly;
    }

    public GroupsListConfiguration setJoinedOnly(boolean joinedOnly) {
        isJoinedOnly = joinedOnly;
        return this;

    }

    public String getSearchKeyWord() {
        return searchKeyWord;
    }

    public GroupsListConfiguration setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
        return this;

    }

    public List<String> getTags() {
        return tags;
    }

    public GroupsListConfiguration setTags(List<String> tags) {
        this.tags = tags;
        return this;

    }

    public int getLimit() {
        return limit;
    }

    public GroupsListConfiguration setLimit(int limit) {
        this.limit = limit;
        return this;

    }
}
