package org.lamsfoundation.lams.flux;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Function;

import reactor.core.publisher.Flux;

public class FluxRegistry {
    @SuppressWarnings("rawtypes")
    private static final Map<String, FluxMap> fluxRegistry = new ConcurrentHashMap<>();
    @SuppressWarnings("rawtypes")
    private static final Map<String, SharedSink> sinkRegistry = new ConcurrentHashMap<>();
    @SuppressWarnings("rawtypes")
    private static final Map<String, Map<String, Function>> boundSinks = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    private static <T> SharedSink<T> getSink(String sinkName) {
	SharedSink<T> sink = sinkRegistry.get(sinkName);
	if (sink != null) {
	    return sink;
	}
	sink = new SharedSink<>(sinkName);
	sinkRegistry.put(sinkName, sink);
	return sink;
    }

    /**
     * Binds sinks so an emit for one sink is also an emit for another
     */
    @SuppressWarnings("rawtypes")
    public static void bindSink(String sourceSinkName, String targetSinkName, Function emitTransformer) {
	// make sure that sinks exist
	FluxRegistry.getSink(sourceSinkName);
	FluxRegistry.getSink(targetSinkName);

	Map<String, Function> sourceSinkBindings = boundSinks.get(sourceSinkName);
	if (sourceSinkBindings == null) {
	    sourceSinkBindings = new ConcurrentHashMap<>();
	    boundSinks.put(sourceSinkName, sourceSinkBindings);
	}
	if (!sourceSinkBindings.containsKey(targetSinkName)) {
	    sourceSinkBindings.put(targetSinkName, emitTransformer);
	}
    }

    @SuppressWarnings("unchecked")
    public static <T> void initFluxMap(String fluxName, String sinkName, BiPredicate<T, T> itemEqualsPredicate,
	    Function<T, String> fetchFunction, Integer throttleSeconds, Integer timeoutSeconds) {
	if (fluxRegistry.containsKey(fluxName)) {
	    throw new IllegalArgumentException("FluxMap for \"" + fluxName + "\" was already initialised");
	}
	SharedSink<T> sink = sinkRegistry.get(sinkName);
	if (sink == null) {
	    sink = FluxRegistry.getSink(sinkName);
	}
	if (itemEqualsPredicate == null) {
	    itemEqualsPredicate = (key, item) -> item.equals(key);
	}
	FluxMap<T, String> fluxMap = new FluxMap<>(fluxName, sink.getFlux(), itemEqualsPredicate, fetchFunction,
		throttleSeconds, timeoutSeconds);
	fluxRegistry.put(fluxName, fluxMap);
    }

    @SuppressWarnings({ "unchecked" })
    public static <T> Flux<String> get(String fluxName, T key) {
	FluxMap<T, String> fluxMap = fluxRegistry.get(fluxName);
	if (fluxMap == null) {
	    throw new IllegalArgumentException("FluxMap for \"" + fluxName + "\" was not initialised");
	}
	return fluxMap.get(key);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void emit(String sinkName, T item) {
	if (item == null) {
	    return;
	}

	SharedSink<T> sink = sinkRegistry.get(sinkName);
	sink.emit(item);

	// check for bound sinks
	Map<String, Function> sinkBindings = boundSinks.get(sinkName);
	if (sinkBindings == null) {
	    return;
	}
	for (Entry<String, Function> binding : sinkBindings.entrySet()) {
	    FluxRegistry.emit(binding.getKey(), binding.getValue().apply(item));
	}

    }
}