package com.wadpam.guja;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wadpam.guja.api.DataTable;
import com.wadpam.guja.jackson.UnixTimestampObjectMapperProvider;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class DataTableTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(DataTableTest.class);


  @Before
  public void setUp() throws Exception {

  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testDataTable() throws Exception {

    DataTable table = DataTable.newBuilder()
        .insertCol(DataTable.Column.newBuilder()
            .id("0")
            .label("value")
            .build(DataTable.Column.Type.STRING))
        .insertCol(DataTable.Column.newBuilder()
            .id("1")
            .label("date")
            .build(DataTable.Column.Type.DATETIME))
        .insertRow(DataTable.Row.newBuilder()
            .addCell("cell 0-0", "Cell 1")
            .addCell(DateTime.now().toDate(), "Cell 2")
            .build())
        .insertRow(DataTable.Row.newBuilder()
            .addCell("cell 1-0", "Cell 3")
            .addCell(DateTime.now().plusDays(1).toDate(), "Cell 4")
            .build())
        .insertRow(DataTable.Row.newBuilder()
            .addCell("cell 2-0", "Cell 5")
            .addCell(DateTime.now().plusDays(2).toDate(), "Cell 6")
            .build())
        .build();

    UnixTimestampObjectMapperProvider provider = new UnixTimestampObjectMapperProvider();
    ObjectMapper mapper = provider.get();

    String json = mapper.writer().writeValueAsString(table);
    //LOGGER.info("json {}", json);
    assertTrue(json.contains("date"));
    assertTrue(json.contains("Cell 6"));

  }

}