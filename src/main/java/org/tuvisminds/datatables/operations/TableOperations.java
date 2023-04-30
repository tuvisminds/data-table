package org.tuvisminds.datatables.operations;

import org.tuvisminds.datatables.data.core.Cell;
import org.tuvisminds.datatables.data.core.DataTable;
import org.tuvisminds.datatables.data.core.Header;
import org.tuvisminds.datatables.data.core.NumberCell;
import org.tuvisminds.datatables.data.core.TextCell;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class TableOperations {
    @Getter
    @Setter
    public class ColumnOperation {
        String columnName;
        ColumnOperator columnOperator;

        public ColumnOperation(String columnName, ColumnOperator columnOperator) {
            this.columnName = columnName;
            this.columnOperator = columnOperator;
        }
    }

    public enum ColumnOperator {
        SUM, AVG, STDDEV;
    }

    DataTable dataTable;
    Map<String, List<Integer>> groupByIndexes = new HashMap<>();

    public TableOperations(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public TableOperations filterColumns(String... columnNames) {
        final DataTable columnTable1 = new DataTable();
        Arrays.stream(columnNames).forEach(columnName -> {
            Header header = this.dataTable.getHeader(columnName);
            if( header != null) {
                columnTable1.addHeader(header);
                columnTable1.getColumns().get(header).addAll(this.dataTable.getColumns().get(header));
            }
        });
        return new TableOperations(columnTable1);
    }

    Map<String, List<Integer>> getGroupByIndexes(String aggregateBy) {
        Map<String, List<Integer>> groupByIndexes = new LinkedHashMap<>();

        AtomicInteger cellIndex = new AtomicInteger(0);
        dataTable.getColumns().get(dataTable.getHeader(aggregateBy)).forEach(cell -> {
            String key = cell.toString();
            if (groupByIndexes.containsKey(key)) {
                groupByIndexes.get(key).add(cellIndex.getAndIncrement());
            } else {
                List<Integer> indices = new ArrayList<>();
                groupByIndexes.put(key, indices);
                indices.add(cellIndex.getAndIncrement());
            }
        });
        return groupByIndexes;
    }

    public TableOperations prepareForAggregate(String groupByColumnName) {
        if(this.groupByIndexes.isEmpty()) {
            this.groupByIndexes = getGroupByIndexes(groupByColumnName);
        }
        return this;
    }

    public TableOperations aggregateAndSum(String groupByColumnName, String... columns) {
        if(this.groupByIndexes.isEmpty()) {
            groupByIndexes = getGroupByIndexes(groupByColumnName);
        }
        DataTable aggregatedTable = new DataTable();
        Arrays.stream(columns).forEach(filteredColumn -> {
            Header header = dataTable.getHeader(filteredColumn);
            aggregatedTable.addHeader(dataTable.getHeader(groupByColumnName));
            aggregatedTable.addHeader(header);
            List<Cell> column = dataTable.getColumns().get(header);
            groupByIndexes.entrySet().stream().forEach(stringListEntry -> {
                aggregatedTable.addCellToColumn(groupByColumnName, new TextCell(stringListEntry.getKey()));
                Double total = stringListEntry.getValue().stream().mapToDouble(entry -> {
                    return (double) column.get(entry).getValue();
                }).summaryStatistics().getSum();
                aggregatedTable.addCellToColumn(header.getName(), new NumberCell(total));
            });
        });
        return new TableOperations(aggregatedTable);
    }

    public TableOperations aggregateAndAverage(String groupByColumnName, ColumnOperator columnOperator) {
        return new TableOperations(dataTable);
    }

}
