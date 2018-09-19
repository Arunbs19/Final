package com.example.user.afinal.Model;

/**
 * Created by USER on 1/11/2018.
 */

public class BaseMessage {

    String id;
    String name;
    String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BaseMessage(String id, String name, String time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }
}
