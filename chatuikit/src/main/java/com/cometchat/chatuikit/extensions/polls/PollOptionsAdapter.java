package com.cometchat.chatuikit.extensions.polls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.R;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;

import java.util.ArrayList;
import java.util.List;

public class PollOptionsAdapter extends RecyclerView.Adapter<PollOptionsAdapter.OptionViewHolder> {
    private final Context context;
    private final List<Integer> optionsArrayList = new ArrayList<>();
    private Palette palette;
    private Typography typography;
    private @StyleRes
    int textAppearance;
    private @ColorInt
    int textColor;
    private @ColorInt
    int hintColor;
    private @ColorInt
    int optionSeparatorColor;
    private CometChatTheme theme;

    public PollOptionsAdapter(Context context) {
        this.context = context;
        theme = CometChatTheme.getInstance(context);
        setOptionSeparatorColor(theme.getPalette().getAccent100());
        setOptionHintColor(theme.getPalette().getAccent600());
        setOptionTextColor(theme.getPalette().getAccent());
        setOptionTextAppearance(theme.getTypography().getText1());
    }

    public void remove(int pos) {
        optionsArrayList.remove(pos);
        notifyDataSetChanged();
    }

    public void add(int optionId) {
        optionsArrayList.add(optionId);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View optionView = LayoutInflater.from(context).inflate(R.layout.polls_option, null, false);
        return new OptionViewHolder(optionView);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        holder.option.setHint(context.getResources().getString(R.string.enter_your_option) + " " + (position + 3));
        holder.option.setTextAppearance(context, textAppearance);
        holder.option.setHintTextColor(hintColor);
        holder.option.setTextColor(textColor);
        holder.option.requestFocus();
        holder.option_separator.setBackgroundColor(optionSeparatorColor);
    }

    public void setOptionTextAppearance(@StyleRes int appearance) {
        if (appearance != 0) {
            this.textAppearance = appearance;
            notifyDataSetChanged();
        }
    }

    public void setOptionTextColor(@ColorInt int color) {
        if (color != 0) {
            this.textColor = color;
            notifyDataSetChanged();
        }
    }

    public void setOptionHintColor(@ColorInt int color) {
        if (color != 0) {
            this.hintColor = color;
            notifyDataSetChanged();
        }
    }

    public void setOptionSeparatorColor(@ColorInt int color) {
        if (color != 0) {
            this.optionSeparatorColor = color;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return optionsArrayList.size();
    }

    public class OptionViewHolder extends RecyclerView.ViewHolder {
        public EditText option;
        public View option_separator;

        OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            option = itemView.findViewById(R.id.option_txt);
            option_separator = itemView.findViewById(R.id.option_separator);
        }
    }
}
