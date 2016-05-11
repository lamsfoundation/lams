package org.jgroups.blocks;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Bela Ban
 *
 */
public interface ReplicatedMap<K extends Serializable, V extends Serializable> extends ConcurrentMap<K, V> {
    V _put(K key, V value);

    void _putAll(Map<? extends K, ? extends V> map);

    void _clear();

    V _remove(Object key);

    V _putIfAbsent(K key, V value);

    boolean _remove(Object key, Object value);

    boolean _replace(K key, V oldValue, V newValue);

    V _replace(K key, V value);
}
