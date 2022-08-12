package com.cometchatworkspace.components.shared.primaryComponents.configurations;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ConversationListConfiguration extends CometChatConfigurations {

    private String conversationType;
    private List<String> tags;
    private List<String> userTags;
    private List<String> groupTags;
    private boolean userAndGroupTags;
    private int limit=30;
    private int emptyView;
    private int errorView;
    private String errorText = "";
    private String emptyText = "";
    private boolean hideError;

    public boolean isHideError() {
        return hideError;
    }

    public ConversationListConfiguration setHideError(boolean hideError) {
        this.hideError = hideError;
        return this;
    }

    public String getConversationType() {
        return conversationType;
    }

    public ConversationListConfiguration setConversationType(String conversationType) {
        this.conversationType = conversationType;
        return this;

    }

    public List<String> getTags() {
        return tags;
    }

    public ConversationListConfiguration setTags(List<String> tags) {
        this.tags = tags;
        return this;

    }

    public List<String> getUserTags() { return userTags; }
    public ConversationListConfiguration setUserTags(List<String> tags) {
        this.userTags = tags;
        return this;
    }

    public List<String> getGroupTags() { return groupTags; }
    public ConversationListConfiguration setGroupTags(List<String> tags) {
        this.groupTags = tags;
        return this;

    }

    public boolean isUserAndGroupTags() {
        return userAndGroupTags;
    }

    public ConversationListConfiguration setUserAndGroupTags(boolean userAndGroupTags) {
        this.userAndGroupTags = userAndGroupTags;
        return this;

    }

    public int getLimit() {
        return limit;
    }

    public ConversationListConfiguration setLimit(int limit) {
        this.limit = limit;
        return this;

    }

    public int getEmptyView() {
        return emptyView;
    }

    public ConversationListConfiguration setEmptyView(int emptyView) {
        this.emptyView = emptyView;
        return this;

    }

    public int getErrorView() {
        return errorView;
    }

    public ConversationListConfiguration setErrorView(int errorView) {
        this.errorView = errorView;
        return this;
    }

    public String getErrorText() {
        return errorText;
    }

    public ConversationListConfiguration setErrorText(String errorText) {
        this.errorText = errorText;
        return this;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public ConversationListConfiguration setEmptyText(String emptyText) {
        this.emptyText = emptyText;
        return this;
    }
}
