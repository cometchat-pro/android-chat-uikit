package com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.cometchat.pro.models.AppEntity;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import com.cometchatworkspace.R;
import com.google.android.material.card.MaterialCardView;

/**
 * Purpose - This class is a subclass of AppCompatImageView, It is a component which is been used by developer
 * to display the avatar of their user or icon of the group. This class contains various methods which
 * are used to change shape of image as circle or rectangle, border with of image, border color of image.
 * and set default drawable if there is no image.
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 20th January 2020
 */

public class CometChatAvatar extends MaterialCardView {

    private static final String TAG = CometChatAvatar.class.getSimpleName();

    /*
     * Place holder drawable (with background color and initials)
     * */
    Drawable drawable;

    /*
     * Contains initials of the member
     * */
    String text;

    /*
     * User whose avatar should be displayed
     * */
    //User user;
    String avatarUrl;


    /*
     * Bounds of the canvas in float
     * Used to set bounds of member initial and background
     * */
    RectF rectF;


    private Context context;

    private int color;

    private int borderColor;

    private int backgroundColor;

    private float borderWidth;

    private float radius;

    private MaterialCardView outerCardView, innerCardView;
    private ImageView imageView;
    private TextView textView;

    public CometChatAvatar(Context context) {
        super(context);
        if (!isInEditMode())
            this.context = context;
    }

    public CometChatAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (!isInEditMode())
            getAttributes(attrs);
    }

    public CometChatAvatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (!isInEditMode())
            getAttributes(attrs);
    }

    private void getAttributes(AttributeSet attrs) {
        View view = View.inflate(context, R.layout.cometchat_avatar, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Avatar,
                0, 0);
        /*
         * Get the shape and set shape field accordingly
         * */
        drawable = a.getDrawable(R.styleable.Avatar_image);
        radius = a.getDimension(R.styleable.Avatar_corner_radius, 18);
        backgroundColor = a.getColor(R.styleable.Avatar_background_color, 0);
        avatarUrl = a.getString(R.styleable.Avatar_avatar);
//            borderRadius = a.getInteger(R.styleable.Avatar_cornerRadius,8);
        borderColor = a.getColor(R.styleable.Avatar_borderColor, 0);
        //          backgroundColor = a.getColor(R.styleable.Avatar_backgroundColor,getResources().getColor(R.color.colorPrimary));
        borderWidth = a.getDimension(R.styleable.Avatar_borderWidth, 0);


        addView(view);

        outerCardView = view.findViewById(R.id.outerView);
        innerCardView = view.findViewById(R.id.cardView);
        setRadius(radius);
        if (color == 0) {
            setBorderColor(getResources().getColor(R.color.primary));
        } else {
            setBorderColor(color);
        }
        innerCardView.setCardBackgroundColor(backgroundColor);
        imageView = view.findViewById(R.id.image);
        textView = view.findViewById(R.id.text);
        if (drawable != null)
            imageView.setImageDrawable(drawable);
    }

    public void setAvatar(@NonNull User user) {

        if (user != null) {
            if (user.getAvatar() != null) {
                avatarUrl = user.getAvatar();
                if (isValidContextForGlide(context)) {
                    setValues();
                }
            } else {
                if (user.getName() != null && !user.getName().isEmpty()) {
                    if (user.getName().length() > 2) {
                        text = user.getName().substring(0, 2);
                    } else {
                        text = user.getName();
                    }
                } else {
                    text = "??";
                }
                imageView.setVisibility(View.GONE);
                setInitials(text.toUpperCase());
            }
        }

    }

    /**
     * This method is used to check if the group parameter passed is null or not. If it is not null then
     * it will show icon of group, else it will show default drawable or first two letter of group name.
     *
     * @param group is an object of Group.class.
     * @see Group
     */
    public void setAvatar(@NonNull Group group) {

        if (group != null) {

            if (group.getIcon() != null) {
                avatarUrl = group.getIcon();
                if (isValidContextForGlide(context))
                    setValues();
            } else {
                if (group.getName().length() > 2)
                    text = group.getName().substring(0, 2);
                else {
                    text = group.getName();
                }
                imageView.setVisibility(View.GONE);
                textView.setText(text);
            }
        }
    }

    private void setAvatar(AppEntity appEntity) {
        if (appEntity instanceof User) {
            setAvatar((User) appEntity);
        } else if (appEntity instanceof Group) {
            setAvatar((Group) appEntity);
        }
    }

    /**
     * This method is used to set image by using url passed in parameter..
     *
     * @param avatarUrl is an object of String.class which is used to set avatar.
     */
    public void setAvatar(@NonNull String avatarUrl) {

        this.avatarUrl = avatarUrl;
        if (isValidContextForGlide(context))
            setValues();

    }

    /**
     * @param drawable  placeholder image
     * @param avatarUrl url of the image
     */
    public void setAvatar(Drawable drawable, @NonNull String avatarUrl) {
        this.drawable = drawable;
        this.avatarUrl = avatarUrl;
        if (isValidContextForGlide(context)) {
            setValues();
        }
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    /**
     * This method is used to set first two character as image. It is used when user, group or url
     * is null.
     *
     * @param name is a object of String.class. Its first 2 character are used in image with no avatar or icon.
     */
    public void setInitials(@NonNull String name) {

        if (name.length() >= 2) {
            text = name.trim().substring(0, 2);
        } else {
            text = name;
        }
        imageView.setVisibility(View.GONE);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
    }

    public void setTextColor(int color) {
        if (color != 0)
            textView.setTextColor(color);
    }

    public void setTextAppearance(int appearance) {
        if (appearance != 0)
            textView.setTextAppearance(context, appearance);
    }

    public void setAvatar(@NonNull String url, @NonNull String name) {
        setAvatar(url);
        if (url == null)
            setInitials(name);
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    /*
     * Set user specific fields in here
     * */
    private void setValues() {
        try {
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                if (context != null) {
                    Glide.with(context)
                            .load(avatarUrl)
                            .placeholder(drawable)
                            .into(imageView);
                }
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        invalidate();
    }

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
        if (color != 0) {
            this.borderColor = color;
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
        innerCardView.setStrokeWidth(borderWidth);
    }

    public void setCornerRadius(int radius) {
        this.radius = radius;
        innerCardView.setRadius(radius);
        setRadius(radius);
    }

    public void setBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        innerCardView.setCardBackgroundColor(backgroundColor);
    }

    public void setBackgroundColor(int[] colorArray, GradientDrawable.Orientation orientation) {
        if (innerCardView != null) {
            GradientDrawable gd = new GradientDrawable(
                    orientation,
                    colorArray);
            gd.setCornerRadius(innerCardView.getRadius());
            innerCardView.setBackgroundDrawable(gd);
        }
    }

    public void setOuterViewColor(@ColorInt int color) {
        if (color != 0)
            outerCardView.setCardBackgroundColor(color);
    }

    public void setOuterViewSpacing(int padding) {
        outerCardView.setContentPadding(padding, padding, padding, padding);
    }
}
