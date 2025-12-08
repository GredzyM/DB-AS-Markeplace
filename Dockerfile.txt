# Usa una imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de la app
WORKDIR /app

# Copia el jar compilado al contenedor
COPY target/*.jar app.jar

# Expone el puerto que Render asignará
ENV PORT=8080
EXPOSE 8080

# Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
