package org.tuvisminds.datatables.data.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DataTable {
    Map<Header, List<Cell>> columns = new LinkedHashMap<>();
    Map<String, Header> columnHeaderMap = new LinkedHashMap<>();

    public void addHeader(Header header) {
        if(!columns.containsKey(header)) {
            columns.put(header, new ArrayList<>());
            columnHeaderMap.put(header.getName(), header);
        }
    }

    public List<Cell> getColumn(String columnName) {
        return columns.get(getHeader(columnName));
    }

    public List<Cell> getColumn(Header columnHeader) {
        return columns.get(columnHeader);
    }

    public Header getHeader(String columnName) {
        for (Header header : columns.keySet()) {
            if (header.getName().equalsIgnoreCase(columnName)) {
                return header;
            }
        }
        return null;
    }

    public int getColumnIndex(String columnName) {
        for (Header header : columns.keySet()) {
            if (header.getName().equalsIgnoreCase(columnName)) {
                return header.getIndex();
            }
        }
        return -1;
    }

    public void addCellToColumn(String columnName, Cell<?> cell) {
        columns.get(columnHeaderMap.get(columnName)).add(cell);
    }
}
