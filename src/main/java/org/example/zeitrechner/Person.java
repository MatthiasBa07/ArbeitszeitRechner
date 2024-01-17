package org.example.zeitrechner;

public class Person {
    private int id;
    private String vorname;
    private String nachname;

    public Person(int id, String vorname, String nachname){
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public int getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getFUllName() {
        return vorname + " " + nachname;
    }
}
