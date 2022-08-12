package com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchatworkspace.R;

import java.util.List;

public class ActionSheetAdapter extends RecyclerView.Adapter<ActionSheetAdapter.ActionItemViewHolder> {

    Context context;
    private final List<ActionItem> actionItems;

    private boolean hideText;
    private final boolean gridLayout;
    private View view;
    public ActionSheetAdapter(Context context, List<ActionItem> actionItems,boolean isGridLayout) {
        this.context = context;
        this.actionItems = actionItems;
        this.gridLayout = isGridLayout;
    }

    @NonNull
    @Override
    public ActionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (gridLayout)
            view = layoutInflater.inflate(R.layout.action_grid_item, parent, false);
        else
            view = layoutInflater.inflate(R.layout.action_list_item,parent,false);
        return new ActionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionItemViewHolder holder, int position) {
        ActionItem item = actionItems.get(position);
        holder.actionText.setText(item.text);
        holder.actionIcons.setImageResource(item.startIcon);
        if (hideText)
            holder.actionText.setVisibility(View.GONE);
        else
            holder.actionText.setVisibility(View.VISIBLE);

        holder.itemView.setTag(R.string.action_item,item);
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
            if (item.id.equals(actionItem.id)) {
                item.startIcon = actionItem.startIcon;
                item.text = actionItem.text;
            }
        }
    }


    class ActionItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView actionIcons;
        public TextView actionText;

        public ActionItemViewHolder(@NonNull View view) {
            super(view);
            actionIcons = view.findViewById(R.id.icon);
            actionText = view.findViewById(R.id.text);
        }
    }
}
