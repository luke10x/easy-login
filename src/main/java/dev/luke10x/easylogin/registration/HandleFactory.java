package dev.luke10x.easylogin.registration;

import dev.luke10x.easylogin.topt.TotpGenerator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HandleFactory {

    public Handle createNewHandle(String desiredHandle) {
        return Handle.builder()
                .handle(desiredHandle)
                .secret(TotpGenerator.generateSecret())
                .enabled(true)
                .build();
    }
}
