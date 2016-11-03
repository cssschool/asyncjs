package ch.css.workshop.asyncjs.data;

import javaslang.Lazy;
import javaslang.collection.List;
import javaslang.concurrent.Future;

public class CitiesService {

    private final CitiesDataReader reader;

    private final Lazy<Future<List<CityData>>> allCities;

    public CitiesService(final String fileName) {
        this.reader = new CitiesDataReader(fileName);
        this.allCities = Lazy.of(()->reader.getCities());
    }

    public Future<List<CityData>> searchCities(final String search) {
     return allCities.map( futur-> futur.map( list -> list.filter( city->city.city.contains(search)))).get();
    }
}