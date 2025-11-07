package co.edu.javeriana.gestionacademica.repository;

import co.edu.javeriana.gestionacademica.model.Materia;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface MateriaRepository extends ReactiveCrudRepository<Materia, Long> {
    Mono<Boolean> existsByNombre(String nombre);
    Mono<Materia> findByNombre(String nombre);
}
