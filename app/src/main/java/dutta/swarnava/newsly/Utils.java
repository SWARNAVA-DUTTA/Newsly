package dutta.swarnava.newsly;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import org.ocpsoft.prettytime.PrettyTime;

public class Utils {
    public static ColorDrawable[] vibrantLightColorList = {new ColorDrawable(Color.parseColor("#ffeead")), new ColorDrawable(Color.parseColor("#93cfb3")), new ColorDrawable(Color.parseColor("#fd7a7a")), new ColorDrawable(Color.parseColor("#faca5f")), new ColorDrawable(Color.parseColor("#1ba798")), new ColorDrawable(Color.parseColor("#6aa9ae")), new ColorDrawable(Color.parseColor("#ffbf27")), new ColorDrawable(Color.parseColor("#d93947"))};

    public static ColorDrawable getRandomDrawbleColor() {
        return vibrantLightColorList[new Random().nextInt(vibrantLightColorList.length)];
    }

    public static String DateToTimeFormat(String str) {
        try {
            return new PrettyTime(new Locale(getCountry())).format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DateFormat(String str) {
        try {
            return new SimpleDateFormat("E, d MMM yyyy", new Locale(getCountry())).format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static String getCountry() {
        return String.valueOf(Locale.getDefault().getCountry()).toLowerCase();
    }

    public static String getLanguage() {
        return String.valueOf(Locale.getDefault().getLanguage()).toLowerCase();
    }
}
