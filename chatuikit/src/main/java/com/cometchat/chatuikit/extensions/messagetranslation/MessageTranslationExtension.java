package com.cometchat.chatuikit.extensions.messagetranslation;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;

public class MessageTranslationExtension implements ExtensionsDataSource {

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(MessageTranslationDecorator::new);
    }
}
