package com.cometchat.chatuikit.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.chatuikit.shared.Interfaces.OnDetailOptionClick;
import com.cometchat.chatuikit.shared.models.CometChatDetailsOption;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.resources.utils.FontUtils;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.chatuikit.R;

import java.util.ArrayList;
import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.myViewHolder> {
    List<CometChatDetailsTemplate> cometChatDetailsTemplates;
    Context context;
    OptionAdapter optionAdapter;
    private User user;
    private Group group;
    private OnDetailOptionClick defaultOnDetailOptionClick;

    public TemplateAdapter(Context context, OnDetailOptionClick defaultOnDetailOptionClick) {
        this.context = context;
        this.defaultOnDetailOptionClick = defaultOnDetailOptionClick;
        cometChatDetailsTemplates = new ArrayList<>();
    }

    @NonNull
    @Override
    public TemplateAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.template_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateAdapter.myViewHolder holder, int position) {
        CometChatDetailsTemplate cometChatDetailsTemplate = cometChatDetailsTemplates.get(position);
        List<CometChatDetailsOption> options = cometChatDetailsTemplate.getOptions(user, group);

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        optionAdapter = new OptionAdapter(context, user, group, cometChatDetailsTemplate.getId(), defaultOnDetailOptionClick);
        holder.recyclerView.setAdapter(optionAdapter);
        optionAdapter.setOptions(options);
        if (cometChatDetailsTemplate.getTitle() != null && !cometChatDetailsTemplate.getTitle().isEmpty())
            holder.title.setText(cometChatDetailsTemplate.getTitle() + "");
        else
            holder.title.setVisibility(View.GONE);
        if (cometChatDetailsTemplate.isHideSectionSeparator())
            holder.separator.setVisibility(View.GONE);
        else
            holder.separator.setVisibility(View.VISIBLE);

        if (cometChatDetailsTemplate.getTitleFont() != null && !cometChatDetailsTemplate.getTitleFont().isEmpty()) {
            holder.title.setTypeface(FontUtils.getInstance(context).getTypeFace(cometChatDetailsTemplate.getTitleFont()));
        }
        if (cometChatDetailsTemplate.getTitleColor() != 0) {
            holder.title.setTextColor(cometChatDetailsTemplate.getTitleColor());
        }
        if (cometChatDetailsTemplate.getTitleAppearance() != 0) {
            holder.title.setTextAppearance(context, cometChatDetailsTemplate.getTitleAppearance());
        }
        if (options == null || options.size() == 0) {
            holder.title.setVisibility(View.GONE);
            holder.separator.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return cometChatDetailsTemplates.size();
    }

    public void setDetailTemplate(List<CometChatDetailsTemplate> templates) {
        if (templates != null) {
            cometChatDetailsTemplates = templates;
            notifyDataSetChanged();
        }
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
            notifyDataSetChanged();
        }
    }

    public void setGroup(Group group) {
        if (group != null) {
            this.group = group;
            notifyDataSetChanged();
        }
    }

    public void addTemplate(CometChatDetailsTemplate template) {
        if (!cometChatDetailsTemplates.contains(template)) {
            cometChatDetailsTemplates.add(template);
            notifyItemInserted(cometChatDetailsTemplates.size());
        }
    }

    public void updateTemplate(CometChatDetailsTemplate template) {
        if (cometChatDetailsTemplates.contains(template)) {
            cometChatDetailsTemplates.set(cometChatDetailsTemplates.indexOf(template), template);
            notifyItemChanged(cometChatDetailsTemplates.indexOf(template));
        }
    }

    public void removeTemplate(String id) {
        CometChatDetailsTemplate cometChatDetailsTemplate = new CometChatDetailsTemplate().setId(id);
        if (cometChatDetailsTemplates.contains(cometChatDetailsTemplate)) {
            int index = cometChatDetailsTemplates.indexOf(cometChatDetailsTemplate);
            cometChatDetailsTemplates.remove(cometChatDetailsTemplate);
            notifyItemRemoved(index);
        }
    }

    public void removeTemplate(CometChatDetailsTemplate cometChatDetailsTemplate) {
        if (cometChatDetailsTemplates.contains(cometChatDetailsTemplate)) {
            int index = cometChatDetailsTemplates.indexOf(cometChatDetailsTemplate);
            cometChatDetailsTemplates.remove(cometChatDetailsTemplate);
            notifyItemRemoved(index);
        }
    }

    public void removeOption(String templateId, CometChatDetailsOption option) {
        CometChatDetailsTemplate cometChatDetailsTemplate = new CometChatDetailsTemplate().setId(templateId);
        if (cometChatDetailsTemplates.contains(cometChatDetailsTemplate)) {
            int index = cometChatDetailsTemplates.indexOf(cometChatDetailsTemplate);
            CometChatDetailsTemplate cometChatDetailsTemplate1 = cometChatDetailsTemplates.get(index);
            if (cometChatDetailsTemplate1.getOptions(user, group).contains(option)) {
                cometChatDetailsTemplate1.getOptions(user, group).remove(option);
                cometChatDetailsTemplates.set(index, cometChatDetailsTemplate1);
                notifyItemChanged(index);
            }
        }
    }

    public void updateOption(String templateId, CometChatDetailsOption option) {
        CometChatDetailsTemplate cometChatDetailsTemplate = new CometChatDetailsTemplate().setId(templateId);
        if (cometChatDetailsTemplates.contains(cometChatDetailsTemplate)) {
            int index = cometChatDetailsTemplates.indexOf(cometChatDetailsTemplate);
            CometChatDetailsTemplate cometChatDetailsTemplate1 = cometChatDetailsTemplates.get(index);
            if (cometChatDetailsTemplate1.getOptions(user, group).contains(option)) {
                cometChatDetailsTemplate1.getOptions(user, group).set(cometChatDetailsTemplate1.getOptions(user, group).indexOf(option), option);
                cometChatDetailsTemplates.set(index, cometChatDetailsTemplate1);
                notifyItemChanged(index);
            }
        }
    }

    public void addOption(String templateId, CometChatDetailsOption option) {
        CometChatDetailsTemplate cometChatDetailsTemplate = new CometChatDetailsTemplate().setId(templateId);
        if (cometChatDetailsTemplates.contains(cometChatDetailsTemplate)) {
            int index = cometChatDetailsTemplates.indexOf(cometChatDetailsTemplate);
            CometChatDetailsTemplate cometChatDetailsTemplate1 = cometChatDetailsTemplates.get(index);
            if (!cometChatDetailsTemplate1.getOptions(user, group).contains(option)) {
                cometChatDetailsTemplate1.getOptions(user, group).add(option);
                cometChatDetailsTemplates.set(index, cometChatDetailsTemplate1);
                notifyItemChanged(index);
            }
        }
    }

    public void setOnOptionClickListener(OnDetailOptionClick onDetailOptionClick) {
        if (onDetailOptionClick != null) {
            this.defaultOnDetailOptionClick = onDetailOptionClick;
            notifyDataSetChanged();
        }
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView separator, title;
        RecyclerView recyclerView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            separator = itemView.findViewById(R.id.separator);
            title = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }
    }
}
