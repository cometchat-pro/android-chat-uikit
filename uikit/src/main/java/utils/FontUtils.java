package utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.ref.WeakReference;

public class FontUtils {

    public static Typeface robotoMedium;

    public static Typeface robotoBlack;

    public static Typeface robotoRegular;

    public static Typeface robotoBold;

    public static Typeface robotoLight;

    public static Typeface robotoThin;

    private static Context context;

    public FontUtils(Context c) {

        context=c;

        initFonts();
    }

    private void initFonts() {
        if (context!=null) {
            robotoMedium = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
            robotoRegular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
            robotoBlack = Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf");
            robotoBold = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
            robotoLight = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
            robotoThin = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        }
    }
}
