package com.cometchat.chatuikit.extensions.linkpreview;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;

public class LinkPreviewExtension implements ExtensionsDataSource {
    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(LinkPreviewExtensionDecorator::new);
    }

}
