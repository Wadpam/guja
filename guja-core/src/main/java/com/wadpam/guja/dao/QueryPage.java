package com.wadpam.guja.dao;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by sosandstrom on 2015-02-07.
 */
public class QueryPage<T, C> implements Serializable {

    /** provide this to get next page */
    private C cursorKey;

    /** the page of items */
    private Collection<T> items;

    /**
     * The total number of items available. Use for progress indication.
     */
    private Integer totalSize;

    public C getCursorKey() {
        return cursorKey;
    }

    public void setCursorKey(C cursorKey) {
        this.cursorKey = cursorKey;
    }

    public Collection<T> getItems() {
        return items;
    }

    public void setItems(Collection<T> items) {
        this.items = items;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

}
