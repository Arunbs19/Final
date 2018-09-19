package com.example.user.afinal.Model;

/**
 * Created by VENKATESAN on 3/30/2017.
 */
public class DropDownData {
    private String id;
    private String name;
    private String questionid;

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

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public DropDownData(String id, String name, String questionid){
        this.id =id;
        this.name =name;
        this.questionid = questionid;
    }
}
