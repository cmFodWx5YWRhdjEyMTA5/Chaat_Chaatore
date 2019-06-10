package com.tamilgk3000.quizexams.database;

/**
 * Created by G TECH on 4/24/2018.
 */

public class ItemListRadio {


    public String Id;
    public String Radio_name;
    public String Radio_url;
    public String Radio_img_url;

     public ItemListRadio(String id, String name, String url, String image) {
        this.Id = id;
        this.Radio_name = name;
        this.Radio_url = url;
        this.Radio_img_url = image;
    }

    public ItemListRadio() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRadio_name() {
        return Radio_name;
    }

    public void setRadio_name(String radio_name) {
        Radio_name = radio_name;
    }

    public String getRadio_url() {
        return Radio_url;
    }

    public void setRadio_url(String radio_url) {
        Radio_url = radio_url;
    }

    public String getRadio_img_url() {
        return Radio_img_url;
    }

    public void setRadio_img_url(String radio_img_url) {
        Radio_img_url = radio_img_url;
    }

}
