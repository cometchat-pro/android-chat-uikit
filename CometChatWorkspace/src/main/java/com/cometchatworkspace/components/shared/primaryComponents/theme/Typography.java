package com.cometchatworkspace.components.shared.primaryComponents.theme;

import android.graphics.Typeface;
import android.graphics.fonts.FontStyle;

import com.cometchatworkspace.R;

public class Typography {

    //create an object of SingletonObject
    private static final Typography instance = new Typography();

    //private constructor so that we cannot instantiate the class
    private Typography() {
    }

    //returns the only available object
    public static Typography getInstance() {
        return instance;
    }


    private int Heading = 0;
    private int Name = 0;
    private int Title1 = 0;
    private int Title2 = 0;
    private int Subtitle1 = 0;
    private int Subtitle2 = 0;
    private int Text1 = 0;
    private int Text2 = 0;
    private int Caption1 = 0;
    private int Caption2 = 0;


    public int getHeading() {
        return Heading != 0 ? Heading : R.style.Heading;
    }

    public int getName() {
        return Name != 0 ? Name : R.style.Name;
    }

    public int getTitle1() {
        return Title1 != 0 ? Title1 : R.style.Title1;
    }

    public int getTitle2() {
        return Title2 != 0 ? Title2 : R.style.Title2;
    }

    public int getSubtitle1() {
        return Subtitle1 != 0 ? Subtitle1 : R.style.Subtitle1;
    }

    public int getSubtitle2() {
        return Subtitle2 != 0 ? Subtitle2 : R.style.Subtitle2;
    }

    public int getText1() {
        return Text1 != 0 ? Text1 : R.style.Text1;
    }

    public int getText2() {
        return Text2 != 0 ? Text2 : R.style.Text2;
    }

    public int getCaption1() {
        return Caption1 != 0 ? Caption1 : R.style.Caption1;
    }

    public int getCaption2() {
        return Caption2 != 0 ? Caption2 : R.style.Caption2;
    }

    public void setHeading(int heading) {
        Heading = heading;
    }

    public void setName(int name) {
        Name = name;
    }

    public void setTitle1(int title1) {
        Title1 = title1;
    }

    public void setTitle2(int title2) {
        Title2 = title2;
    }

    public void setSubtitle1(int subtitle1) {
        Subtitle1 = subtitle1;
    }

    public void setSubtitle2(int subtitle2) {
        Subtitle2 = subtitle2;
    }

    public void setText1(int text1) {
        Text1 = text1;
    }

    public void setText2(int text2) {
        Text2 = text2;
    }

    public void setCaption1(int caption1) {
        Caption1 = caption1;
    }

    public void setCaption2(int caption2) {
        Caption2 = caption2;
    }

}
