package fightinggame.entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GameTimer {

    public static String FORMAT_HMS = "HH:mm:ss";
    public static String FORMAT_MS = "mm:ss";
    
    private LocalTime time;
    private static GameTimer instance;

    public static GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }
    
    private GameTimer() {
        time = LocalTime.now();
    }
    
    public void tick() {
        time = LocalTime.now();
    }
    
    public LocalTime addMinutes(long minute) {
        return time.plusMinutes(minute);
    }
    public LocalTime addHours(long hour) {
        return time.plusHours(hour);
    }
    public LocalTime addSeconds(long seconds) {
        return time.plusSeconds(seconds);
    }
    
    public LocalTime minusMinutes(long minute) {
        return time.minusMinutes(minute);
    }
    public LocalTime minusHours(long hour) {
        return time.minusHours(hour);
    }
    public LocalTime minusSeconds(long seconds) {
        return time.minusSeconds(seconds);
    }
    
    public String countDownString(LocalTime localTime, String format) {
        return formatTimeString(minusLocalTime(localTime), format);
    }
    
    public boolean countDownEnd(LocalTime localTime) {
        if(localTime == null) return false;
        return time.compareTo(localTime) >= 0;
    } 
    
    public boolean checkEquals(LocalTime time1, LocalTime time2) {
        if(time1 == null || time2 == null) return false;
        return time1.compareTo(time2) == 0;
    }
    
    public boolean checkEquals(LocalTime localTime) {
        return checkEquals(time, localTime);
    }
    
    public LocalTime minusLocalTime(LocalTime time1, LocalTime time2) {
        if(time1 == null || time2 == null) return null;
        long nanoTime = time1.toNanoOfDay() - time2.toNanoOfDay();
        nanoTime = Math.abs(nanoTime);
        return LocalTime.ofNanoOfDay(nanoTime);
    }
    
    public LocalTime minusLocalTime(LocalTime localTime) {
        return minusLocalTime(time, localTime);
    }
    
    public String formatTimeString(LocalTime localTime) {
        return formatTimeString(localTime, FORMAT_HMS);
    }
    
    public String formatTimeString(LocalTime localTime, String format) {
        return localTime.format(DateTimeFormatter.ofPattern(format));
    }

    @Override
    public String toString() {
        return formatTimeString(time);
    }

}
