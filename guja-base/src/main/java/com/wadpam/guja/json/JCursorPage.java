package com.wadpam.guja.json;

import java.util.List;

/**
 * JSON representation of cursorKey page.
 */
public class JCursorPage<T> {

    private Integer totalSize;

    private String cursorKey;

    private List<T> items;

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public String getCursorKey() {
        return cursorKey;
    }

    public void setCursorKey(String cursorKey) {
        this.cursorKey = cursorKey;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
