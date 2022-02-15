package com.cometchatworkspace.components.shared.secondaryComponents.cometchatDate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.cometchat.pro.models.BaseMessage;
import com.cometchatworkspace.R;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Purpose - This class is a subclass of LinearLayout, It is a component which is been used by developer
 * to display the Badgecount for unread message count or unread conversations. It can also
 * be used for other purpose. This class contains various methods which
 * are used to update the count, set count background color, set count color, set count size
 *
 * Created on - 20th December 2019
 *
 * Modified on  - 16th January 2020
 *
*/

@BindingMethods(value = {@BindingMethod(type = CometChatDate.class, attribute = "app:date", method = "setDate")})
public class CometChatDate extends RelativeLayout {

    private MaterialCardView layoutDate;   //Used to display date

    private TextView textDate;

    private String text;    //Used to store value of date

    private String format; //Used to store value of date

    private float size;    //Used to store size of date

    private int color;     //Used to store color of date

    private int backgroundColor;      //Used to store background color of count.

    private FontUtils fontUtils;

    Context context;
    public CometChatDate(Context context) {
        super(context);
        this.context = context;
        initViewComponent(context, null, -1);
    }

    public CometChatDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViewComponent(context, attrs, -1);
    }

    public CometChatDate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViewComponent(context, attrs, defStyleAttr);
    }

    /**
     * This method is used to initialize the view present in component and set the value if it is
     * available.
     *
     * @param context
     * @param attributeSet
     * @param defStyleAttr
     */
    private void initViewComponent(Context context, AttributeSet attributeSet, int defStyleAttr) {
        View view = View.inflate(context, R.layout.cometchat_date, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CometChatDate,
                0, 0);
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
        setBackground(backgroundColor);
        textColor(color);
    }

    public void setTransparentBackground(boolean isTrue) {
        if (isTrue) {
            layoutDate.setStrokeColor(Color.TRANSPARENT);
            layoutDate.setStrokeWidth(0);
            layoutDate.setCardElevation(0f);
            layoutDate.setPadding(0,0,0,0);
        } else {
            layoutDate.setPadding(6,6,6,6);
        }
    }
    /**
     * This method is used to set background color of count.
     * @param color is an object of Color.class . It is used as color for background.
     */
    public void setBackground(@ColorInt int color) {
        layoutDate.setCardBackgroundColor(color);
    }


    /**
     * This method is used to set color of count i.e integer.
     * @param color is an object of Color.class. It is used as color of text in tvCount (TextView)
     */
    public void textColor(@ColorInt int color) {
        if (color!=0 && textDate!=null)
            textDate.setTextColor(color);
    }

    /**
     * This method is used to set size of text present in tvCount(TextView) i.e count size;
     * @param size
     */
    public void textSize(float size) {
        textDate.setTextSize(size);

    }

    public void cornerRadius(int radius) {
        if (layoutDate!=null)
            layoutDate.setRadius(radius);
    }

    public void borderColor(@ColorInt int color) {
        if (layoutDate!=null && color!=0) {
            layoutDate.setStrokeColor(color);
        }
    }

    public void borderWidth(int width) {
        if (layoutDate!=null)
            layoutDate.setStrokeWidth(width);
    }

    public void text(String text) {
        textDate.setText(text);
    }


    /**
     * This method is used to show message time below message.
     *
     * @see BaseMessage
     */

    public void setDate(long timestamp, String format) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp*1000L);
        String date = DateFormat.format(format, cal).toString();
        textDate.setText(date);
    }

    public void setHeaderDate(long timestamp,String format) {
        textDate.setVisibility(View.VISIBLE);
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance(Locale.ENGLISH);
        timeToCheck.setTimeInMillis(timestamp*1000L);
        long currentTimeStamp = System.currentTimeMillis();

        long diffTimeStamp = currentTimeStamp - timestamp * 1000;
        if (now.get(Calendar.DAY_OF_YEAR)==timeToCheck.get(Calendar.DAY_OF_YEAR)) {
            textDate.setText(context.getString(R.string.today));

        } else if ((now.get(Calendar.DAY_OF_YEAR)-1)==timeToCheck.get(Calendar.DAY_OF_YEAR)) {

            textDate.setText(context.getString(R.string.yesterday));
        } else
            textDate.setText(DateFormat.format(format, timeToCheck).toString());
    }

    public void setDate(long timestamp) {
        String lastMessageTime = new SimpleDateFormat("h:mm a").format(new java.util.Date(timestamp * 1000));
        String lastMessageDate = new SimpleDateFormat("dd MMM yyyy").format(new java.util.Date(timestamp * 1000));
        String lastMessageWeek = new SimpleDateFormat("EEE").format(new java.util.Date(timestamp * 1000));

        long currentTimeStamp = System.currentTimeMillis();

        long diffTimeStamp = currentTimeStamp - timestamp * 1000;

        if (diffTimeStamp < 24 * 60 * 60 * 1000) {
            textDate.setText(lastMessageTime);

        } else if (diffTimeStamp < 48 * 60 * 60 * 1000) {
            textDate.setText(context.getString(R.string.yesterday));
        } else if (diffTimeStamp < 7 * 24 * 60 * 60 * 1000) {
             textDate.setText(lastMessageWeek);
        } else {
            textDate.setText(lastMessageDate);
        }
    }

    public void textFont(String font) {
        if (textDate!=null)
            textDate.setTypeface(fontUtils.getTypeFace(font));
    }

}
