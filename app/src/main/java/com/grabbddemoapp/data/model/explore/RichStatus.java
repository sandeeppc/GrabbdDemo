
package com.grabbddemoapp.data.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RichStatus {

    @SerializedName("entities")
    @Expose
    private List<Object> entities = null;
    @SerializedName("text")
    @Expose
    private String text;

    public List<Object> getEntities() {
        return entities;
    }

    public void setEntities(List<Object> entities) {
        this.entities = entities;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
