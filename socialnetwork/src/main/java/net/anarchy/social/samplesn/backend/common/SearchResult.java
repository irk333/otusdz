package net.anarchy.social.samplesn.backend.common;

import java.util.List;

public class SearchResult<T> {
    private int pageNo;
    private int pageSize;
    private int recordCount;
    private List<T> records;

    public int getPageGount() {
        int n = recordCount/pageSize;
        if (n*pageSize == recordCount) {
            return n;
        } else {
            return n+1;
        }
    }

    public boolean isEmpty() {
        return recordCount == 0;
    }

    public int getStart() {
        return pageNo*pageSize;
    }

    public int getEnd() {
        return pageNo*pageSize + pageSize - 1;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
