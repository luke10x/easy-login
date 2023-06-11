#!/bin/bash
PASSWORD=password
KEYFILE=myFileName
FQHN=mydomain.com
$JBOSS_HOME/bin/jboss-cli.sh --connect <<EOF
  batch
  module add --name=org.postgresql \
    --resources=/opt/jboss/wildfly/postgresql-42.6.0.jar \
    --dependencies=javax.api,javax.transaction.api

  /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql)

  data-source add --jndi-name=java:/jboss/PostgresqlDS \
    --name=PostgresPool \
    --connection-url=jdbc:postgresql://${POSTGRES_CONNECTION} \
    --driver-name=postgresql \
    --user-name=${POSTGRES_USER} \
    --password=${POSTGRES_PASSWORD}
  run-batch
  exit
EOF
