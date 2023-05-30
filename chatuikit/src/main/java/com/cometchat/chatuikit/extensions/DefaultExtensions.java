package com.cometchat.chatuikit.extensions;

import com.cometchat.chatuikit.shared.framework.ExtensionsDataSource;
import com.cometchat.chatuikit.extensions.collaborative.document.CollaborativeDocumentExtension;
import com.cometchat.chatuikit.extensions.collaborative.whiteboard.CollaborativeWhiteboardExtension;
import com.cometchat.chatuikit.extensions.imagemoderation.ImageModerationExtension;
import com.cometchat.chatuikit.extensions.linkpreview.LinkPreviewExtension;
import com.cometchat.chatuikit.extensions.messagetranslation.MessageTranslationExtension;
import com.cometchat.chatuikit.extensions.polls.PollsExtension;
import com.cometchat.chatuikit.extensions.reaction.ReactionExtension;
import com.cometchat.chatuikit.extensions.smartreplies.SmartRepliesExtension;
import com.cometchat.chatuikit.extensions.sticker.StickerExtension;
import com.cometchat.chatuikit.extensions.textmoderation.TextModerationExtension;
import com.cometchat.chatuikit.extensions.thumbnailgeneration.ThumbnailGenerationExtension;

import java.util.ArrayList;
import java.util.List;

public class DefaultExtensions {
    public static List<ExtensionsDataSource> get() {
        List<ExtensionsDataSource> list = new ArrayList<>();
        list.add(new CollaborativeDocumentExtension());
        list.add(new CollaborativeWhiteboardExtension());
        list.add(new ImageModerationExtension());
        list.add(new LinkPreviewExtension());
        list.add(new MessageTranslationExtension());
        list.add(new PollsExtension());
        list.add(new ReactionExtension());
        list.add(new SmartRepliesExtension());
        list.add(new StickerExtension());
        list.add(new TextModerationExtension());
        list.add(new ThumbnailGenerationExtension());
        return list;
    }

}
