package lk.nd.cycler.model;

import java.io.Serializable;

public class LocationCard implements Serializable {

    private int id;
    private String shopName;
    private String location;
    private String rating;
    private int bicycleCount;
    private String imageUrl;

    public LocationCard(int id, String shopName, String location, String rating, int bicycleCount, String imageUrl) {
        this.id = id;
        this.shopName = shopName;
        this.location = location;
        this.rating = rating;
        this.bicycleCount = bicycleCount;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getBicycleCount() {
        return bicycleCount;
    }

    public void setBicycleCount(int bicycleCount) {
        this.bicycleCount = bicycleCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
