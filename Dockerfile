# ============================================================
# STAGE 1 - BUILD THE SPRING BOOT APPLICATION
# ============================================================
# This stage uses the full Java JDK because Maven needs
# a compiler to build the application JAR.
# ============================================================

FROM eclipse-temurin:21-jdk-jammy AS build


# ------------------------------------------------------------
# Set the working directory inside the Docker build container.
# All following commands will run from /app.
# ------------------------------------------------------------

WORKDIR /app


# ------------------------------------------------------------
# Copy Maven Wrapper files first.
#
# Why?
# Docker can cache dependency-related layers.
# If only Java source code changes, Maven dependencies do not
# need to be downloaded again unnecessarily.
# ------------------------------------------------------------

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./


# ------------------------------------------------------------
# Git Bash/Windows can save mvnw with CRLF line endings.
# Linux containers expect LF.
#
# sed removes the Windows carriage-return character.
# chmod makes the Maven wrapper executable.
# ------------------------------------------------------------

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw


# ------------------------------------------------------------
# Download Maven dependencies before copying application code.
#
# This improves Docker build caching.
# ------------------------------------------------------------

RUN ./mvnw \
    --batch-mode \
    --no-transfer-progress \
    dependency:go-offline


# ------------------------------------------------------------
# Copy the actual Spring Boot source code.
# ------------------------------------------------------------

COPY src/ src/


# ------------------------------------------------------------
# Build the Spring Boot JAR.
#
# - clean      : removes previous build output
# - package    : creates the application JAR
# - DskipTests : tests are already executed in GitHub Actions,
#                so Docker does not run them again.
#
# After Maven builds the application, find the real executable
# JAR and copy it to a simple fixed name: /app/app.jar
#
# Using app.jar means the runtime container does not need to
# know whether the Maven version is 1.0.5.1, 1.0.10.1, etc.
# ------------------------------------------------------------

RUN ./mvnw \
    --batch-mode \
    --no-transfer-progress \
    clean package \
    -DskipTests \
    && JAR_FILE="$(find target \
        -maxdepth 1 \
        -type f \
        -name '*.jar' \
        ! -name 'original-*.jar' \
        ! -name '*-sources.jar' \
        ! -name '*-javadoc.jar' \
        | head -1)" \
    && test -n "$JAR_FILE" \
    && cp "$JAR_FILE" /app/app.jar



# ============================================================
# STAGE 2 - RUN THE APPLICATION
# ============================================================
# We do NOT need Maven or the full JDK anymore.
#
# We only need Java Runtime Environment (JRE) to start the JAR.
#
# This keeps the final Docker image smaller and cleaner.
# ============================================================

FROM eclipse-temurin:21-jre-jammy


# ------------------------------------------------------------
# Application working directory.
# ------------------------------------------------------------

WORKDIR /app


# ------------------------------------------------------------
# Create a dedicated non-root Linux user.
#
# Why?
# Running an application as root inside a container is a
# security risk.
#
# UID 10001 is used consistently across our Boutique services.
# ------------------------------------------------------------

RUN useradd \
    --system \
    --uid 10001 \
    --no-create-home \
    appuser


# ------------------------------------------------------------
# Copy ONLY the final JAR from the build stage.
#
# --chown makes UID 10001 the owner of the application JAR.
# ------------------------------------------------------------

COPY --from=build \
    --chown=10001:10001 \
    /app/app.jar \
    /app/app.jar


# ------------------------------------------------------------
# Everything after this runs as the non-root application user.
# ------------------------------------------------------------

USER 10001


# ------------------------------------------------------------
# Application port.
#
# IMPORTANT:
# Change this according to the actual microservice.
#
# Example:
# Order Service   = 8084
# Payment Service = 8085
# ------------------------------------------------------------

EXPOSE 8085


# ------------------------------------------------------------
# Start the Spring Boot application.
#
# -Duser.timezone=UTC
#     Keeps application timestamps consistent.
#
# -XX:MaxRAMPercentage=75.0
#     Allows Java to use about 75% of the container memory
#     instead of trying to consume all available memory.
#
# -jar /app/app.jar
#     Starts our Spring Boot application.
# ------------------------------------------------------------

ENTRYPOINT ["java","-Duser.timezone=UTC","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]