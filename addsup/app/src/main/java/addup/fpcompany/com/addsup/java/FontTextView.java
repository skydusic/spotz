package addup.fpcompany.com.addsup.java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

@SuppressLint("AppCompatCustomView")
public class FontTextView {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

}
