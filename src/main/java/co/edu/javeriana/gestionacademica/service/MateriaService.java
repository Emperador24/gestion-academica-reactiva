package co.edu.javeriana.gestionacademica.service;

import co.edu.javeriana.gestionacademica.dto.MateriaConEstudiantesDTO;
import co.edu.javeriana.gestionacademica.model.Materia;
import co.edu.javeriana.gestionacademica.repository.EstudianteMateriaRepository;
import co.edu.javeriana.gestionacademica.repository.EstudianteRepository;
import co.edu.javeriana.gestionacademica.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MateriaService {
    private final MateriaRepository materiaRepository;
    private final EstudianteMateriaRepository estudianteMateriaRepository;
    private final EstudianteRepository estudianteRepository;

    @Transactional
    public Mono<Materia> crear(Materia materia) {
        return materiaRepository.existsByNombre(materia.getNombre())
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new IllegalArgumentException(
                        "Ya existe una materia con el nombre: " + materia.getNombre()));
                }
                materia.setCreatedAt(LocalDateTime.now());
                return materiaRepository.save(materia);
            });
    }

    public Flux<Materia> listarTodas() {
        return materiaRepository.findAll();
    }

    public Mono<Materia> obtenerPorId(Long id) {
        return materiaRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Materia no encontrada")));
    }

    @Transactional
    public Mono<Materia> actualizar(Long id, Materia materia) {
        return materiaRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Materia no encontrada")))
            .flatMap(materiaExistente -> {
                if (!materiaExistente.getNombre().equals(materia.getNombre())) {
                    return materiaRepository.existsByNombre(materia.getNombre())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new IllegalArgumentException(
                                    "Ya existe una materia con el nombre: " + materia.getNombre()));
                            }
                            materiaExistente.setNombre(materia.getNombre());
                            materiaExistente.setCreditos(materia.getCreditos());
                            return materiaRepository.save(materiaExistente);
                        });
                }
                materiaExistente.setCreditos(materia.getCreditos());
                return materiaRepository.save(materiaExistente);
            });
    }

    @Transactional
    public Mono<Void> eliminar(Long id) {
        return materiaRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Materia no encontrada")))
            .flatMap(materia -> materiaRepository.deleteById(id));
    }

    public Mono<MateriaConEstudiantesDTO> obtenerConEstudiantes(Long materiaId) {
        return materiaRepository.findById(materiaId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Materia no encontrada")))
            .flatMap(materia -> 
                estudianteMateriaRepository.findByMateriaId(materiaId)
                    .flatMap(em -> estudianteRepository.findById(em.getEstudianteId()))
                    .collectList()
                    .map(estudiantes -> new MateriaConEstudiantesDTO(
                        materia.getId(),
                        materia.getNombre(),
                        materia.getCreditos(),
                        estudiantes
                    ))
            );
    }
}