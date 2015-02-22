package com.wadpam.guja.api;


import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Google DataTable representation.
 * Can be used to populate Google Charts https://developers.google.com/chart/
 *
 * @author mattiaslevin
 */
public class DataTable {

  private Collection<Column> cols;
  private Collection<Row> rows;

  private DataTable(Collection<Column> cols, Collection<Row> rows) {
    this.cols = checkNotNull(cols);
    this.rows = checkNotNull(rows);
  }

  public Collection<Column> getCols() {
    return cols;
  }

  public Collection<Row> getRows() {
    return rows;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private List<Column> cols = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();

    public Builder insertCol(Column column) {
      checkNotNull(column);
      cols.add(column);
      return this;
    }

    public Builder insertRow(Row row) {
      checkNotNull(row);
      rows.add(row);
      return this;
    }

    public Builder insertRows(Collection<Row> rows) {
      checkNotNull(rows);
      this.rows.addAll(rows);
      return this;
    }

    public DataTable build() {
      return new DataTable(cols, Collections2.transform(rows, new Function<Row, Row>() {
        @Override
        public Row apply(Row input) {
          Row.Builder rowBuilder = Row.newBuilder();
          for (int column = 0; column < input.getC().size() ; column++) {
            rowBuilder.addCell(convertCell(input.getC().get(column), cols.get(column)._type));
          }
          return rowBuilder.build();
        }
      }));
    }

    private Cell convertCell(Cell oldCell, Column.Type type) {

      Object value = oldCell.getV();
      String displayValue = oldCell.getF();

      switch (type) {
        case DATETIME:
          return RawValueCell.with(toJavaScriptDate((Date) value), displayValue);
        default:
          return oldCell;
      }
    }

  }

  public static class Column {

    public enum Type {
      NUMBER, STRING, DATE, DATETIME, TIMEOFDAY;

      @Override
      public String toString() {
        return super.toString().toLowerCase();
      }

    }

    private Type _type;
    private String type;
    private String id;
    private String label;

    private Column(Type type, String id, String label) {
      this._type = type;
      this.type = type.toString();
      this.id = id;
      this.label = label;
    }

    public String getType() {
      return type;
    }

    public String getId() {
      return id;
    }

    public String getLabel() {
      return label;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public static class Builder {
      private String id;
      private String label;

      public Builder id(String id) {
        this.id = id;
        return this;
      }

      public Builder label(String label) {
        this.label = label;
        return this;
      }

      public Column build(Type type) {
        return new Column(type, id, label);
      }
    }

  }

  public static class Row {
    private List<Cell> c;

    private Row(List<Cell> c) {
      checkNotNull(c);
      this.c = c;
    }

    public List<Cell> getC() {
      return c;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public static class Builder {
      private List<Cell> cells = new ArrayList<>();

      public Builder addCell(Cell cell) {
        checkNotNull(cell);

        cells.add(cell);
        return this;
      }

      public Builder addCell(Object value, String displayValue) {
        cells.add(Cell.with(value, displayValue));
        return this;
      }

      public Row build() {
        return new Row(cells);
      }
    }

  }


  public static class Cell {
    private Object v;
    private String f;

    public Cell(Object v, String f) {
      this.v = checkNotNull(v);
      this.f = f;
    }

    public static Cell with(Object value) {
      return new Cell(value, null);
    }

    public static Cell with(Object value, String displayValue) {
      return new Cell(value, displayValue);
    }

    public Object getV() {
      return v;
    }

    public String getF() {
      return f;
    }
  }

  /**
   * Cell value will be serialized by including literal String value
   * of the property as is, without quoting of characters.
   */
  public static class RawValueCell extends Cell {

    public RawValueCell(Object v, String f) {
      super(v, f);
    }

    public static RawValueCell with(Object value) {
      return new RawValueCell(value, null);
    }

    public static RawValueCell with(Object value, String displayValue) {
      return new RawValueCell(value, displayValue);
    }

    @JsonRawValue
    @Override
    public Object getV() {
      return super.getV();
    }

  }

  public static String toJavaScriptDate(Date time) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);

    return String.format("new Date(%s, %s, %s, %s, %s, %s)",
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE),
        calendar.get(Calendar.HOUR),
        calendar.get(Calendar.MINUTE),
        calendar.get(Calendar.SECOND));
  }

}
