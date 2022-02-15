package com.cometchatworkspace.components.shared.secondaryComponents.cometchatActionSheet;

import androidx.annotation.StringDef;


/*
 * Constants to define Layout
 * */
public class ActionSheet {
    @StringDef({LayoutMode.listMode, LayoutMode.gridMode})
    public @interface LayoutMode {
        String listMode = "LIST";
        String gridMode = "GRID";
    }

    public static String getValue(int pos) {
        if (pos==0)
            return LayoutMode.listMode;
        else
            return LayoutMode.gridMode;
    }
}

