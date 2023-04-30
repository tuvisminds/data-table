package org.tuvisminds.datatables.queries;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MapQueryStore implements QueryStore {
    Map<String, String> queryMap = new HashMap<>();

    @Override
    public String getQuery(String name) {
        return queryMap.get(name);
    }
}
