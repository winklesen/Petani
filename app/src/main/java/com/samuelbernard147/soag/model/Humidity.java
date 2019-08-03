package com.samuelbernard147.soag.model;

import org.json.JSONArray;

public class Humidity {
    private int id;
    private String time;
    private int humidity;

    public Humidity(int id, int humidity, String time) {
        this.id = id;
        this.time = time;
        this.humidity = humidity;
    }

    public Humidity(JSONArray object, int posisi) {
        try {
            int id = object.getJSONObject(posisi).getInt("id");
            int kelembapan = Integer.valueOf(object.getJSONObject(posisi).getString("kelembapan"));
            String waktu = object.getJSONObject(posisi).getString("created_at");

            this.id = id;
            this.time = waktu;
            this.humidity = kelembapan;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}