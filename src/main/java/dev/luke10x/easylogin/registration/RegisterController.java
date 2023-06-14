package dev.luke10x.easylogin.registration;

import dev.luke10x.easylogin.common.FlashContainer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.UriRef;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;

@RequestScoped
@Controller
@Path(RegisterController.URI)
public class RegisterController {
    public static final String URI = "register";

    @Inject
    private RegistrationFormMapper registrationFormMapper;

    @Inject
    private RegisterService registerService;

    @Inject
    private RegistrationModel model;

    @Inject
    private BindingResult bindingResult;

    @Inject
    private FlashContainer flashContainer;

    @GET
    @UriRef("register")
    @Produces(MediaType.TEXT_HTML)
    @View("register.jsp")
    public void register() {
        System.out.println("dev.luke10x.easylogin.Register controller visited");
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.TEXT_HTML)
    @View("register.jsp")
    public Response handleSubmit(@Valid @BeanParam RegisterForm registrationForm) throws SQLException {
        // Validation
        if (bindingResult.isFailed()) {
            bindingResult.getAllErrors().stream().findFirst().ifPresentOrElse(
                    pe -> model.setHandleValidationError(pe.getMessage()),
                    () -> model.setHandleValidationError("Validation failed but error not set")
            );
            return Response.status(BAD_REQUEST).build();
        }

        final Handle handle = registrationFormMapper.toNewHandle(registrationForm);

        try {
            registerService.registerNewHandle(handle);

            flashContainer.setMessage("Handle allocated successfully!");

            return Response
                    .status(SEE_OTHER)
                    .location(java.net.URI.create("onboarding"))
                    .build();
        } catch (HandleAlreadyTakenException e) {
            model.setHandleValidationError(e.getMessage());
        } catch (HandleDoesNotFitDatabaseFieldException e) {
            model.setHandleValidationError(e.getMessage());
        } catch (UnknownDatabaseErrorSavingHandle e) {
            model.setHandleValidationError(e.getMessage());
        }

        return Response.status(INTERNAL_SERVER_ERROR).build();
    }
}
