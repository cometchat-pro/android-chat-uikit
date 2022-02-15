package com.cometchatworkspace.components.messages.message_list.message_bubble.utils;

import androidx.annotation.StringDef;

/*
 * Constants to define shape
 * */
public class Alignment {

    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    @StringDef({LEFT, RIGHT})


    public @interface MessageAlignment {
    }

    public static String getValue(int pos) {
        if (pos==0)
            return LEFT;
        else
            return RIGHT;
    }
}
