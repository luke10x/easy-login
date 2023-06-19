package dev.luke10x.easylogin.registration;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import dev.luke10x.easylogin.UserApplication;
import dev.luke10x.easylogin.common.FlashContainer;
import dev.luke10x.easylogin.community.UserController;
import dev.luke10x.easylogin.topt.TotpGenerator;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(ArquillianExtension.class)
public class RegistrationControllerTest {

    @Inject
    RegistrationServiceAlternative registrationService;

    @Inject
    UserServiceAlternative userService;

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = true)
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
                .addAsManifestResource(new StringAsset("Dependencies: jdk.unsupported\n" /* required by Mockito */), "MANIFEST.MF")


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
        var opts = client.getOptions();
        opts.setThrowExceptionOnScriptError(false);
        opts.setThrowExceptionOnFailingStatusCode(false);
        opts.setCssEnabled(false);
        opts.setRedirectEnabled(true);
        opts.setJavaScriptEnabled(false);
    }


    @BeforeEach
    public void resetMocks() {
        Mockito.reset(registrationService.getMock());
        Mockito.reset(userService.getMock());
    }

    @AfterEach
    public void teardown() {
        if (this.client != null) {
            this.client.close();
        }
    }

    @Test
    public void testTodosAPI() throws Exception {
        Page page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());

        assertEquals(200, page.getWebResponse().getStatusCode());

        final String body = page.getWebResponse().getContentAsString();
        System.out.println("Body: " + body);
    }

    @Test
    public void emptyHandleShowsErrorMessage() throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, IOException {
        HtmlPage page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());
        var form = page.getForms().get(0);

        form.getInputByName("handle").type("");
        HtmlPage resultPage = form.getButtonByName("submit-registration").click();

        assertEquals(400, resultPage.getWebResponse().getStatusCode());
        verify(registrationService.getMock(), times(0)).registerNewHandle(any());
    }

    @Test
    public void redirectsForward() throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, IOException {

        HtmlPage page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());
        var form = page.getForms().get(0);

        form.getInputByName("handle").type("myname");
        Page resultPage = form.getButtonByName("submit-registration").click();

        // It is 404 because it redirected out of deployed ShrinkWrap web archive
        assertEquals(404, resultPage.getWebResponse().getStatusCode());
        assertEquals(
                new URL(baseUrl, "mvc/onboarding").getPath(),
                resultPage.getWebResponse().getWebRequest().getUrl().getPath());

        verify(registrationService.getMock(), times(1)).registerNewHandle(any());
    }
}
