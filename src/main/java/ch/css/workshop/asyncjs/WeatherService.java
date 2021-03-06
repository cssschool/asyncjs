package ch.css.workshop.asyncjs;

import ch.css.workshop.asyncjs.data.CitiesService;
import ch.css.workshop.asyncjs.data.CityData;
import pl.setblack.badass.Politician;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.*;

public class WeatherService {

    private final CitiesService citiesService;

    public WeatherService(CitiesService citiesService) {
        this.citiesService = citiesService;
    }



    private BigDecimal getTemperatureForCity(final CityData city) {
        final double distToEq = Math.abs(90 - city.latitude) / 90.0;
        final double distSQ = Math.pow(distToEq, 0.3);
        final double temp = distSQ*60.0-30.0;
        return new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP);
    }

    public CompletionStage<BigDecimal> getTemperature(Long cityId, LocalDate date) {
        final CompletableFuture<BigDecimal> result = new CompletableFuture<>();

        citiesService.getCity(cityId).map((option)-> option.map(this::getTemperatureForCity)).onComplete( distSearch -> {
            distSearch.onSuccess( tempOption ->  result.complete(tempOption.getOrElseThrow(()->new IllegalArgumentException
               (""+cityId))));
        });

        return result;
    }



}
