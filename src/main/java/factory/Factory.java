package factory;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class Factory<K, T, D> {
    private final Map<K, Function<D, T>> factory = new HashMap<>();

    public void add(K key, Function<D, T> creatorFunc) {
        factory.put(key, creatorFunc);

    }

    public void remove(K key) {
        factory.remove(key);
    }

    public T create(K key, D data) {
        Function<D, T> func = factory.get(key);
        if (null == func) {
            throw new NoSuchElementException("no method is registered for key " + key);
        }
        return func.apply(data);
    }

}