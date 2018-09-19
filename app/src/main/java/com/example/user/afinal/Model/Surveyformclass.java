package com.example.user.afinal.Model;

/**
 * Created by RAMMURALI on 5/2/2017.
 */

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Rammurali on 4/28/2017.
 */
public class Surveyformclass {
    String formid;
    String formtitle;
    String formtype;
    JSONObject jsonobj;
    JSONArray formrulearray;

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getFormtitle() {
        return formtitle;
    }

    public void setFormtitle(String formtitle) {
        this.formtitle = formtitle;
    }

    public JSONObject getJsonobj() {
        return jsonobj;
    }

    public void setJsonobj(JSONObject jsonobj) {
        this.jsonobj = jsonobj;
    }

    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    public JSONArray getFormrulearray() {
        return formrulearray;
    }

    public void setFormrulearray(JSONArray formrulearray) {
        this.formrulearray = formrulearray;
    }

    public Surveyformclass(String formid, String formtitle, JSONObject jsonobj, String formtype, JSONArray formrulearray)
    {
        this.formid = formid;
        this.formtitle = formtitle;
        this.jsonobj = jsonobj;
        this.formtype = formtype;
        this.formrulearray = formrulearray;
    }
}
