package org.lamsfoundation.lams.flux;

import org.apache.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;
import reactor.util.Loggers;

import java.util.logging.Level;

/**
 * This class allows both sink functionality (manually pushing elements to Flux) and hot publisher (so multiple other
 * Fluxes can use it as source).
 *
 * @author Marcin Cieslak
 */
public class SharedSink<T> {

    private static Logger log = Logger.getLogger(SharedSink.class.getName());

    private final Sinks.Many<T> sink;
    private final Flux<T> flux;

    static {
	Loggers.useJdkLoggers();
    }

    public SharedSink(String name) {
	if (log.isDebugEnabled()) {
	    log.debug("Created sink for \"" + name + "\"");
	}
	sink = Sinks.many().replay().latest();
	flux = sink.asFlux().log("reactor.", Level.FINE, true).onBackpressureLatest().doFinally((signalType) -> {
	    if (log.isDebugEnabled()) {
		log.debug("Terminated (" + signalType + ") sink for \"" + name + "\"");
	    }
	}).publish().autoConnect();
    }

    public void emit(T item) {
	sink.tryEmitNext(item);
    }

    public Flux<T> getFlux() {
	return flux;
    }

    public void shutdown() {
	sink.emitComplete(EmitFailureHandler.FAIL_FAST);
    }
}