package co.edu.javeriana.gestionacademica.service;

import co.edu.javeriana.gestionacademica.dto.EstudianteConMateriasDTO;
import co.edu.javeriana.gestionacademica.model.Estudiante;
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
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;
    private final EstudianteMateriaRepository estudianteMateriaRepository;
    private final MateriaRepository materiaRepository;

    @Transactional
    public Mono<Estudiante> crear(Estudiante estudiante) {
        return estudianteRepository.existsByEmail(estudiante.getEmail())
            .flatMap(existsEmail -> {
                if (existsEmail) {
                    return Mono.error(new IllegalArgumentException(
                        "Ya existe un estudiante con el email: " + estudiante.getEmail()));
                }
                return estudianteRepository.existsByCodigo(estudiante.getCodigo())
                    .flatMap(existsCodigo -> {
                        if (existsCodigo) {
                            return Mono.error(new IllegalArgumentException(
                                "Ya existe un estudiante con el código: " + estudiante.getCodigo()));
                        }
                        estudiante.setCreatedAt(LocalDateTime.now());
                        return estudianteRepository.save(estudiante);
                    });
            });
    }

    public Flux<Estudiante> listarTodos() {
        return estudianteRepository.findAll();
    }

    public Mono<Estudiante> obtenerPorId(Long id) {
        return estudianteRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Estudiante no encontrado")));
    }

    @Transactional
    public Mono<Estudiante> actualizar(Long id, Estudiante estudiante) {
        return estudianteRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Estudiante no encontrado")))
            .flatMap(estudianteExistente -> {
                Mono<Boolean> validacionEmail = Mono.just(true);
                Mono<Boolean> validacionCodigo = Mono.just(true);
                
                if (!estudianteExistente.getEmail().equals(estudiante.getEmail())) {
                    validacionEmail = estudianteRepository.existsByEmail(estudiante.getEmail())
                        .flatMap(exists -> exists ? 
                            Mono.error(new IllegalArgumentException("El email ya está en uso")) : 
                            Mono.just(false));
                }
                
                if (!estudianteExistente.getCodigo().equals(estudiante.getCodigo())) {
                    validacionCodigo = estudianteRepository.existsByCodigo(estudiante.getCodigo())
                        .flatMap(exists -> exists ? 
                            Mono.error(new IllegalArgumentException("El código ya está en uso")) : 
                            Mono.just(false));
                }
                
                return Mono.zip(validacionEmail, validacionCodigo)
                    .flatMap(tuple -> {
                        estudianteExistente.setNombre(estudiante.getNombre());
                        estudianteExistente.setApellido(estudiante.getApellido());
                        estudianteExistente.setEmail(estudiante.getEmail());
                        estudianteExistente.setCodigo(estudiante.getCodigo());
                        return estudianteRepository.save(estudianteExistente);
                    });
            });
    }

    @Transactional
    public Mono<Void> eliminar(Long id) {
        return estudianteRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Estudiante no encontrado")))
            .flatMap(estudiante -> estudianteRepository.deleteById(id));
    }

    public Mono<EstudianteConMateriasDTO> obtenerConMaterias(Long estudianteId) {
        return estudianteRepository.findById(estudianteId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Estudiante no encontrado")))
            .flatMap(estudiante -> 
                estudianteMateriaRepository.findByEstudianteId(estudianteId)
                    .flatMap(em -> materiaRepository.findById(em.getMateriaId()))
                    .collectList()
                    .map(materias -> new EstudianteConMateriasDTO(
                        estudiante.getId(),
                        estudiante.getNombre(),
                        estudiante.getApellido(),
                        estudiante.getEmail(),
                        estudiante.getCodigo(),
                        materias
                    ))
            );
    }
}