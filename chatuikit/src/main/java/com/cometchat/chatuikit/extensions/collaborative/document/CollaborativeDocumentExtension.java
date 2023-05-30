package com.cometchat.chatuikit.extensions.collaborative.document;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.extensions.collaborative.CollaborativeBoardBubbleConfiguration;

public class CollaborativeDocumentExtension implements ExtensionsDataSource {
    private CollaborativeBoardBubbleConfiguration configuration;

    public CollaborativeDocumentExtension(CollaborativeBoardBubbleConfiguration configuration) {
        this.configuration = configuration;
    }

    public CollaborativeDocumentExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new CollaborativeDocumentExtensionDecorator(var1, configuration));
    }
}
