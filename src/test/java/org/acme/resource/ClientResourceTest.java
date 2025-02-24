package org.acme.resource;

import org.acme.dto.PaginationDto;
import org.acme.dto.PaginationResponse;
import org.acme.dto.UpdateClientDto;
import org.acme.entity.Client;
import org.acme.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientResourceTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientResource clientResource;

    private Client client;
    private UpdateClientDto updateClientDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client();
        client.setId(1L);
        client.setCountry("US");
        client.setDemonym("American");

        updateClientDto = new UpdateClientDto();
        updateClientDto.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Should retrieve all clients with pagination")
    void should_retrieve_all_clients_with_pagination() {
        PaginationResponse<Client> response = new PaginationResponse<>(List.of(client), 1, 10, 1);

        when(clientService.getClients(any(PaginationDto.class))).thenReturn(response);

        PaginationResponse<Client> result = clientResource.getAllClients(1, 10);

        assertNotNull(result);
        assertEquals(response, result);
        verify(clientService).getClients(any(PaginationDto.class));
    }

    @Test
    @DisplayName("Should retrieve a single client by ID if present")
    void should_retrieve_single_client_by_id_if_present() {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(client));

        Optional<Client> result = clientResource.getClientById(1L);

        assertTrue(result.isPresent());
        assertEquals(client, result.get());
        verify(clientService).getClientById(1L);
    }

    @Test
    @DisplayName("Should retrieve an empty optional if client not found by ID")
    void should_return_empty_optional_when_client_not_found_by_id() {
        when(clientService.getClientById(1L)).thenReturn(Optional.empty());

        Optional<Client> result = clientResource.getClientById(1L);

        assertFalse(result.isPresent());
        verify(clientService).getClientById(1L);
    }

    @Test
    @DisplayName("Should retrieve clients by country with pagination")
    void should_retrieve_clients_by_country_with_pagination() {
        PaginationResponse<Client> response = new PaginationResponse<>(List.of(client), 1, 10, 1);

        when(clientService.getClientsByCountry(eq("US"), any(PaginationDto.class))).thenReturn(response);

        PaginationResponse<Client> result = clientResource.getClientsByCountry("US", 1, 10);

        assertNotNull(result);
        assertEquals(response, result);
        verify(clientService).getClientsByCountry(eq("US"), any(PaginationDto.class));
    }

    @Test
    @DisplayName("Should create a new client and return HTTP 201")
    void should_create_new_client_and_return_http_201() {
        doNothing().when(clientService).createClient(client);

        Response response = clientResource.createClient(client);

        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(client, response.getEntity());
        verify(clientService).createClient(client);
    }

    @Test
    @DisplayName("Should update a client and return updated client")
    void should_update_client_and_return_updated_client() {
        when(clientService.updateClient(1L, updateClientDto)).thenReturn(client);

        Response response = clientResource.updateClient(1L, updateClientDto);

        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(client, response.getEntity());
        verify(clientService).updateClient(1L, updateClientDto);
    }

    @Test
    @DisplayName("Should delete a client by ID")
    void should_delete_client_by_id() {
        doNothing().when(clientService).deleteClient(1L);

        clientResource.deleteClient(1L);

        verify(clientService).deleteClient(1L);
    }
}
