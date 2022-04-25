package org.lamsfoundation.lams.flux;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import reactor.core.publisher.Flux;

public class FluxRegistry {
    private static final Map<String, FluxMap> fluxRegistry = new ConcurrentHashMap<>();
    private static final Map<String, SharedSink> sinkRegistry = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Function>> boundSinks = new ConcurrentHashMap<>();

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

    public static <T> void initFluxMap(String fluxName, String sinkName, Function<T, String> fetchFunction,
	    Integer throttleSeconds, Integer timeoutSeconds) {
	if (fluxRegistry.containsKey(fluxName)) {
	    throw new IllegalArgumentException("FluxMap for \"" + fluxName + "\" was already initialised");
	}
	SharedSink<T> sink = sinkRegistry.get(sinkName);
	if (sink == null) {
	    sink = FluxRegistry.getSink(sinkName);
	}
	FluxMap<T, String> fluxMap = new FluxMap<>(fluxName, sink.getFlux(), fetchFunction, throttleSeconds,
		timeoutSeconds);
	fluxRegistry.put(fluxName, fluxMap);
    }

    public static <T, String> Flux<String> get(String fluxName, T key) {
	FluxMap<T, String> fluxMap = fluxRegistry.get(fluxName);
	if (fluxMap == null) {
	    throw new IllegalArgumentException("FluxMap for \"" + fluxName + "\" was not initialised");
	}
	return fluxMap.get(key);
    }

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