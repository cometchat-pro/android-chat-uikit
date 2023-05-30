package com.cometchat.chatuikit.shared.views.cometchatActionSheet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ActionSheetAdapter extends RecyclerView.Adapter<ActionSheetAdapter.ActionItemViewHolder> {

    Context context;
    private final List<ActionItem> actionItems;

    private boolean hideText;
    private final boolean gridLayout;
    private View view;
    private ActionSheetStyle actionSheetStyle;

    public ActionSheetAdapter(Context context, List<ActionItem> actionItems, boolean isGridLayout) {
        this.context = context;
        this.actionItems = actionItems;
        this.gridLayout = isGridLayout;
    }

    @NonNull
    @Override
    public ActionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (gridLayout) view = layoutInflater.inflate(R.layout.action_grid_item, parent, false);
        else view = layoutInflater.inflate(R.layout.action_list_item, parent, false);
        return new ActionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionItemViewHolder holder, int position) {
        ActionItem item = actionItems.get(position);
        if (item != null) {
            holder.actionText.setText(item.getText());
            holder.actionIcons.setImageResource(item.getStartIcon());
            if (hideText) holder.actionText.setVisibility(View.GONE);
            else holder.actionText.setVisibility(View.VISIBLE);

            if (item.getAppearance() != 0)
                holder.actionText.setTextAppearance(context, item.getAppearance());

            if (item.getTextColor() != 0)
                holder.actionText.setTextColor(item.getTextColor());

            if (item.getTextFont() != null)
                holder.actionText.setTypeface(FontUtils.getInstance(context).getTypeFace(item.getTextFont()));

            if (item.getStartIconTint() != 0)
                holder.actionIcons.setImageTintList(ColorStateList.valueOf(item.getStartIconTint()));

            if (item.getIconBackgroundColor() != 0)
                holder.iconBackground.setBackgroundColor(item.getIconBackgroundColor());

            if (!gridLayout)
                setLinearItems(holder, position, item);
            else
                setGridItems(holder, item);

        }
        holder.itemView.setTag(R.string.action_item, item);
    }

    private void setGridItems(ActionItemViewHolder holder, ActionItem actionItem) {
        Drawable drawable = context.getDrawable(R.drawable.action_sheet_background);
        @ColorInt int backgroundColor = context.getResources().getColor(R.color.accent50);
        if (actionSheetStyle != null) {
            if (actionSheetStyle.getListItemDrawable() != null) {
                backgroundColor=0;
                drawable = actionSheetStyle.getListItemDrawable();
            }
            if (actionItem.getBackground() != 0) {
                backgroundColor = actionItem.getBackground();
            }
        }
        if(backgroundColor!=0)
            drawable.setTint(backgroundColor);
        holder.layout.setBackground(drawable);

    }

    private void setLinearItems(ActionItemViewHolder holder, int position, ActionItem actionItem) {
        Drawable drawable = null;
        @ColorInt int backgroundColor = context.getResources().getColor(R.color.accent50);
        @ColorInt int separatorColor = context.getResources().getColor(R.color.accent100);
        if (actionSheetStyle != null) {
            if (actionSheetStyle.getListItemDrawable() != null) {
                drawable = actionSheetStyle.getListItemDrawable();
            }
            if (actionItem.getBackground() != 0) {
                backgroundColor = actionItem.getBackground();
            }
            if (actionSheetStyle.getListItemSeparatorColor() != 0) {
                separatorColor = actionSheetStyle.getListItemSeparatorColor();
            }
        }

        if (position == 0) {
            if (drawable == null)
                drawable = context.getDrawable(R.drawable.cc_action_item_top_curve);
            drawable.setTint(backgroundColor);
        } else if (position == actionItems.size() - 1) {
            if (drawable == null)
                drawable = context.getDrawable(R.drawable.cc_action_item_bottom_curve);
            drawable.setTint(backgroundColor);
            holder.separator.setVisibility(View.GONE);
        } else {
            holder.layout.setBackgroundColor(backgroundColor);
        }
        holder.cardView.setBackground(drawable);
        holder.separator.setBackgroundColor(separatorColor);
    }

    @Override
    public int getItemCount() {
        return actionItems.size();
    }

    public void hideText(boolean b) {
        hideText = b;
        notifyDataSetChanged();
    }

    public void addActionItem(ActionItem actionItem) {
        actionItems.add(actionItem);
        notifyDataSetChanged();
    }

    public void updateActionItem(ActionItem actionItem) {
        for (ActionItem item : actionItems) {
            if (item.getId().equals(actionItem.getId())) {
                item.setText(actionItem.getText());
                item.setStartIcon(actionItem.getStartIcon());
            }
        }
    }

    public void setStyle(ActionSheetStyle actionSheetStyle) {
        this.actionSheetStyle = actionSheetStyle;
        notifyDataSetChanged();
    }

    class ActionItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView actionIcons;
        private TextView actionText;
        private LinearLayout layout;
        private TextView separator;
        private MaterialCardView cardView, iconBackground;

        public ActionItemViewHolder(@NonNull View view) {
            super(view);
            actionIcons = view.findViewById(R.id.icon);
            actionText = view.findViewById(R.id.text);
            layout = view.findViewById(R.id.layout);
            cardView = view.findViewById(R.id.card);
            iconBackground = view.findViewById(R.id.iconBackground);
            if (!gridLayout) {
                separator = view.findViewById(R.id.separator);
            }
        }
    }
}
