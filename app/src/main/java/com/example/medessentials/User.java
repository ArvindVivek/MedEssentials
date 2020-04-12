package com.example.medessentials;

public class User {

    public String name;
    public String preference;

    public User(String name, String preference) {
        this.name = name;
        this.preference = preference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }
}
