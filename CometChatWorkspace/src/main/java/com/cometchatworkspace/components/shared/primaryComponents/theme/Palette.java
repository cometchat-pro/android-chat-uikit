package com.cometchatworkspace.components.shared.primaryComponents.theme;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.ColorInt;

import com.cometchatworkspace.R;

public class Palette {

    private int background = 0;

    private int searchBackground = 0;

    private int primary = 0;

    private int error = 0;

    private int accent = 0;

    private int accent50 = 0;

    private int accent100 = 0;

    private int accent200 = 0;

    private int accent300 = 0;

    private int accent400 = 0;

    private int accent500 = 0;

    private int accent600 = 0;

    private int accent700 = 0;

    private int accent800 = 0;

    private int accent900 = 0;

    private static Context conxt;


    private String mode = CometChatTheme.MODE.LIGHT;

    //create an object of SingletonObject
    private static Palette instance = new Palette();

    //private constructor so that we cannot instantiate the class
    private Palette() {
    }

    //returns the only available object
    public static Palette getInstance(Context context) {
        conxt = context;
        return instance;
    }

    public void mode(@CometChatTheme.MODE String themeMode) {
        mode = themeMode;
    }

    public void background(@ColorInt int color) {
        background = color;
    }

    public void primary(@ColorInt int color) {
        primary = color;
    }

    public void error(@ColorInt int color) {
        error = color;
    }

    public void accent(@ColorInt int color) {
        accent = color;

    }

    public void accent50(@ColorInt int color) {
        accent50 = color;

    }

    public void accent100(@ColorInt int color) {
        accent100 = color;

    }

    public void accent200(@ColorInt int color) {
        accent200 = color;

    }

    public void accent300(@ColorInt int color) {
        accent300 = color;

    }

    public void accent400(@ColorInt int color) {
        accent400 = color;

    }

    public void accent500(@ColorInt int color) {
        accent500 = color;

    }

    public void accent600(@ColorInt int color) {
        accent600 = color;

    }

    public void accent700(@ColorInt int color) {
        accent700 = color;

    }

    public void accent800(@ColorInt int color) {
        accent800 = color;

    }

    public void accent900(@ColorInt int color) {
        accent900 = color;

    }

    public void searchBackground(@ColorInt int color) {
        searchBackground = color;

    }


    public @ColorInt
    int getBackground() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return background != 0 ? background : conxt.getResources().getColor(R.color.background);
        else
            return background != 0 ? background : conxt.getResources().getColor(R.color.background_dark);

    }

    public @ColorInt
    int getPrimary() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return primary != 0 ? primary : conxt.getResources().getColor(R.color.primary);
        else
            return primary != 0 ? primary : conxt.getResources().getColor(R.color.primary_dark);

    }

    public @ColorInt
    int getError() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return error != 0 ? error : conxt.getResources().getColor(R.color.error);
        else
            return error != 0 ? error : conxt.getResources().getColor(R.color.error_dark);

    }

    public @ColorInt
    int getAccent() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent != 0 ? accent : conxt.getResources().getColor(R.color.accent);
        else
            return accent != 0 ? accent : conxt.getResources().getColor(R.color.accent_dark);


    }

    public @ColorInt
    int getAccent50() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent50 != 0 ? accent50 : conxt.getResources().getColor(R.color.accent50);
        else
            return accent50 != 0 ? accent50 : conxt.getResources().getColor(R.color.accent50_dark);
    }

    public @ColorInt
    int getAccent100() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent100 != 0 ? accent100 : conxt.getResources().getColor(R.color.accent100);
        else
            return accent100 != 0 ? accent100 : conxt.getResources().getColor(R.color.accent100_dark);
    }

    public @ColorInt
    int getAccent200() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent200 != 0 ? accent200 : conxt.getResources().getColor(R.color.accent200);
        else
            return accent200 != 0 ? accent200 : conxt.getResources().getColor(R.color.accent200_dark);
    }

    public @ColorInt
    int getAccent300() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent300 != 0 ? accent300 : conxt.getResources().getColor(R.color.accent300);
        else
            return accent300 != 0 ? accent300 : conxt.getResources().getColor(R.color.accent300_dark);
    }

    public @ColorInt
    int getAccent400() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent400 != 0 ? accent400 : conxt.getResources().getColor(R.color.accent400);
        else
            return accent400 != 0 ? accent400 : conxt.getResources().getColor(R.color.accent400_dark);
    }

    public @ColorInt
    int getAccent500() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent500 != 0 ? accent500 : conxt.getResources().getColor(R.color.accent500);
        else
            return accent500 != 0 ? accent500 : conxt.getResources().getColor(R.color.accent500_dark);
    }

    public @ColorInt
    int getAccent600() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent600 != 0 ? accent600 : conxt.getResources().getColor(R.color.accent600);
        else
            return accent600 != 0 ? accent600 : conxt.getResources().getColor(R.color.accent600_dark);
    }

    public @ColorInt
    int getAccent700() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent700 != 0 ? accent700 : conxt.getResources().getColor(R.color.accent700);
        else
            return accent700 != 0 ? accent700 : conxt.getResources().getColor(R.color.accent700_dark);
    }

    public @ColorInt
    int getAccent800() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent800 != 0 ? accent800 : conxt.getResources().getColor(R.color.accent800);
        else
            return accent800 != 0 ? accent800 : conxt.getResources().getColor(R.color.accent800_dark);
    }

    public @ColorInt
    int getAccent900() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return accent900 != 0 ? accent900 : conxt.getResources().getColor(R.color.accent900);
        else
            return accent900 != 0 ? accent900 : conxt.getResources().getColor(R.color.accent900_dark);
    }

    public @ColorInt
    int getSearchBackground() {
        if (mode.equalsIgnoreCase(CometChatTheme.MODE.LIGHT))
            return searchBackground != 0 ? searchBackground : conxt.getResources().getColor(R.color.searchBackground);
        else
            return searchBackground != 0 ? searchBackground : conxt.getResources().getColor(R.color.searchBackground_dark);
    }

    public String getMode() {
        return mode;
    }


}
