package com.cometchatworkspace.components.shared.secondaryComponents.cometchatBadgeCount;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.cometchatworkspace.R;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

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

@BindingMethods(value = {@BindingMethod(type = CometChatBadgeCount.class, attribute = "app:count", method = "setCount")})
public class CometChatBadgeCount extends MaterialCardView {

    private TextView tvCount;   //Used to display count

    private MaterialCardView countView;

    private int count;      //Used to store value of count

    private float countSize;    //Used to store size of count

    private int countColor;     //Used to store color of count

    private int countBackgroundColor;      //Used to store background color of count.

    private FontUtils fontUtils;

    public CometChatBadgeCount(Context context) {
        super(context);
        initViewComponent(context, null, -1);
    }

    public CometChatBadgeCount(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs, -1);
    }

    public CometChatBadgeCount(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        fontUtils = FontUtils.getInstance(context);
        View view = View.inflate(context, R.layout.cometchat_badge_count, null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.BadgeCount,
                0, 0);
        count = a.getInt(R.styleable.BadgeCount_count, 0);
        countSize = a.getDimension(R.styleable.BadgeCount_count_size, 12);
        countColor = a.getColor(R.styleable.BadgeCount_count_color,0);
        countBackgroundColor=a.getColor(R.styleable.BadgeCount_count_background_color,0);


        addView(view);

          if (count==0){
              setVisibility(INVISIBLE);
          }else {
              setVisibility(VISIBLE);
          }

          countView = view.findViewById(R.id.count_parent);
          tvCount = view.findViewById(R.id.tvSetCount);
          tvCount.setTextSize(countSize);
          tvCount.setTextColor(countColor);
          tvCount.setText(String.valueOf(count));
          setBackground(countBackgroundColor);
    }

    /**
     * This method is used to set background color of count.
     * @param color is an object of Color.class . It is used as color for background.
     */
    public void setBackground(@ColorInt int color) {
        countView.setCardBackgroundColor(color);
        setCardBackgroundColor(color);
    }


    public void cornerRadius(float radius) {
        countView.setRadius(radius);
        setRadius(radius);
    }
    /**
     * This method is used to set color of count i.e integer.
     * @param color is an object of Color.class. It is used as color of text in tvCount (TextView)
     */
    public void setTextColor(@ColorInt int color) {
        tvCount.setTextColor(color);
    }

    public void setTextColor(ColorStateList color) {
        tvCount.setTextColor(color);
    }
    /**
     * This method is used to set size of text present in tvCount(TextView) i.e count size;
     * @param size
     */
    public void setTextSize(float size) {
        tvCount.setTextSize(size);

    }

    public void borderColor(@ColorInt int color) {
        setStrokeColor(color);
    }

    public void borderWidth(int width) {
        setStrokeWidth(width);
    }

    public void setTextFont(String font) {
        if (tvCount!=null)
            tvCount.setTypeface(fontUtils.getTypeFace(font));
    }

    /**
     * This method is used to set count in tvCount(TextView). If count is 0 then tvCount is invisible
     * If count is more than 1, then it will display the value. The limit of the count shown will be 999.
     *
     * @param count is an Integer which is set in tvCount(TextView).
     */
    public void setCount(int count) {
        this.count = count;
        if (count == 0)
            setVisibility(GONE);
        else
            setVisibility(View.VISIBLE);
        if (count<999)
            tvCount.setText(String.valueOf(count));
        else
            tvCount.setText("999+");
    }

    public int getCount() {
        if (tvCount!=null)
            return Integer.parseInt(tvCount.getText().toString());
        else
            return 0;
    }

}
