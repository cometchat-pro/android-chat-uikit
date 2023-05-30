package com.cometchat.chatuikit.extensions.thumbnailgeneration;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.extensions.imagemoderation.CometChatImageModerationBubble;
import com.cometchat.chatuikit.extensions.imagemoderation.ImageModerationConfiguration;
import com.cometchat.chatuikit.extensions.imagemoderation.ImageModerationStyle;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.resources.utils.custom_dialog.CometChatDialog;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.ImageBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.VideoBubbleStyle;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.framework.DataSource;
import com.cometchat.chatuikit.shared.framework.DataSourceDecorator;
import com.cometchat.chatuikit.extensions.Extensions;

public class ThumbnailGenerationExtensionDecorator extends DataSourceDecorator {

    private VideoBubbleStyle videoBubbleStyle;
    private ImageModerationStyle style;
    private ImageModerationConfiguration imageModerationConfiguration;
    @StyleRes
    int alertTextAppearance;
    @ColorInt
    int alertPositiveButtonColor, alertNegativeButtonColor, alertDialogBackgroundColor, alertTextColor;

    public ThumbnailGenerationExtensionDecorator(DataSource dataSource) {
        super(dataSource);
        style = new ImageModerationStyle();
    }

    public ThumbnailGenerationExtensionDecorator(DataSource dataSource, VideoBubbleStyle videoBubbleStyle) {
        super(dataSource);
        this.videoBubbleStyle = videoBubbleStyle;
        style = new ImageModerationStyle();
    }


    @Override
    public View getVideoBubbleContentView(Context context, String thumbnailUrl, VideoBubbleStyle videoBubbleStyle, MediaMessage mediaMessage, UIKitConstants.MessageBubbleAlignment alignment) {
        if (this.videoBubbleStyle == null) this.videoBubbleStyle = videoBubbleStyle;
        return super.getVideoBubbleContentView(context, Extensions.getThumbnailGeneration(mediaMessage), this.videoBubbleStyle, mediaMessage, alignment);
    }

    @Override
    public View getImageBubbleContentView(Context context, MediaMessage mediaMessage, ImageBubbleStyle imageBubbleStyle, UIKitConstants.MessageBubbleAlignment alignment) {
        if (mediaMessage != null && Extensions.getImageModeration(context, mediaMessage) && !mediaMessage.getSender().getUid().equalsIgnoreCase(CometChatUIKit.getLoggedInUser().getUid()))
            return getUnsafeContentView(context, mediaMessage, super.getImageBubbleContentView(context, mediaMessage, imageBubbleStyle, alignment));
        else return super.getImageBubbleContentView(context, mediaMessage, imageBubbleStyle, alignment);
    }

    private View getUnsafeContentView(Context context, MediaMessage mediaMessage, View previousView) {
        if (context != null && mediaMessage != null) {
            LinearLayout layout = new LinearLayout(context);

            layout.setBackgroundColor(context.getResources().getColor(R.color.primary));
            CometChatImageModerationBubble moderationBubble = new CometChatImageModerationBubble(context);
            style.setWarningTextColor(context.getResources().getColor(R.color.textColorWhite)).setAlertTextColor(CometChatTheme.getInstance(context).getPalette().getAccent()).setWarningIconTint(context.getResources().getColor(R.color.textColorWhite)).setWarningTextAppearance(CometChatTheme.getInstance(context).getTypography().getText3()).setAlertTextColor(CometChatTheme.getInstance(context).getPalette().getAccent()).setAlertTextAppearance(CometChatTheme.getInstance(context).getTypography().getName()).setAlertPositiveButtonColor(CometChatTheme.getInstance(context).getPalette().getPrimary()).setAlertNegativeButtonColor(CometChatTheme.getInstance(context).getPalette().getPrimary()).setAlertDialogBackgroundColor(CometChatTheme.getInstance(context).getPalette().getBackground());
            moderationBubble.setStyle(style);
            alertTextAppearance = style.getAlertTextAppearance();
            alertPositiveButtonColor = style.getAlertPositiveButtonColor();
            alertNegativeButtonColor = style.getAlertNegativeButtonColor();
            alertDialogBackgroundColor = style.getAlertDialogBackgroundColor();
            alertTextColor = style.getAlertTextColor();
            if (imageModerationConfiguration != null) {
                moderationBubble.setSensitiveContentBackgroundImage(imageModerationConfiguration.getSensitiveContentBackgroundImage());
                moderationBubble.setWarningImageIcon(imageModerationConfiguration.getWarningImageIcon());
                moderationBubble.setWarningText(imageModerationConfiguration.getWarningText());
                moderationBubble.setStyle(imageModerationConfiguration.getStyle());
                ImageModerationStyle imageModerationStyle = imageModerationConfiguration.getStyle();
                if (imageModerationStyle != null) {
                    if (imageModerationStyle.getAlertDialogBackgroundColor() != 0)
                        alertDialogBackgroundColor = imageModerationStyle.getAlertDialogBackgroundColor();
                    if (imageModerationStyle.getAlertPositiveButtonColor() != 0)
                        alertPositiveButtonColor = imageModerationStyle.getAlertPositiveButtonColor();
                    if (imageModerationStyle.getAlertNegativeButtonColor() != 0)
                        alertNegativeButtonColor = imageModerationStyle.getAlertNegativeButtonColor();
                    if (imageModerationStyle.getAlertTextAppearance() != 0)
                        alertTextAppearance = imageModerationStyle.getAlertTextAppearance();
                    if (imageModerationStyle.getAlertTextColor() != 0)
                        alertTextColor = imageModerationStyle.getAlertTextColor();
                }
            }
            moderationBubble.setOnClick(() -> new CometChatDialog(context, 0,
                    alertTextAppearance, CometChatTheme.getInstance(context).getTypography().getText3(), alertDialogBackgroundColor, 0, alertTextColor, imageModerationConfiguration != null && imageModerationConfiguration.getAlertText() != null && !imageModerationConfiguration.getAlertText().isEmpty() ? imageModerationConfiguration.getAlertText() : "Are you sure want to see unsafe content?", "", imageModerationConfiguration != null && imageModerationConfiguration.getPositiveButtonText() != null && !imageModerationConfiguration.getPositiveButtonText().isEmpty() ? imageModerationConfiguration.getPositiveButtonText() : context.getString(R.string.yes), imageModerationConfiguration != null && imageModerationConfiguration.getNegativeButtonText() != null && !imageModerationConfiguration.getNegativeButtonText().isEmpty() ? imageModerationConfiguration.getPositiveButtonText() : context.getString(R.string.cancel), "", alertPositiveButtonColor, alertNegativeButtonColor, 0, (alertDialog, which, popupId) -> {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    alertDialog.dismiss();
                    layout.removeAllViews();
                    Utils.removeParentFromView(previousView);
                    layout.addView(previousView);
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    alertDialog.dismiss();
                }
            }, 0, false));
            layout.addView(moderationBubble);
            return layout;
        }
        return null;
    }


    @Override
    public String getId() {
        return ThumbnailGenerationExtension.class.getName();
    }
}
