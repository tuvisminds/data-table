package org.tuvisminds.datatables.dao;

import org.tuvisminds.datatables.data.core.DataTable;
import org.tuvisminds.datatables.reader.DataRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
public class DataTableDaoImpl implements DataTableDao {

    @Autowired
    DataRepository dataRepository;

    @Override
    public DataTable readTable(String query, Map<String, Object> parameters) throws Exception {
        return dataRepository.readData(query, parameters);
    }
}
