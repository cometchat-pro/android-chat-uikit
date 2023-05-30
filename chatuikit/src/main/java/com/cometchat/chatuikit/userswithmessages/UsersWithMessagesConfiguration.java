package com.cometchat.chatuikit.userswithmessages;

import com.cometchat.chatuikit.messages.MessagesConfiguration;
import com.cometchat.chatuikit.users.UsersConfiguration;

 class UsersWithMessagesConfiguration {

    private UsersConfiguration usersConfiguration;
    private MessagesConfiguration messagesConfiguration;

    public UsersWithMessagesConfiguration setUsersConfiguration(UsersConfiguration usersConfiguration) {
        this.usersConfiguration = usersConfiguration;
        return this;
    }

    public UsersWithMessagesConfiguration setMessagesConfiguration(MessagesConfiguration messagesConfiguration) {
        this.messagesConfiguration = messagesConfiguration;
        return this;
    }

    public UsersConfiguration getUsersConfiguration() {
        return usersConfiguration;
    }

    public MessagesConfiguration getMessagesConfiguration() {
        return messagesConfiguration;
    }
}
