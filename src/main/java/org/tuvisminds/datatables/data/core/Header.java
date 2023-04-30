package org.tuvisminds.datatables.data.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Header {
    int index;
    String name;
    DataType dataType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Header header = (Header) o;
        return Objects.equals(getName(), header.getName()) && getDataType() == header.getDataType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDataType());
    }
}
