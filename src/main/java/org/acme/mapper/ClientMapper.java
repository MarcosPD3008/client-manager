package org.acme.mapper;

import org.acme.dto.UpdateClientDto;
import org.acme.entity.Client;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientMapper {

    public void updateClientFromDto(UpdateClientDto dto, Client client) {
        if (dto.getEmail() != null) {
            client.setEmail(dto.getEmail());
        }
        if (dto.getAddress() != null) {
            client.setAddress(dto.getAddress());
        }
        if (dto.getPhone() != null) {
            client.setPhone(dto.getPhone());
        }
        if (dto.getCountry() != null) {
            client.setCountry(dto.getCountry());
        }
    }
}
