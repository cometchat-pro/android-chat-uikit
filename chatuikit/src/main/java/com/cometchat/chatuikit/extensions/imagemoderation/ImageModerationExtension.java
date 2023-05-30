package com.cometchat.chatuikit.extensions.imagemoderation;

import com.cometchat.chatuikit.shared.framework.ChatConfigurator;
import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;

public class ImageModerationExtension implements ExtensionsDataSource {
    private ImageModerationConfiguration configuration;

    public ImageModerationExtension(ImageModerationConfiguration configuration) {
        this.configuration = configuration;
    }

    public ImageModerationExtension() {
    }

    @Override
    public void enable() {
        ChatConfigurator.behaviorEnabler(var1 -> new ImageModerationDecorator(var1, configuration));
    }
}
