package org.acme.validation;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.acme.repository.ClientRepository;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Inject
    ClientRepository clientRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // Null values are considered valid
        }

        return clientRepository.find("email", email).firstResultOptional().isEmpty();
    }
}
