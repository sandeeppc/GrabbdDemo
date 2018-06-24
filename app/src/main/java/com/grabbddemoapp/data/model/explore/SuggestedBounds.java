
package com.grabbddemoapp.data.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuggestedBounds {

    @SerializedName("ne")
    @Expose
    private Ne_ ne;
    @SerializedName("sw")
    @Expose
    private Sw_ sw;

    public Ne_ getNe() {
        return ne;
    }

    public void setNe(Ne_ ne) {
        this.ne = ne;
    }

    public Sw_ getSw() {
        return sw;
    }

    public void setSw(Sw_ sw) {
        this.sw = sw;
    }

}
