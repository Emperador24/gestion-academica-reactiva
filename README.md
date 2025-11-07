gestion-academica-reactiva/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── co/edu/javeriana/gestionacademica/
│   │   │       ├── GestionAcademicaReactivaApplication.java
│   │   │       ├── config/
│   │   │       │   ├── DatabaseConfig.java
│   │   │       │   └── WebConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── EstudianteController.java
│   │   │       │   ├── InscripcionController.java
│   │   │       │   ├── MateriaController.java
│   │   │       │   └── NotaController.java
│   │   │       ├── dto/
│   │   │       │   ├── ErrorResponse.java
│   │   │       │   ├── EstudianteConMateriasDTO.java
│   │   │       │   ├── InscripcionDTO.java
│   │   │       │   ├── MateriaConEstudiantesDTO.java
│   │   │       │   ├── NotaAcumuladaDTO.java
│   │   │       │   └── NotaConDetallesDTO.java
│   │   │       ├── exception/
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       ├── model/
│   │   │       │   ├── Estudiante.java
│   │   │       │   ├── EstudianteMateria.java
│   │   │       │   ├── Materia.java
│   │   │       │   └── Nota.java
│   │   │       ├── repository/
│   │   │       │   ├── EstudianteMateriaRepository.java
│   │   │       │   ├── EstudianteRepository.java
│   │   │       │   ├── MateriaRepository.java
│   │   │       │   └── NotaRepository.java
│   │   │       └── service/
│   │   │           ├── EstudianteService.java
│   │   │           ├── InscripcionService.java
│   │   │           ├── MateriaService.java
│   │   │           └── NotaService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       └── java/
└── pom.xml

frontend/
├── public/
│   └── index.html
├── src/
│   ├── App.jsx
│   └── index.js
├── package.json
└── README.md