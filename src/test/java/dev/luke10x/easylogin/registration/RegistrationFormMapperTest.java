package dev.luke10x.easylogin.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.luke10x.easylogin.topt.TotpGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@DisplayName("Registration form mapper test")
class RegistrationFormMapperTest {

    @InjectMocks
    RegistrationFormMapper registrationFormMapper = new RegistrationFormMapper();

    @Mock
    private TotpGenerator totpGenerator;

    @BeforeEach
    public void setUpClass() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Mapper maps")
    public void mapperMaps() {
        when(totpGenerator.generateSecret()).thenReturn("RaNdOMlY GeNeRaTeD");

        final RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setHandle("Inputted handle");

        final Handle handle = registrationFormMapper.toNewHandle(registrationForm);

        assertEquals("Inputted handle", handle.getHandle());
        assertEquals("RaNdOMlY GeNeRaTeD", handle.getSecret());
        assertTrue(handle.isEnabled());
    }
}
