package lk.nd.cycler.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class LocationCard implements Parcelable {
    private int id;
    private String shopName;
    private String location;
    private String rating;
    private int bicycleCount;
    private String imageUrl;
    private LatLng latLng;
    private int pricePerHour;

    public LocationCard(int id, String shopName, String location, String rating, int bicycleCount, String imageUrl, LatLng latLng, int pricePerHour) {
        this.id = id;
        this.shopName = shopName;
        this.location = location;
        this.rating = rating;
        this.bicycleCount = bicycleCount;
        this.imageUrl = imageUrl;
        this.latLng = latLng;
        this.pricePerHour = pricePerHour;
    }

    protected LocationCard(Parcel in) {
        id = in.readInt();
        shopName = in.readString();
        location = in.readString();
        rating = in.readString();
        bicycleCount = in.readInt();
        imageUrl = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shopName);
        dest.writeString(location);
        dest.writeString(rating);
        dest.writeInt(bicycleCount);
        dest.writeString(imageUrl);
        dest.writeParcelable(latLng, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationCard> CREATOR = new Creator<LocationCard>() {
        @Override
        public LocationCard createFromParcel(Parcel in) {
            return new LocationCard(in);
        }

        @Override
        public LocationCard[] newArray(int size) {
            return new LocationCard[size];
        }
    };

    // Getters
    public int getId() { return id; }
    public String getShopName() { return shopName; }
    public String getLocation() { return location; }
    public String getRating() { return rating; }
    public int getBicycleCount() { return bicycleCount; }
    public String getImageUrl() { return imageUrl; }
    public LatLng getLatLng() { return latLng; }

    public int getPricePerHour() {
        return pricePerHour;
    }
}
