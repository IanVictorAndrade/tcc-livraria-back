# Use uma imagem base do OpenJDK
FROM openjdk:17

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR para o diretório de trabalho
COPY target/tcc-livraria-back-0.0.1-SNAPSHOT.jar app.jar

# Defina o comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]