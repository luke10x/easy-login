package dev.luke10x.easylogin.registration;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Data;

@Data
@RequestScoped
@Named("registrationModel")
public class RegistrationModel {
    private String handleValidationError;
}
