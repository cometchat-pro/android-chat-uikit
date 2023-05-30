package com.cometchat.chatuikit.shared.views.CometChatMessageInput;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

public class CometChatMessageInput extends MaterialCardView {
    private View view;
    private Context context;
    private TextChangedListener textWatcher;
    private CometChatEditText composeBox;
    private TextView separator;
    private LinearLayout parent, bottomContainer, secondaryButtonView, primaryButtonView;
    private RelativeLayout auxiliaryButtonView;

    /**
     * Constructs a new instance of CometChatMessageInput.
     *
     * @param context The context in which the message input is created.
     */
    public CometChatMessageInput(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructs a new instance of CometChatMessageInput.
     *
     * @param context The context in which the message input is created.
     * @param attrs   The attribute set for the message input.
     */
    public CometChatMessageInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructs a new instance of CometChatMessageInput.
     *
     * @param context      The context in which the message input is created.
     * @param attrs        The attribute set for the message input.
     * @param defStyleAttr The default style attribute for the message input.
     */
    public CometChatMessageInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the message input by setting up its layout and obtaining references to its child views.
     *
     * @param context The context in which the message input is created.
     */
    private void init(Context context) {
        setCardBackgroundColor(Color.TRANSPARENT);
        setCardElevation(0);
        setRadius(0);
        this.context = context;
        view = View.inflate(context, R.layout.message_input_layout, null);
        composeBox = view.findViewById(R.id.compose_box);
        separator = view.findViewById(R.id.divider);
        bottomContainer = view.findViewById(R.id.bottom_container);
        parent = view.findViewById(R.id.parent);
        secondaryButtonView = view.findViewById(R.id.secondary_view);
        auxiliaryButtonView = view.findViewById(R.id.auxiliary_view);
        primaryButtonView = view.findViewById(R.id.primary_view);

        composeBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (textWatcher != null) textWatcher.beforeTextChanged(charSequence, i, i1, i2);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (textWatcher != null) textWatcher.onTextChanged(charSequence, i, i1, i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (textWatcher != null) textWatcher.afterTextChanged(editable);
            }
        });
        addView(view);
    }

    /**
     * Sets the color of the separator line between the compose box and auxiliary buttons.
     *
     * @param color The color to set for the separator line.
     */
    public void setSeparatorColor(@ColorInt int color) {
        if (color != 0) separator.setBackgroundColor(color);
    }

    /**
     * Applies the specified style to the message input.
     *
     * @param style The style to apply to the message input.
     */
    public void setStyle(MessageInputStyle style) {
        if (style != null) {
            if (style.getDrawableBackground() != null)
                this.setBackground(style.getDrawableBackground());
            else if (style.getBackground() != 0) this.setCardBackgroundColor(style.getBackground());
            if (style.getBorderWidth() >= 0) this.setStrokeWidth(style.getBorderWidth());
            if (style.getCornerRadius() >= 0) this.setRadius(style.getCornerRadius());
            if (style.getBorderColor() != 0) this.setStrokeColor(style.getBorderColor());

            setInputBackgroundColor(style.getInputBackground());
            setSeparatorColor(style.getSeparatorColor());
            setTextColor(style.getTextColor());
            setTextFont(style.getTextFont());
            setTextAppearance(style.getInputTextAppearance());
            setPlaceHolderTextColor(style.getPlaceHolderColor());
        }
    }

    /**
     * Sets the alignment of the auxiliary button view within the message input.
     *
     * @param alignment The alignment to set for the auxiliary button view.
     */
    public void setAuxiliaryButtonAlignment(UIKitConstants.AuxiliaryButtonAlignment alignment) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) auxiliaryButtonView.getLayoutParams();
        if (UIKitConstants.AuxiliaryButtonAlignment.LEFT.equals(alignment))
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        else if (UIKitConstants.AuxiliaryButtonAlignment.RIGHT.equals(alignment))
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        auxiliaryButtonView.setLayoutParams(params);
    }

    /**
     * Sets the maximum number of lines for the compose box.
     *
     * @param value The maximum number of lines to set.
     */
    public void setMaxLine(int value) {
        if (value > 0) composeBox.setMaxLines(value);
    }

    /**
     * Sets the text of the compose box.
     *
     * @param text The text to set.
     */
    public void setText(String text) {
        if (text != null) composeBox.setText(text + "");
    }

    /**
     * Sets the placeholder text for the compose box.
     *
     * @param placeHolderText The placeholder text to set.
     */
    public void setPlaceHolderText(String placeHolderText) {
        if (placeHolderText != null) composeBox.setHint(placeHolderText);
    }

    /**
     * Sets the background color of the input.
     *
     * @param color The background color to set.
     */
    public void setInputBackgroundColor(@ColorInt int color) {
        if (color != 0) this.setCardBackgroundColor(color);
    }

    /**
     * Sets the text appearance for the compose box.
     *
     * @param appearance The text appearance to set.
     */
    public void setTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) composeBox.setTextAppearance(context, appearance);
    }

    /**
     * Sets the text color of the compose box.
     *
     * @param color The text color to set.
     */
    public void setTextColor(@ColorInt int color) {
        if (color != 0) composeBox.setTextColor(color);
    }

    /**
     * Sets the font for the compose box text.
     *
     * @param font The font to set.
     */
    public void setTextFont(String font) {
        if (font != null && !font.isEmpty())
            composeBox.setTypeface(FontUtils.getInstance(context).getTypeFace(font));
    }

    /**
     * Sets the placeholder text color for the compose box.
     *
     * @param color The placeholder text color to set.
     */
    public void setPlaceHolderTextColor(@ColorInt int color) {
        if (color != 0) composeBox.setHintTextColor(color);
    }

    /**
     * Sets the secondary button view for the message input.
     *
     * @param view The secondary button view to set.
     */
    public void setSecondaryButtonView(View view) {
        if (view != null) {
            Utils.handleView(secondaryButtonView, view, false);
        }
    }

    /**
     * Sets the primary button view for the message input.
     *
     * @param view The primary button view to set.
     */
    public void setPrimaryButtonView(View view) {
        if (view != null) {
            Utils.handleView(primaryButtonView, view, false);
        }
    }

    /**
     * Sets the auxiliary button view for the message input.
     *
     * @param view The auxiliary button view to set.
     */
    public void setAuxiliaryButtonView(View view) {
        Utils.handleView(auxiliaryButtonView, view, false);
    }

    /**
     * Sets the listener for text changes in the compose box.
     *
     * @param textWatcher The listener to set.
     */
    public void setOnTextChangedListener(TextChangedListener textWatcher) {
        this.textWatcher = textWatcher;
    }

    /**
     * Returns the bottom container of the message input.
     *
     * @return The bottom container view.
     */
    public LinearLayout getBottomContainer() {
        return bottomContainer;
    }

    /**
     * Returns the secondary button view of the message input.
     *
     * @return The secondary button view.
     */
    public LinearLayout getSecondaryButtonView() {
        return secondaryButtonView;
    }

    /**
     * Returns the auxiliary button view of the message input.
     *
     * @return The auxiliary button view.
     */
    public RelativeLayout getAuxiliaryButtonView() {
        return auxiliaryButtonView;
    }

    /**
     * Returns the primary button view of the message input.
     *
     * @return The primary button view.
     */
    public LinearLayout getPrimaryButtonView() {
        return primaryButtonView;
    }

    /**
     * Returns the parent view of the message input.
     *
     * @return The parent view.
     */
    public LinearLayout getView() {
        return parent;
    }

    /**
     * The listener interface for text changes in the compose box.
     */
    public interface TextChangedListener {
        /**
         * Called when the text in the compose box is changed.
         *
         * @param charSequence The changed text.
         * @param start        The start position of the changed text.
         * @param before       The length of the text before the change.
         * @param count        The length of the changed text.
         */
        void onTextChanged(CharSequence charSequence, int start, int before, int count);

        /**
         * Called before the text in the compose box is changed.
         *
         * @param s     The current text in the compose box.
         * @param start The start position of the changed text.
         * @param count The length of the changed text.
         * @param after The length of the text after the change.
         */
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        /**
         * Called after the text in the compose box is changed.
         *
         * @param editable The editable text after the change.
         */
        void afterTextChanged(Editable editable);
    }

}
