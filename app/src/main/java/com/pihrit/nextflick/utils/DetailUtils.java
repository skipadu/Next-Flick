package com.pihrit.nextflick.utils;

import android.content.Context;

import com.pihrit.nextflick.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailUtils {

    private DetailUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getDateFormattedByLocale(Context context, String dateStr) {
        String formattedDateStr = dateStr;
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.tmdb_date_format));
        Date date;
        try {
            date = sdf.parse(formattedDateStr);
            formattedDateStr = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
        } catch (ParseException e) {
            // In case of exception, we are returning the date in form that we acquired it from the API
        }
        return formattedDateStr;
    }

    public static String getVoteAverageStr(Context context, double voteAverage) {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat(context.getString(R.string.vote_average_decimalformat));
        sb.append(df.format(voteAverage)).append(context.getString(R.string.vote_average_max));
        return sb.toString();
    }
}
