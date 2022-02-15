package com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;

import com.cometchat.pro.models.BaseMessage;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.common.extensions.Extensions;
import com.cometchatworkspace.components.messages.common.extensions.Reactions.CometChatReactionInfoActivity;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatReaction.model.Reaction;
import com.cometchatworkspace.resources.constants.UIKitConstants;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;

public class CometChatMessageReaction extends RelativeLayout {

    private Context context;
    private View view;

    private int activeColor;
    private int passiveColor;
    private int borderColor;
    private int backgroundColor;
    private int textColor;
    private int borderWidth;
    private float borderRadius;
    private int reactionGravity;

    private ChipGroup reactionLayout;

    private OnReactionClickListener eventListener;

    public CometChatMessageReaction(Context context) {
        super(context);
        initComponent(context,null);
    }

    public CometChatMessageReaction(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent(context, attrs);
    }

    public CometChatMessageReaction(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent(context, attrs);
    }

   private void initComponent(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
               attrs,
               R.styleable.CometChatMessageReaction,
               0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            passiveColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_passiveColor,
                   getContext().getColor(android.R.color.transparent));
            activeColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_activeColor,
                    getContext().getColor(R.color.colorPrimary));
            borderColor = a.getColor(R.styleable.CometChatMessageReaction_borderColor,
                    getContext().getColor(R.color.grey));
        }
        borderRadius  = a.getFloat(R.styleable.CometChatMessageReaction_borderRadius,
                12);
        borderWidth = a.getInt(R.styleable.CometChatMessageReaction_borderWidth,
                1);
        textColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_textColor,
                0);
        backgroundColor = a.getColor(R.styleable.CometChatMessageReaction_messageReaction_backgroundColor,0);
        reactionGravity = a.getInt(R.styleable.CometChatMessageReaction_messageReaction_gravity, Gravity.END);
        view = LayoutInflater.from(context).inflate(R.layout.cometchat_message_reactions,null);
        reactionLayout = view.findViewById(R.id.reactions_layout);
        reactionLayout.setForegroundGravity(Gravity.END);
        addView(view);
   }


    public void setMessage(BaseMessage baseMessage) {
        reactionLayout.setVisibility(View.GONE);
        HashMap<String,String> reactionOnMessage = Extensions.getReactionsOnMessage(baseMessage);
        if (reactionOnMessage!=null && reactionOnMessage.size()>0) {
            reactionLayout.setVisibility(View.VISIBLE);
            reactionLayout.removeAllViews();
            for (String str : reactionOnMessage.keySet()) {
//                if (reactionLayout.getChildCount()<reactionOnMessage.size()) {

                Chip chip = new Chip(context);

                chip.setShapeAppearanceModel(
                        chip.getShapeAppearanceModel().toBuilder().setAllCornerSizes(borderRadius).build());
                chip.setChipStrokeWidth(borderWidth);
                chip.setChipBackgroundColor(ColorStateList.valueOf(passiveColor));
                chip.setChipStrokeColor(ColorStateList.valueOf(borderColor));
                chip.setText(str + " " + reactionOnMessage.get(str));
                if (Extensions.isReactedByLoggedInUser(baseMessage.getMetadata(),str)) {
                    chip.setChipBackgroundColor(
                            ColorStateList.valueOf(Utils.adjustAlpha(activeColor,0.4f)));
                }

                reactionLayout.addView(chip);
                chip.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent intent = new Intent(context, CometChatReactionInfoActivity.class);
                        intent.putExtra(UIKitConstants.IntentStrings.REACTION_INFO,baseMessage.getMetadata().toString());
                        context.startActivity(intent);
                        return true;
                    }
                });
                chip.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Reaction reaction = new Reaction(str, Integer.parseInt(reactionOnMessage.get(str)));
                        eventListener.onReactionClick(reaction,baseMessage.getId());
                    }
                });
//                }
            }
        }
    }
    public void setPassiveColor(@ColorInt int color) {
        this.passiveColor = color;
        invalidate();
    }
    public void setActiveColor(@ColorInt int color) {
        this.activeColor = color;
        invalidate();
    }
    public void setBorderColor(@ColorInt int color) {
        this.borderColor = color;
        invalidate();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        invalidate();
    }

    public void setBorderRadius(float radius) {
        this.borderRadius = radius;
        invalidate();
    }

    public void setReactionEventListener(OnReactionClickListener clickListener) {
        this.eventListener = clickListener;
    }

    public interface OnReactionClickListener {
        void onReactionClick(Reaction reaction,int baseMessageID);
    }
}
