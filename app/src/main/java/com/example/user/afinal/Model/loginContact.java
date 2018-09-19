package com.example.user.afinal.Model;

/**
 * Created by USER on 11/25/2017.
 */

public class loginContact {
    String type;
    String username;
    byte[] _img;



    public loginContact( String type, String username, byte[] img){
        super();

        this.type = type;
        this.username = username;
        this._img = img;

    }  public loginContact(){
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getImage(){
        return _img;
    }
    public void setImage(byte[] b){
        this._img=b;
    }

}
