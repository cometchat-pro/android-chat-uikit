package com.cometchatworkspace.resources.utils.pattern_utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Patterns;
import android.widget.TextView;


import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cometchatworkspace.components.shared.primaryComponents.CometChatTheme;

public class PatternUtils {
    public static void setHyperLinkSupport(Context context, TextView txtMessage) {
        new PatternBuilder().
                addPattern(Pattern.compile("(^|[\\s.:;?\\-\\]<\\(])" +
                                "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\\(\\))?)" +
                                "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])"),
                        context.getResources().getColor(CometChatTheme.urlColor),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (!text.trim().contains("http")) {
                                    text = "http://"+text;
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(text.trim()));
                                context.startActivity(Intent.createChooser(intent, "Url"));
                            }
                        }).into(txtMessage);
        new PatternBuilder().
                addPattern(Patterns.PHONE, context.getResources().getColor(CometChatTheme.phoneNumberColor),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse(text));
                                intent.setData(Uri.parse("tel:"+text));
                                context.startActivity(Intent.createChooser(intent, "Dial"));
                            }
                        }).into(txtMessage);
        new PatternBuilder().
                addPattern(Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"),
                        context.getResources().getColor(CometChatTheme.emailColor),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" +text));
                                intent.putExtra(Intent.EXTRA_EMAIL, text);
                                context.startActivity(Intent.createChooser(intent, "Mail"));
                            }
                        }).into(txtMessage);
    }


    public static void setHtmlSupport(TextView textView) {
        String textMessage = textView.getText().toString();
        textView.setText(Html.fromHtml(textMessage));
    }
    /**
     * Below method is used to remove the emojis from string
     * @param content is a String object
     * @return a String value without emojis.
     */
    public static String removeEmojiAndSymbol(String content) {
        String utf8tweet = "";
        byte[] utf8Bytes = content.getBytes(StandardCharsets.UTF_8);
        utf8tweet = new String(utf8Bytes, StandardCharsets.UTF_8);
        Pattern unicodeOutliers = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE |
                        Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8tweet);
        utf8tweet = unicodeOutlierMatcher.replaceAll(" ");
        return utf8tweet;
    }
}
