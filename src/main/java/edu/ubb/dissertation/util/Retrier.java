package edu.ubb.dissertation.util;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import net.jodah.failsafe.function.CheckedSupplier;

import java.time.Duration;

public final class Retrier {

    private Retrier() {
    }

    public static <T> T retry(final CheckedSupplier<T> supplier, final int maxRetries, final Class<? extends Throwable> exceptionClass) {
        final RetryPolicy<Object> policy = new RetryPolicy<>()
                .handle(exceptionClass)
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(maxRetries);
        return Failsafe.with(policy)
                .get(supplier);
    }

    public static <T> T retry(final CheckedSupplier<T> supplier, final Class<? extends Throwable> exceptionClass) {
        return retry(supplier, 3, exceptionClass);
    }
}
