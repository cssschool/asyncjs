package ch.css.workshop.asyncjs;

import ch.css.workshop.asyncjs.data.CitiesService;
import ch.css.workshop.asyncjs.data.CityData;
import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.collection.List;
import javaslang.jackson.datatype.JavaslangModule;
import pl.setblack.badass.Politician;
import ratpack.exec.Promise;
import ratpack.handling.Chain;
import ratpack.server.RatpackServer;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

public class WeatherServer {

    private final SlowWeatherService weatherService = new SlowWeatherService();

    private final CitiesService cityService = new CitiesService("/data/worldcitiespop.txt");

    public static void main(String... args) throws Exception {
        final WeatherServer server = new WeatherServer();
        server.start();
    }

    private void start() throws Exception {
        final File  baseDir = new File("src/main/webapp").getAbsoluteFile();
         System.out.println("base dir=" + baseDir);
        RatpackServer.start(server -> server
                .serverConfig(c ->c.baseDir(baseDir))
                .registryOf(r->r.add( new ObjectMapper().registerModule(new JavaslangModule())))
                .handlers(chain -> chain
                        .prefix("files", stat -> stat.files(f->f.dir("/").indexFiles("index.html"))
                        )
                        .prefix ("services", services -> {
                            defineServices(services);
                        })

                )
        );
    }

    private void defineServices(Chain services) {
        services.get("temperature/:name", ctx -> {
            final String city = ctx.getPathTokens().get("name");
            System.out.println("getting temp for:"+ city);
            final CompletionStage<BigDecimal> result = weatherService.getTemperature(city,
                    LocalDate.now
                            ());
            final CompletionStage<String> stringRes = result.thenApply(dec ->dec.toPlainString());
            final Promise promise = Promise.async(downstream -> {
                downstream.accept(stringRes);
            });
            ctx.render(promise);
        })
                .get("transport/:name", ctx -> {
                    ctx.render("200");
                })
                .get("cities/:search", ctx ->  {
                    final String search = ctx.getPathTokens().get("search");
                    final CompletionStage<String> result=  CFConverter
                            .toCompletable(cityService.searchCities(search))
                            .thenApply(list->
                                    Politician.beatAroundTheBush( ()->ctx.get(ObjectMapper.class).writer().writeValueAsString(list)));
                    final Promise promise = Promise.async(downstream -> {

                        downstream.accept(result);
                    });
                    ctx.render(promise);
                });
    }

}
