
package com.grabbddemoapp.data.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestedFilters {

    @SerializedName("header")
    @Expose
    private String header;
    @SerializedName("filters")
    @Expose
    private List<Filter> filters = null;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

}
