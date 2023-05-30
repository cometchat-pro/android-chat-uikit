package com.cometchat.chatuikit.extensions.thumbnailgeneration;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.VideoBubbleStyle;

public class ThumbnailGenerationExtension implements ExtensionsDataSource {
    private VideoBubbleStyle videoBubbleStyle;

    public ThumbnailGenerationExtension(VideoBubbleStyle videoBubbleStyle) {
        this.videoBubbleStyle = videoBubbleStyle;
    }

    public ThumbnailGenerationExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new ThumbnailGenerationExtensionDecorator(var1, videoBubbleStyle));
    }
}
