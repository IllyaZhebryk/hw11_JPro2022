import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeList<T> implements List<T> {
    private static final int DEFAULT_LENGTH = 16;

    private volatile T[] elements;

    private AtomicInteger size = new AtomicInteger(0);
    private volatile int rebuildSize = DEFAULT_LENGTH - 1;


    public ThreadSafeList() {
        elements = (T[]) new Object[DEFAULT_LENGTH];
    }


    @Override
    public synchronized void add(T t) {
        int currentSize = size.getAndIncrement();
        if (currentSize >= rebuildSize) {
            resize(currentSize);
        }
        elements[currentSize] = t;
    }

    // Note Stream.arrayCopy is not thread safe.
    private void resize(int newSize) {
        synchronized (elements) {
            synchronized (size) {
                if(newSize >= rebuildSize) {
                    int tempSize = Math.min(size.get() * 2, Integer.MAX_VALUE);
                    T[] newArray = (T[]) new Object[(int) tempSize];
                    System.arraycopy(elements, 0, newArray, 0, elements.length);
                    elements = newArray;
                    rebuildSize = elements.length - 1;
                }
            }
        }
    }
    @Override
    public T get(int index) {
        if (index >= 0 && index < elements.length) {
            return elements[index];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }

    }
    @Override
    public void delete(int index) {
        if (index >= 0 && index < elements.length) {
            for (int i = index; i < size.get(); i++) {
                elements[i] = elements[i + 1];
            }
            elements[size.get()] = null;
            size.decrementAndGet();
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }

    }
}

