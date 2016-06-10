package ru.nabsky.helper;

import java.util.Calendar;
import java.util.Date;

public class DayHelper {
    public static Integer[] dateToArray(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Integer[] result = new Integer[]{year, month, day};
        return result;
    }
}
