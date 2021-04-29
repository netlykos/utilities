package org.netlykos.dbquery.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCQuery {

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  
  @Value("${org.netlykos.dbquery.jdbcQuery.maxFetchSize:100}")
  private int maxFetchSize;

  public Map<String, Object> queryForMap(String query, Map<String, Object> parameters) {
    return namedParameterJdbcTemplate.queryForMap(query, parameters);
  }

  public List<Map<String, Object>> queryForList(String query, Map<String, Object> parameters) {
    return queryForList(query, parameters, maxFetchSize);
  }

  public List<Map<String, Object>> queryForList(String query, Map<String, Object> parameters, int fetchSize) {
    // fetch size cannot be greater than maxFetchSize
    int queryFetchSize = fetchSize < maxFetchSize ? fetchSize : maxFetchSize;
    namedParameterJdbcTemplate.getJdbcTemplate().setFetchSize(queryFetchSize);
    return namedParameterJdbcTemplate.queryForList(query, parameters);
  }

}
