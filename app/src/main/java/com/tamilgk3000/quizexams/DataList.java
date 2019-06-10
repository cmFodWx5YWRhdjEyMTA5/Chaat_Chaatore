package com.tamilgk3000.quizexams;

import java.io.Serializable;

public class DataList implements Serializable {

    private String sno;

    private String audio_name;
    private String audio_link;
    public void DataList() {

    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getAudio_name() {
        return audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }

    public String getAudio_link() {
        return audio_link;
    }

    public void setAudio_link(String audio_link) {
        this.audio_link = audio_link;
    }
}