package dev.luke10x.easylogin.registration;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import dev.luke10x.easylogin.UserApplication;
import dev.luke10x.easylogin.community.UserService;
import jakarta.inject.Inject;
import lombok.Delegate;
import lombok.Getter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import dev.luke10x.easylogin.testutils.HtmlUnitWebClientFactory;
import dev.luke10x.easylogin.testutils.MockRegistry;
import dev.luke10x.easylogin.testutils.WebArchiveFactory;

import javax.enterprise.inject.Default;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Registration controller")
@ExtendWith(ArquillianExtension.class)
public class RegistrationControllerTest {

    @Default public static class RegistrationServiceAlternative implements RegistrationService {
        @Getter
        @Delegate(types = { RegistrationService.class })
        private final RegistrationService mock = (RegistrationServiceAlternative) MockRegistry.get(
                RegistrationServiceAlternative.class);
    }

    @Default public static class UserServiceAlternative implements UserService {
        @Getter
        @Delegate(types = UserService.class)
        private final UserService mock = (UserServiceAlternative) MockRegistry.get(UserServiceAlternative.class);
    }

    @Inject
    RegistrationServiceAlternative registrationService;

    @Inject
    UserServiceAlternative userService;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = WebArchiveFactory.createCommonWebArchive()
                .addPackage(dev.luke10x.easylogin.registration.RegistrationController.class.getPackage())
                .addPackage(dev.luke10x.easylogin.community   .UserController.class.getPackage())
                .addPackage(dev.luke10x.easylogin.common      .FlashContainer.class.getPackage())
                .addPackage(dev.luke10x.easylogin.topt        .TotpGenerator.class.getPackage())
                .addPackage(dev.luke10x.easylogin.testutils   .MockRegistry.class.getPackage())
                .addClass(RegistrationServiceAlternative.class)
                .addClass(UserServiceAlternative.class)
                .addClass(UserApplication.class);

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
        client = HtmlUnitWebClientFactory.createCommonWebClient();
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

    final static private String ERROR_PLACEHOLDER = "span[data-testid='registration-error']";
    @Test
    @DisplayName("Shows registration form")
    public void showsRegistrationForm() throws Exception {
        HtmlPage page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());

        assertEquals(200, page.getWebResponse().getStatusCode());

        final DomNode error = page.querySelector(ERROR_PLACEHOLDER);

        assertNull(error);
        assertEquals(200, page.getWebResponse().getStatusCode());
    }

    @Test
    @DisplayName("Submitting registration form with empty handle shows error")
    public void submittingRegistrationFormWithEmptyHandleShowsError()
            throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, IOException {
        final HtmlPage initialPage = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());
        var form = initialPage.getForms().get(0);
        form.getInputByName("handle").type("");

        final HtmlPage resultPage = form.getButtonByName("submit-registration").click();

        final String error = resultPage.querySelector(ERROR_PLACEHOLDER).getVisibleText();

        assertEquals("Name cannot be blank", error);
        assertEquals(400, resultPage.getWebResponse().getStatusCode());
        verify(registrationService.getMock(), times(0)).registerNewHandle(any());
    }

    @Test
    @DisplayName("Submitting filled registration form redirects to onboarding")
    public void redirectsForward()
            throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException, IOException {

        HtmlPage page = client.getPage(new URL(baseUrl, "mvc/register").toExternalForm());
        var form = page.getForms().get(0);
        form.getInputByName("handle").type("myname");

        Page resultPage = form.getButtonByName("submit-registration").click();

        // It is 404 because it redirected out of deployed ShrinkWrap web archive
        assertEquals(404, resultPage.getWebResponse().getStatusCode());
        assertEquals(
                new URL(baseUrl, "mvc/onboarding").getPath(),
                resultPage.getWebResponse().getWebRequest().getUrl().getPath());

        verify(registrationService.getMock(), times(1))
                .registerNewHandle(any());
    }
}
