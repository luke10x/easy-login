package dev.luke10x.easylogin.registration;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Handle {
    private final String handle;
    private final String secret;
    private final boolean enabled;
}
