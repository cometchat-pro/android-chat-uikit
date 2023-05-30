package com.cometchat.chatuikit.extensions.sticker;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.extensions.sticker.keyboard.StickerKeyboardConfiguration;

public class StickerExtension implements ExtensionsDataSource {

    private StickerKeyboardConfiguration configuration;

    public StickerExtension(StickerKeyboardConfiguration configuration) {
        this.configuration = configuration;
    }

    public StickerExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new StickerExtensionDecorator(var1,configuration));
    }
}
