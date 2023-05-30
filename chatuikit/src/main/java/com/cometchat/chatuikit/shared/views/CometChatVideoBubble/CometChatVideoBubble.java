package com.cometchat.chatuikit.shared.views.CometChatVideoBubble;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.views.mediaview.CometChatMediaViewActivity;
import com.cometchat.pro.constants.CometChatConstants;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * Represents a custom video bubble view that extends the MaterialCardView.
 * <p>
 * The CometChatVideoBubble is used to display a video thumbnail with a play button overlay.
 */
public class CometChatVideoBubble extends MaterialCardView {
    private Context context;
    private View view;
    private LinearLayout parentLayout;
    private ShapeableImageView shapeableImageView;
    private ImageView playImageView;
    private @DrawableRes
    int placeHolderImage;
    private String videoUrl;
    private OnClick onClick;

    /**
     * Constructs a new CometChatVideoBubble with the provided context.
     *
     * @param context The context used for inflating the view.
     */
    public CometChatVideoBubble(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new CometChatVideoBubble with the provided context and attribute set.
     *
     * @param context The context used for inflating the view.
     * @param attrs   The attribute set used for custom styling.
     */
    public CometChatVideoBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new CometChatVideoBubble with the provided context, attribute set, and default style attribute.
     *
     * @param context      The context used for inflating the view.
     * @param attrs        The attribute set used for custom styling.
     * @param defStyleAttr The default style attribute.
     */
    public CometChatVideoBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the CometChatVideoBubble view by setting up the required components and listeners.
     *
     * @param context The context used for initialization.
     */
    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        placeHolderImage = R.drawable.black_placeholder;
        view = View.inflate(context, R.layout.video_bubble, null);
        parentLayout = view.findViewById(R.id.parent);
        shapeableImageView = view.findViewById(R.id.video);
        playImageView = view.findViewById(R.id.playBtn);
        addView(view);
        playImageView.setOnClickListener(view -> {
            if (onClick != null) onClick.onClick();
            else openMediaViewActivity();
        });
    }

    /**
     * Sets the video URL and placeholder image for the video thumbnail.
     *
     * @param videoUrl         The URL of the video.
     * @param placeHolderImage The placeholder image resource ID.
     */
    public void setVideoUrl(String videoUrl, @DrawableRes int placeHolderImage) {
        this.videoUrl = videoUrl;
        Glide.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(placeHolderImage != 0 ? placeHolderImage : this.placeHolderImage).skipMemoryCache(false).load(videoUrl).into(shapeableImageView);
    }

    /**
     * Sets the thumbnail URL and placeholder image for the video thumbnail.
     *
     * @param thumbnailUrl     The URL of the thumbnail image.
     * @param placeHolderImage The placeholder image resource ID.
     */
    public void setThumbnailUrl(String thumbnailUrl, @DrawableRes int placeHolderImage) {
        Glide.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(placeHolderImage != 0 ? placeHolderImage : this.placeHolderImage).skipMemoryCache(false).load(thumbnailUrl).into(shapeableImageView);
    }

    private void openMediaViewActivity() {
        if (videoUrl != null) {
            Intent intent = new Intent(context, CometChatMediaViewActivity.class);
            intent.putExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE, videoUrl);
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, CometChatConstants.MESSAGE_TYPE_VIDEO);
            context.startActivity(intent);
        }
    }

    /**
     * Sets the onClick listener for the play button.
     *
     * @param onClick The onClick listener to be set.
     */
    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    /**
     * Sets the play icon resource for the play button.
     *
     * @param playIcon The play icon resource ID.
     */
    public void setPlayIcon(@DrawableRes int playIcon) {
        if (playIcon != 0) playImageView.setImageResource(playIcon);
    }

    /**
     * Sets the tint color for the play button icon.
     *
     * @param color The tint color to be applied.
     */
    public void setPlayIconTintColor(@ColorInt int color) {
        if (color != 0) playImageView.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets the background color for the play button.
     *
     * @param color The background color to be applied.
     */
    public void setPlayIconBackgroundColor(@ColorInt int color) {
        if (color != 0) {
            playImageView.setBackgroundTintList(ColorStateList.valueOf(color));
        }
    }

    /**
     * Applies the specified style to the CometChatVideoBubble view.
     *
     * @param style The VideoBubbleStyle object containing the style attributes.
     * @see VideoBubbleStyle
     */
    public void setStyle(VideoBubbleStyle style) {
        if (style != null) {
            setPlayIconTintColor(style.getPlayIconTint());
            setPlayIconBackgroundColor(style.getPlayIconBackgroundColor());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    public LinearLayout getView() {
        return parentLayout;
    }

    public ShapeableImageView getVideoImageView() {
        return shapeableImageView;
    }

    public ImageView getPlayButtonImageView() {
        return playImageView;
    }

}
