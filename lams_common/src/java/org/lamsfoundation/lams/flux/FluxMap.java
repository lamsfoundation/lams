package org.lamsfoundation.lams.flux;

import org.apache.log4j.Logger;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Utility class for serving updates to shared Fluxes. It receives signals from a source Flux, probably a part of
 * SharedSink. For each requested key it creates a hot publisher Flux which fetches data for the key. Supports
 * time-based throttling. If the sink does not produce any matching signals for given timeout, the flux gets removed.
 *
 * @author Marcin Cieslak
 */
public class FluxMap<T, U> {
    private static Logger log = Logger.getLogger(FluxMap.class.getName());

    public static final int SHORT_THROTTLE = 5;
    public static final int STANDARD_THROTTLE = 10;
    public static final int LONG_THROTTLE = 30;
    public static final int STANDARD_TIMEOUT = 5 * 60;

    private final Map<T, Flux<U>> map;

    // only for logging purposes
    private final String name;
    private final Flux<T> source;
    private final BiPredicate<T, T> itemEqualsPredicate;
    private final Function<T, U> fetchFunction;
    // default timeout is null, i.e. never expire
    private final Integer timeoutSeconds;
    // default throttle time is null, i.e. no throttling
    private final Integer throttleSeconds;

    public FluxMap(String name, Flux<T> source, BiPredicate<T, T> itemEqualsPredicate, Function<T, U> fetchFunction,
	    Integer throttleSeconds, Integer timeoutSeconds) {
	this.name = name;
	this.source = source;
	this.itemEqualsPredicate = itemEqualsPredicate;
	this.fetchFunction = fetchFunction;
	this.throttleSeconds = throttleSeconds;
	this.timeoutSeconds = timeoutSeconds;
	this.map = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Get a hot publisher Flux for the given key.
     */
    public Flux<U> get(T key) {
	synchronized (map) {
	    // try to get existing Flux
	    Flux<U> flux = map.get(key);

	    if (flux == null) {
		// create a new Flux
		if (log.isDebugEnabled()) {
		    log.debug("Creating new flux for \"" + name + "\" with key " + key);
		}

		// filter out signals which do not match the key
		Flux<T> filteringFlux = source.filter(item -> itemEqualsPredicate.test(item, key));

		// do not emit more often than this amount of time
		if (throttleSeconds != null) {
		    filteringFlux = filteringFlux.sample(Duration.ofSeconds(throttleSeconds));
		}

		// Manually complete this flux if all subscribers are gone.
		// Using available factory methods it does not seem possible to have a cached and shared Flux.
		AtomicInteger subscriberCounter = new AtomicInteger();
		filteringFlux = filteringFlux.handle((item, sink) -> {
		    int counter = subscriberCounter.get();
		    if (counter <= 0) {
			if (log.isDebugEnabled()) {
			    log.debug("Completing and removing flux for \"" + name + "\" with key " + key);
			}
			sink.complete();
			map.remove(key);
			return;
		    }
		    sink.next(item);
		});

		// map items from sink to fetch function result
		flux = filteringFlux.map(item -> fetchFunction.apply(item))
			// push initial value to the Flux so data is available immediately after subscribing
			.startWith(fetchFunction.apply(key))

			// make sure the subsequent subscribers also have data immediately available
			.cache(1)

			// just some logging
			.doOnSubscribe(subscription -> {
			    int counter = subscriberCounter.incrementAndGet();
			    if (counter <= 0) {
				subscriberCounter.set(1);
				counter = 1;
			    }

			    if (log.isDebugEnabled()) {
				log.debug("Subscribed (" + counter + ") to flux \"" + name + "\" with key " + key);
			    }
			})

			// just some logging
			.doOnCancel(() -> {
			    int counter = subscriberCounter.decrementAndGet();

			    if (log.isDebugEnabled()) {
				log.debug("Cancelling (" + counter + ") subscription to flux for \"" + name
					+ "\" with key " + key);
			    }
			})

			// detach all subscribers when flux is complete
			.onTerminateDetach();

		// remove Flux when source Flux did not emit an accepted signal before given timeout
		if (timeoutSeconds != null) {
		    flux = flux.timeout(Duration.ofSeconds(timeoutSeconds));
		}

		// backpressure and error handling
		flux = flux.onBackpressureLatest().onErrorResume(throwable -> {
		    if (throwable instanceof TimeoutException) {
			if (log.isDebugEnabled()) {
			    log.debug("Removing timed out flux for \"" + name + "\" with key " + key);
			}
		    } else {
			log.error("Error while processing flux for \"" + name + "\" with key " + key, throwable);
		    }

		    // remove terminated Flux from the map
		    map.remove(key);
		    // switch subscribers to a dummy Flux which completes and cancels their subscriptions
		    return Flux.empty();
		});

		map.put(key, flux);
	    }

	    return flux;
	}
    }
}