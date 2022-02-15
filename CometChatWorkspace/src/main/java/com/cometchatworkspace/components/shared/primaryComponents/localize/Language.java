package com.cometchatworkspace.components.shared.primaryComponents.localize;

import androidx.annotation.StringDef;

/*
 * Constants to define Language
 * */
public class Language {
   @StringDef({Code.GERMAN,Code.ARABIC,Code.CHINESE_TAIWA,Code.ENGLISH,Code.FRENCH,Code.SPANISH,
   Code.HINDI,Code.HUNGARIAN,Code.LITHUANIAM,Code.MALAY,Code.RUSSIAN,Code.CHINESE,Code.SWEDISH,
   Code.PORTUGUESE})

   public @interface Code {
      String GERMAN="de";
      String ARABIC="ar";
      String CHINESE_TAIWA="zh-TW";
      String ENGLISH="en";
      String SPANISH="es";
      String MALAY="ms";
      String SWEDISH="sv";
      String  HUNGARIAN="hu";
      String LITHUANIAM="lt";
      String RUSSIAN="ru";
      String  FRENCH="fr";
      String  PORTUGUESE="pt";
      String  HINDI="hi";
      String  CHINESE="zh";

   }
}
