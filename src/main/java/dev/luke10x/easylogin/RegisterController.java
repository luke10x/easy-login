package dev.luke10x.easylogin;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.UriRef;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.mvc.binding.ParamError;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

import static jakarta.ws.rs.core.Response.Status.*;

@RequestScoped
@Controller
@Path(RegisterController.URI)
public class RegisterController {
    public static final String URI = "register";

    @Inject
    private Models models;

    @Inject
    private BindingResult bindingResult;

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
    public Response handleSubmit(@Valid @BeanParam RegisterForm form) {
        // Validation
        if (bindingResult.isFailed()) {
            Set<ParamError> errors = bindingResult.getAllErrors();
            for (ParamError error : errors) {
                String paramName = error.getParamName();
                String message = error.getMessage();
                models.put("error_" + paramName, message);
                System.out.println("> ERROR: " + paramName + " = " + message);
            }
            System.out.println("Unfortunately failed to save (Errors set)");

            return Response
                    .status(BAD_REQUEST)
                    .build();
        }

        // if passed, then save
        System.out.println("SAVE ME: " + form.getHandle());

        // After saving
        return Response
                .status(SEE_OTHER)
                .location(java.net.URI.create(UserController.URI))
                .build();
    }
}
