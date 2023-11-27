FROM maven:3.9.5-eclipse-temurin-21-alpine AS builder
COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml -DskipTests=true clean package

FROM maven:3.9.5-eclipse-temurin-21-alpine
COPY --from=builder /usr/src/app/target/*.jar app.jar

COPY ./docker/certificates/KeycloakStaging.crt $JAVA_HOME/lib/security
COPY ./docker/certificates/KeycloakProduction.crt $JAVA_HOME/lib/security

RUN cd $JAVA_HOME/lib/security && \
    keytool -keystore cacerts -storepass changeit -noprompt -trustcacerts -importcert  \
    -alias "keycloak-staging" -file "./KeycloakStaging.crt"

RUN cd $JAVA_HOME/lib/security && \
    keytool -keystore cacerts -storepass changeit -noprompt -trustcacerts -importcert  \
    -alias "keycloak-production" -file "./KeycloakProduction.crt"

EXPOSE $SERVER_PORT

ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=$PROFILE

ENTRYPOINT [ "java", "-jar", "app.jar" ]