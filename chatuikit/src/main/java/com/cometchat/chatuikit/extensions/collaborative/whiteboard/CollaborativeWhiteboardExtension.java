package com.cometchat.chatuikit.extensions.collaborative.whiteboard;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.extensions.collaborative.CollaborativeBoardBubbleConfiguration;

public class CollaborativeWhiteboardExtension implements ExtensionsDataSource {

    private CollaborativeBoardBubbleConfiguration configuration;

    public CollaborativeWhiteboardExtension(CollaborativeBoardBubbleConfiguration configuration) {
        this.configuration = configuration;
    }

    public CollaborativeWhiteboardExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new CollaborativeWhiteboardExtensionDecorator(var1, configuration));
    }

}
