package nology.io.employee.common.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class PageQueryParams {

    @Min(1)
    private Integer page = 1;


    @Min(1)
    @Max(20)
    private Integer size = 10;

    private String searchTerm = "";


    public Integer getPage() {
        return page;
    }


    public String getSearchTerm() {
        return searchTerm;
    }


    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }


    public void setPage(Integer page) {
        this.page = page;
    }


    public Integer getSize() {
        return size;
    }


    public void setSize(Integer size) {
        this.size = size;
    }


    public PageQueryParams() {
    }

    
    
}
