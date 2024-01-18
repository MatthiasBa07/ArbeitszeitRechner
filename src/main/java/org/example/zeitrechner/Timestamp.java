package org.example.zeitrechner;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ...
 * @author Simon Gauss
 */
public class Timestamp {
    private int sek;
    private Person person;
    private LocalDate date;
    public Timestamp(int id,Person person, LocalDate date, int sek){
        this.person=person;
        this.sek=sek;
        this.date=date;
    }
    /*
    Macht einen Zeitstempel der aktuellen Zeit und rechnet sie
    in sekunden um.
    @autor Simon
     */
    public void makeTimestamp() {
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        this.setSek(localTime.getSecond() + localTime.getMinute() * 60 + localTime.getHour() * 3600);
        System.out.println(this.getSek());
    }
    public int getSek() {
        return sek;
    }
    public void setSek(int sek) {
        this.sek = sek;
    }
}
