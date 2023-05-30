package com.cometchat.chatuikit.shared.resources.Localise;

import androidx.annotation.StringDef;

/**
 * The Language class provides constants and annotations for supported language codes.
 * <p>
 * It defines a set of language codes as String constants and provides a StringDef annotation for validation.
 */
public class Language {
    /**
     * Annotation for defining valid language codes.
     */
    @StringDef({Code.de, Code.ar, Code.zh_tw, Code.en, Code.fr, Code.es,
            Code.hi, Code.hu, Code.lt, Code.ms, Code.ru, Code.zh, Code.sv,
            Code.pt})

    public @interface Code {
        String de = "de";
        String ar = "ar";
        String zh_tw = "zh-TW";
        String en = "en";
        String es = "es";
        String ms = "ms";
        String sv = "sv";
        String hu = "hu";
        String lt = "lt";
        String ru = "ru";
        String fr = "fr";
        String pt = "pt";
        String hi = "hi";
        String zh = "zh";

    }
}
