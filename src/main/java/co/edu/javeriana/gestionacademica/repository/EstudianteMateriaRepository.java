package co.edu.javeriana.gestionacademica.repository;

import co.edu.javeriana.gestionacademica.model.EstudianteMateria;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EstudianteMateriaRepository extends ReactiveCrudRepository<EstudianteMateria, Long> {
    Flux<EstudianteMateria> findByEstudianteId(Long estudianteId);
    Flux<EstudianteMateria> findByMateriaId(Long materiaId);
    Mono<EstudianteMateria> findByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
    Mono<Boolean> existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);
}