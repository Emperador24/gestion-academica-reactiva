package co.edu.javeriana.gestionacademica.repository;

import co.edu.javeriana.gestionacademica.model.Estudiante;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EstudianteRepository extends ReactiveCrudRepository<Estudiante, Long> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByCodigo(String codigo);
    Mono<Estudiante> findByEmail(String email);
}