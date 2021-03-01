package com.app.development.stackoverflowapi.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionList {

    @SerializedName("items")
    private List<EachQuestion> items;

    @SerializedName("has_more")
    private boolean  has_more;

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public QuestionList(List<EachQuestion> items, boolean has_more) {
        this.items = items;
        this.has_more=has_more;
    }

    public List<EachQuestion> getItems() {
        return items;
    }

    public void setItems(List<EachQuestion> items) {
        this.items = items;
    }
}
