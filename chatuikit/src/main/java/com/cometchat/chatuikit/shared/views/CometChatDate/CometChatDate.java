package com.cometchat.chatuikit.shared.views.CometChatDate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.Function1;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * CometChatDate is a custom view that displays a date. It extends LinearLayout.
 * <p>
 * It provides various methods to customize the appearance and behavior of the date view.
 *
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 22nd December 2022
 */

public class CometChatDate extends LinearLayout {

    private MaterialCardView layoutDate;   //Used to display date

    private TextView textDate;

    private String text;    //Used to store value of date

    private String format; //Used to store value of date

    private float size;    //Used to store size of date

    private int color;     //Used to store color of date

    private int backgroundColor;      //Used to store background color of count.

    private FontUtils fontUtils;

    private Function1<Long, String> customPattern;

    Context context;

    private long timestamp;

    private Pattern pattern;

    private long ONE_DAY = 24 * 60 * 60 * 1000;
    private long TWO_DAY = 48 * 60 * 60 * 1000;
    private long ONE_WEEK = 7 * 24 * 60 * 60 * 1000;

    /**
     * Constructs a new CometChatDate object.
     *
     * @param context the context in which the CometChatDate is created
     */
    public CometChatDate(Context context) {
        super(context);
        this.context = context;
        initViewComponent(context, null, -1);
    }

    /**
     * Constructs a new CometChatDate object with the specified attributes.
     *
     * @param context the context in which the CometChatDate is created
     * @param attrs   the attributes of the XML tag that is inflating the view
     */
    public CometChatDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViewComponent(context, attrs, -1);
    }

    /**
     * Constructs a new CometChatDate object with the specified attributes and style.
     *
     * @param context      the context in which the CometChatDate is created
     * @param attrs        the attributes of the XML tag that is inflating the view
     * @param defStyleAttr an attribute in the current theme that contains a reference to a style resource
     */
    public CometChatDate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * Initializes the view components and sets their values if available.
     *
     * @param context      the context in which the view components are initialized
     * @param attributeSet the attributes of the XML tag that is inflating the view
     * @param defStyleAttr an attribute in the current theme that contains a reference to a style resource
     */
    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {
        View view = View.inflate(context, R.layout.cometchat_date, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CometChatDate, 0, 0);
        text = a.getString(R.styleable.CometChatDate_text);
        size = a.getInteger(R.styleable.CometChatDate_text_size, 12);
        color = a.getColor(R.styleable.CometChatDate_text_color, 0);
        format = a.getString(R.styleable.CometChatDate_date_format);
        addView(view);
        fontUtils = FontUtils.getInstance(context);
        layoutDate = view.findViewById(R.id.date_layout);
        textDate = view.findViewById(R.id.date_text);
        textDate.setTextSize(size);
        textDate.setText(text);
        setTransparentBackground(true);
        setBackground(backgroundColor);
        setTextColor(color);
    }

    public void setTransparentBackground(boolean isTrue) {
        if (isTrue) {
            layoutDate.setStrokeColor(Color.TRANSPARENT);
            layoutDate.setStrokeWidth(0);
            layoutDate.setCardElevation(0f);
            layoutDate.setPadding(0, 0, 0, 0);
        } else {
            layoutDate.setPadding(6, 6, 6, 6);
        }
    }

    /**
     * Sets the background color of the count.
     *
     * @param color the background color to set
     */
    public void setBackground(@ColorInt int color) {
        layoutDate.setCardBackgroundColor(color);
    }

    /**
     * Sets the background drawable for the count.
     *
     * @param drawable the drawable to set as the background
     */
    public void setBackground(Drawable drawable) {
        layoutDate.setBackgroundDrawable(drawable);
    }


    /**
     * Sets the text color of the date.
     *
     * @param color the text color to set
     */
    public void setTextColor(@ColorInt int color) {
        if (color != 0 && textDate != null) textDate.setTextColor(color);
    }

    /**
     * Sets the text size of the date.
     *
     * @param size the text size to set
     */
    public void setTextSize(int size) {
        if (size > 0) textDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Sets the text appearance of the date.
     *
     * @param appearance the style resource for text appearance
     */
    public void setTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) textDate.setTextAppearance(context, appearance);
    }

    /**
     * Sets the corner radius of the count's background.
     *
     * @param radius the corner radius to set
     */
    public void setCornerRadius(float radius) {
        if (layoutDate != null) layoutDate.setRadius(radius);
    }

    /**
     * Sets the border color of the count's background.
     *
     * @param color the border color to set
     */
    public void setBorderColor(@ColorInt int color) {
        if (layoutDate != null && color != 0) {
            layoutDate.setStrokeColor(color);
        }
    }

    /**
     * Sets the border width of the count's background.
     *
     * @param width the border width to set
     */
    public void setBorderWidth(int width) {
        if (layoutDate != null) layoutDate.setStrokeWidth(width);
    }

    /**
     * Sets the text for the date.
     *
     * @param text the text to set
     */
    public void text(String text) {
        textDate.setText(text);
    }

    /**
     * Sets the date using the specified timestamp and format.
     *
     * @param timestamp the timestamp of the date
     * @param format    the format of the date
     */
    public void setDate(long timestamp, String format) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp * 1000L);
        String date = DateFormat.format(format, cal).toString();
        textDate.setText(date);
    }

    /**
     * Sets the date using the specified timestamp and pattern.
     *
     * @param timestamp the timestamp of the date
     * @param pattern   the pattern for displaying the date
     */
    public void setDate(long timestamp, Pattern pattern) {
        textDate.setVisibility(VISIBLE);
        if (getCustomPattern(timestamp) != null) {
            textDate.setText(getCustomPattern(timestamp));
        } else {
            if (timestamp != 0 && pattern != null) {
                if (Pattern.TIME.equals(pattern)) {
                    textDate.setText(getTime(timestamp));
                } else if (Pattern.DAY_DATE.equals(pattern)) {
                    textDate.setText(getDayDate(timestamp));
                } else if (Pattern.DAY_DATE_TIME.equals(pattern)) {
                    textDate.setText(getDayDateTime(timestamp));
                }
            }
        }
    }

    /**
     * Sets the timestamp of the date.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        if (timestamp != 0) {
            this.timestamp = timestamp;
            setDate(timestamp, pattern);
        }
    }

    /**
     * Sets the pattern for displaying the date.
     *
     * @param pattern the pattern to set
     * @see Pattern
     */
    public void setPattern(Pattern pattern) {
        if (pattern != null) {
            this.pattern = pattern;
            setDate(timestamp, pattern);
        }
    }

    private String getTime(long timestamp) {
        return new SimpleDateFormat("h:mm a").format(new java.util.Date(timestamp * 1000)) + "";
    }

    private String getDayDate(long timestamp) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance(Locale.ENGLISH);
        timeToCheck.setTimeInMillis(timestamp * 1000L);
        if (now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.today) + "";
        } else if ((now.get(Calendar.DAY_OF_YEAR) - 1) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.yesterday) + "";
        } else return DateFormat.format("dd MMM yyyy", timeToCheck).toString() + "";
    }

    private String getDayDateTime(long timestamp) {
        String lastMessageDate = new SimpleDateFormat("dd MMM yyyy").format(new java.util.Date(timestamp * 1000));
        String lastMessageWeek = new SimpleDateFormat("EEE").format(new java.util.Date(timestamp * 1000));
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance(Locale.ENGLISH);
        timeToCheck.setTimeInMillis(timestamp * 1000L);
        if (now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            return getTime(timestamp);
        } else if ((now.get(Calendar.DAY_OF_YEAR) - 1) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.yesterday) + "";
        } else if (((now.get(Calendar.DAY_OF_YEAR) - 7) <= timeToCheck.get(Calendar.DAY_OF_YEAR))) {
            return lastMessageWeek;
        } else return lastMessageDate;
    }

    /**
     * Sets the font for the text of the date.
     *
     * @param font the font to set
     */
    public void setTextFont(String font) {
        if (font != null) textDate.setTypeface(fontUtils.getTypeFace(font));
    }

    private void setCustomPattern(Function1<Long, String> customPattern) {
        if (customPattern != null) {
            this.customPattern = customPattern;
            setDate(timestamp, pattern);
        }

    }

    /**
     * Sets a custom string for the date.
     *
     * @param string the custom string to set
     */
    public void setCustomDateString(String string) {
        if (string != null && !string.isEmpty()) textDate.setText(string);
    }

    private String getCustomPattern(long timeStamp) {
        if (customPattern != null) return customPattern.apply(timeStamp);
        return null;
    }

    /**
     * Applies the specified style to the date view.
     *
     * @param dateStyle the style to apply
     */
    public void setStyle(DateStyle dateStyle) {
        if (dateStyle != null) {
            setTextAppearance(dateStyle.getTextAppearance());
            setTextColor(dateStyle.getTextColor());
            setTextFont(dateStyle.getTextFont());
            setTextSize(dateStyle.getTextSize());
            setBackground(dateStyle.getBackground());
            setBorderWidth(dateStyle.getBorderWidth());
            setBorderColor(dateStyle.getBorderColor());
            setCornerRadius(dateStyle.getCornerRadius());
            setBackground(dateStyle.getDrawableBackground());
        }
    }

}
