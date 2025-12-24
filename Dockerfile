# ===== ETAPA DE CONSTRUCCIÓN =====
FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /sentiment-tech-api
COPY pom.xml .
COPY src ./src

# Cachea dependencias y construye el JAR
RUN mvn clean package -DskipTests

# ===== ETAPA DE EJECUCIÓN =====
FROM eclipse-temurin:21-jre-jammy

WORKDIR /sentiment-tech-api

# Copia SOLO el JAR desde la etapa builder
COPY --from=builder /sentiment-tech-api/target/*.jar sentiment-tech-api.jar

# Asegura permisos (opcional pero recomendado)
RUN chmod +x sentiment-tech-api.jar

# Variables configurables
ENV PORT=8080
EXPOSE $PORT

# Comando de arranque
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "sentiment-tech-api.jar"]
