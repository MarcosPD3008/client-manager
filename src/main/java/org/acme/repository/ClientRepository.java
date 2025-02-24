package org.acme.repository;

import java.util.List;

import org.acme.dto.PaginationDto;
import org.acme.dto.PaginationResponse;
import org.acme.entity.Client;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientRepository extends GenericRepository<Client, Long> {
    public PaginationResponse<Client> findByCountryPaginated(String country, PaginationDto paginationDto) {
        List<Client> clients = find("LOWER(country)", country.toLowerCase())
                .page(Page.of(paginationDto.getPageNumber() - 1, paginationDto.getPageSize()))
                .list();

        long totalElements = count("LOWER(country)", country.toLowerCase());

        return new PaginationResponse<>(clients, paginationDto.getPageNumber(), paginationDto.getPageSize(), totalElements);
    }
}
