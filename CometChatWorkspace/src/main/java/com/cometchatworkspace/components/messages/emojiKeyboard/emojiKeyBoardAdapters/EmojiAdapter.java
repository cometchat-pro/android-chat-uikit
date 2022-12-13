package com.cometchatworkspace.components.messages.emojiKeyboard.emojiKeyBoardAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.User;
import com.cometchatworkspace.R;
import com.cometchatworkspace.components.messages.emojiKeyboard.CometChatEmojiKeyboard;
import com.cometchatworkspace.components.messages.emojiKeyboard.EmojiKeyboardStyle;
import com.cometchatworkspace.components.messages.emojiKeyboard.model.Emoji;
import com.cometchatworkspace.components.messages.emojiKeyboard.model.EmojiCategory;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatSmartReplies.CometChatSmartReplies;
import com.cometchatworkspace.components.users.CometChatUserEvents;
import com.cometchatworkspace.resources.utils.FontUtils;
import com.cometchatworkspace.resources.utils.recycler_touch.ClickListener;
import com.cometchatworkspace.resources.utils.recycler_touch.RecyclerTouchListener;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {
    private Context context;
    private List<EmojiCategory> emojiCategories;
    private EmojiItemAdapter emojiItemAdapter;
    private CometChatEmojiKeyboard.onClick onClick;
    private EmojiKeyboardStyle emojiKeyboardStyle;

    public EmojiAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmojiAdapter.EmojiViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cc_emoji_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        EmojiCategory emojiCategory=emojiCategories.get(position);
        ((Activity) context).runOnUiThread(() -> {
            if (emojiKeyboardStyle != null) {
                if (emojiKeyboardStyle.getSectionHeaderColor() != 0) {
                    holder.categoryName.setTextColor(emojiKeyboardStyle.getSectionHeaderColor());
                }
                if (emojiKeyboardStyle.getSectionHeaderFont() != null) {
                    holder.categoryName.setTypeface(FontUtils.getInstance(context).getTypeFace(emojiKeyboardStyle.getSectionHeaderFont()));
                }
                if (emojiKeyboardStyle.getSectionHeaderAppearance() != 0) {
                    holder.categoryName.setTextAppearance(context, emojiKeyboardStyle.getSectionHeaderAppearance());
                }
            }
            holder.categoryName.setText(emojiCategory.getName() + "");
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 8));
            emojiItemAdapter = new EmojiItemAdapter(context);
            holder.recyclerView.setAdapter(emojiItemAdapter);
            emojiItemAdapter.setEmojis(emojiCategory.getEmojis());
        });

        holder.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, holder.recyclerView, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                Emoji emoji = (Emoji) var1.getTag(R.string.emoji);
                if (onClick != null)
                    onClick.onClick(emoji.getEmoji());

            }

            @Override
            public void onLongClick(View var1, int var2) {
                Emoji emoji = (Emoji) var1.getTag(R.string.emoji);
                if (onClick != null) {
                    onClick.onLongClick(emoji.getEmoji());
                }
            }
        }));

    }

    @Override
    public int getItemCount() {
        return emojiCategories.size();
    }

    public void setOnClick(CometChatEmojiKeyboard.onClick onClick) {
        this.onClick = onClick;
    }

    public void setEmojiCategories(List<EmojiCategory> emojiCategories) {
        this.emojiCategories = emojiCategories;
        notifyDataSetChanged();
    }

    public void setStyle(EmojiKeyboardStyle emojiKeyboardStyle) {
        this.emojiKeyboardStyle = emojiKeyboardStyle;
        notifyDataSetChanged();
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView recyclerView;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            recyclerView = itemView.findViewById(R.id.emoji_recycler);

        }
    }

}
