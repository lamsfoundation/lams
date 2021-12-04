package org.lamsfoundation.lams.util;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.apache.log4j.Logger;

import reactor.core.publisher.Flux;

/**
 * Utility class for serving updates to shared Fluxes.
 * It receives signals from a source Flux, probably a Sink.Many.
 * For each requested key it creates a hot publisher Flux which fetches data for the key.
 * If the sink does not produce any matching signals for given timeout, the flux gets removed.
 *
 * @author Marcin Cieslak
 */
public class FluxMap<T, U> extends ConcurrentHashMap<T, Flux<U>> {
    private static final long serialVersionUID = -8271928054306412858L;

    private static Logger log = Logger.getLogger(FluxMap.class.getName());

    public static final int STANDARD_THROTTLE = 1;
    public static final int STANDARD_TIMEOUT = 5 * 60;

    // only for logging purposes
    private final String operationDescription;
    private final Flux<T> source;
    // default timeout is null, i.e. never expire
    private final Integer timeoutSeconds;
    // default throttle time is null, i.e. no throttling
    private final Integer throttleSeconds;
    private final Function<T, U> fetchFunction;

    public FluxMap(String operationDescritpion, Flux<T> source, Function<T, U> fetchFunction, Integer throttleSeconds,
	    Integer timeoutSeconds) {
	this.operationDescription = operationDescritpion;
	this.source = source;
	this.fetchFunction = fetchFunction;
	this.throttleSeconds = throttleSeconds;
	this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Get a hot publisher Flux for the given key.
     */
    public Flux<U> getFlux(T key) {
	// try to get existing Flux
	Flux<U> flux = get(key);

	if (flux == null) {
	    // create a new Flux
	    if (log.isDebugEnabled()) {
		log.debug("Creating new flux for \"" + operationDescription + "\" with key " + key);
	    }

	    // filter out signals which do not match the key
	    Flux<T> filteringFlux = source.filter(item -> item.equals(key));
	    // do not emit more often than this amount of time
	    if (throttleSeconds != null) {
		filteringFlux = filteringFlux.sample(Duration.ofSeconds(throttleSeconds));
	    }
	    // fetch data based on the key
	    flux = filteringFlux.map(item -> fetchFunction.apply(item));

	    // push initial value to the Flux so data is available immediately after subscribing
	    flux = flux.startWith(fetchFunction.apply(key))
		    // make sure the subsequent subscribers also have data immediately available
		    .cache(1)
		    // just some logging
		    .doOnSubscribe(subscription -> {
			if (log.isDebugEnabled()) {
			    log.debug("Subscribed to flux \"" + operationDescription + "\" with key " + key);
			}
		    })
		    // just some logging
		    .doOnCancel(() -> {
			if (log.isDebugEnabled()) {
			    log.debug("Cancelling subscription to flux for \"" + operationDescription + "\" with key "
				    + key);
			}
		    });

	    // remove Flux on timeout
	    if (timeoutSeconds != null) {
		flux = flux.timeout(Duration.ofSeconds(timeoutSeconds)).onErrorResume(TimeoutException.class,
			throwable -> {
			    if (log.isDebugEnabled()) {
				log.debug("Removing flux for \"" + operationDescription + "\" with key " + key);
			    }
			    // remove terminated Flux from the map
			    remove(key);
			    // switch subscribers to a dummy Flux which completes and cancels their subscriptions
			    return Flux.empty();
			});
	    }

	    put(key, flux);
	}

	return flux;
    }
}