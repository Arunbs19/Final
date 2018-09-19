package com.example.user.afinal.Model;

/**
 * Created by RAMMURALI on 2/10/2017.
 */
public class SimpleListClass {
    String question;
    String questionid;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public SimpleListClass(String questionid, String question)
    {
        this.question = question;
        this.questionid = questionid;
    }
}
