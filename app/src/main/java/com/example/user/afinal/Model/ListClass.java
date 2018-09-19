package com.example.user.afinal.Model;

import java.util.ArrayList;

/**
 * Created by RAMMURALI on 2/10/2017.
 */
public class ListClass {
    String uniqueid;
    String question;
    String questionid;
    ArrayList<SimpleListClass> questionlist;

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<SimpleListClass> getQuestionlist() {
        return questionlist;
    }

    public void setQuestionlist(ArrayList<SimpleListClass> questionlist) {
        this.questionlist = questionlist;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public ListClass(String questionid, String question, ArrayList<SimpleListClass> questionlist, String uniqueid)
    {
     this.question = question;
     this.questionlist = questionlist;
        this.questionid = questionid;
        this.uniqueid = uniqueid;
    }
}
