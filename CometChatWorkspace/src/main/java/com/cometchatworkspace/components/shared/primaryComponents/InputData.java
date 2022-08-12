package com.cometchatworkspace.components.shared.primaryComponents;

import java.util.function.Function;

public class InputData<T> {


    private boolean thumbnail;
    private boolean status;
    private boolean title;
    private Function<T, Object> subTitle;
    private String metaData;
    public InputData(){}
    public InputData(boolean thumbnail, boolean status, boolean title, Function<T, Object> subTitle) {
        this.thumbnail = thumbnail;
        this.status = status;
        this.title = title;
        this.subTitle = subTitle;
    }

    public InputData(boolean thumbnail, boolean status, boolean title, String metaData) {
        this.thumbnail = thumbnail;
        this.status = status;
        this.title = title;
        this.metaData = metaData;
    }

    public boolean isThumbnail() {
        return thumbnail;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isTitle() {
        return title;
    }

    public Object getSubTitle(T obj) {
        Object string = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (subTitle!=null)
                string = subTitle.apply(obj);
        }
        return string;
    }

    public String getMetaData() {
        return metaData;
    }
}
