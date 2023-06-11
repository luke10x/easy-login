# Easy Login - OpenID Connect authentication provider

Easy password-less logins for your web-apps.

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