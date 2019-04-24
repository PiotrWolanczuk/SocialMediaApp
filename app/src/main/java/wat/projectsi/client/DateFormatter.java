package wat.projectsi.client;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    public static long maxDate = System.currentTimeMillis();
    public static long minDate = System.currentTimeMillis() - 1000L * 3600L * 24L * 365L * 3600L;

    public static DateFormat viewDateFormat(){
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
    }

    public static DateFormat viewPostDateFormat(final Context context){
        return new SimpleDateFormat((android.text.format.DateFormat.is24HourFormat(context)? "yyyy-MM-dd  hh:mm:ss" : "yyyy-MM-dd  hh:mm:ss aa"), Locale.getDefault());
    }

    public static DateFormat apiDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static String convertToApi(String date)
    {
        try {
            return DateFormatter.apiDateFormat().format( DateFormatter.viewDateFormat().parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date().toString();
        }
    }

}
