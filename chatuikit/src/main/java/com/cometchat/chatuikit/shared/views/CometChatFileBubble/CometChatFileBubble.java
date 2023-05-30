package com.cometchat.chatuikit.shared.views.CometChatFileBubble;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.MediaUtils;
import com.google.android.material.card.MaterialCardView;

/**
 * CometChatFileBubble is a custom view that extends MaterialCardView and represents a file bubble in a chat application.
 * It provides functionality to display a file icon, title, and subtitle, and handles click events for downloading the file.
 */
public class CometChatFileBubble extends MaterialCardView {

    private Context context;
    private View view;
    private LinearLayout layout;
    private ImageView downloadIcon;
    private TextView title, subtitle;
    private String titleText;
    private String fileUrl;
    private OnClick onClick;

    /**
     * Constructs a new CometChatFileBubble with the given context.
     *
     * @param context The context of the view.
     */
    public CometChatFileBubble(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new CometChatFileBubble with the given context and attribute set.
     *
     * @param context The context of the view.
     * @param attrs   The attribute set of the view.
     */
    public CometChatFileBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new CometChatFileBubble with the given context, attribute set, and default style attribute.
     *
     * @param context      The context of the view.
     * @param attrs        The attribute set of the view.
     * @param defStyleAttr The default style attribute of the view.
     */
    public CometChatFileBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the CometChatFileBubble by setting up the initial state and inflating the layout.
     *
     * @param context The context of the view.
     */
    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        view = View.inflate(context, R.layout.file_bubble, null);
        layout = view.findViewById(R.id.parent);
        title = view.findViewById(R.id.tv_title);
        subtitle = view.findViewById(R.id.tv_subtitle);
        downloadIcon = view.findViewById(R.id.iv_download);
        addView(view);
        downloadIcon.setVisibility(GONE);
        title.setVisibility(GONE);
        subtitle.setVisibility(GONE);
        downloadIcon.setOnClickListener(view -> {
            if (onClick == null)
                MediaUtils.downloadFileFromUrl(context, fileUrl, titleText != null && !titleText.isEmpty() ? titleText : System.currentTimeMillis() + "", UIKitConstants.files.OPEN);
            else
                onClick.onClick();
        });
    }

    /**
     * Sets the text for the title of the file bubble.
     *
     * @param text The text to set as the title.
     */
    public void setTitleText(String text) {
        if (text != null && !text.isEmpty()) {
            title.setVisibility(VISIBLE);
            titleText = text;
            title.setText(text + "");
        }
    }

    /**
     * Sets the text color for the title of the file bubble.
     *
     * @param color The color to set for the title text.
     */
    public void setTitleTextColor(@ColorInt int color) {
        if (color != 0)
            title.setTextColor(color);
    }

    /**
     * Sets the font for the title of the file bubble.
     *
     * @param font The font to set for the title text.
     */
    public void setTitleTextFont(String font) {
        if (font != null && !font.isEmpty())
            title.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance for the title of the file bubble.
     *
     * @param appearance The text appearance resource ID to set for the title.
     */
    public void setTitleTextAppearance(@StyleRes int appearance) {
        if (appearance != 0)
            title.setTextAppearance(context, appearance);
    }

    /**
     * Sets the text for the subtitle of the file bubble.
     *
     * @param text The text to set as the subtitle.
     */
    public void setSubtitleText(String text) {
        if (text != null && !text.isEmpty()) {
            subtitle.setVisibility(VISIBLE);
            subtitle.setText(text + "");
        }
    }

    /**
     * Sets the text color for the subtitle of the file bubble.
     *
     * @param color The color to set for the subtitle text.
     */
    public void setSubtitleTextColor(@ColorInt int color) {
        if (color != 0)
            subtitle.setTextColor(color);
    }

    /**
     * Sets the font for the subtitle of the file bubble.
     *
     * @param font The font to set for the subtitle text.
     */
    public void setSubtitleTextFont(String font) {
        if (font != null && !font.isEmpty())
            subtitle.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance for the subtitle of the file bubble.
     *
     * @param appearance The text appearance resource ID to set for the subtitle.
     */
    public void setSubtitleTextAppearance(@StyleRes int appearance) {
        if (appearance != 0)
            subtitle.setTextAppearance(context, appearance);
    }

    /**
     * Sets the tint color for the download icon of the file bubble.
     *
     * @param color The color to set as the tint for the download icon.
     */
    public void setDownloadIconTintColor(@ColorInt int color) {
        if (color != 0)
            downloadIcon.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the icon for the download button of the file bubble.
     *
     * @param image The resource ID of the drawable to set as the download icon.
     */
    public void setDownloadIcon(@DrawableRes int image) {
        if (image != 0)
            downloadIcon.setImageResource(image);
    }

    /**
     * Sets the file URL, title text, and subtitle text for the file bubble.
     *
     * @param fileUrl      The URL of the file.
     * @param titleText    The text to set as the title.
     * @param subtitleText The text to set as the subtitle.
     */
    public void setFileUrl(String fileUrl, String titleText, String subtitleText) {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            this.fileUrl = fileUrl;
            downloadIcon.setVisibility(VISIBLE);
        }
        setTitleText(titleText);
        setSubtitleText(subtitleText);
    }

    /**
     * Applies the given style to the file bubble.
     *
     * @param style The FileBubbleStyle to apply.
     */
    public void setStyle(FileBubbleStyle style) {
        if (style != null) {
            setTitleTextColor(style.getTitleTextColor());
            setTitleTextFont(style.getTitleTextFont());
            setTitleTextAppearance(style.getTitleTextAppearance());
            setSubtitleTextColor(style.getSubtitleTextColor());
            setSubtitleTextFont(style.getSubtitleTextFont());
            setSubtitleTextAppearance(style.getSubtitleTextAppearance());
            setDownloadIconTintColor(style.getDownloadIconTint());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    /**
     * Sets the click listener for the file bubble.
     *
     * @param onClick The OnClick listener to set.
     */
    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public LinearLayout getView() {
        return layout;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getSubtitle() {
        return subtitle;
    }

    public ImageView getDownloadImageView() {
        return downloadIcon;
    }
}
