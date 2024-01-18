package org.example.zeitrechner;

/**
 * Diese Klasse stellt eine Person dar, deren Zeit gemanagt wird.
 * @author Matthias Baumgartner
 */
public class Person {
    private int id;
    private String vorname;
    private String nachname;

    public Person(int id, String vorname, String nachname){
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public boolean equals(Person person) {
        if (this.id == person.id) return true;
        else return false;
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

    public String getFullName() {
        return vorname + " " + nachname;
    }
}
