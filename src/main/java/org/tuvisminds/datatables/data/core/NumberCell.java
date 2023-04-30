package org.tuvisminds.datatables.data.core;

public class NumberCell extends Cell<Double> {
    public NumberCell(Double value) {
        super(value);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
