package org.acme.dto;

public class PaginationDto {
    private int pageNumber = 1;
    private int pageSize = 10;

    public PaginationDto() {}

    public PaginationDto(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber < 1 ? 1 : pageNumber;
        this.pageSize = pageSize < 1 ? 10 : pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber < 1 ? 1 : pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? 10 : pageSize;
    }
}
