package tech.babashnik.castit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GE72 on 09.10.2017.
 */

public class Responce {


    @SerializedName("track")
    @Expose
    public List<Track> track = null;
    @SerializedName("type")
    @Expose
    public String type;

}
