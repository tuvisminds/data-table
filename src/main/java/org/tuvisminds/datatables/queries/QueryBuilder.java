package org.tuvisminds.datatables.queries;

public class QueryBuilder {
    Query query;

    public QueryBuilder() {
        query = new Query();
    }

    public QueryBuilder setName(String queryName) {
        this.query.setName(queryName);
        return this;
    }

    public QueryBuilder setQuery(String query) {
        this.query.setQuery(query);
        return this;
    }
}
