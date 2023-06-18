package dev.luke10x.easylogin.registration;

import dev.luke10x.easylogin.UserApplication;
import dev.luke10x.easylogin.common.FlashContainer;
import dev.luke10x.easylogin.community.User;
import dev.luke10x.easylogin.community.UserController;
import dev.luke10x.easylogin.community.UserService;
import dev.luke10x.easylogin.topt.TotpGenerator;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.enterprise.inject.Default;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.TEXT_HTML;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(ArquillianExtension.class)
public class RegistrationControllerTest {
    @Default
    public static class RegistrationServiceAlternative implements RegistrationService {
        @Override
        public void registerNewHandle(Handle handle) throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException {}
    }
    @Inject RegistrationServiceAlternative registrationService;

    @Default
    public static class UserServiceAlternative implements UserService {
        @Override
        public List<User> getAllUsers() {
            return null;
        }
    }
    @Inject
    UserServiceAlternative userService;

    @BeforeEach
    public void reassignAlternativesToMocks() {
        registrationService = Mockito.mock(RegistrationServiceAlternative.class);
        userService = Mockito.mock(UserServiceAlternative.class);
    }

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        File[] files = Maven.resolver().loadPomFromFile("build/publications/sample/pom-default.xml")
                .importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST)
                .resolve()
                .withTransitivity()
                .asFile();
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackage(RegistrationController.class.getPackage())
                .addPackage(UserController.class.getPackage())
                .addPackage(FlashContainer.class.getPackage())
                .addPackage(TotpGenerator.class.getPackage())
                .addClass(UserApplication.class)
                .addClass(RegistrationServiceAlternative.class)
                .addClass(UserServiceAlternative.class)
                .addAsLibraries(files)
                // Enable CDI
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                                .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                        "/", Filters.include(".*\\.(xhtml|css|xml)$")
                );
        war.getContent().entrySet().stream().forEach(entry -> {
            System.out.println("#### " +  entry.getValue());
        });
        return war;
    }

    @ArquillianResource
    URL baseUrl;

    private Client client;

    @BeforeEach
    public void setup() {
        this.client = ClientBuilder.newClient();
    }

    @AfterEach
    public void teardown() {
        if (this.client != null) {
            this.client.close();
        }
    }

    @Test
    public void testTodosAPI() throws Exception {

        RegistrationService registrationService = Mockito.mock(RegistrationService.class);

        final String url = new URL(baseUrl, "mvc/register").toExternalForm();
        final WebTarget target = client.target(url);
        try (final Response response = target.request().accept(TEXT_HTML).get()) {
            assertEquals(200, response.getStatus());

            String responseBody = response.readEntity(String.class);
            System.out.println("Body: " + responseBody);
        }
    }

    @Test
    public void emptyHandleShowsErrorMessage() throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, MalformedURLException {
        RegistrationService registrationService = Mockito.mock(RegistrationService.class);

        final String url = new URL(baseUrl, "mvc/register").toExternalForm();
        final WebTarget target = client.target(url);

        var form = new Form();
        form.param("handle", null);
        final Response response = target.request().accept(TEXT_HTML).post(Entity.form(form));

        assertEquals(400, response.getStatus());
        verify(registrationService, times(0)).registerNewHandle(any());
    }

    @Test
    public void validHandleSavedUsingService() throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, MalformedURLException {
        RegistrationService registrationService = Mockito.mock(RegistrationService.class);

        client.register((ClientRequestFilter) requestContext -> {
            requestContext.setProperty("http.protocol.handle-redirects", false);
        });

        final String url = new URL(baseUrl, "mvc/register").toExternalForm();
        final WebTarget target = client.target(url);

        var form = new Form();
        form.param("handle", "normal_handle");

        final Response response = target.request().post(Entity.form(form));

        assertEquals(404, response.getStatus());
        verify(registrationService, times(1)).registerNewHandle(any());
    }
}
