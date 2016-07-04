package ru.nabsky.helper;

import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {

    public static final String TIME_DELIMITER = ":";

    public static Integer[] dateToArray(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        Integer[] result = new Integer[]{year, month, day};
        return result;
    }

    public static Integer[] timeStringToArray(String timeString){
        String[] timeParts = timeString.split(TIME_DELIMITER);
        if(timeParts.length != 2){
            throw new IllegalArgumentException("Incorrect Time Format");
        }
        Integer hour = null;
        Integer minute = null;
        try {
            hour = Integer.parseInt(timeParts[0]);
            minute = Integer.parseInt(timeParts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Incorrect Time Format");
        }
        Integer[] result = new Integer[]{hour, minute};
        return result;
    }

    public static String timeArrayToNormalizedString(Integer[] timeArray){
        Integer hour = timeArray[0] + timeArray[1]/60;
        Integer minute = timeArray[1]%60;
        String result = String.join(TIME_DELIMITER, hour.toString(), minute.toString());
        return result;
    }
}
