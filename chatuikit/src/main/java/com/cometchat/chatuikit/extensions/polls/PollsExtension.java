package com.cometchat.chatuikit.extensions.polls;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;

public class PollsExtension implements ExtensionsDataSource {
    private PollsConfiguration pollsConfiguration;

    public PollsExtension(PollsConfiguration pollsConfiguration) {
        this.pollsConfiguration = pollsConfiguration;
    }

    public PollsExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new PollsExtensionDecorator(var1, pollsConfiguration));
    }
}
