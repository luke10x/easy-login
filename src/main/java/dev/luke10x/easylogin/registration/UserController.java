package dev.luke10x.easylogin.registration;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Models;
import jakarta.mvc.UriRef;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.mvc.Controller;

import java.util.List;

@RequestScoped
@Controller @Path(UserController.URI)
public class UserController {
    public static final String URI = "user-list";

    @Inject
    private UserService applicationService;

    @Inject
    private Models models;

    @GET
    @UriRef(URI)
    @Produces(MediaType.TEXT_HTML)
    public String listApplications() {
        List<User> users = applicationService.getAllUsers();
        models.put("users", users);

        System.out.println("users set" +  users.toString());
        return "users.xhtml";
    }
}
