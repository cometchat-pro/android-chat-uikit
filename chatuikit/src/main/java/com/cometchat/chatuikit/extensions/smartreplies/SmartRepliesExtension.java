package com.cometchat.chatuikit.extensions.smartreplies;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.extensions.smartreplies.view.SmartRepliesConfiguration;

public class SmartRepliesExtension implements ExtensionsDataSource {
    private SmartRepliesConfiguration configuration;

    public SmartRepliesExtension(SmartRepliesConfiguration configuration) {
        this.configuration = configuration;
    }

    public SmartRepliesExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new SmartRepliesExtensionDecorator(var1, configuration));
    }
}
