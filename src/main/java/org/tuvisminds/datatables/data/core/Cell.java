package org.tuvisminds.datatables.data.core;

public abstract class Cell<T> {
    T t;

//    public Cell() {}
    public Cell(T t) {this.t = t;}

    public T getValue() {
        return t;
    }

    public void setValue(T t) {
        this.t = t;
    }

}
