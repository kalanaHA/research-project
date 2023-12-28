package com.eshan.healthapp.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class EmotionDetectionResponse {

    @SerializedName("class")
    private int classValue;

    @SerializedName("emotion")
    private String emotion;

    @SerializedName("music_link")
    private String musicLink;

    public EmotionDetectionResponse() {

    }

    public EmotionDetectionResponse(int classValue, String emotion, String musicLink) {
        this.classValue = classValue;
        this.emotion = emotion;
        this.musicLink = musicLink;
    }

    public int getClassValue() {
        return classValue;
    }

    public String getEmotion() {
        return emotion;
    }

    public String getMusicLink() {
        return musicLink;
    }
}
