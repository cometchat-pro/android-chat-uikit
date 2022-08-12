package com.cometchatworkspace.components.messages.message_list.message_bubble.utils;

import com.cometchatworkspace.components.shared.primaryComponents.InputData;

import java.util.function.Function;

public class MessageInputData extends InputData {

    private final boolean readReceipts;
    private final boolean timeStamp;

    public MessageInputData(boolean thumbnail,
                            boolean status,
                            boolean title,
                            boolean readReceipts,
                            boolean timeStamp) {
        super(thumbnail, status, title,"");
        this.readReceipts = readReceipts;
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean isThumbnail() {
        return super.isThumbnail();
    }

    @Override
    public boolean isStatus() {
        return super.isStatus();
    }

    @Override
    public boolean isTitle() {
        return super.isTitle();
    }

    @Override
    public Object getSubTitle(Object obj) {
        return super.getSubTitle(obj);
    }

    @Override
    public String getMetaData() {
        return super.getMetaData();
    }

    public boolean isTimeStamp() {
        return timeStamp;
    }

    public boolean isReadReceipts() {
        return readReceipts;
    }
}
