package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GroupsListConfiguration extends CometChatConfigurations {
    private final Context context;
    private int emptyView = 0;
    private boolean isHideError, isJoinedOnly;

    private String searchKeyWord = "";

    private List<String> tags = new ArrayList<>();
    private int limit=30;
    private int errorView = 0;
    private String errorText="";
    private String emptyText="";

    public GroupsListConfiguration(Context context) {
        this.context = context;
    }

    public int getErrorView() {
        return errorView;
    }

    public GroupsListConfiguration setErrorView(int errorView) {
        this.errorView = errorView;
        return this;
    }

    public String getErrorText() {
        return errorText;
    }

    public GroupsListConfiguration setErrorText(String errorText) {
        this.errorText = errorText;
        return this;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public GroupsListConfiguration setEmptyText(String emptyText) {
        this.emptyText = emptyText;
        return this;
    }
    public int getEmptyView() {
        return emptyView;
    }

    public GroupsListConfiguration setEmptyView(int emptyView) {
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
