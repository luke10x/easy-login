package dev.luke10x.easylogin.topt;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;
import java.util.Base64;

@ApplicationScoped
public class TotpGenerator {

    public String generateSecret() {
        // Generate a new TOTP secret key
        byte[] secretBytes = new byte[20];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secretBytes);

        // Encode the secret key as Base64
        String secret = Base64.getEncoder().encodeToString(secretBytes);

        return secret;
    }
}