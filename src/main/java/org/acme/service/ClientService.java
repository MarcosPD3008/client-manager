package org.acme.service;
import org.acme.dto.PaginationDto;
import org.acme.dto.PaginationResponse;
import org.acme.dto.UpdateClientDto;
import org.acme.entity.Client;
import org.acme.mapper.ClientMapper;
import org.acme.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class ClientService {
    @Inject
    ClientRepository clientRepository;
    
    @Inject
    CountriesService countryService;
    
    @Inject
    ClientMapper clientMapper;
    
    public PaginationResponse<Client> getClients(PaginationDto paginationDto) {
        return clientRepository.findAllPaginated(paginationDto);
    }
    
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findByIdOptional(id);
    }
    
    public void createClient(Client client) {
        if (client.getDemonym() == null || client.getDemonym().isEmpty()) {
            var demonym = countryService.fetchDemonymByCountryCode(client.getCountry());
            client.setDemonym(demonym);
        }

        clientRepository.save(client);
    }

    public PaginationResponse<Client> getClientsByCountry(String country, PaginationDto paginationDto) {
        return clientRepository.findByCountryPaginated(country, paginationDto);
    }

    public Client updateClient(Long id, UpdateClientDto updateClientDto) {
        Optional<Client> existingClientOpt = clientRepository.findByIdOptional(id);

        if (existingClientOpt.isPresent()) {
            Client client = existingClientOpt.get();

            if (!client.getCountry().equals(updateClientDto.getCountry())) {
                var demonym = countryService.fetchDemonymByCountryCode(updateClientDto.getCountry());
                client.setDemonym(demonym);
            }

            clientMapper.updateClientFromDto(updateClientDto, client);
            clientRepository.save(client);
            clientRepository.getEntityManager().flush();
            return client;
        } 
        else {
            throw new RuntimeException("Client not found");
        }
    }
    
    
    public void deleteClient(Long id) {
        clientRepository.deleteEntity(id);
    }
}
