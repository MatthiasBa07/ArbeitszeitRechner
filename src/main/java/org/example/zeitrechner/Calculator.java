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
    public int timeToSek() {
        int sekHour = this.getHour() * 3600;
        int sekMin = this.getMin() * 60;
        int sekSek = this.getSek() + sekHour + sekMin;
        return sekSek;
    }
    public int calculateTime(int sekStampOut) {
        int sekStampIn = this.timeToSek();
        if (sekStampIn > sekStampOut) {
            sekStampOut += 86400;
        }
        int stampDiff = sekStampOut - sekStampIn;
        return stampDiff;
    }
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
    public static void main(String[] args) {
        Calculator timeIn = new Calculator();
        timeIn.setHour(15);
        timeIn.setMin(33);
        timeIn.setSek(0);
        timeIn.printTime();
        Calculator timeOut = new Calculator();
        timeOut.setHour(2);
        timeOut.setMin(25);
        timeOut.setSek(0);
        timeOut.printTime();
        timeIn.sekToTime(timeIn.calculateTime(timeOut.timeToSek()));
    }
}
