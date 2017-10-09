package tech.babashnik.castit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("duration")
    @Expose
    public String duration;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("key_scale")
    @Expose
    public String keyScale;
    @SerializedName("bpm")
    @Expose
    public String bpm;

}