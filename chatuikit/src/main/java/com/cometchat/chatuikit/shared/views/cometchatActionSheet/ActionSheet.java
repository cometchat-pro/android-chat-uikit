package com.cometchat.chatuikit.shared.views.cometchatActionSheet;

import androidx.annotation.StringDef;


/**
 * The ActionSheet class provides a LayoutMode annotation to indicate the current layout mode of the ActionSheet.
 */
public class ActionSheet {
    /**
     * The LayoutMode annotation provides two values for the layout mode of the ActionSheet:
     * LIST - Indicates that the ActionSheet is currently in list mode.
     * GRID - Indicates that the ActionSheet is currently in grid mode.
     */
    @StringDef({LayoutMode.listMode, LayoutMode.gridMode})
    public @interface LayoutMode {
        String listMode = "LIST";
        String gridMode = "GRID";
    }
    /**
     The getValue method is used to return the corresponding layout mode value of the given position.
     @param pos The position of the layout mode.
     @return Returns the corresponding layout mode value.
     */
    public static String getValue(int pos) {
        if (pos == 0) return LayoutMode.listMode;
        else return LayoutMode.gridMode;
    }
}

