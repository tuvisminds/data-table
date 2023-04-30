package org.tuvisminds.datatables.data.core;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Row {
    Map<Integer, Cell> cells = new HashMap<>();

    public void addCell(Cell cell) {
        cells.put(cells.size(), cell);
    }
}
