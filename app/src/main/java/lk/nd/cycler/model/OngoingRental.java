package lk.nd.cycler.model;

import java.io.Serializable;
import java.util.Date;

public class OngoingRental implements Serializable {
    private int id;
    private int locationId;
    private String shopName;
    private int rentedCycleCount;
    private Date rentedDateTime;
    private String status;
    private int paymentPerHour;

    public OngoingRental(int id, int locationId, String shopName, int rentedCycleCount, Date rentedDateTime, String status, int paymentPerHour) {
        this.id = id;
        this.locationId = locationId;
        this.shopName = shopName;
        this.rentedCycleCount = rentedCycleCount;
        this.rentedDateTime = rentedDateTime;
        this.status = status;
        this.paymentPerHour = paymentPerHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getRentedCycleCount() {
        return rentedCycleCount;
    }

    public void setRentedCycleCount(int rentedCycleCount) {
        this.rentedCycleCount = rentedCycleCount;
    }

    public Date getRentedDateTime() {
        return rentedDateTime;
    }

    public void setRentedDateTime(Date rentedDateTime) {
        this.rentedDateTime = rentedDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPaymentPerHour() {
        return paymentPerHour;
    }

    public void setPaymentPerHour(int paymentPerHour) {
        this.paymentPerHour = paymentPerHour;
    }

    public long getTimeSpentInHours(){
        long diffInMillis = new Date().getTime() - rentedDateTime.getTime();
        return diffInMillis / (1000 * 60 * 60);
    }

    public long getTimeSpentInMinutes(){
        long diffInMillis = new Date().getTime() - rentedDateTime.getTime();
        return diffInMillis / (1000 * 60);
    }

}
