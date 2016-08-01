package sniper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * @author Sergey Ivanov.
 */
public class Announcer<T extends EventListener> {
    private final T proxy;
    private List<T> listeners = new ArrayList<>();

    public Announcer(Class<? extends T> listenerType) {
        this.proxy = listenerType.cast(Proxy.newProxyInstance(
                listenerType.getClassLoader(),
                new Class<?>[]{listenerType},
                (proxy1, method, args) -> {
                    announce(method, args);
                    return null;
                }
        ));
    }

    public void addListener(T listener) {
        listeners.add(listener);
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }

    public T announce() {
        return proxy;
    }

    private void announce(Method m, Object[] args) {
        try {
            for (T listener : listeners)
                m.invoke(listener, args);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("could not invoke listener", e);

        } catch (InvocationTargetException e) {
            Throwable couse = e.getCause();

            if (couse instanceof RuntimeException)
                throw (RuntimeException) couse;
            else if (couse instanceof Error)
                throw (Error) couse;
            else
                throw new UnsupportedOperationException("listener threw exception", couse);
        }
    }

    public static <T extends EventListener> Announcer<T> to(Class<? extends T> listeners) {
        return new Announcer<>(listeners);
    }
}
