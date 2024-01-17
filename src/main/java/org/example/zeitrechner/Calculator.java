package org.example.zeitrechner;

public class Calculator {
    private int hour;
    private int min;
    private int sek;

    public void printTime() {
        String output = new String();
        output = "Zeit: " + this.getHour() + ":" + this.getMin() + ":" + this.getSek();
        System.out.println(output);
    }
    /*
    Rechnet die Zeit in sekunden zu berechnne um.
    @autor Simon
     */
    public int timeToSek() {
        int sekHour = this.getHour() * 3600;
        int sekMin = this.getMin() * 60;
        int sekSek = this.getSek() + sekHour + sekMin;
        return sekSek;
    }
    /*
    Rechnet die gearbeitete Zeit aus mittels Subtraction.
    Bei Zeit über Mitternacht wird der Ausstemelzeit ein Tag zugerechnet
    um nich ins minus zu kommen.
    @autor Simon
     */
    public int calculateTime(int sekStampIn, int sekStampOut) {
        if (sekStampIn > sekStampOut) {
            sekStampOut += 86400;
        }
        int stampDiff = sekStampOut - sekStampIn;
        return stampDiff;
    }

    /*
    Rechnet anzahl Sekunden in leserliche Zeit um.
    @autor Simon
     */
    public void sekToTime(int sekTime) {
        int hoursTime;
        int minTime;
        hoursTime = sekTime / 3600;
        sekTime -= 3600 * hoursTime;
        minTime = sekTime / 60;
        sekTime -= 60 * minTime;
        System.out.println("Zeit gearbeitet: " + hoursTime + ":" + minTime + ":" + sekTime);
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
}
