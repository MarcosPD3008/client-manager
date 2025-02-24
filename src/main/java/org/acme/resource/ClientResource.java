package org.acme.resource;

import org.acme.dto.PaginationDto;
import org.acme.dto.PaginationResponse;
import org.acme.dto.UpdateClientDto;
import org.acme.entity.Client;
import org.acme.service.ClientService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("clients")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Client API", description = "CRUD operations for clients") // ✅ Apply here
public class ClientResource {

    @Inject
    ClientService clientService;

    @GET
    @Operation(summary = "Retrieve all clients with pagination")
    public PaginationResponse<Client> getAllClients(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPageNumber(page != null ? page : 1);
        paginationDto.setPageSize(size != null ? size : 10);

        return clientService.getClients(paginationDto);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Retrieve a single client by ID")
    public Optional<Client> getClientById(@PathParam("id") Long id) {
        return clientService.getClientById(id);
    }

    @GET
    @Path("/by-country/{country}")
    @Operation(summary = "Retrieve clients by country with pagination")
    public PaginationResponse<Client> getClientsByCountry(
            @PathParam("country") String country,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {

        PaginationDto paginationDto = new PaginationDto();
        paginationDto.setPageNumber(page != null ? page : 1);
        paginationDto.setPageSize(size != null ? size : 10);

        return clientService.getClientsByCountry(country, paginationDto);
    }

    @POST
    @Transactional
    @Operation(summary = "Create a new client")
    public Response createClient(@Valid Client client) {
        clientService.createClient(client);
        return Response.status(Response.Status.CREATED).entity(client).build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Update client (email, address, phone, country)")
    public Response updateClient(@PathParam("id") Long id, @Valid UpdateClientDto updateClientDto) {
        Client updatedClient = clientService.updateClient(id, updateClientDto);
        return Response.ok(updatedClient).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Operation(summary = "Delete a client by ID")
    public void deleteClient(@PathParam("id") Long id) {
        clientService.deleteClient(id);
    }
}
