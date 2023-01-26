package fightinggame.entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GameTimer {

    public static final String FORMAT_HMS = "HH:mm:ss";
    public static final String FORMAT_MS = "mm:ss";
    
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
        return addMinutes(time, minute);
    }
    public LocalTime addHours(long hour) {
        return addHours(time, hour);
    }
    public LocalTime addSeconds(long seconds) {
        return addSeconds(time, seconds);
    }
    
    public LocalTime minusMinutes(long minute) {
        return minusMinutes(time, minute);
    }
    public LocalTime minusHours(long hour) {
        return minusHours(time, hour);
    }
    public LocalTime minusSeconds(long seconds) {
        return minusSeconds(time, seconds);
    }
    
    public LocalTime addMinutes(LocalTime localTime, long minute) {
        return localTime.plusMinutes(minute);
    }
    public LocalTime addHours(LocalTime localTime, long hour) {
        return localTime.plusHours(hour);
    }
    public LocalTime addSeconds(LocalTime localTime, long seconds) {
        return localTime.plusSeconds(seconds);
    }
    
    public LocalTime minusMinutes(LocalTime localTime, long minute) {
        return localTime.minusMinutes(minute);
    }
    public LocalTime minusHours(LocalTime localTime, long hour) {
        return localTime.minusHours(hour);
    }
    public LocalTime minusSeconds(LocalTime localTime, long seconds) {
        return localTime.minusSeconds(seconds);
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
    
    public int compareTo(LocalTime localTime) {
        return time.compareTo(localTime);
    }

    @Override
    public String toString() {
        return formatTimeString(time);
    }

}
