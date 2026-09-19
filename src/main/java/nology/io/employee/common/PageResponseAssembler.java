package nology.io.employee.common;

import java.util.function.Function;

import org.springframework.data.domain.Page;

import nology.io.employee.common.dtos.PageResponse;

public class PageResponseAssembler {

    public static <T,R> PageResponse<R> toPageResposne(Page<T> page, Function<T,R> mapper){

        int currentPage = page.getNumber() + 1;
        Integer nextPage = currentPage < page.getTotalPages() ? currentPage + 1 :null;
        Integer prevPage = currentPage > 1 ? currentPage - 1 : null;

        return new PageResponse<>(currentPage, page.getTotalPages(), page.getTotalElements(), page.getSize(), nextPage, prevPage, page.map(mapper).getContent());

    }
    
}
