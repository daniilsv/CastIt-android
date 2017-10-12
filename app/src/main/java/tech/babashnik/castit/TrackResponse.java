package tech.babashnik.castit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


class TrackResponse {


    @SerializedName("track")
    @Expose
    public List<Track> track = null;
    @SerializedName("type")
    @Expose
    public String type;

}
