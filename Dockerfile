ARG JAVA_IMAGE
FROM $JAVA_IMAGE
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:InitialRAMPercentage=75", "-XX:MaxRAMPercentage=75", "-Djava.security.egd=file:/dev/./urandom", "-cp", "app.jar", "-Dloader.path=ssl", "org.springframework.boot.loader.launch.PropertiesLauncher"]
