package org.example.zeitrechner;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Timestamp {
    private int hour;
    private int min;
    private int sek;

    public void makeTimestamp() {
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getSek() {
        return sek;
    }
    public void setSek(int sek) {
        this.sek = sek;
    }
    public static void main(String[] args) {
        Timestamp time = new Timestamp();
        time.makeTimestamp();
    }
}
