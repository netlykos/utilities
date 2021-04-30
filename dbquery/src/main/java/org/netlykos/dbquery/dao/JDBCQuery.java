package org.netlykos.dbquery.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * @author Adi B (@netlykos)
 *
 * This class is used to run user provided queries against the given data source.
 */
@Repository
public class JDBCQuery {

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Value("${org.netlykos.dbquery.jdbcQuery.maxResultSize:50}")
  private int maxResultSize;

  public Map<String, Object> queryForMap(String query, Map<String, Object> parameters) {
    namedParameterJdbcTemplate.getJdbcTemplate().setFetchSize(-1);
    return namedParameterJdbcTemplate.queryForMap(query, parameters);
  }

  public List<Map<String, Object>> queryForList(String query, Map<String, Object> parameters) {
    return queryForList(query, parameters, maxResultSize);
  }

  public List<Map<String, Object>> queryForList(String query, Map<String, Object> parameters, int resultSize) {
    // fetch size cannot be infinite and cannot be larger than the maxResultSize defined
    int maxRows = resultSize < 1 ?  maxResultSize : resultSize > maxResultSize ? maxResultSize : resultSize;
    namedParameterJdbcTemplate.getJdbcTemplate().setFetchSize(maxRows);
    namedParameterJdbcTemplate.getJdbcTemplate().setMaxRows(maxRows);
    return namedParameterJdbcTemplate.queryForList(query, parameters);
  }

}
