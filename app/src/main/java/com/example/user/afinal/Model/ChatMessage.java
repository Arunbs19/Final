package com.example.user.afinal.Model;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Belal on 5/29/2016.
 */
public class ChatMessage {
    private String usersId;
    private String message;
    private String sentAt;
    private String name;
    private int imgstatus;
    RecyclerView.ViewHolder view;
    private boolean isselected = false;


    public ChatMessage(String usersId, String message, String sentAt, String name) {
        this.usersId = usersId;
        this.message = message;
        this.sentAt = sentAt;
        this.name = name;

    }


    public String getUsersId() {
        return usersId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getName() {
        return name;
    }

    public int getImgstatus() {
        return imgstatus;
    }

    public void setImgstatus(int imgstatus) {
        this.imgstatus = imgstatus;
    }

    public RecyclerView.ViewHolder getView() {
        return view;
    }

    public void setView(RecyclerView.ViewHolder view) {
        this.view = view;
    }


}
