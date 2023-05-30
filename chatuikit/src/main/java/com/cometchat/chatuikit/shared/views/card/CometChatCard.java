package com.cometchat.chatuikit.shared.views.card;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.CometChatAvatar;
import com.google.android.material.card.MaterialCardView;

/**
 * Represents a custom card view that extends MaterialCardView.
 * <p>
 * This card view is specifically designed for CometChat application.
 */
public class CometChatCard extends MaterialCardView {
    private Context context;
    private LinearLayout subtitleViewLayout, bottomViewLayout;
    private TextView title;
    private CometChatAvatar avatar;

    /**
     * Constructor for creating a CometChatCard with only the context.
     *
     * @param context The context of the card.
     */
    public CometChatCard(Context context) {
        super(context);
        init(context, null, -1);
    }

    /**
     * Constructor for creating a CometChatCard with context and attributes.
     *
     * @param context The context of the card.
     * @param attrs   The attributes of the card.
     */
    public CometChatCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    /**
     * Constructor for creating a CometChatCard with context, attributes, and default style.
     *
     * @param context      The context of the card.
     * @param attrs        The attributes of the card.
     * @param defStyleAttr The default style attribute of the card.
     */
    public CometChatCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the CometChatCard by setting up the required views and properties.
     *
     * @param context      The context of the card.
     * @param attrs        The attributes of the card.
     * @param defStyleAttr The default style attribute of the card.
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        View view = View.inflate(context, R.layout.cometchat_card, null);
        subtitleViewLayout = view.findViewById(R.id.subtitle_view);
        bottomViewLayout = view.findViewById(R.id.bottom_view);
        title = view.findViewById(R.id.title);
        avatar = view.findViewById(R.id.avatar);
        avatar.setRadius(100);
        avatar.setCardElevation(0);
        addView(view);
    }

    /**
     * Sets the view for the bottom section of the card.
     *
     * @param view The view to be set as the bottom view.
     */
    public void setBottomView(View view) {
        Utils.handleView(bottomViewLayout, view, true);
    }

    /**
     * Sets the view for the subtitle section of the card.
     *
     * @param view The view to be set as the subtitle view.
     */
    public void setSubtitleView(View view) {
        Utils.handleView(subtitleViewLayout, view, true);
    }

    /**
     * Sets the style for the avatar in the card.
     *
     * @param avatarStyle The style configuration for the avatar.
     */
    public void setAvatarStyle(AvatarStyle avatarStyle) {
        if (avatarStyle != null) {
            avatar.setStyle(avatarStyle);
            if (avatarStyle.getWidth() > 0 && avatarStyle.getHeight() > 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.convertDpToPx(context, avatarStyle.getWidth()), Utils.convertDpToPx(context, avatarStyle.getHeight()));
                params.leftMargin = Utils.convertDpToPx(context, 16);
                avatar.setLayoutParams(params);
            }
        }
    }

    /**
     * Sets the image and name for the avatar in the card.
     *
     * @param url  The URL of the avatar image.
     * @param name The name associated with the avatar.
     */
    public void setAvatar(String url, String name) {
        if (avatar != null) avatar.setImage(url, name);
    }

    /**
     * Sets the font for the title in the card.
     *
     * @param fonts The name of the font file in the assets directory.
     */
    public void setTitleFont(String fonts) {
        if (fonts != null && !fonts.isEmpty())
            title.setTypeface(FontUtils.getInstance(context).getTypeFace(fonts));
    }

    /**
     * Sets the text appearance for the title in the card.
     *
     * @param appearance The style resource ID for the title text appearance.
     */
    public void setTitleTextAppearance(int appearance) {
        if (title != null && appearance != 0) title.setTextAppearance(context, appearance);
    }

    /**
     * Sets the color for the title in the card.
     *
     * @param color The color resource ID for the title text.
     */
    public void setTitleColor(int color) {
        if (color != 0) title.setTextColor(color);
    }

    /**
     * Sets the title text for the card.
     *
     * @param titleStr The text to be set as the title.
     */
    public void setTitle(String titleStr) {
        if (titleStr != null) {
            title.setText(titleStr);
        }
    }

    /**
     * Sets the style for the card.
     *
     * @param listItemStyle The style configuration for the card.
     * @see CardStyle
     */
    public void setStyle(CardStyle listItemStyle) {
        if (listItemStyle != null) {
            setTitleFont(listItemStyle.getTitleFont());
            setTitleColor(listItemStyle.getTitleColor());
            setTitleTextAppearance(listItemStyle.getTitleAppearance());
            if (listItemStyle.getDrawableBackground() != null)
                setBackgroundDrawable(listItemStyle.getDrawableBackground());
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
