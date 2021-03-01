package com.app.development.stackoverflowapi.Model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class EachQuestion {

    @SerializedName("title")
    private String title;

    @SerializedName("link")
    private String link;

    @SerializedName("is_answered")
    private String  isAnswered;

    @SerializedName("owner")
    private HashMap<String, String> owner;

    @SerializedName("score")
    private String score;

    @SerializedName("creation_date")
    private String creation_date;

    @SerializedName("answer_count")
    private  String answer_count;

    @SerializedName("tags")
    private List<String> tags;

    public HashMap<String, String> getOwner() {
        return owner;
    }

    public void setOwner(HashMap<String, String> owner) {
        this.owner = owner;
    }


    public EachQuestion(String title, String link, String  isAnswered, HashMap<String,String> owner,String score, String date, String answer_count, List<String> tags) {
        this.title = title;
        this.link = link;
        this.isAnswered = isAnswered;
        this.owner=owner;
        this.score=score;
        this.creation_date = date;
        this.answer_count=answer_count;
        this.tags=tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String isAnswered() {
        return isAnswered;
    }

    public void setAnswered(String  answered) {
        isAnswered = answered;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(String answer_count) {
        this.answer_count = answer_count;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
