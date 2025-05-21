# Franchise Service

Este proyecto es una API reactiva construida con Spring Boot WebFlux y R2DBC, que utiliza MySQL como base de datos. A continuación se detallan los elementos necesarios para ejecutar localmente el proyecto en tu máquina.

## Prerrequisitos

### Herramientas de sistema

* **Java 21 JDK** instalado y en el `PATH` (`java -version`).
* **Maven** (o usar el *wrapper* incluido `./mvnw`).
* **Git** para clonar el repositorio.
* **Docker Desktop** con:

  * **Docker Engine** (motor de contenedores).
  * **Docker Compose v2** para orquestar múltiples contenedores.
* **cURL** o **Postman** (opcional) para probar endpoints REST.

### Archivos y configuraciones del proyecto

* Archivo **`pom.xml`** (con todas las dependencias de Spring Boot, WebFlux, R2DBC, Flyway, etc.).
* **`Dockerfile`** multi-stage en la raíz del proyecto.
* **`docker-compose.yml`** en la raíz del proyecto, que define servicios:

  * `db`: contenedor MySQL 8 con healthcheck.
  * `api`: la aplicación Spring Boot.
* **`src/main/resources/application-dev.yaml`** con la configuración de conexión R2DBC a MySQL local.
* Carpetas de migraciones Flyway en **`src/main/resources/db/migration`** (archivos `V*_*.sql`).

### Variables de entorno

Al arrancar localmente con Docker Compose se usa el perfil **`dev`**:

```bash
export SPRING_PROFILES_ACTIVE=dev
```

Los valores de conexión (usuario/contraseña) están hardcodeados para desarrollo en el Compose (root/root), pero en producción se inyectan desde Secrets Manager.

## Flujo de ejecución local

1. **Clonar** el repositorio:

   ```bash
   ```

git clone [https://github.com/tu-org/franchise-service.git](https://github.com/tu-org/franchise-service.git)
cd franchise-service

````
2. **Construir** el JAR (opcional, Docker Compose lo hará en la build):
```bash
./mvnw clean package -DskipTests
````

3. **Arrancar** la API + MySQL:

   ```bash
   ```

docker compose up --build

````
4. **Verificar** salud de la API:
```bash
curl http://localhost:8080/actuator/health
# Debe devolver { "status": "UP", ... }
````

---
