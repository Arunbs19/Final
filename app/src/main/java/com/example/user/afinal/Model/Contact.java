package com.example.user.afinal.Model;

/**
 * Created by USER on 11/25/2017.
 */

public class Contact {
    int _id;
    String _name;
    String _age;
    String _address;
    byte[] _img;



    public Contact(int id, String name,String age,String address, byte[] img){
        super();
        this._id = id;
        this._name = name;
        this._age = age;
        this._address =address;
        this._img = img;

    }  public Contact(){
        super();
    }
    public int getID(){
        return _id;
    }
    public void setID(int id){
        this._id = id;
    }
    public String getName(){
        return _name;
    }
    public void setName(String name){
        this._name = name;
    }
    public  String getAge(){ return  _age; }
    public void setAge(String age) { this._age=age; }

    public  String getAddress() { return  _address; }
    public void setAddress(String address){ this._address = address; }


    public byte[] getImage(){
        return _img;
    }
    public void setImage(byte[] b){
        this._img=b;
    }

}
