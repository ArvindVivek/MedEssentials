package com.example.medessentials;

public class offerData {

    public String companyName;
    public String productName;
    public String quant;
    public String descrip;
    public String email;
    public String latitude;
    public String longitude;


    public offerData(String companyName, String productName, String quant, String descrip, String email, String longitude, String latitude) {
        this.companyName = companyName;
        this.productName = productName;
        this.quant = quant;
        this.descrip = descrip;
        this.email = email;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
