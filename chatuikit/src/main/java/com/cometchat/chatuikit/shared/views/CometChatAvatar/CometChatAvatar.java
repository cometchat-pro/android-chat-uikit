package com.cometchat.chatuikit.shared.views.CometChatAvatar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

/**
 * The CometChatAvatar class is a custom view in Android that represents an avatar, typically used for displaying user profile pictures.
 * It extends the MaterialCardView class and provides various customization options such as setting the image, text, background color, border color, and border width.
 * The avatar can be set with an image URL or a drawable, and if no image is available, it can display a two-letter initial based on the user's name.
 * The class uses the Glide library for loading images asynchronously.
 * It also supports applying styles to the avatar using an AvatarStyle object.
 * Overall, the CometChatAvatar class provides a flexible and customizable avatar view for Android applications.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 27th May 2023
 */

public class CometChatAvatar extends MaterialCardView {

    private static final String TAG = CometChatAvatar.class.getSimpleName();

    private Drawable drawable;

    private String text;

    private String avatarUrl;

    private RectF rectF;

    private Context context;

    private int color;

    private int borderColor;

    private int backgroundColor;

    private float borderWidth;

    private float radius;

    private MaterialCardView outerCardView, innerCardView;

    private RelativeLayout innerLayout;

    private ImageView imageView;

    private TextView textView;

    /**
     * Constructor for creating a CometChatAvatar instance.
     *
     * @param context the context of the current state of the application
     */
    public CometChatAvatar(Context context) {
        super(context);
        if (!isInEditMode()) this.context = context;
    }

    /**
     * Constructor for creating a CometChatAvatar instance with attribute set.
     *
     * @param context the context of the current state of the application
     * @param attrs   the attributes of the XML tag that is inflating the view
     */
    public CometChatAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (!isInEditMode()) getAttributes(attrs);
    }

    /**
     * Constructor for creating a CometChatAvatar instance with attribute set and default style.
     *
     * @param context      the context of the current state of the application
     * @param attrs        the attributes of the XML tag that is inflating the view
     * @param defStyleAttr the default style to apply to this view
     */
    public CometChatAvatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (!isInEditMode()) getAttributes(attrs);
    }

    private void getAttributes(AttributeSet attrs) {
        View view = View.inflate(context, R.layout.cometchat_avatar, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Avatar, 0, 0);

        drawable = a.getDrawable(R.styleable.Avatar_image);
        radius = a.getDimension(R.styleable.Avatar_corner_radius, 18);
        backgroundColor = a.getColor(R.styleable.Avatar_background_color, 0);
        avatarUrl = a.getString(R.styleable.Avatar_avatar);
        borderColor = a.getColor(R.styleable.Avatar_borderColor, 0);
        borderWidth = a.getDimension(R.styleable.Avatar_borderWidth, 0);
        addView(view);

        innerCardView = view.findViewById(R.id.card_view);
        innerLayout = view.findViewById(R.id.inner_view_layout);
        if (color == 0) {
            setBorderColor(getResources().getColor(R.color.primary));
        } else {
            setBorderColor(color);
        }
        innerCardView.setCardBackgroundColor(backgroundColor);
        setRadius(radius);
        setBorderColor(borderColor);
        setBorderWidth((int) borderWidth);
        imageView = view.findViewById(R.id.image);
        textView = view.findViewById(R.id.text);
        initCard(this);
        initCard(innerCardView);
        if (drawable != null) imageView.setImageDrawable(drawable);
    }

    private void initCard(MaterialCardView cardView) {
        if (cardView != null) {
            this.setCardBackgroundColor(Color.TRANSPARENT);
            this.setCardElevation(0);
            this.setRadius(0);
        }
    }

    /**
     * Sets the image of the CometChatAvatar using the provided avatar URL.
     *
     * @param avatarUrl a non-null String representing the URL of the avatar image.
     */

    public void setImage(@NonNull String avatarUrl) {
        this.avatarUrl = avatarUrl;
        if (isValidContextForGlide(context)) setValues();
    }

    /**
     * Sets the image for the avatar with the provided drawable and avatar URL.
     * If the provided drawable is not null, it will be used as the placeholder image.
     * If the provided avatar URL is not null, it will be used to load the image using Glide library.
     * If the context is valid for Glide, the image will be loaded and set using setValues() method.
     *
     * @param drawable  the placeholder drawable for the avatar image. Can be null.
     * @param avatarUrl the URL of the avatar image to be loaded. Cannot be null.
     */
    public void setImage(Drawable drawable, @NonNull String avatarUrl) {
        this.drawable = drawable;
        this.avatarUrl = avatarUrl;
        if (isValidContextForGlide(context)) {
            setValues();
        }
    }

    /**
     * Returns the current avatar URL of the view.
     *
     * @return the avatar URL of the view.
     */
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    /**
     * Sets the name to be displayed in the avatar.
     *
     * @param name The name to be displayed.
     */
    public void setName(@NonNull String name) {

        if (name.length() >= 2) {
            text = name.trim().substring(0, 2);
        } else {
            text = name;
        }
        imageView.setVisibility(View.GONE);
        textView.setText(text.toUpperCase());
        textView.setVisibility(View.VISIBLE);
    }

    /**
     * Sets the text color of the avatar's text.
     *
     * @param color The color to set the avatar's text to.
     */
    public void setTextColor(int color) {
        if (color != 0) textView.setTextColor(color);
    }

    /**
     * Sets the font to use for the avatar's text.
     *
     * @param font The name of the font to use for the avatar's text.
     */
    public void setTextFont(String font) {
        if (textView != null && font != null && !font.isEmpty())
            textView.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the text appearance of the avatar's text.
     *
     * @param appearance The appearance to set the avatar's text to.
     */
    public void setTextAppearance(int appearance) {
        if (appearance != 0) textView.setTextAppearance(context, appearance);
    }

    /**
     * Sets the text size of the avatar's text.
     *
     * @param size The size to set the avatar's text to.
     */
    public void setTextSize(int size) {
        if (size > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    /**
     * Sets the image of the avatar using the provided avatar URL, or sets the name as the text if no URL is provided.
     *
     * @param url  The avatar URL to load the image from, or null if no image should be loaded.
     * @param name The name to display as the avatar's text if no image is loaded.
     */
    public void setImage(@NonNull String url, @NonNull String name) {
        if (url == null || url.isEmpty())
            setName(name);
        else
            setImage(url);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    /**
     * Sets the image to be displayed in the CometChatAvatar.
     *
     * @param drawable The Drawable to be displayed.
     */
    public void setImage(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    /**
     * This method sets the image and other values like visibility for the CometChatAvatar view.
     * If the provided avatarUrl is not null or empty, it loads the image using Glide library and sets the visibility
     * of imageView to VISIBLE and textView to GONE.
     * If the provided avatarUrl is null or empty, it sets the visibility of imageView to GONE and textView to VISIBLE.
     * It also sets a placeholder drawable if it is provided.
     * If any exception occurs during the Glide loading process, it shows a toast with the error message.
     */
    private void setValues() {
        try {
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                if (context != null) {
                    Glide.with(context).load(avatarUrl).placeholder(drawable).into(imageView);
                }
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        invalidate();
    }

    /**
     * Checks if a given context is a valid context for Glide image loading.
     *
     * @param context The context to check.
     * @return True if the context is valid, false otherwise.
     */
    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            return !activity.isDestroyed() && !activity.isFinishing();
        }
        return true;
    }

    /**
     * This method is used to set border color of avatar.
     *
     * @param color
     */
    public void setBorderColor(@ColorInt int color) {
        this.borderColor = color;
        if (color != 0) {
            innerCardView.setStrokeColor(color);
        }
    }

    /**
     * This method is used to set border width of avatar
     *
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        if (borderWidth >= 0) innerCardView.setStrokeWidth(borderWidth);
    }

    /**
     * Sets the corner radius for the CometChatAvatar view.
     *
     * @param radius the value of corner radius to be set
     */
    public void setCornerRadius(float radius) {
        this.radius = radius;
        if (radius >= 0) {
            innerCardView.setRadius(radius);
            setRadius(radius);
        }
    }

    /**
     * Sets the background color of the inner card view and layout.
     *
     * @param color The color to set as the background color
     */
    public void setInnerBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        if (color != 0) {
            innerCardView.setCardBackgroundColor(color);
            innerLayout.setBackgroundColor(color);
        }
    }

    /**
     * Sets the inner background gradient of the avatar view.
     *
     * @param colorArray  an array of colors to be used for the gradient
     * @param orientation the orientation of the gradient
     */
    public void setInnerBackgroundGradient(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (innerCardView != null) {
            GradientDrawable gd = new GradientDrawable(orientation, colorArray);
            gd.setCornerRadius(innerCardView.getRadius());
            innerCardView.setBackgroundDrawable(gd);
        }
    }

    /**
     * Sets the inner background drawable of the CometChatAvatar.
     *
     * @param drawable The drawable to be set as the inner background of the avatar.
     */
    public void setInnerBackgroundDrawable(Drawable drawable) {
        if (innerCardView != null && drawable != null) {
            innerCardView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Sets the background color of the outer card view
     *
     * @param color The color to be set as the background color
     */
    public void setOuterBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        if (color != 0) {
            this.setCardBackgroundColor(backgroundColor);
        }
    }

    /**
     * Sets the outer card view background to a gradient drawable with the provided colors and orientation.
     *
     * @param colorArray  The array of colors for the gradient
     * @param orientation The orientation of the gradient
     */
    public void setOuterBackgroundGradient(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (innerCardView != null) {
            GradientDrawable gd = new GradientDrawable(orientation, colorArray);
            gd.setCornerRadius(innerCardView.getRadius());
            this.setBackgroundDrawable(gd);
        }
    }

    /**
     * Sets the drawable resource as the background of the outer card view.
     *
     * @param drawable The drawable resource to be set as the background of the outer card view.
     */
    public void setOuterBackgroundDrawable(Drawable drawable) {
        if (innerCardView != null && drawable != null) {
            this.setBackgroundDrawable(drawable);
        }
    }

    /**
     * Sets the background color of the outer card view.
     *
     * @param color The color to set as background
     */
    public void setOuterViewColor(@ColorInt int color) {
        if (color != 0) this.setCardBackgroundColor(color);
    }

    /**
     * Sets the spacing/padding around the outer view of the avatar.
     *
     * @param padding The padding value in pixels to be set. Must be non-negative.
     */
    public void setOuterViewSpacing(int padding) {
        if (padding >= 0) this.setContentPadding(padding, padding, padding, padding);
    }

    /**
     * Sets the width of the border of the outer card view.
     *
     * @param borderWidth the width of the border in pixels
     */
    public void setOuterViewWidth(int borderWidth) {
        if (borderWidth >= 0) this.setStrokeWidth(borderWidth);
    }

    /**
     * Sets the border color for the outer card view.
     *
     * @param color the color value to set for the border
     */
    public void setOuterViewBorderColor(@ColorInt int color) {
        if (color != 0) this.setStrokeColor(color);
    }

    /**
     * Sets the style of the avatar with the specified {@link AvatarStyle}.
     *
     * @param style the style to apply to the avatar
     */
    public void setStyle(AvatarStyle style) {
        if (style != null) {
            setTextFont(style.getTextFont());
            setTextColor(style.getTextColor());
            setTextSize(style.getTextSize());
            setTextAppearance(style.getTextAppearance());

            setBorderWidth(style.getInnerViewWidth());
            setBorderColor(style.getInnerViewBorderColor());
            setCornerRadius(style.getInnerViewRadius());
            setInnerBackgroundColor(style.getInnerBackgroundColor());
            setInnerBackgroundDrawable(style.getInnerBackgroundDrawable());

            setOuterViewSpacing(style.getOuterViewSpacing());
            setOuterViewBorderColor(style.getBorderColor());
            setOuterViewWidth(style.getBorderWidth());
            setOuterBackgroundColor(style.getBackground());
            setRadius(style.getCornerRadius());
            setOuterBackgroundDrawable(style.getDrawableBackground());
        }
    }
}
