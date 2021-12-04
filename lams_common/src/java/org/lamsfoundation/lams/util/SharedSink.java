package org.lamsfoundation.lams.util;

import org.apache.log4j.Logger;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * This class allows both sink functionality (manually pushing elements to Flux)
 * and hot publisher (so multiple other Fluxes can use it as source).
 *
 * @author Marcin Cieslak
 */
public class SharedSink<T> {

    private static Logger log = Logger.getLogger(SharedSink.class.getName());

    private final Sinks.Many<T> sink;
    private final Flux<T> flux;

    public SharedSink(String operationDescription) {
	if (log.isDebugEnabled()) {
	    log.debug("Created sink for \"" + operationDescription + "\"");
	}
	sink = Sinks.many().replay().latest();
	flux = sink.asFlux().doFinally((signalType) -> {
	    if (log.isDebugEnabled()) {
		log.debug("Terminated (" + signalType + ") sink for \"" + operationDescription + "\"");
	    }
	}).publish().autoConnect();
    }

    public void emit(T item) {
	sink.tryEmitNext(item);
    }

    public Flux<T> getFlux() {
	return flux;
    }
}