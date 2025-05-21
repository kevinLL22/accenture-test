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

git clone [https://github.com/tu-org/franchise-service.git](https://github.com/kevinLL22/accenture-test.git)
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

## Despliegue de la base de datos en AWS con Terraform

### Prerrequisitos Cloud 📋

* **AWS CLI** instalada y autenticada (`aws configure`).
* **Terraform ≥ 1.7** instalado.
* **Bucket S3** versionado y **tabla DynamoDB** creados para el backend remoto.
* **Credenciales IAM** con permisos para RDS, EC2 (SG), IAM (Secrets Manager), S3 y DynamoDB.

### Estructura de archivos IaC 🔨

Crea la carpeta `infra/mysql/` en la raíz del proyecto con estos ficheros:

```
infra/mysql/
├─ backend.tf       # Configura S3 y DynamoDB como backend remoto
├─ variables.tf     # Declara las variables del stack (vpc_id, subnets, etc.)
├─ main.tf          # Define Security Group, módulo RDS, Secrets Manager y outputs
└─ dev.tfvars       # Valores concretos de tu entorno (no subir a GitHub)
```

### 1. Configurar el backend remoto

En `backend.tf`, apunta a tu bucket S3 y tu tabla DynamoDB:

```hcl
terraform {
  backend "s3" {
    bucket         = "<TU_BUCKET>"
    key            = "mysql/terraform.tfstate"
    region         = "<TU_REGION>"
    dynamodb_table = "<TU_TABLA_LOCKS>"
    encrypt        = true
  }
}
```

### 2. Definir variables

En `variables.tf` declara las entradas necesarias:

```hcl
variable "vpc_id" { type = string }
variable "private_subnet_ids" { type = list(string) }
variable "db_name" { type = string default = "appdb" }
variable "db_username" { type = string default = "appuser" }
variable "api_sg_id" { type = string }
# …otras variables genéricas: project, environment, aws_region
```

### 3. Crear los recursos en `main.tf`

Usa el módulo oficial `terraform-aws-modules/rds/aws` junto a:

* `aws_security_group` para MySQL.
* `random_password` para la contraseña.
* `aws_secretsmanager_secret` y `aws_secretsmanager_secret_version`.
* `output "r2dbc_url"` con la URL R2DBC.

*(Consulta el ejemplo completo en la carpeta `infra/mysql/main.tf`.)*

### 4. Rellenar `dev.tfvars`

Ejemplo de `infra/mysql/dev.tfvars` ():

```hcl
project            = "franchise"
environment        = "dev"
aws_region         = ""
vpc_id             = "vpc-1234"
private_subnet_ids = ["subnet-1234","subnet-1234"]
db_name            = "appdb"
db_username        = "appuser"
api_sg_id          = "sg-1234"

Cambiar a valores reales y eliminar el ".example" del archivo
```

### 5. Comandos Terraform

```bash
cd infra/mysql
terraform init
terraform plan -var-file=dev.tfvars
terraform apply -var-file=dev.tfvars -auto-approve
```

### 6. Obtener la URL de conexión

```bash
terraform output -raw r2dbc_url
# r2dbc:mysql://<ENDPOINT_RDS>:3306/appdb
```

### 7. Configurar Spring Boot en producción

En `src/main/resources/application-prod.yaml` añade:

```yaml
spring:
  r2dbc:
    url: ${R2DBC_URL}         # o copia directamente la URL
    username: ${DB_USER}
    password: ${DB_PASS}

  flyway:
    enabled: true
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    user: ${DB_USER}
    password: ${DB_PASS}

management:
  endpoints:
    web:
      exposure: health,info
```

Configura las variables de entorno (`R2DBC_URL`, `DB_USER`, `DB_PASS`, etc.) en tu despliegue.

----------------

## Uso de la API

La API está disponible en `http://localhost:8080` (perfil `dev`) o en la URL de tu entorno (perfil `prod`). A continuación se describen los endpoints agrupados por recurso.

### Franquicias (`/api/franchises`)

| Método | Ruta                   | Descripción           | Cuerpo JSON                                | Código | Respuesta                                                               |
| ------ | ---------------------- | --------------------- | ------------------------------------------ | ------ | ----------------------------------------------------------------------- |
| POST   | `/api/franchises`      | Crear franquicia      | `{ "name": "Tienda A"}`  | 201    | `{ "id": 1, "name": "Tienda A" }` |
| GET    | `/api/franchises`      | Listar todas          | —                                          | 200    | `[ {...}, {...} ]`                                                      |
| GET    | `/api/franchises/{id}` | Obtener por ID        | —                                          | 200    | `{ "id":1, ... }`                                                       |
| PUT    | `/api/franchises/{id}` | Actualizar franquicia | `{ "name": "Tienda B"}` | 200    | `{ "id":1, "name":"Tienda B"}`                  |
| DELETE | `/api/franchises/{id}` | Eliminar franquicia   | —                                          | 204    | —                                                                       |


### Sucursales (`/api/branches`)

| Método | Ruta                                       | Descripción                         | Cuerpo JSON                                  | Código | Respuesta                                              |
| ------ | ------------------------------------------ | ----------------------------------- | -------------------------------------------- | ------ | ------------------------------------------------------ |
| POST   | `/api/branches`                            | Crear sucursal                      | `{ "name": "Sucursal 1", "franchiseId": 1 }` | 201    | `{ "id": 10, "name": "Sucursal 1", "franchiseId": 1 }` |
| GET    | `/api/branches`                            | Listar todas                        | —                                            | 200    | `[ {...}, {...} ]`                                     |
| GET    | `/api/branches/{id}`                       | Obtener por ID                      | —                                            | 200    | `{ "id":10, ... }`                                     |
| PUT    | `/api/branches/{id}`                       | Actualizar sucursal                 | `{ "name": "Sucursal X", "franchiseId": 1 }` | 200    | `{ "id":10, "name":"Sucursal X", ...}`                 |
| DELETE | `/api/branches/{id}`                       | Eliminar sucursal                   | —                                            | 204    | —                                                      |
| GET    | `/api/branches/by-franchise/{franchiseId}` | Listar sucursales de una franquicia | —                                            | 200    | `[ {...}, {...} ]`                                     |


### Productos (`/api/products`)

| Método | Ruta                                                 | Descripción                             | Cuerpo JSON                                              | Código | Respuesta                                                       |
| ------ | ---------------------------------------------------- | --------------------------------------- | -------------------------------------------------------- | ------ | --------------------------------------------------------------- |
| POST   | `/api/products`                                      | Crear producto                          | `{ "name": "Producto A", "stock": 100, "branchId": 10 }` | 201    | `{ "id": 5, "name": "Producto A", "stock":100, "branchId":10 }` |
| GET    | `/api/products`                                      | Listar todos los productos              | —                                                        | 200    | `[ {...}, {...} ]`                                              |
| GET    | `/api/products/{id}`                                 | Obtener producto por ID                 | —                                                        | 200    | `{ "id":5, ... }`                                               |
| GET    | `/api/products/by-branch/{branchId}`                 | Listar productos de una sucursal        | —                                                        | 200    | `[ {...}, {...} ]`                                              |
| PUT    | `/api/products/{id}`                                 | Actualizar producto                     | `{ "name": "Producto B", "stock": 80, "branchId": 10 }`  | 200    | `{ "id":5, "name":"Producto B", ...}`                           |
| DELETE | `/api/products/{id}`                                 | Eliminar producto                       | —                                                        | 204    | —                                                               |
| GET    | `/api/products/by-franchise/{franchiseId}/max-stock` | Top producto por stock en cada sucursal | —                                                        | 200    | `[ {"branchId":10,...}, {...} ]`                                |

---

