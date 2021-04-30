package org.netlykos.dbquery.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("unit-test")
@JdbcTest(includeFilters = @ComponentScan.Filter(Repository.class))
@ContextConfiguration(classes = {JDBCQuery.class})
public class JDBCQueryTest {

  private static final Logger LOGGER = LogManager.getLogger();

  @Autowired
  private JDBCQuery dataQuery;

  @Test
  public void testQueryForList() {
    String query = "select * from book where submission_date > :submission_date";
    Map<String, Object> parameters = new HashMap<>();
    int year = 2000, month = 1, dayOfMonth = 1, hour = 0, minute = 0;
    LocalDateTime submissionDate = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    parameters.put("submission_date", submissionDate);
    List<Map<String, Object>> list = dataQuery.queryForList(query, parameters);
    int counter = 0;
    for (Map<String, Object> map : list) {
      counter++;
      LOGGER.info("Row {}", counter);
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        LOGGER.debug("{}: {}", entry.getKey(), entry.getValue());
      }
    }
    assertNotNull(list);
    assertFalse(list.isEmpty());
  }

  @Test
  public void testQueryForListWithResultSize() {
    String query = "select * from book";
    int rowSize = 2;
    List<Map<String, Object>> list = dataQuery.queryForList(query, Collections.emptyMap(), rowSize);
    assertEquals(rowSize, list.size());
    assertNotNull(list);
    assertFalse(list.isEmpty());

    int entriesInDb = 8; // should match 8 rows inserted via data-hsqldb.sql
    list = dataQuery.queryForList(query, Collections.emptyMap(), 0);
    assertEquals(entriesInDb, list.size());
    assertNotNull(list);
    assertFalse(list.isEmpty());

    // Query for a value larger than maxRowSize
    list = dataQuery.queryForList(query, Collections.emptyMap(), 51);
    assertEquals(entriesInDb, list.size());
    assertNotNull(list);
    assertFalse(list.isEmpty());
  }

  @Test
  public void testQueryForRow() {
    String query = "select * from book where id = :id";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("id", 1);
    Map<String, Object> map = dataQuery.queryForMap(query, parameters);
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      LOGGER.debug("{}: {}", entry.getKey(), entry.getValue());
    }
    assertNotNull(map);
    assertFalse(map.isEmpty());
  }

}
