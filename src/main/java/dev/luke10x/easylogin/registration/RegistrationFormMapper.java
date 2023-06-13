package dev.luke10x.easylogin.registration;

import dev.luke10x.easylogin.topt.TotpGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RegistrationFormMapper {
    @Inject
    private TotpGenerator totpGenerator;

    public Handle toNewHandle(RegisterForm form) {
        return Handle.builder()
                .handle(form.getHandle())
                .secret(totpGenerator.generateSecret())
                .enabled(true)
                .build();
    }
}
