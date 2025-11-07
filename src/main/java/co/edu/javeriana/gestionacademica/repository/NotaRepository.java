package co.edu.javeriana.gestionacademica.repository;

import co.edu.javeriana.gestionacademica.model.Nota;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NotaRepository extends ReactiveCrudRepository<Nota, Long> {
    Flux<Nota> findByEstudianteMateriaId(Long estudianteMateriaId);
    
    @Query("SELECT COALESCE(SUM(porcentaje), 0) FROM notas WHERE estudiante_materia_id = :estudianteMateriaId")
    Mono<Integer> sumPorcentajesByEstudianteMateriaId(Long estudianteMateriaId);
    
    @Query("SELECT COALESCE(SUM(porcentaje), 0) FROM notas WHERE estudiante_materia_id = :estudianteMateriaId AND id != :notaId")
    Mono<Integer> sumPorcentajesExcludingNota(Long estudianteMateriaId, Long notaId);
}