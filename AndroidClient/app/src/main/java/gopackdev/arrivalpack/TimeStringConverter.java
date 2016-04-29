package gopackdev.arrivalpack;

/**
 * Created by Chi-Han on 4/26/2016.
 */
public class TimeStringConverter {
    /**
     * Convert the our time form for sleep and wake
     * @param time
     */
    public static String convertFromTime(int time){
        int hour = time/100;
        int min = time%100;
        String hourStr = String.format("%02d", hour);
        String minStr = String.format("%02d",min);
        return hourStr + ":" + minStr;
    }
}
