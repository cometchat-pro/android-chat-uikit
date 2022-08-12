package com.cometchatworkspace.components.shared.secondaryComponents.cometchatSectionList;

import android.view.View;

import com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet.ActionItem;

import java.util.List;

public class SectionData {

    private final String title;
    private final List<ActionItem> actionItems;
    private boolean sectionSeparator;
    private boolean itemSeparator;
    private String titleFont;
    private int titleColor;
    private int titleAppearance;
    private int sectionSeparatorColor;
    private int itemSeparatorColor;


    public SectionData(String title, List<ActionItem> actionItems) {
        this.title = title;
        this.actionItems = actionItems;
    }

    public SectionData(String title, List<ActionItem> actionItems, boolean sectionSeparator, boolean itemSeparator) {
        this.actionItems = actionItems;
        this.title = title;
        this.sectionSeparator = sectionSeparator;
        this.itemSeparator = itemSeparator;
    }

    public SectionData(String title, List<ActionItem> actionItems, boolean sectionSeparator, boolean itemSeparator, String titleFont, int titleColor, int titleAppearance, int sectionSeparatorColor, int itemSeparatorColor) {
        this.title = title;
        this.actionItems = actionItems;
        this.sectionSeparator = sectionSeparator;
        this.itemSeparator = itemSeparator;
        this.titleFont = titleFont;
        this.titleColor = titleColor;
        this.titleAppearance = titleAppearance;
        this.sectionSeparatorColor = sectionSeparatorColor;
        this.itemSeparatorColor = itemSeparatorColor;
    }

    public boolean isSectionSeparator() {
        return sectionSeparator;
    }

    public int getItemSeparatorColor() {
        return itemSeparatorColor;
    }

    public boolean isItemSeparator() {
        return itemSeparator;
    }

    public String getTitle() {
        return title;
    }

    public List<ActionItem> getActionItems() {
        return actionItems;
    }

    public String getTitleFont() {
        return titleFont;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public int getTitleAppearance() {
        return titleAppearance;
    }

    public int getSectionSeparatorColor() {
        return sectionSeparatorColor;
    }
}
