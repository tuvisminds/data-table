package org.tuvisminds.datatables.dao;

import org.tuvisminds.datatables.data.core.DataTable;

import java.util.Map;

public interface DataTableDao {
    DataTable readTable(String query, Map<String, Object> parameters) throws Exception;
}
