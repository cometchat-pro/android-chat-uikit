package com.cometchat.chatuikit.extensions.linkpreview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.extensions.Extensions;

import org.json.JSONObject;

import java.util.HashMap;

public class LinkPreviewExtensionDecorator extends DataSourceDecorator {

    private LinkPreviewBubbleStyle linkPreviewBubbleStyle;

    public LinkPreviewExtensionDecorator(DataSource dataSource) {
        super(dataSource);
    }

    public LinkPreviewExtensionDecorator(DataSource dataSource, LinkPreviewBubbleStyle linkPreviewBubbleStyle) {
        super(dataSource);
        this.linkPreviewBubbleStyle = linkPreviewBubbleStyle;
    }

    @Override
    public View getTextBubbleContentView(Context context, TextMessage textMessage, TextBubbleStyle bubbleStyle, int gravity, UIKitConstants.MessageBubbleAlignment alignment) {
        HashMap<String, JSONObject> extensionList = Extensions.extensionCheck(textMessage);
        if (extensionList != null) {
            if (extensionList.containsKey("linkPreview")) {
                LinkPreviewBubble linkPreviewBubble = new LinkPreviewBubble(context);
                linkPreviewBubble.addChildView(super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment));
                JSONObject linkPreviewJsonObject = extensionList.get("linkPreview");
                try {
                    if (linkPreviewJsonObject != null) {
                        LinkPreviewBubbleStyle linkPreviewBubbleDefaultStyle;
                        if (linkPreviewBubbleStyle == null) {
                            if (gravity == Gravity.START) {
                                linkPreviewBubbleDefaultStyle = new LinkPreviewBubbleStyle().setPlayIconTint(CometChatTheme.getInstance(context).getPalette().getAccent()).setPlayIconBackgroundTint(CometChatTheme.getInstance(context).getPalette().getAccent300()).setTitleColor(CometChatTheme.getInstance(context).getPalette().getAccent()).setTitleAppearance(CometChatTheme.getInstance(context).getTypography().getName()).setSubTitleColor(CometChatTheme.getInstance(context).getPalette().getAccent600()).setSubTitleAppearance(CometChatTheme.getInstance(context).getTypography().getSubtitle2());
                            } else {
                                linkPreviewBubbleDefaultStyle = new LinkPreviewBubbleStyle().setPlayIconTint(CometChatTheme.getInstance(context).getPalette().getAccent()).setPlayIconBackgroundTint(CometChatTheme.getInstance(context).getPalette().getAccent300()).setTitleColor(context.getResources().getColor(R.color.textColorWhite)).setTitleAppearance(CometChatTheme.getInstance(context).getTypography().getName()).setSubTitleColor(CometChatTheme.getInstance(context).getPalette().getAccent600()).setSubTitleAppearance(CometChatTheme.getInstance(context).getTypography().getSubtitle2());
                            }
                        }else linkPreviewBubbleDefaultStyle=linkPreviewBubbleStyle;
                        String strDescription = linkPreviewJsonObject.getString("description");
                        String strImage = linkPreviewJsonObject.getString("image");
                        String strTitle = linkPreviewJsonObject.getString("title");
                        String url = linkPreviewJsonObject.getString("url");
                        linkPreviewBubble.setUrl(url);
                        linkPreviewBubble.image(strImage);
                        linkPreviewBubble.title(strTitle);
                        linkPreviewBubble.subtitle(strDescription);
                        linkPreviewBubble.setStyle(linkPreviewBubbleDefaultStyle);

                    } else
                        super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment);
                } catch (Exception e) {
                    e.printStackTrace();
                    super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment);
                }
                return linkPreviewBubble;
            }
        }
        return super.getTextBubbleContentView(context, textMessage, bubbleStyle, gravity, alignment);
    }

    @Override
    public String getId() {
        return LinkPreviewExtensionDecorator.class.getName();
    }
}
