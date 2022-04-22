package org.lamsfoundation.lams.flux;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import reactor.core.publisher.Flux;

public class FluxRegistry {
    private static final Map<String, FluxMap> fluxRegistry = new ConcurrentHashMap<>();
    private static final Map<String, SharedSink> sinkRegistry = new ConcurrentHashMap<>();

    public static <T> SharedSink<T> initSink(String sinkName) {
	SharedSink<T> sink = sinkRegistry.get(sinkName);
	if (sink != null) {
	    return sink;
	}
	sink = new SharedSink<>(sinkName);
	sinkRegistry.put(sinkName, sink);
	return sink;
    }

    public static <T> void initFluxMap(String fluxName, String sinkName, Function<T, String> fetchFunction,
	    Integer throttleSeconds, Integer timeoutSeconds) {
	if (fluxRegistry.containsKey(fluxName)) {
	    throw new IllegalArgumentException("FluxMap for \"" + fluxName + "\" was already initialised");
	}
	SharedSink<T> sink = sinkRegistry.get(sinkName);
	if (sink == null) {
	    sink = FluxRegistry.initSink(sinkName);
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
	SharedSink<T> sink = sinkRegistry.get(sinkName);
	if (sink == null) {
	    throw new IllegalArgumentException("Sink for \"" + sinkName + "\" was not initialised");
	}
	sink.emit(item);
    }
}