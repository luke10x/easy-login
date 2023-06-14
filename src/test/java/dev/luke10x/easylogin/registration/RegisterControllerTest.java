package dev.luke10x.easylogin.registration;

import jakarta.mvc.Models;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.RuntimeDelegate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.glassfish.jersey.internal.RuntimeDelegateImpl;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Tests for Registration controller")
class RegisterControllerTest  {

    @InjectMocks
    RegisterController registerController = new RegisterController();

    @Mock
    private RegistrationFormMapper registrationFormMapper;

    @Mock
    private RegisterService registerService;

    @Mock
    private Models models;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUpClass() {
        RuntimeDelegate.setInstance(new RuntimeDelegateImpl());
    }

    @BeforeEach
    public void setUpMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("after successful registration")
    void whenValidCallsService() throws
            UnknownDatabaseErrorSavingHandle,
            HandleAlreadyTakenException,
            HandleDoesNotFitDatabaseFieldException,
            SQLException {
        // Setup
        when(bindingResult.isFailed()).thenReturn(false);

        Handle createdByFactory = Handle.builder()
                .handle("my-handle")
                .secret("whatever")
                .enabled(true)
                .build();
        when(registrationFormMapper.toNewHandle(any())).thenReturn(createdByFactory);

        doNothing().when(registerService).registerNewHandle(any());

        RegisterForm form = new RegisterForm();
        form.setHandle("my-handle");

        // Act
        Response response = registerController.handleSubmit(form);

        // Verify
        verify(registerService, times(1))
                .registerNewHandle(createdByFactory);

        assertEquals(303, response.getStatus());
        assertEquals("REDIRECTION", response.getStatusInfo().getFamily().name());
        assertEquals("See Other", response.getStatusInfo().getReasonPhrase());

        assertEquals("http://dfsdfs/fdfd", response.getHeaders().getFirst("Location"));
    }
}