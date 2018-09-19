package com.example.user.afinal.Model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Rammurali on 4/28/2017.
 */
public class DisplayorderClass {
    String formid;
    String formtitle;
    String formtype;
    JSONObject jsonobj;
    JSONArray jarrayformrule;

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

    public JSONArray getJarrayformrule() {
        return jarrayformrule;
    }

    public void setJarrayformrule(JSONArray jarrayformrule) {
        this.jarrayformrule = jarrayformrule;
    }

    public DisplayorderClass(String formid, String formtitle, JSONObject jsonobj, String formtype, JSONArray jarrayformrule)
    {
        this.formid = formid;
        this.formtitle = formtitle;
        this.jsonobj = jsonobj;
        this.formtype = formtype;
        this.jarrayformrule = jarrayformrule;
    }
}
