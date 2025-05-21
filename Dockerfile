# ---------- Etapa 1: build ----------
FROM eclipse-temurin:21 AS builder
WORKDIR /workspace
COPY . .
RUN ./mvnw -q -DskipTests package

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
