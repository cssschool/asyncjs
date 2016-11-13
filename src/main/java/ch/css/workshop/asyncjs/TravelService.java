package ch.css.workshop.asyncjs;

import ch.css.workshop.asyncjs.data.CitiesService;
import ch.css.workshop.asyncjs.data.CityData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class TravelService {

   private final CitiesService citiesService;

   private final CityData referenceCity = new CityData("ch", "luzern", "Luzern",
      47.08333f, 8.266667f
      );

   public TravelService(CitiesService citiesService) {
      this.citiesService = citiesService;
   }


   private BigDecimal getTravelCost(final CityData data) {
      final float R = 6371e3f;
      final double psi1 = Math.toRadians(data.latitude);
      final double psi2 = Math.toRadians(referenceCity.latitude);
      final double deltaPsi  = Math.toRadians( referenceCity.latitude - data.latitude );
      final double deltaLambda  = Math.toRadians(referenceCity.longitude - data.longitude);
      final double a  = Math.sin(deltaPsi/2.0) * Math.sin(deltaPsi/2.0) +
                  Math.cos(psi1) * Math.cos(psi2) *
                   Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
      final double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      final double d = R*c;
     // System.out.println("Distance to " + data.cityAccent  +" in "+ data.country + " =>" + d );
      final double distInKm = d/1000.0;
      final double cost = distInKm * 0.1 + 20;

      return  new BigDecimal(cost).setScale(0,BigDecimal.ROUND_DOWN);

   }

   public CompletionStage<BigDecimal> getTravelCost(Long cityId, LocalDate date) {
      final CompletableFuture<BigDecimal> result = new CompletableFuture<>();
      citiesService.getCity(cityId).map((option)-> option.map(this::getTravelCost)).onComplete( distSearch -> {
         distSearch.onSuccess( distOption ->  result.complete(distOption.getOrElseThrow(()->new IllegalArgumentException
            (""+cityId))));
      });
      return result;

   }
}
