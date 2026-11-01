# --- Etapa de build ---
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package

# --- Etapa de runtime ---
FROM tomcat:11.0-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

# O WAR e expandido no build para o runtime poder ter filesystem somente leitura.
COPY --from=build /app/target/*.war /tmp/ROOT.war
RUN mkdir -p /usr/local/tomcat/webapps/ROOT \
    && cd /usr/local/tomcat/webapps/ROOT \
    && jar -xf /tmp/ROOT.war \
    && rm /tmp/ROOT.war \
    && chown -R 1000:1000 /usr/local/tomcat
USER 1000:1000

EXPOSE 8080

CMD ["catalina.sh", "run"]
