package ch.css.workshop.asyncjs;

import ch.css.workshop.asyncjs.data.CitiesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javaslang.jackson.datatype.JavaslangModule;
import pl.setblack.badass.Politician;
import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.registry.RegistrySpec;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletionStage;

public class WeatherServer {

    private final CitiesService cityService = new CitiesService("/data/worldcitiespop.txt");

    private final SlowWeatherService weatherService = new SlowWeatherService(cityService);

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
        return c.baseDir(baseDir);
    }

    private void registerJavaSlangMapper( RegistrySpec r) {
        r.add(new ObjectMapper().registerModule(new JavaslangModule()));
    }

    private void defineServices(Chain services) {
        services.get("temperature/:id", ctx -> {
            final Long cityId = Long.parseLong(ctx.getPathTokens().get("id"));
            System.out.println("getting temp for:" + cityId);
            final CompletionStage<BigDecimal> result = weatherService.getTemperature(cityId,
                    LocalDate.now
                            ());
            final CompletionStage<String> stringRes = result.thenApply(dec -> dec.toPlainString());
            final Promise promise = Promise.async(downstream -> {
                downstream.accept(stringRes);
            });
            ctx.render(promise);
        })
                .get("transport/:id", ctx -> {
                    ctx.render("200");
                })
                .get("cities/:search", ctx -> {
                    final String search = ctx.getPathTokens().get("search");
                    final CompletionStage<String> result = CFConverter
                            .toCompletable(cityService.searchCities(search))
                            .thenApply(list ->
                                    Politician.beatAroundTheBush(() -> ctx.get(ObjectMapper.class).writer().writeValueAsString(list)));
                    final Promise promise = Promise.async(downstream -> {
                        downstream.accept(result);
                    });
                    ctx.render(promise);
                });
    }

}
