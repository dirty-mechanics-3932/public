package org.dirtymechanics.frc.util;

/**
 * A basic List.
 *
 * @author Daniel Ruess
 */
public class List {

    private final Object[] array;
    private int size = 0;

    public List(int size) {
        array = new Object[size];
    }

    public List() {
        this(50);
    }

    public void put(Object o) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                array[i] = o;
                size++;
                return;
            }
        }
        throw new IndexOutOfBoundsException("The list is full!");
    }

    public void remove(Object o) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == o) {
                array[i] = null;
                size--;
                return;
            }
        }
    }

    public Object get(int i) {
        return array[i];
    }

    public Object[] getObjects() {
        Object[] o = new Object[size];
        int z = 0;
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != null) {
                o[z++] = array[i];
            }
        }
        return o;
    }
}
