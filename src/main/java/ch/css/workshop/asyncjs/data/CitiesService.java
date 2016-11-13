package ch.css.workshop.asyncjs.data;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.HashMap;
import javaslang.concurrent.Future;
import javaslang.control.Option;



public class CitiesService {

    private final CitiesDataReader reader;

    private final Lazy<Future<List<CityData>>> allCities;

    private final Lazy<Future<Map<Long,CityData>>> citiesByKey;

    public CitiesService(final String fileName) {
        this.reader = new CitiesDataReader(fileName);
        this.allCities = Lazy.of(()->reader.getCities());
        this.citiesByKey = Lazy.of ( () -> allCities.get().map(list->
            HashMap.ofEntries( list.zipWithIndex().map(tuple -> Tuple.of(tuple._2, tuple._1)))
        ));
    }

    private List<Tuple2<Long,CityData>> toList(Map<Long,CityData> cities) {
        return List.ofAll(cities.iterator());
    }

    public Future<List<Tuple2<Long, CityData>>> searchCities(final String search) {
     return citiesByKey.map(
             futur-> futur.map(
                     map->toList(map.filter(tuple -> tuple._2.city.contains(search)) ))).get();
    }

    public Future<Option<CityData>> getCity(final long cityId) {
        return citiesByKey.map( futur-> futur.map( m->{
            final long startTime = System.currentTimeMillis();
            Option<CityData> result = m.get(cityId);
            final long endTime = System.currentTimeMillis();

            return result;
        })).get();
    }

}
