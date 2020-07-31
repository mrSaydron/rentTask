package ru.mrak.rent.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Pageable<T> {
    
    private T filter;
    
    private Integer page = 0;
    private Integer pageSize = 10;
    
    private String sortBy = "id";
    private String sortDir = "asc";
    
    public Integer getOffset() {
        return getPage() * getPageSize();
    }
    
    public Integer getLimit() {
        return getPageSize();
    }
    
    public T getFilter() {
        return filter;
    }
    
    public Integer getPage() {
        return page == null || page < 0 ? 0 : page;
    }
    
    public Integer getPageSize() {
        return pageSize == null || pageSize <= 0 ? Integer.MAX_VALUE : pageSize;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public String getSortDir() {
        return sortDir;
    }
}
