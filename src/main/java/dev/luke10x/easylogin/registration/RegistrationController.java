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

import static jakarta.ws.rs.core.Response.Status.*;

@RequestScoped
@Controller
@Path(RegistrationController.URI)
public class RegistrationController {
    public static final String URI = "register";

    @Inject
    private RegistrationFormMapper registrationFormMapper;

    @Inject
    private RegistrationService registrationService;

    @Inject
    private RegistrationModel model;

    @Inject
    private BindingResult bindingResult;

    @Inject
    private FlashContainer flashContainer;

    @GET
    @UriRef("register")
    @Produces(MediaType.TEXT_HTML)
    @View("register.xhtml")
    public void register() {
        System.out.println("dev.luke10x.easylogin.Register controller visited");
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @View("register.xhtml")
    public Response handleSubmit(@Valid @BeanParam RegistrationForm registrationForm) throws SQLException {
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
            registrationService.registerNewHandle(handle);

            flashContainer.setMessage("Handle allocated successfully!");

            // It actually responds with 303 for Post-Redirect-Get
            return Response.accepted("redirect:onboarding").build();

        } catch (HandleAlreadyTakenException |
                 HandleSizeException |
                 HandleStorageException e) {
            model.setHandleValidationError(e.getMessage());
        }

        return Response.status(INTERNAL_SERVER_ERROR).build();
    }
}
