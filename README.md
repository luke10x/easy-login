# Easy Login - OpenID Connect authentication provider

Easy password-less logins for your web-apps.

This project is originally be meant to be both practical and exploration of technology.

## OpeinID Connect authentication server stub in development

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

### Resources
- [Jakarta MVC Spec](https://jakarta.ee/specifications/mvc/2.0/jakarta-mvc-spec-2.0.html#redirect)
- [Jakarta & Krazo @ Mastertheboss](https://www.mastertheboss.com/java-ee/jakarta-ee/jakarta-mvc-made-simple/)
- [Jakarta & Krazo @ Baeldung](https://www.baeldung.com/java-ee-mvc-eclipse-krazo)