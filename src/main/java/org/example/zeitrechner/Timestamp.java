package org.example.zeitrechner;
import java.time.LocalDate;
import java.time.LocalTime;

public class Timestamp {
    private int hour;
    private int min;
    private int sek;
    private Person person;
    private LocalDate date;
    public Timestamp(Person person, LocalDate date , int sek){
        this.person=person;
        this.sek=sek;
        this.date=date;
    }

    public void makeTimestamp() {
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        this.setHour(localTime.getHour());
        this.setMin(localTime.getMinute());
        this.setSek(localTime.getSecond());
    }
    public void printTime() {
        System.out.println("Stunde: " + this.getHour());
        System.out.println("Minute: " + this.getMin());
        System.out.println("Sekunde: " + this.getSek());
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
        time.printTime();
    }
}
