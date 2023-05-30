package com.cometchat.chatuikit.shared.views.CometChatImageBubble;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.OnClick;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.views.mediaview.CometChatMediaViewActivity;
import com.cometchat.pro.constants.CometChatConstants;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * The CometChatImageBubble class is a custom view in Android that represents an image bubble, commonly used in messaging applications to display images with or without captions.
 * It extends the MaterialCardView class and provides various customization options.
 * <p>
 * The class features a layout with an image view, a clickable overlay, and a text view for the caption.
 * It supports setting the image URL, placeholder image, and whether the image is a GIF.
 * The image is loaded asynchronously using the Glide library.
 * <p>
 * Additional customization options include setting the caption text, text color, font, and appearance.
 * The bubble's style can be modified by changing the background, border width, corner radius, and border color.
 * <p>
 * The class also provides an OnClick listener interface to handle click events on the image bubble.
 * It exposes methods to retrieve the parent layout, image view, and text view for further customization.
 * <p>
 * Furthermore, the CometChatImageBubble class includes a method to set a custom style using an ImageBubbleStyle object, allowing for easy and consistent theming across different image bubbles.
 * <p>
 * Lastly, the class provides functionality to open a media view activity when the image bubble is clicked, allowing users to view the image in full-screen mode.
 * <p>
 * Overall, the CometChatImageBubble class offers a flexible and customizable image bubble view for Android applications, particularly useful in messaging interfaces.
 */

public class CometChatImageBubble extends MaterialCardView {

    private Context context;
    private LinearLayout parent;
    private View view;
    private @DrawableRes
    int placeHolderImage;
    private ShapeableImageView shapeableImageView;
    private View openImageView;
    private TextView textView;
    private OnClick onClick;
    private String imageUrl;

    /**
     * Constructs a new instance of the CometChatImageBubble class.
     *
     * @param context The context in which the view is created.
     */
    public CometChatImageBubble(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new instance of the CometChatImageBubble class.
     *
     * @param context The context in which the view is created.
     * @param attrs   The attribute set.
     */
    public CometChatImageBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new instance of the CometChatImageBubble class.
     *
     * @param context      The context in which the view is created.
     * @param attrs        The attribute set.
     * @param defStyleAttr The default style attribute.
     */
    public CometChatImageBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the view by setting up the layout, image view, click listeners, and default values.
     *
     * @param context The context in which the view is created.
     */
    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        placeHolderImage = R.drawable.ic_default_image;
        this.context = context;
        view = View.inflate(context, R.layout.image_bubble, null);
        shapeableImageView = view.findViewById(R.id.image);
        openImageView = view.findViewById(R.id.click);
        parent = view.findViewById(R.id.parent);
        textView = view.findViewById(R.id.caption);
        textView.setVisibility(GONE);
        addView(view);
        openImageView.setOnClickListener(view -> {
            if (onClick != null) onClick.onClick();
            else openMediaViewActivity();
        });
    }

    /**
     * Sets the caption for the image bubble.
     *
     * @param caption The caption text to be set.
     */
    public void setCaption(String caption) {
        if (caption != null && !caption.isEmpty()) {
            textView.setVisibility(VISIBLE);
            textView.setText(caption);
        } else textView.setVisibility(GONE);
    }

    /**
     * Sets the text color for the caption.
     *
     * @param color The color value to be set.
     */
    public void setCaptionTextColor(@ColorInt int color) {
        if (color != 0) {
            textView.setTextColor(color);
        }
    }

    /**
     * Sets the font for the caption text.
     *
     * @param font The font name or path to be set.
     */
    public void setCaptionTextFont(String font) {
        if (font != null && !font.isEmpty()) {
            textView.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
        }
    }

    /**
     * Sets the text appearance for the caption.
     *
     * @param appearance The text appearance style resource.
     */
    public void setCaptionTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) {
            textView.setTextAppearance(context, appearance);
        }
    }

    /**
     * Sets the image URL, placeholder image, and whether the image is a GIF.
     * The image is loaded asynchronously using the Glide library.
     *
     * @param url              The URL of the image to be displayed.
     * @param placeHolderImage The placeholder image resource ID.
     * @param isGif            Determines if the image is a GIF or not.
     */
    public void setImageUrl(String url, @DrawableRes int placeHolderImage, boolean isGif) {
        this.imageUrl = url;
        if (!isGif)
            Glide.with(context).asBitmap().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(placeHolderImage != 0 ? placeHolderImage : this.placeHolderImage).skipMemoryCache(false).load(url).into(shapeableImageView);
        else
            Glide.with(context).asGif().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(placeHolderImage != 0 ? placeHolderImage : this.placeHolderImage).skipMemoryCache(false).load(url).into(shapeableImageView);
    }

    /**
     * Sets the click listener for the image bubble.
     *
     * @param onClick The OnClick listener to be set.
     */
    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public LinearLayout getView() {
        return parent;
    }

    public ShapeableImageView getImageView() {
        return shapeableImageView;
    }

    public TextView getTextView() {
        return textView;
    }

    /**
     * Sets the style for the image bubble using an ImageBubbleStyle object.
     *
     * @param style The ImageBubbleStyle object representing the style to be applied.
     */
    public void setStyle(ImageBubbleStyle style) {
        if (style != null) {
            setCaptionTextColor(style.getTextColor());
            setCaptionTextFont(style.getTextFont());
            setCaptionTextAppearance(style.getTextAppearance());
            if (style.getDrawableBackground() != null)
                super.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0)
                super.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) super.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) super.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) super.setStrokeColor(style.getBorderColor());
        }
    }

    private void openMediaViewActivity() {
        if (imageUrl != null) {
            Intent intent = new Intent(context, CometChatMediaViewActivity.class);
            intent.putExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE, imageUrl);
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, CometChatConstants.MESSAGE_TYPE_IMAGE);
            context.startActivity(intent);
        }
    }
}
