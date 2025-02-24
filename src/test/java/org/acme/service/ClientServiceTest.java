package org.acme.service;

import org.acme.dto.PaginationDto;
import org.acme.dto.PaginationResponse;
import org.acme.dto.UpdateClientDto;
import org.acme.entity.Client;
import org.acme.mapper.ClientMapper;
import org.acme.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CountriesService countryService;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private UpdateClientDto updateClientDto;

    @Mock
    private EntityManager entityManager; // Mock EntityManager

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client();
        client.setId(1L);
        client.setCountry("US");
        client.setDemonym(null);
    
        updateClientDto = new UpdateClientDto();
        updateClientDto.setEmail("test@example.com");
    
        lenient().when(clientRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    @DisplayName("Should return paginated clients")
    void should_return_paginated_clients() {
        PaginationDto paginationDto = new PaginationDto();
    
        List<Client> clientList = List.of(new Client());
        PaginationResponse<Client> response = new PaginationResponse<>(clientList, 1, 10, clientList.size());
    
        when(clientRepository.findAllPaginated(paginationDto)).thenReturn(response);
    
        PaginationResponse<Client> result = clientService.getClients(paginationDto);
    
        assertNotNull(result);
        assertEquals(response, result);
        verify(clientRepository).findAllPaginated(paginationDto);
    }
    

    @Test
    @DisplayName("Should return client by ID if present")
    void should_return_client_by_id_if_present() {
        when(clientRepository.findByIdOptional(1L)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClientById(1L);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
        verify(clientRepository).findByIdOptional(1L);
    }

    @Test
    @DisplayName("Should return empty optional when client not found by ID")
    void should_return_empty_optional_when_client_not_found_by_id() {
        when(clientRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.getClientById(1L);

        assertFalse(result.isPresent());
        verify(clientRepository).findByIdOptional(1L);
    }

    @Test
    @DisplayName("Should create a client with fetched demonym")
    void should_create_client_with_fetched_demonym() {
        when(countryService.fetchDemonymByCountryCode("US")).thenReturn("American");

        clientService.createClient(client);

        assertEquals("American", client.getDemonym());
        verify(countryService).fetchDemonymByCountryCode("US");
        verify(clientRepository).save(client);
    }

    @Test
    @DisplayName("Should create a client without fetching demonym if already set")
    void should_create_client_without_fetching_demonym_if_already_set() {
        client.setDemonym("Canadian");

        clientService.createClient(client);

        verify(countryService, never()).fetchDemonymByCountryCode(anyString());
        verify(clientRepository).save(client);
    }

    @Test
    @DisplayName("Should return paginated clients by country")
    void should_return_paginated_clients_by_country() {
        PaginationDto paginationDto = new PaginationDto();
        
        List<Client> clientList = List.of(new Client());
        PaginationResponse<Client> response = new PaginationResponse<>(clientList, 1, 10, clientList.size());

        when(clientRepository.findByCountryPaginated("US", paginationDto)).thenReturn(response);

        PaginationResponse<Client> result = clientService.getClientsByCountry("US", paginationDto);

        assertNotNull(result);
        assertEquals(response, result);
        verify(clientRepository).findByCountryPaginated("US", paginationDto);
    }


    @Test
    @DisplayName("Should update client if exists")
    void should_update_client_if_exists() {
        when(clientRepository.findByIdOptional(1L)).thenReturn(Optional.of(client));
    
        clientService.updateClient(1L, updateClientDto);
    
        verify(clientMapper).updateClientFromDto(updateClientDto, client);
        verify(clientRepository).save(client);
        verify(entityManager).flush();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing client")
    void should_throw_exception_when_updating_non_existing_client() {
        when(clientRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                clientService.updateClient(1L, updateClientDto));

        assertEquals("Client not found", exception.getMessage());
        verify(clientMapper, never()).updateClientFromDto(any(), any());
        verify(clientRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete client by ID")
    void should_delete_client_by_id() {
        clientService.deleteClient(1L);

        verify(clientRepository).deleteEntity(1L);
    }
}
