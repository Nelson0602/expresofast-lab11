# ExpresoFast

Aplicación para la gestión y el seguimiento de envíos, compuesta por un
backend REST desarrollado con Spring Boot y un frontend desarrollado con
Angular.

## Estructura del proyecto

```text
expresofast-lab11/
├── expresofast-backend/   # API REST con Spring Boot
├── expresofast-frontend/  # Interfaz web con Angular
└── README.md
```

## Requisitos

- Java 21 o superior
- Maven (también se incluye el Maven Wrapper en el backend)
- Node.js y npm
- Acceso a la base de datos configurada para el backend

## Backend

El backend expone la API en `http://localhost:8080`.

1. Configura las credenciales y la URL de la base de datos en
   `expresofast-backend/src/main/resources/application.properties`.
2. Ejecuta la aplicación desde la carpeta del backend:

   ```bash
   cd expresofast-backend
   .\mvnw.cmd spring-boot:run
   ```

   En Linux o macOS:

   ```bash
   ./mvnw spring-boot:run
   ```

## Frontend

El frontend se ejecuta por defecto en `http://localhost:4200`.

1. Instala las dependencias:

   ```bash
   cd expresofast-frontend
   npm install
   ```

2. Inicia el servidor de desarrollo:

   ```bash
   npm start
   ```

La configuración de la URL del backend se encuentra en
`expresofast-frontend/src/environments/`.

## Construcción y pruebas

Desde `expresofast-frontend`:

```bash
npm run build
npm test
```

Desde `expresofast-backend`:

```bash
.\mvnw.cmd test
```

## Documentación de la API

Con el backend en ejecución, la documentación OpenAPI/Swagger está disponible
en:

```text
http://localhost:8080/swagger-ui/index.html
```
