package com.cometchat.chatuikit.groupswithmessages;

import com.cometchat.chatuikit.creategroup.CreateGroupConfiguration;
import com.cometchat.chatuikit.groups.GroupsConfiguration;
import com.cometchat.chatuikit.joinprotectedgroup.JoinProtectedGroupConfiguration;
import com.cometchat.chatuikit.messages.MessagesConfiguration;

 class GroupsWithMessagesConfiguration {
    
    private JoinProtectedGroupConfiguration joinProtectedGroupConfiguration;
    private CreateGroupConfiguration createGroupConfiguration;
    private MessagesConfiguration messagesConfiguration;
    private GroupsConfiguration groupsConfiguration;

    public GroupsWithMessagesConfiguration setJoinProtectedGroupConfiguration(JoinProtectedGroupConfiguration joinProtectedGroupConfiguration) {
        this.joinProtectedGroupConfiguration = joinProtectedGroupConfiguration;return this;
    }

    public GroupsWithMessagesConfiguration setCreateGroupConfiguration(CreateGroupConfiguration createGroupConfiguration) {
        this.createGroupConfiguration = createGroupConfiguration;return this;
    }

    public GroupsWithMessagesConfiguration setMessagesConfiguration(MessagesConfiguration messagesConfiguration) {
        this.messagesConfiguration = messagesConfiguration;return this;
    }

    public GroupsWithMessagesConfiguration setGroupsConfiguration(GroupsConfiguration groupsConfiguration) {
        this.groupsConfiguration = groupsConfiguration;return this;
    }

    public JoinProtectedGroupConfiguration getJoinProtectedGroupConfiguration() {
        return joinProtectedGroupConfiguration;
    }

    public CreateGroupConfiguration getCreateGroupConfiguration() {
        return createGroupConfiguration;
    }

    public MessagesConfiguration getMessagesConfiguration() {
        return messagesConfiguration;
    }

    public GroupsConfiguration getGroupsConfiguration() {
        return groupsConfiguration;
    }


}
