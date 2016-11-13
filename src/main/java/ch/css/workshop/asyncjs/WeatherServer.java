package ch.css.workshop.asyncjs;

import ch.css.workshop.asyncjs.data.CitiesService;
import ch.css.workshop.asyncjs.data.CityData;
import com.fasterxml.jackson.databind.ObjectMapper;


import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.jackson.datatype.JavaslangModule;
import pl.setblack.badass.Politician;
import ratpack.exec.Promise;

import ratpack.handling.Chain;
import ratpack.handling.Context;
import ratpack.registry.RegistrySpec;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class WeatherServer {

   private final CitiesService cityService = new CitiesService("/data/worldcitiespop.txt");

   private final SlowWeatherService weatherService = new SlowWeatherService(cityService);

   private final TravelService travelService = new TravelService(cityService);

   private final Destabilizer destabilizer = new Destabilizer(100, 0.05, 0.01);



   public static void main(String... args) throws Exception {
      final WeatherServer server = new WeatherServer();
      server.start();
   }


   private void start() throws Exception {

      RatpackServer.start(server -> server
         .serverConfig(this::setBaseDirectory)
         .registryOf(this::registerJavaSlangMapper)
         .handlers(chain -> chain
            .prefix("files", stat -> stat.files(f -> f.dir("/").indexFiles("index.html"))
            )
            .prefix("services", services -> {
               defineServices(services);
            })

         )
      );
   }

   private ServerConfigBuilder setBaseDirectory(ServerConfigBuilder c) {
      final File baseDir = new File("src/main/webapp").getAbsoluteFile();

      return c.baseDir(baseDir)
         .threads(1000)
              .development(false);
   }

   private void registerJavaSlangMapper(RegistrySpec r) {
      r.add(new ObjectMapper().registerModule(new JavaslangModule()));
   }

   private void defineServices(Chain services) {
      services.get("temperature/:id", ctx -> {
         final Long cityId = Long.parseLong(ctx.getPathTokens().get("id"));
         final CompletionStage<String> result = this.slowWeatherService.apply(cityId);
         renderFuture(ctx, result);

      })
         .get("transport/:id", ctx -> {
            final Long cityId = Long.parseLong(ctx.getPathTokens().get("id"));
            final CompletionStage<String> result = this.slowTravelService.apply(cityId);
            renderFuture(ctx, result);

         })
         .get("cities/:search", ctx -> {
            final String search = ctx.getPathTokens().get("search");
            final CompletionStage<String> result = this.slowCityService.apply(search).thenApply(getMapper(ctx));
            renderFuture(ctx, result);
         });
   }

   private void renderFuture(Context ctx, CompletionStage<String> result) {
      final Promise promise = Promise.async(downstream -> {
         downstream.accept(result);
      });
      ctx.render(promise);
   }

   private Function<Object, String> getMapper(final Context ctx) {
      return obj -> Politician.beatAroundTheBush(() -> ctx.get(ObjectMapper.class).writer().writeValueAsString(obj));
   }

   private final Function<String, CompletionStage<List<Tuple2<Long, CityData>>>> slowCityService = destabilizer
      .makeSlowAndUnstable(
         (String search) -> CFConverter
            .toCompletable(cityService.searchCities(search)));

   private final Function<Long, CompletionStage<String>> slowWeatherService = destabilizer
      .makeSlowAndUnstable(
         (Long cityId) -> weatherService.getTemperature(cityId, LocalDate.now()).thenApply(BigDecimal::toPlainString));

   private final Function<Long, CompletionStage<String>> slowTravelService = destabilizer
      .makeSlowAndUnstable(
         (Long cityId) -> travelService.getTravelCost(cityId, LocalDate.now()).thenApply(BigDecimal::toPlainString));


}
