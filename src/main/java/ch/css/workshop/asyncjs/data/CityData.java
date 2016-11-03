package ch.css.workshop.asyncjs.data;

public class CityData {
    public final String country;
    public final String city;
    public final String cityAccent;
    public final float latitude;
    public final float longitude;

    public CityData(String country, String city, String cityAccent, float latitude, float longitude) {
        this.country = country;
        this.city = city;
        this.cityAccent = cityAccent;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static CityData fromStrings(String[] cols) {
        return new CityData(cols[0], cols[1], cols[2], Float.parseFloat(cols[5]),Float.parseFloat(cols[6]) );
    }
}
