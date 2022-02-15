package com.cometchatworkspace.components.messages.message_list.message_bubble.utils;

import androidx.annotation.StringDef;

/*
 * Constants to define shape
 * */
public class TextMessageType {

    @StringDef({Type.TEXT_MESSSAGE, Type.LINK_MESSAGE})

    public @interface Type {
        String TEXT_MESSSAGE = "TEXT_MESSAGE";
        String LINK_MESSAGE = "LINK_MESSAGE";
    }

    public static String getValue(int pos) {
        if (pos==0)
            return Type.TEXT_MESSSAGE;
        else
            return Type.LINK_MESSAGE;
    }
}
