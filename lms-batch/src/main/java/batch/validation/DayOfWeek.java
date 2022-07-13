package batch.validation;

import java.util.Calendar;

public class DayOfWeek {
    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        int dayOfWeek = rightNow.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }
}
