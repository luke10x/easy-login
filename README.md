# Easy Login - OpenID Connect authentication provider

Easy password-less logins for your web-apps.

This project is originally be meant to be both practical and exploration of technology.


## OpenID Connect authentication server stub in development

From practical point of view it is an OpenID Connect compatible authentication server.
Mostly to be used in development/testing environments
and maybe for some not mission critical production settings.
But the main intention is to facilitate development environments, giving developers a test-authentication
server, which they can run in their dev environment 
allowing to develop authentication-enabled apps offline.
While, in production this will be swapped with full-featured authentication-server solution,
like [Keycloak](https://www.keycloak.org/). 


### Exploration of Jakarta web stack

Developing web applications in Java, Spring Framework is often used as a default choice.
Yet, Jakarta EE platform has its MVC implementation provided by
[Krazo](https://projects.eclipse.org/projects/ee4j.krazo).

Given that Jakarta EE is the official standard for developing enterprise-level Java applications,
including web applications it is necessary to explore this technology,
and evaluate its fit for purpose, before just jumping into Spring hype-train.

Also, I need some clean reference implementation of authentication server,
for my other projects.

This projects also does some exploration into TOTP,
as it uses TOTP as its primary authentication method.
On one hand it provides a trendy "password-less" authentication,
yet, TOTP is primarily designed for a second factor authentication,
and using it as one and only way to authenticate,
may not be the best idea from security point of view.


### Development 

Start wildfly with local DB:

    docker-compose up -d --force-recreate --build

There is a development helper script [deploy.txt](./deploy.txt)
which rebuilds and deploys the war to local container.

To shell into local database client:

    docker-compose exec db psql postgresql://easylogin_user@localhost:5432/easylogin_db

To shell to local wildfly console:

    docker-compose exec wildfly bash -c '$JBOSS_HOME/bin/jboss-cli.sh --connect'

Development web UI accessible:

- [WildFly dashboard](http://0.0.0.0:19990/)
- [Easy Login dashboard](http://0.0.0.0:19980/)


# IntelliJ settings

To run tests using the green triangle is better to set a template for Junit run configurations,
which has default VM options set to:

    -ea -Djboss.home=build/wildfly-27.0.1/wildfly-27.0.1.Final

To debug Arquillian tests from IntelliJ a Remote JVM Debug configuration need to be added with
Debugger mode set to "Listening on remote JVM" port 15007.

To debug Wildfly running in a Docker container a Remote JVM Debug configuration need to be added with
Debugger mode set to "Attach to remote JVM" port 15005.


### Issues and Workarounds

I have resolves some issues with the platform, and some of them are just common pitfalls,
but some are strange and not well known behaviours.

- JSF does not seem to work with the "latest" tag of [Wildfly image](https://quay.io/repository/wildfly/wildfly).
  Using "27.0.1.Final-jdk17" works.lk
- Must use `Response.accepted("redirect:onboarding").build()` syntax for redirects
  if @RedirectScope is used. Using long syntax of the Response builder with 2XX codes does not seem to follow
  Location header; and using 3XX code redirects but cannot access object ser to @RedirectScope before redirection.
- If @Deployment(testable = false), then the during the test it stops on breakpoints set in main code using remote
  debug configuration on the container (On breakpoints in test code is possible to break using local debug for junit 
  run). But this mode makes it impossible to use Mockito mocks.
- If @Deployment(testable = true) it is possible to use Mockito mocks as the tests run in the container using this
  mode, but for some reason it does not seem to stop on breakpoints while debugging.
- As kind of separate issue, the tests cannot start if IDE debugger is not listening.
- Does not look like tests run in Testable Deployments at all that can be todo with missing Junit dependencies 
  in the shrinkwrap archive of the web container.
  Sometimes to spot errors like that is easy as Test result is successful(!), but in reality no test has run. 
  `07:29:07,646 INFO  [org.jboss.weld.Bootstrap] (Weld Thread Pool -- 2) WELD-000119: Not generating any bean definitions from dev.luke10x.easylogin.registration.RegistrationControllerTest because of underlying class loading error: Type com.gargoylesoftware.htmlunit.WebClient from [Module "deployment.44fde11d-8f41-4669-8169-32e44babfa56.war" from Service Module Loader] not found.  If this is unexpected, enable DEBUG logging to see the full error.`
  I think that in this case it should treat the failure as critical and fail the test.
- Found Mockito configuration that works. Unfortunately that is an old version. With this version now
  MockitoAnnotations.openMocks does not work...


### Resources

- [Jakarta MVC Spec](https://jakarta.ee/specifications/mvc/2.0/jakarta-mvc-spec-2.0.html#redirect)
- [Jakarta & Krazo @ Mastertheboss](https://www.mastertheboss.com/java-ee/jakarta-ee/jakarta-mvc-made-simple/)
- [Jakarta & Krazo @ Baeldung](https://www.baeldung.com/java-ee-mvc-eclipse-krazo)
- [Another example by Hantsy](https://github.com/hantsy/jakartaee-mvc-sample/blob/master/pom.xml)
- [Getting started with JSF](https://www.mastertheboss.com/java-ee/jsf/getting-started-with-jsf-4-0-on-wildfly-27/)
- [About Jakarta Faces without MVC](https://www.mastertheboss.com/java-ee/jsf/getting-started-with-jsf-4-0-on-wildfly-27/)
