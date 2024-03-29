package org.example.zeitrechner;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

public class Calculator {

    private static final Calculator calculator = new Calculator();

    public static Calculator getInstance() {
        return calculator;
    }

    public String addZero(int zeit) {
        String result;
        if (zeit < 10 && zeit >= 0) result = "0" + zeit;
        else if (zeit > 10) result = Integer.toString(zeit);
        else if (zeit < 0 && zeit > -10) result = Integer.toString(zeit);
        else result = Integer.toString(zeit);
        return result;
    }

    public void printTime(int s) {
        int[] zeit = Calculator.getInstance().sekToTime(s);
        String[] result = new String[]{Calculator.getInstance().addZero(zeit[0]), Calculator.getInstance().addZero(zeit[1]), Calculator.getInstance().addZero(zeit[2])};
        System.out.println("Zeit: " + result[0] + ":" + result[1] + ":" + result[2]);
    }

    /*
    Rechnet die Zeit in Sekunden um.
    @autor Simon
     */
    public int timeToSek(int[] zeit) {
        if (zeit.length == 3) {
            int sekHour = zeit[0] * 3600;
            int sekMin = zeit[1] * 60;
            return zeit[2] + sekHour + sekMin;
        } else return 0;
    }

    /*
    Rechnet die gearbeitete Zeit aus mittels Subtraktion.
    Bei Zeit über Mitternacht wird der ausstempel-Zeit ein Tag zugerechnet,
    damit das Resultat stimmt.
    @autor Simon
     */
    public int calculateTime(Timestamp einStamp, Timestamp ausStamp) {
        if (einStamp.getPerson().equals(ausStamp.getPerson())) {
            int sekStampIn = einStamp.getSek();
            int sekStampOut = ausStamp.getSek();

            if (sekStampIn > sekStampOut) {
                sekStampOut += 86400;
            }
            return sekStampOut - sekStampIn;
        } else return 0;
    }

    /*
    Rechnet die Insgesamt gearbeitete Zeit der angegebenen Timestamps aus mittels Subtraktion.
    Bei Zeit über Mitternacht wird der ausstempel-Zeit ein Tag zugerechnet,
    damit das Resultat stimmt.
    @autor Simon
     */
    public int calculateEntireTime(ArrayList<Timestamp> array) {
        if (!array.isEmpty() && array.size() % 2 == 0) {
            boolean gud = true;
            for (int i = 0; i < array.size() - 1; i++) {
                if (!(array.getFirst().getPerson().equals(array.get(i + 1).getPerson()))) {
                    gud = false;
                }
            }
            if (gud) {
                int eins = 0;
                int zwei = 0;
                int insg = 0;
                for (int i = 0; i <= array.size() / 2; i = i + 2) {
                    eins = array.get(i).getSek();
                    zwei = array.get(i + 1).getSek();
                    if (eins > zwei) {
                        zwei += 86400;
                    }
                    insg = insg + (zwei - eins);
                }
                return insg;
            } else return 0;
        } else return 0;




        /*    sekStampOut += 86400;
        }
        return sekStampOut - sekStampIn;*/
    }

    /*
    Rechnet die Anzahl Sekunden in leserliche Zeit um.
    @autor Simon
     */
    public int[] sekToTime(int sekTime) {
        int hoursTime;
        int minTime;
        hoursTime = sekTime / 3600;
        sekTime -= 3600 * hoursTime;
        minTime = sekTime / 60;
        sekTime -= 60 * minTime;
        return new int[]{hoursTime, minTime, sekTime};
    }

    /*
    Rechnet die Überstunden oder Minusstunden aus.
    @autor Simon
    */

    public int calculateOverTime(Person person) {
        ArrayList<Timestamp>  timestamps;
        try {
            timestamps = TimestampJDBCDao.getInstance().getTimestampByPerson(person);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (timestamps.size() % 2 != 0) {
            return -999999;
        }else {
            ArrayList<LocalDate> dates = new ArrayList<>();

            for (Timestamp timestamp : timestamps) {
                dates.add(timestamp.getDate());
            }
            Set<LocalDate> newDate = new LinkedHashSet<>(dates);
            dates.clear();
            dates.addAll(newDate);

            int worktime = 30240 * dates.size();
            int worked = Calculator.getInstance().calculateEntireTime(timestamps);
            return worked-worktime;
        }
    }
}
