package com.cometchat.chatuikit.shared.cometchatuikit;

import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;

import java.util.List;

/**
 * The settings class for configuring the UIKit functionality.
 */
public class UIKitSettings {
    private final String appId;
    private final String region;
    private final String subscriptionType;
    private final boolean autoEstablishSocketConnection;
    private final String authKey;
    private final List<String> roles;
    private final List<ExtensionsDataSource> extensions;

    /**
     * Constructs a new instance of `UIKitSettings` using the builder pattern.
     *
     * @param builder The builder object used to construct the settings.
     */
    private UIKitSettings(UIKitSettingsBuilder builder) {
        this.appId = builder.appId;
        this.region = builder.region;
        this.subscriptionType = builder.subscriptionType;
        this.autoEstablishSocketConnection = builder.autoEstablishSocketConnection;
        this.authKey = builder.authKey;
        this.extensions = builder.extensions;
        this.roles = builder.roles;
    }

    /**
     * Returns the app ID.
     *
     * @return The app ID.
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Returns the region where the app is hosted.
     *
     * @return The region where the app is hosted.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns the list of roles associated with the app.
     *
     * @return The list of roles associated with the app.
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Returns the subscription type for presence updates.
     *
     * @return The subscription type for presence updates.
     */
    public String getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * Returns the authentication key for the app.
     *
     * @return The authentication key for the app.
     */
    public String getAuthKey() {
        return authKey;
    }

    /**
     * Returns the list of extensions data sources.
     *
     * @return The list of extensions data sources.
     */
    public List<ExtensionsDataSource> getExtensions() {
        return extensions;
    }

    /**
     * Returns whether the socket connection is automatically established.
     *
     * @return `true` if the socket connection is automatically established, `false` otherwise.
     */
    public boolean isAutoEstablishSocketConnection() {
        return autoEstablishSocketConnection;
    }

    /**
     * Builder class for constructing `UIKitSettings` instances.
     */
    public static class UIKitSettingsBuilder {
        private String appId;
        private String region;
        private String subscriptionType = "NONE";
        private List<String> roles;
        private Boolean autoEstablishSocketConnection = true;
        private String authKey;
        private List<ExtensionsDataSource> extensions;

        /**
         * Constructs a new instance of `UIKitSettingsBuilder`.
         */
        public UIKitSettingsBuilder() {
        }

        /**
         * Builds a new instance of `UIKitSettings` using the provided configuration.
         *
         * @return A new instance of `UIKitSettings`.
         */
        public UIKitSettings build() {
            return new UIKitSettings(this);
        }

        /**
         * Sets the app ID.
         *
         * @param appId The app ID.
         * @return The builder object.
         */
        public UIKitSettingsBuilder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * Sets the region where the app is hosted.
         *
         * @param region The region where the app is hosted.
         * @return The builder object.
         */
        public UIKitSettingsBuilder setRegion(String region) {
            this.region = region;
            return this;
        }

        /**
         * Sets the subscription type to subscribe to presence updates for all users.
         *
         * @return The builder object.
         */
        public UIKitSettingsBuilder subscribePresenceForAllUsers() {
            this.subscriptionType = "ALL_USERS";
            return this;
        }

        /**
         * Sets the subscription type to subscribe to presence updates for the specified roles.
         *
         * @param roles The list of roles to subscribe to.
         * @return The builder object.
         */
        public UIKitSettingsBuilder subscribePresenceForRoles(List<String> roles) {
            this.subscriptionType = "ROLES";
            this.roles = roles;
            return this;
        }

        /**
         * Sets the subscription type to subscribe to presence updates for friends.
         *
         * @return The builder object.
         */
        public UIKitSettingsBuilder subscribePresenceForFriends() {
            this.subscriptionType = "FRIENDS";
            return this;
        }

        /**
         * Sets the list of roles associated with the app.
         *
         * @param roles The list of roles associated with the app.
         * @return The builder object.
         */
        public UIKitSettingsBuilder setRoles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        /**
         * Sets whether to automatically establish a socket connection.
         *
         * @param autoEstablishSocketConnection Flag indicating whether to automatically establish a socket connection.
         * @return The builder object.
         */
        public UIKitSettingsBuilder setAutoEstablishSocketConnection(Boolean autoEstablishSocketConnection) {
            this.autoEstablishSocketConnection = autoEstablishSocketConnection;
            return this;
        }

        /**
         * Sets the authentication key for the app.
         *
         * @param authKey The authentication key for the app.
         * @return The builder object.
         */
        public UIKitSettingsBuilder setAuthKey(String authKey) {
            this.authKey = authKey;
            return this;
        }

        /**
         * Sets the list of extensions data sources.
         *
         * @param extensions The list of extensions data sources.
         * @return The builder object.
         */
        public UIKitSettingsBuilder setExtensions(List<ExtensionsDataSource> extensions) {
            this.extensions = extensions;
            return this;
        }
    }
}