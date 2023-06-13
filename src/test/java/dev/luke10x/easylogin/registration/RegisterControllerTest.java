package dev.luke10x.easylogin.registration;

import jakarta.mvc.Models;
import jakarta.mvc.binding.BindingResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Tests for Registration controller")
class RegisterControllerTest {
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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Success saving")
    void whenValidCallsService() throws UnknownDatabaseErrorSavingHandle, HandleAlreadyTakenException, HandleDoesNotFitDatabaseFieldException, SQLException {
        when(bindingResult.isFailed()).thenReturn(false);

        Handle createdByFactory = Handle.builder()
                .handle("my-handle")
                .secret("whatever")
                .enabled(true)
                .build();
        when(registrationFormMapper.toNewHandle(any())).thenReturn(createdByFactory);

        doNothing().when(registerService).registerNewHandle(any());

        RegisterForm f = new RegisterForm();
        f.setHandle("my-handle");

        registerController.handleSubmit(f);

        verify(registerService).registerNewHandle(createdByFactory);
    }
}