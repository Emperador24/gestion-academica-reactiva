-- Eliminar tablas si existen
DROP TABLE IF EXISTS notas CASCADE;
DROP TABLE IF EXISTS estudiante_materia CASCADE;
DROP TABLE IF EXISTS estudiantes CASCADE;
DROP TABLE IF EXISTS materias CASCADE;

-- Tabla de Materias
CREATE TABLE materias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    creditos INTEGER NOT NULL CHECK (creditos > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Estudiantes
CREATE TABLE estudiantes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla intermedia Estudiante-Materia
CREATE TABLE estudiante_materia (
    id SERIAL PRIMARY KEY,
    estudiante_id INTEGER NOT NULL REFERENCES estudiantes(id) ON DELETE CASCADE,
    materia_id INTEGER NOT NULL REFERENCES materias(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(estudiante_id, materia_id)
);

-- Tabla de Notas
CREATE TABLE notas (
    id SERIAL PRIMARY KEY,
    estudiante_materia_id INTEGER NOT NULL REFERENCES estudiante_materia(id) ON DELETE CASCADE,
    valor DECIMAL(3,2) NOT NULL CHECK (valor >= 0.0 AND valor <= 5.0),
    porcentaje INTEGER NOT NULL CHECK (porcentaje > 0 AND porcentaje <= 100),
    descripcion VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_estudiante_materia_estudiante ON estudiante_materia(estudiante_id);
CREATE INDEX idx_estudiante_materia_materia ON estudiante_materia(materia_id);
CREATE INDEX idx_notas_estudiante_materia ON notas(estudiante_materia_id);