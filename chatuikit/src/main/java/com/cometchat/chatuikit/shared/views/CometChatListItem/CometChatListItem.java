package com.cometchat.chatuikit.shared.views.CometChatListItem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.CometChatAvatar;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.CometChatStatusIndicator;
import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.StatusIndicatorStyle;
import com.google.android.material.card.MaterialCardView;

/**
 * CometChatListItem is a custom view that represents an item in a list with a title, avatar, status indicator, and optional views.
 * It extends MaterialCardView to provide card-like appearance and styling.
 * This view provides various methods to customize its appearance and behavior, including setting the avatar image, title text,
 * status indicator color, separator color, and more. It also supports adding additional views to the tail and subtitle sections.
 * Usage example:
 * <pre>{@code
 * CometChatListItem listItem = new CometChatListItem(context);
 * listItem.setTitle("John Doe");
 * listItem.setAvatar("https://example.com/avatar.png", "John Doe");
 * listItem.setStatusIndicatorColor(Color.GREEN);
 * listItem.hideSeparator(true);
 * listItem.setTailView(tailView);
 * listItem.setSubtitleView(subtitleView);
 * // Add the listItem to the parent view or layout
 * parentView.addView(listItem);
 * }</pre>
 */
public class CometChatListItem extends MaterialCardView {

    private CometChatAvatar cometChatAvatar;
    private CometChatStatusIndicator cometChatStatusIndicator;
    private TextView title, separator;
    private LinearLayout subtitleView, tailView;
    private Context context;
    private View view;
    private boolean separatorHidden;

    public CometChatListItem(Context context) {
        super(context);
        init(context, null, -1);
    }

    public CometChatListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public CometChatListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        view = View.inflate(context, R.layout.cometchat_list_item, null);
        cometChatAvatar = view.findViewById(R.id.item_avatar);
        cometChatStatusIndicator = view.findViewById(R.id.item_presence);
        title = view.findViewById(R.id.item_title);
        subtitleView = view.findViewById(R.id.subtitle_view);
        tailView = view.findViewById(R.id.tail_view);
        separator = view.findViewById(R.id.tv_Separator);
        cometChatAvatar.setRadius(100);
        addView(view);
    }

    /**
     * Sets the tail view for the list item.
     *
     * @param view The tail view to be set.
     */
    public void setTailView(View view) {
        Utils.handleView(tailView, view, true);
    }

    /**
     * Sets the subtitle view for the list item.
     *
     * @param view The subtitle view to be set.
     */
    public void setSubtitleView(View view) {
        Utils.handleView(subtitleView, view, true);
    }

    /**
     * Hides or shows the separator based on the given flag.
     *
     * @param separatorHidden true to hide the separator, false to show it.
     */
    public void hideSeparator(boolean separatorHidden) {
        separator.setVisibility(separatorHidden ? GONE : VISIBLE);
        this.separatorHidden = separatorHidden;
    }

    /**
     * Hides or shows the status indicator based on the given flag.
     *
     * @param hide true to hide the status indicator, false to show it.
     */
    public void hideStatusIndicator(boolean hide) {
        cometChatStatusIndicator.setVisibility(hide ? GONE : VISIBLE);
    }

    /**
     * Sets the color of the separator.
     *
     * @param color The color to set for the separator.
     */
    public void setSeparatorColor(@ColorInt int color) {
        if (color != 0) separator.setBackgroundColor(color);
    }

    /**
     * Sets the drawable for the separator.
     *
     * @param drawable The drawable to set for the separator.
     */
    public void setSeparatorDrawable(Drawable drawable) {
        if (drawable != null) separator.setBackground(drawable);
    }

    /**
     * Sets the style for the avatar.
     *
     * @param avatarStyle The style to apply to the avatar.
     * @see AvatarStyle
     */
    public void setAvatarStyle(AvatarStyle avatarStyle) {
        if (avatarStyle != null) {
            cometChatAvatar.setStyle(avatarStyle);
            if (avatarStyle.getWidth() > 0 && avatarStyle.getHeight() > 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.convertDpToPx(context, avatarStyle.getWidth()), Utils.convertDpToPx(context, avatarStyle.getHeight()));
                params.leftMargin = Utils.convertDpToPx(context, 16);
                cometChatAvatar.setLayoutParams(params);
            }
        }
    }

    /**
     * Sets the style for the status indicator.
     *
     * @param style The style to apply to the status indicator.
     * @see StatusIndicatorStyle
     */
    public void setStatusIndicatorStyle(StatusIndicatorStyle style) {
        if (style != null) {
            cometChatStatusIndicator.setStyle(style);
            if (style.getWidth() > 0 && style.getHeight() > 0)
                statusIndicatorDimensions(style.getWidth(), style.getHeight());
        }
    }

    /**
     * Sets the dimensions for the status indicator.
     *
     * @param weight The weight (width) of the status indicator.
     * @param height The height of the status indicator.
     */
    public void statusIndicatorDimensions(int weight, int height) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.convertDpToPx(context, weight), Utils.convertDpToPx(context, height));
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.item_avatar);
        layoutParams.addRule(RelativeLayout.ALIGN_END, R.id.item_avatar);
        layoutParams.topMargin = -15;
        cometChatStatusIndicator.setLayoutParams(layoutParams);
    }

    public CometChatAvatar getCometChatAvatar() {
        return cometChatAvatar;
    }

    public CometChatStatusIndicator getCometChatStatusIndicator() {
        return cometChatStatusIndicator;
    }

    public TextView getTitleView() {
        return title;
    }

    public TextView getSeparatorView() {
        return separator;
    }

    /**
     * Sets the avatar image and name.
     *
     * @param url  The URL of the avatar image.
     * @param name The name associated with the avatar.
     */
    public void setAvatar(String url, String name) {
        if (cometChatAvatar != null) cometChatAvatar.setImage(url, name);
    }

    /**
     * Sets the background color of the status indicator.
     *
     * @param color The color to set for the status indicator.
     */
    public void setStatusIndicatorColor(@ColorInt int color) {
        if (cometChatStatusIndicator != null) {
            cometChatStatusIndicator.setBackgroundColor(color);
        }
    }

    /**
     * Sets the background image for the status indicator.
     *
     * @param drawable The drawable resource to set as the background image.
     */
    public void setStatusIndicatorIcon(@DrawableRes int drawable) {
        if (cometChatStatusIndicator != null) {
            cometChatStatusIndicator.setBackgroundImage(drawable);
        }
    }

    /**
     * Sets the font for the title TextView.
     *
     * @param fonts The name of the font to set.
     */
    public void setTitleFont(String fonts) {
        if (fonts != null && !fonts.isEmpty())
            title.setTypeface(FontUtils.getInstance(context).getTypeFace(fonts));
    }

    /**
     * Sets the text appearance for the title TextView.
     *
     * @param appearance The text appearance resource ID to set.
     */
    public void setTitleTextAppearance(int appearance) {
        if (title != null && appearance != 0) title.setTextAppearance(context, appearance);
    }

    /**
     * Sets the color for the title TextView.
     *
     * @param color The color resource ID to set.
     */
    public void setTitleColor(int color) {
        if (color != 0) title.setTextColor(color);
    }

    /**
     * Sets the title text for the list item.
     *
     * @param titleStr The title text to set.
     */
    public void setTitle(String titleStr) {
        if (titleStr != null) {
            title.setText(titleStr);
        }
    }

    /**
     * Sets the style for the list item.
     *
     * @param listItemStyle The style to apply to the list item.
     * @see ListItemStyle
     */
    public void setStyle(ListItemStyle listItemStyle) {
        if (listItemStyle != null) {
            setTitleFont(listItemStyle.getTitleFont());
            setTitleColor(listItemStyle.getTitleColor());
            setTitleTextAppearance(listItemStyle.getTitleAppearance());
            setSeparatorColor(listItemStyle.getSeparatorColor());
            setSeparatorDrawable(listItemStyle.getSeparatorDrawable());

            if (listItemStyle.getDrawableBackground() != null)
                setBackgroundDrawable(listItemStyle.getSeparatorDrawable());
            else if (listItemStyle.getBackground() != 0)
                setBackgroundColor(listItemStyle.getBackground());

            if (listItemStyle.getCornerRadius() > -1) {
                setRadius(listItemStyle.getCornerRadius());
            }
            if (listItemStyle.getBorderColor() != 0) {
                setStrokeColor(listItemStyle.getBorderColor());
            }
            if (listItemStyle.getBorderWidth() > -1) {
                setStrokeWidth(listItemStyle.getBorderWidth());
            }

        }
    }

}
