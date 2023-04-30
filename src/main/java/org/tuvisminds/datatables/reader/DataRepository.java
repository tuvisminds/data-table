package org.tuvisminds.datatables.reader;

import org.tuvisminds.datatables.data.core.BooleanCell;
import org.tuvisminds.datatables.data.core.Cell;
import org.tuvisminds.datatables.data.core.DataTable;
import org.tuvisminds.datatables.data.core.DataType;
import org.tuvisminds.datatables.data.core.DateCell;
import org.tuvisminds.datatables.data.core.Header;
import org.tuvisminds.datatables.data.core.NumberCell;
import org.tuvisminds.datatables.data.core.ObjectCell;
import org.tuvisminds.datatables.data.core.TextCell;
import org.tuvisminds.datatables.data.core.TimestampCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

@Component
public class DataRepository {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DataTable readData(String query, Map<String, Object> parameters) throws Exception {
        DataTable columnTable = new DataTable();
        namedParameterJdbcTemplate.getJdbcTemplate().setFetchSize(50000);
        namedParameterJdbcTemplate.getJdbcTemplate().setMaxRows(500000);
        namedParameterJdbcTemplate.query(query, parameters, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                for (int columnIndex = 1; columnIndex < rs.getMetaData().getColumnCount(); columnIndex++) {
                    DataType columnDataType = DataType.UNKNOWN;;
                    Cell cell;
                    Object object = rs.getObject(columnIndex);
                    if (object instanceof Boolean) {
                        cell = new BooleanCell((Boolean) object);
                        columnDataType = DataType.BOOLEAN;
                    } else if (object instanceof Integer) {
                        cell = new NumberCell((Double) object);
                        columnDataType = DataType.NUMBER;
                    } else if (object instanceof BigDecimal) {
                        cell = new NumberCell(((BigDecimal)object).doubleValue());
                        columnDataType = DataType.NUMBER;
                    } else if (object instanceof String) {
                        cell = new TextCell((String) object);
                        columnDataType = DataType.TEXT;
                    } else if (object instanceof Date) {
                        cell = new DateCell((Date) object);
                        columnDataType = DataType.DATETIME;
                    } else if (object instanceof Timestamp) {
                        cell = new TimestampCell((Timestamp) object);
                        columnDataType = DataType.TIMESTAMP;
                    } else {
                        cell = (new ObjectCell(object));
                    }
                    if(rowNum == 0) {
                        Header header = new Header();
                        header.setName(rs.getMetaData().getColumnName(columnIndex));
                        header.setDataType(columnDataType);
                        columnTable.addHeader(header);
                    }
                    columnTable.addCellToColumn(rs.getMetaData().getColumnName(columnIndex), cell);
                }
                return "";
            }
        });
        return columnTable;
    }

}
