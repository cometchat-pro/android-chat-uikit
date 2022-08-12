package com.cometchatworkspace.components.shared.secondaryComponents.cometchatSectionList;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

import com.cometchatworkspace.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;
import com.cometchatworkspace.resources.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

public class CometChatSectionList extends RelativeLayout {
    private Context context;
    private View view;
    private LinearLayout parentView;
    private final List<ActionItem> dataItems = new ArrayList<>();
    private final List<SectionData> sectionDataList = new ArrayList<>();
    private ItemActionListener itemActionListener;
    private FontUtils fontUtils;

    public CometChatSectionList(Context context) {
        super(context);
        init(context, null, -1);

    }

    public CometChatSectionList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);

    }

    public CometChatSectionList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        fontUtils = FontUtils.getInstance(context);
        view = View.inflate(context, R.layout.user_action, null);
        parentView = view.findViewById(R.id.parentlay);
        addView(view);
    }

    public void setDataItems(List<SectionData> sectionData) {
        if (sectionData != null && sectionData.size() > 0) {
            clear();
            sectionDataList.addAll(sectionData);
            makeRow();
        }
    }

    public void clear() {
        dataItems.clear();
        sectionDataList.clear();
        parentView.removeAllViewsInLayout();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        makeRow();
    }

    private void makeRow() {
        if (sectionDataList.size() > 0) {
            for (SectionData sectionData : sectionDataList) {
                View view = View.inflate(context, R.layout.actions_row, null);
                LinearLayout separator_container = view.findViewById(R.id.separator_container);
                View separator = view.findViewById(R.id.separator);
                TextView option_title = view.findViewById(R.id.option_title);

                if (sectionData.getActionItems() != null && sectionData.getActionItems().size() > 0) {
                    for (ActionItem actionItem : sectionData.getActionItems()) {
                        dataItems.add(actionItem);
                        View view1 = View.inflate(context, R.layout.actions_row, null);
                        TextView title = view1.findViewById(R.id.title);
                        ImageView start_icon = view1.findViewById(R.id.start_icon);
                        ImageView end_icon = view1.findViewById(R.id.end_icon);
                        View item_separator = view1.findViewById(R.id.item_separator);
                        RelativeLayout action_row = view1.findViewById(R.id.action_row);

                        setTitle(title, actionItem.getText());
                        setStartIcon(start_icon, actionItem.getStartIcon());
                        setEndIcon(end_icon, actionItem.getEndIcon());
                        startIconTint(start_icon, actionItem.getStartIconTint());
                        endIconTint(end_icon, actionItem.getEndIconTint());
                        setTitleAppearance(title, actionItem.getAppearance());
                        setTitleColor(title, actionItem.getTextColor());

                        view1.setOnLongClickListener(new OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                if (itemActionListener != null) {
                                    itemActionListener.onLongClick(actionItem, dataItems.indexOf(actionItem));
                                    return true;
                                } else
                                    return false;
                            }
                        });

                        view1.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (itemActionListener != null) {
                                    itemActionListener.onClick(actionItem, dataItems.indexOf(actionItem));
                                }
                            }
                        });

                        action_row.setVisibility(VISIBLE);
                        if (sectionData.isItemSeparator()) {
                            hideSeparator(item_separator, !sectionData.isItemSeparator());
                        }
                        setSeparatorColor(item_separator, sectionData.getItemSeparatorColor());
                        parentView.addView(view1);
                    }
                }
                dataItems.add(null);
                if (sectionData.isSectionSeparator() && !sectionData.isItemSeparator()) {
                    hideSeparator(separator, !sectionData.isSectionSeparator());
                } else {
                    hideSeparator(separator, true);
                }
                separator_container.setVisibility(VISIBLE);
                setSeparatorColor(separator, sectionData.getSectionSeparatorColor());
                setOptionTitle(option_title, sectionData.getTitle());
                setOptionTitleFont(option_title, sectionData.getTitleFont());
                setOptionTitleColor(option_title, sectionData.getTitleColor());
                setOptionTitleAppearance(option_title, sectionData.getTitleAppearance());
                parentView.addView(view);
            }


        }

    }

    private void hideSeparator(View separator, boolean value) {
        if (value)
            separator.setVisibility(GONE);
        else
            separator.setVisibility(VISIBLE);
    }

    public void removeRow(int pos) {
        parentView.removeViewInLayout(parentView.getChildAt(pos));
    }

    public void updateRow(ActionItem actionItem, int pos) {
        if (pos != -1) {
            dataItems.set(pos, actionItem);
            View view = View.inflate(context, R.layout.actions_row, null);
            TextView title = view.findViewById(R.id.title);
            ImageView start_icon = view.findViewById(R.id.start_icon);
            ImageView end_icon = view.findViewById(R.id.end_icon);
            RelativeLayout action_row = view.findViewById(R.id.action_row);


            setTitle(title, actionItem.getText());
            setStartIcon(start_icon, actionItem.getStartIcon());
            setEndIcon(end_icon, actionItem.getEndIcon());
            startIconTint(start_icon, actionItem.getStartIconTint());
            endIconTint(end_icon, actionItem.getEndIconTint());
            setTitleAppearance(title, actionItem.getAppearance());
            setTitleColor(title, actionItem.getTextColor());


            view.setOnLongClickListener(view1 -> {
                if (itemActionListener != null) {
                    itemActionListener.onLongClick(actionItem, pos);
                    return true;
                } else
                    return false;
            });

            view.setOnClickListener(view12 -> {
                if (itemActionListener != null) {
                    itemActionListener.onClick(actionItem, pos);
                }
            });
            parentView.removeViewInLayout(parentView.getChildAt(pos));
            action_row.setVisibility(VISIBLE);
            parentView.addView(view, pos);
        }
    }

    public void addRow(ActionItem actionItem) {
        sectionDataList.get(sectionDataList.size() - 1).getActionItems().add(actionItem);
    }

    public void addSection(List<SectionData> sectionData) {
        sectionDataList.addAll(sectionData);

    }

    public void addRowAt(ActionItem actionItem, int sectionPos, int itemPosition) {
        try {
            sectionDataList.get(sectionPos).getActionItems().add(itemPosition, actionItem);
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage() + "", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTitleColor(TextView title, int textColor) {
        if (textColor != 0)
            title.setTextColor(textColor);
    }

    private void setTitleAppearance(TextView title, @StyleRes int appearance) {
        if (appearance != 0)
            title.setTextAppearance(context, appearance);
    }

    private void setOptionTitle(TextView option_title, String title) {
        if (title != null && !title.isEmpty()) {
            option_title.setText(title);
            option_title.setVisibility(VISIBLE);
        } else
            option_title.setVisibility(GONE);
    }

    private void setOptionTitleAppearance(TextView option_title, @StyleRes int appearance) {
        if (appearance != 0)
            option_title.setTextAppearance(context, appearance);

    }

    private void setOptionTitleColor(TextView option_title, int textColor) {
        if (textColor != 0 && option_title != null)
            option_title.setTextColor(textColor);
    }

    private void setOptionTitleFont(TextView option_title, String font) {
        if (font != null && option_title != null)
            option_title.setTypeface(fontUtils.getTypeFace(font));
    }

    private void setStartIcon(ImageView start_icon, @DrawableRes int res) {
        if (res != 0 && start_icon != null) {
            start_icon.setVisibility(VISIBLE);
            start_icon.setImageResource(res);
        } else
            start_icon.setVisibility(GONE);
    }

    public void startIconTint(ImageView start_icon, @ColorInt int color) {
        if (color != 0 && start_icon != null)
            start_icon.setImageTintList(ColorStateList.valueOf(color));

    }

    private void setEndIcon(ImageView end_icon, @DrawableRes int res) {
        if (res != 0 && end_icon != null) {
            end_icon.setVisibility(VISIBLE);
            end_icon.setImageResource(res);
        } else
            end_icon.setVisibility(GONE);
    }

    public void endIconTint(ImageView end_icon, @ColorInt int color) {
        if (color != 0 && end_icon != null)
            end_icon.setImageTintList(ColorStateList.valueOf(color));
    }

    private void setTitle(TextView title, String title1) {
        title.setText(title1);
        title.setSelected(true);
    }


    private void setSeparatorColor(View divider, int borderColor) {
        if (borderColor != 0)
            divider.setBackgroundColor(borderColor);
    }

    public void setDividerBackground(View divider, int[] colorArray, GradientDrawable.Orientation orientation) {
        GradientDrawable gd = new GradientDrawable(
                orientation,
                colorArray);
        divider.setBackground(gd);
    }

    public void addOnItemClickListener(ItemActionListener itemActionListener) {
        this.itemActionListener = itemActionListener;
    }

    public interface ItemActionListener {
        void onClick(ActionItem actionItem, int pos);

        void onLongClick(ActionItem actionItem, int pos);
    }

}
