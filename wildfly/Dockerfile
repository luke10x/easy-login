FROM quay.io/wildfly/wildfly:27.0.1.Final-jdk17
RUN wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar -P ${JBOSS_HOME}
COPY ./scripts /scripts
