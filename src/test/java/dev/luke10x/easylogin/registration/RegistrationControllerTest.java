package dev.luke10x.easylogin.registration;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import dev.luke10x.easylogin.UserApplication;
import dev.luke10x.easylogin.common.FlashContainer;
import dev.luke10x.easylogin.community.User;
import dev.luke10x.easylogin.community.UserController;
import dev.luke10x.easylogin.community.UserService;
import dev.luke10x.easylogin.topt.TotpGenerator;
import jakarta.inject.Inject;
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
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private WebClient client;

    @BeforeEach
    public void setUp() {
        client = new WebClient();
        client.getOptions()
                .setThrowExceptionOnScriptError(false);
        client.getOptions()
                .setThrowExceptionOnFailingStatusCode(false);
        client.getOptions()
                .setRedirectEnabled(true);
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
        Page page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());

        assertEquals(200, page.getWebResponse().getStatusCode());

        final String body = page.getWebResponse().getContentAsString();
        System.out.println("Body: " + body);
    }

    @Test
    public void emptyHandleShowsErrorMessage() throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, IOException {
        RegistrationService registrationService = Mockito.mock(RegistrationService.class);

        HtmlPage page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());
        var form = page.getForms().get(0);

        form.getInputByName("handle").type("");
        HtmlPage resultPage = form.getButtonByName("submit-registration").click();

        // Browser is likely not even reaching backend validation
        assertEquals(200, resultPage.getWebResponse().getStatusCode());
        final String body = resultPage.getWebResponse().getContentAsString();
        System.out.println("Body: " + body);
    }

    @Test
    public void dsfsdfsd() throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, IOException {
        RegistrationService registrationService = Mockito.mock(RegistrationService.class);

        HtmlPage page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());
        var form = page.getForms().get(0);

        form.getInputByName("handle").type("myname");
        Page resultPage = form.getButtonByName("submit-registration").click();

        // It is 404 because it redirected out of deployed ShrinkWrap web archive
        assertEquals(404, resultPage.getWebResponse().getStatusCode());
        assertEquals(
                new URL(baseUrl, "mvc/onboarding").getPath(),
                resultPage.getWebResponse().getWebRequest().getUrl().getPath());

        final String body = resultPage.getWebResponse().getContentAsString();
        System.out.println("Body: " + body);
    }
}
