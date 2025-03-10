package lk.nd.cycler.model;

import java.io.Serializable;

public class PaymentHistoryCard implements Serializable {
    private String shopName;
    private int cycleCount;
    private int total;

    public PaymentHistoryCard(String shopName, int cycleCount, int total) {
        this.shopName = shopName;
        this.cycleCount = cycleCount;
        this.total = total;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
