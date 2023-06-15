package dev.luke10x.easylogin.onboarding;

import dev.luke10x.easylogin.common.FlashContainer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.*;
import jakarta.mvc.*;

@RequestScoped
@Controller
@Path("onboarding")
public class OnboardingController {
//
//    @Inject
//    private FlashContainer flashContainer;

    @Inject
    Models models;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @View("onboarding.xhtml")
    public void showOnboardingInstructions() {
//        flashContainer.setMessage("detted");
//        models.put("flashContainer", flashContainer.getMessage());
//        flashContainer.setMessage(null);
    }
}
