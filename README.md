# Sistema de Gestión Académica Reactiva

Sistema de gestión académica desarrollado con Spring WebFlux y programación reactiva para la Universidad Javeriana.

## 📋 Descripción

Aplicación web reactiva que permite administrar estudiantes, materias y notas con cálculo automático de promedios ponderados. Implementa el stack reactivo completo con Spring WebFlux, R2DBC y React.

## 🚀 Tecnologías Utilizadas

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring WebFlux (Programación Reactiva)
- Spring Data R2DBC
- PostgreSQL con R2DBC Driver
- Project Reactor
- Lombok
- Maven

### Frontend
- React 18.2.0
- Tailwind CSS
- Lucide React (iconos)

## 📦 Requisitos Previos

- Java JDK 17 o superior
- Maven 3.6+
- PostgreSQL 12 o superior
- Node.js 16+ y npm (para el frontend)

## 🔧 Configuración e Instalación

### 1. Configurar Base de Datos

```bash
# Crear base de datos en PostgreSQL
createdb gestion_academica

# O usando psql
psql -U postgres
CREATE DATABASE gestion_academica;
```

### 2. Configurar Credenciales

Editar `src/main/resources/application.properties`:

```properties
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/gestion_academica
spring.r2dbc.username=tu_usuario
spring.r2dbc.password=tu_contraseña
```

### 3. Crear Esquema de Base de Datos

Ejecutar el script `src/main/resources/schema.sql` en PostgreSQL:

```bash
psql -U tu_usuario -d gestion_academica -f src/main/resources/schema.sql
```

### 4. Compilar y Ejecutar Backend

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080`

### 5. Instalar y Ejecutar Frontend

```bash
# Navegar al directorio frontend
cd frontend

# Instalar dependencias
npm install

# Iniciar servidor de desarrollo
npm start
```

La aplicación frontend estará disponible en: `http://localhost:3000`

## 📚 Estructura del Proyecto

```
gestion-academica-reactiva/
├── src/main/java/co/edu/javeriana/gestionacademica/
│   ├── config/              # Configuraciones (CORS, Database, WebFlux)
│   ├── controller/          # Controladores REST reactivos
│   ├── dto/                 # Data Transfer Objects
│   ├── exception/           # Manejo global de excepciones
│   ├── model/              # Entidades del dominio
│   ├── repository/         # Repositorios R2DBC
│   └── service/            # Lógica de negocio
├── src/main/resources/
│   ├── application.properties
│   └── schema.sql
└── frontend/
    └── src/
        ├── App.jsx         # Componente principal
        └── index.css       # Estilos Tailwind
```

## 🎯 Funcionalidades Principales

### Gestión de Materias
- ✅ Crear, actualizar y eliminar materias
- ✅ Listar materias con endpoint de streaming (Server-Sent Events)
- ✅ Ver estudiantes inscritos por materia
- ✅ Validación de nombres únicos

### Gestión de Estudiantes
- ✅ CRUD completo de estudiantes
- ✅ Validación de email y código únicos
- ✅ Ver materias inscritas por estudiante

### Gestión de Inscripciones
- ✅ Inscribir estudiantes en materias
- ✅ Desinscribir estudiantes
- ✅ Validación de inscripciones duplicadas

### Gestión de Notas
- ✅ Registrar notas con valor y porcentaje
- ✅ Validación de porcentajes (máximo 100% por materia)
- ✅ Cálculo reactivo de nota acumulada
- ✅ Proyección de nota final
- ✅ Actualizar y eliminar notas

## 🔄 Características Reactivas

### Backpressure
El endpoint `/api/materias/stream` implementa contrapresión mediante Server-Sent Events:

```java
@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Materia> listarTodasStream() {
    return materiaService.listarTodas()
        .delayElements(Duration.ofMillis(500));
}
```

### Transacciones Reactivas
Todas las operaciones de escritura utilizan `@Transactional` para garantizar consistencia:

```java
@Transactional
public Mono<Nota> crear(Nota nota) {
    return validarPorcentaje(...)
        .flatMap(valido -> notaRepository.save(nota));
}
```

### Composición de Operadores
Uso extensivo de operadores reactivos:
- `flatMap`: transformaciones asíncronas
- `switchIfEmpty`: manejo de casos sin resultados
- `zip`: combinación de múltiples flujos
- `collectList`: agregación de resultados

## 📊 Modelo de Datos

```
Estudiante (1) ──── (*) EstudianteMateria (*) ──── (1) Materia
                              │
                              │ (1)
                              │
                             (*) Nota
```

### Validaciones Implementadas
- Email único por estudiante
- Código único por estudiante
- Nombre único por materia
- Notas entre 0.0 y 5.0
- Porcentajes entre 1 y 100
- Suma de porcentajes ≤ 100% por materia
- Créditos > 0

## 🧮 Cálculo de Notas

### Nota Acumulada
```
NotaAcumulada = Σ(Nota × Porcentaje / 100)
```

### Nota Final Proyectada
```
Si PorcentajeAcumulado < 100:
    NotaFinalProyectada = NotaAcumulada × (100 / PorcentajeAcumulado)
    
Si PorcentajeAcumulado = 100:
    NotaFinalProyectada = NotaAcumulada
```

## 🔍 Endpoints API

### Materias
- `POST /api/materias` - Crear materia
- `GET /api/materias` - Listar todas
- `GET /api/materias/stream` - Streaming con backpressure
- `GET /api/materias/{id}` - Obtener por ID
- `GET /api/materias/{id}/estudiantes` - Estudiantes de la materia
- `PUT /api/materias/{id}` - Actualizar
- `DELETE /api/materias/{id}` - Eliminar

### Estudiantes
- `POST /api/estudiantes` - Crear estudiante
- `GET /api/estudiantes` - Listar todos
- `GET /api/estudiantes/{id}` - Obtener por ID
- `GET /api/estudiantes/{id}/materias` - Materias del estudiante
- `PUT /api/estudiantes/{id}` - Actualizar
- `DELETE /api/estudiantes/{id}` - Eliminar

### Inscripciones
- `POST /api/inscripciones` - Inscribir estudiante
- `GET /api/inscripciones?estudianteId=X&materiaId=Y` - Obtener relación
- `DELETE /api/inscripciones` - Desinscribir

### Notas
- `POST /api/notas` - Crear nota
- `PUT /api/notas/{id}` - Actualizar
- `DELETE /api/notas/{id}` - Eliminar
- `GET /api/notas/materia/{id}` - Notas de una materia
- `GET /api/notas/estudiante/{estudianteId}/materia/{materiaId}/acumulada` - Calcular nota acumulada

## 🛡️ Manejo de Errores

El sistema implementa un manejador global de excepciones (`GlobalExceptionHandler`) que captura:
- Errores de validación
- Argumentos ilegales
- Excepciones genéricas

Respuesta de error ejemplo:
```json
{
  "error": "Bad Request",
  "message": "Ya existe un estudiante con el email: juan@javeriana.edu.co",
  "timestamp": "2025-11-08T10:30:00",
  "path": "/api/estudiantes"
}
```

## 🧪 Pruebas

### Probar Backpressure
```bash
curl -N http://localhost:8080/api/materias/stream
```

Verás las materias llegando con un delay de 500ms entre cada una.

## 📝 Notas de Desarrollo

- El frontend usa `fetch` API para consumir los endpoints REST
- CORS está habilitado para permitir peticiones desde `http://localhost:3000`
- El pool de conexiones R2DBC está configurado con 10-20 conexiones
- Los logs están en modo DEBUG para desarrollo

## 👥 Autores

Proyecto desarrollado para el curso de Teoría de la Computación - Universidad Javeriana

## 📄 Licencia

Este proyecto es de uso académico para la Universidad Javeriana.
