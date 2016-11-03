package ch.css.workshop.asyncjs;

import javaslang.concurrent.Future;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CFConverter {

    public static <T>CompletionStage<T> toCompletable(Future<T> future) {
        final CompletableFuture<T> result = new CompletableFuture<>();
        future.onSuccess( (value) -> result.complete(value));
        return result;
    }
}
