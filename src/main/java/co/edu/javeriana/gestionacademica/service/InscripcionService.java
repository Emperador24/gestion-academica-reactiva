package co.edu.javeriana.gestionacademica.service;

import co.edu.javeriana.gestionacademica.model.EstudianteMateria;
import co.edu.javeriana.gestionacademica.repository.EstudianteMateriaRepository;
import co.edu.javeriana.gestionacademica.repository.EstudianteRepository;
import co.edu.javeriana.gestionacademica.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InscripcionService {
    private final EstudianteMateriaRepository estudianteMateriaRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;

    @Transactional
    public Mono<EstudianteMateria> inscribir(Long estudianteId, Long materiaId) {
        return estudianteRepository.findById(estudianteId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Estudiante no encontrado")))
            .flatMap(estudiante -> materiaRepository.findById(materiaId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Materia no encontrada")))
            )
            .flatMap(materia -> 
                estudianteMateriaRepository.existsByEstudianteIdAndMateriaId(estudianteId, materiaId)
                    .flatMap(exists -> {
                        if (exists) {
                            return Mono.error(new IllegalArgumentException(
                                "El estudiante ya está inscrito en esta materia"));
                        }
                        EstudianteMateria em = new EstudianteMateria();
                        em.setEstudianteId(estudianteId);
                        em.setMateriaId(materiaId);
                        em.setCreatedAt(LocalDateTime.now());
                        return estudianteMateriaRepository.save(em);
                    })
            );
    }

    public Mono<EstudianteMateria> obtenerRelacion(Long estudianteId, Long materiaId) {
        return estudianteMateriaRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException(
                "El estudiante no está inscrito en esta materia")));
    }

    @Transactional
    public Mono<Void> desinscribir(Long estudianteId, Long materiaId) {
        return estudianteMateriaRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException(
                "El estudiante no está inscrito en esta materia")))
            .flatMap(em -> estudianteMateriaRepository.deleteById(em.getId()));
    }
}