package com.example.medessentials;

public class RequestData {

    public String productName;
    public String type;
    public String quantity;
    public String description;
    public String latitude;
    public String longitude;
    public String user;

    public RequestData(String user, String pN, String t, String quant, String descrip, String lat, String longi) {
        this.user = user;
        productName = pN;
        type = t;
        quantity = quant;
        description = descrip;
        latitude = lat;
        longitude = longi;
    }
}
