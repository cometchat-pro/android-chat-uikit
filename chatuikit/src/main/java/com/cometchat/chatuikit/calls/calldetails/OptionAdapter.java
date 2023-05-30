package com.cometchat.chatuikit.calls.calldetails;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.Interfaces.OnDetailOptionClick;
import com.cometchat.chatuikit.shared.models.CometChatDetailsOption;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

class OptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CometChatDetailsOption> list;
    private Context context;
    private @ColorInt
    int separatorColor;
    private boolean hideSeparator = true;
    private User user;
    private Group group;
    private String templateId;
    private OnDetailOptionClick onDetailOptionClick, defaultOnDetailOptionClick;
    private RecyclerView.LayoutManager layoutManager;
    private int GRID_LAYOUT = 1;
    private int LINEAR_LAYOUT = 2;

    public OptionAdapter(Context context, User user, Group group, String templateId, OnDetailOptionClick defaultOnDetailOptionClick, RecyclerView.LayoutManager layoutManager) {
        this.context = context;
        this.user = user;
        this.group = group;
        this.templateId = templateId;
        this.defaultOnDetailOptionClick = defaultOnDetailOptionClick;
        this.layoutManager = layoutManager;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (layoutManager instanceof GridLayoutManager) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_row, parent, false);
            view.setTag(GRID_LAYOUT);
            return new MyGridViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actions_row, parent, false);
            view.setTag(LINEAR_LAYOUT);
            return new myViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewTypes(position);
    }

    private int getItemViewTypes(int position) {
        if (layoutManager instanceof GridLayoutManager) return GRID_LAYOUT;
        else return LINEAR_LAYOUT;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CometChatDetailsOption cometChatDetailsOption = list.get(position);
        onDetailOptionClick = cometChatDetailsOption.getOnClick();
        if (holder.getItemViewType() == GRID_LAYOUT) {
            bindGridViewHolder((MyGridViewHolder) holder, position, cometChatDetailsOption);
        } else {
            bindLinearViewHolder((myViewHolder) holder, position, cometChatDetailsOption);
        }

        holder.itemView.setOnClickListener(view -> {
            if (onDetailOptionClick != null)
                onDetailOptionClick.onClick(user, group, templateId, cometChatDetailsOption, context);
            else if (defaultOnDetailOptionClick != null)
                defaultOnDetailOptionClick.onClick(user, group, templateId, cometChatDetailsOption, context);
        });

    }

    private void bindLinearViewHolder(myViewHolder holder, int position, CometChatDetailsOption option) {
        if (option != null) {
            View view = option.getView(context, user, group);
            if (view == null) {
                holder.title.setText(option.getTitle() + "");
                if (option.getIcon() != 0) {
                    holder.startIcon.setVisibility(View.VISIBLE);
                    holder.startIcon.setImageResource(option.getIcon());
                }
                if (option.getStartIconTint() != 0) {
                    holder.startIcon.setImageTintList(ColorStateList.valueOf(option.getStartIconTint()));
                }
                if (option.getEndIcon() != 0) {
                    holder.endIcon.setVisibility(View.VISIBLE);
                    holder.endIcon.setImageResource(option.getEndIcon());
                }
                if (option.getEndIconTint() != 0) {
                    holder.endIcon.setImageTintList(ColorStateList.valueOf(option.getEndIconTint()));
                }

                if (option.getTitleFont() != null && !option.getTitleFont().isEmpty()) {
                    holder.title.setTypeface(FontUtils.getInstance(context).getTypeFace(option.getTitleFont()));
                }
                if (option.getTitleColor() != 0) {
                    holder.title.setTextColor(option.getTitleColor());
                }
                if (option.getTitleAppearance() != 0) {
                    holder.title.setTextAppearance(context, option.getTitleAppearance());
                }
                if (!hideSeparator) holder.itemSeparator.setVisibility(View.VISIBLE);
                if (separatorColor != 0) holder.itemSeparator.setBackgroundColor(separatorColor);
            } else {
                holder.relativeLayout.removeAllViews();
                holder.relativeLayout.addView(view);
            }
        }
    }

    private void bindGridViewHolder(MyGridViewHolder holder, int position, CometChatDetailsOption option) {
        if (option != null) {
            View view = option.getView(context, user, group);
            if (view == null) {
                holder.cardView.setRadius(20);
                holder.cardView.setCardElevation(0);
                holder.cardView.setCardBackgroundColor(CometChatTheme.getInstance(context).getPalette().getAccent100());
                holder.title.setText(option.getTitle());
                if (option.getTitleFont() != null && !option.getTitleFont().isEmpty()) {
                    holder.title.setTypeface(FontUtils.getInstance(context).getTypeFace(option.getTitleFont()));
                }
                if (option.getTitleColor() != 0) {
                    holder.title.setTextColor(option.getTitleColor());
                }
                if (option.getTitleAppearance() != 0) {
                    holder.title.setTextAppearance(context, option.getTitleAppearance());
                }
                if (option.getStartIconTint() != 0) {
                    holder.icon.setImageTintList(ColorStateList.valueOf(option.getStartIconTint()));
                }
                if (option.getIcon() != 0) {
                    holder.icon.setVisibility(View.VISIBLE);
                    holder.icon.setImageResource(option.getIcon());
                } else holder.icon.setVisibility(View.GONE);
            } else {
                holder.linearLayout.removeAllViews();
                holder.linearLayout.addView(view);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOptions(List<CometChatDetailsOption> options) {
        if (options != null) {
            list = options;
            notifyDataSetChanged();
        }
    }

    public void removeOption(int index) {
        if (index > -1 && index < list.size()) {
            list.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void removeOption(CometChatDetailsOption option) {
        if (list.contains(option)) {
            int index = list.indexOf(option);
            list.remove(option);
            notifyItemRemoved(index);
        }
    }

    public void addOption(CometChatDetailsOption option) {
        if (!list.contains(option)) {
            list.add(option);
            notifyItemInserted(list.size() - 1);
        }
    }

    public void updateOption(CometChatDetailsOption option) {
        if (list.contains(option)) {
            list.set(list.indexOf(option), option);
            notifyItemChanged(list.indexOf(option), option);
        }
    }

    public void setSeparator(@ColorInt int color) {
        if (color != 0) {
            this.separatorColor = color;
            notifyDataSetChanged();
        }
    }

    public void hideSeparator(boolean hide) {
        this.hideSeparator = hide;
        notifyDataSetChanged();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayout;
        private ImageView startIcon, endIcon;
        private TextView itemSeparator, title;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.parent);
            startIcon = itemView.findViewById(R.id.start_icon);
            endIcon = itemView.findViewById(R.id.end_icon);
            itemSeparator = itemView.findViewById(R.id.item_separator);
            title = itemView.findViewById(R.id.title);
        }
    }

    public class MyGridViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private ImageView icon;
        private TextView title;
        private MaterialCardView cardView;

        public MyGridViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.parent);
            icon = itemView.findViewById(R.id.icon);
            cardView = itemView.findViewById(R.id.card_view);
            title = itemView.findViewById(R.id.title);
        }
    }
}
