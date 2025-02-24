package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.acme.dto.PaginationDto;
import org.acme.dto.PaginationResponse;

public abstract class GenericRepository<T, ID extends Serializable> implements PanacheRepositoryBase<T, ID> {

    public Optional<T> findByIdOptional(ID id) {
        return find("id", id).firstResultOptional();
    }

    public PaginationResponse<T> findAllPaginated(PaginationDto paginationDto) {
        List<T> items = findAll().page(Page.of(paginationDto.getPageNumber() - 1, paginationDto.getPageSize())).list();
        
        long totalElements = count();
        
        int totalPages = (int) Math.ceil((double) totalElements / paginationDto.getPageSize());

        return new PaginationResponse<>(items, 
                                        totalElements, 
                                        totalPages, 
                                        paginationDto.getPageNumber(), 
                                        paginationDto.getPageSize()
                                        );
    }

    public long countClients() {
        return count();
    }

    public void save(T entity) {
        persist(entity);
    }

    public void deleteEntity(ID id) {
        delete("id", id);
    }
}
