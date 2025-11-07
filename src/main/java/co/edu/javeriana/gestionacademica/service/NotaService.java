// ========== NotaService.java ==========
package co.edu.javeriana.gestionacademica.service;

import co.edu.javeriana.gestionacademica.dto.NotaAcumuladaDTO;
import co.edu.javeriana.gestionacademica.dto.NotaConDetallesDTO;
import co.edu.javeriana.gestionacademica.model.EstudianteMateria;
import co.edu.javeriana.gestionacademica.model.Nota;
import co.edu.javeriana.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepository;
    private final EstudianteMateriaRepository estudianteMateriaRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;

    @Transactional
    public Mono<Nota> crear(Nota nota) {
        return estudianteMateriaRepository.findById(nota.getEstudianteMateriaId())
            .switchIfEmpty(Mono.error(new IllegalArgumentException(
                "La relación estudiante-materia no existe")))
            .flatMap(em -> validarPorcentaje(nota.getEstudianteMateriaId(), nota.getPorcentaje(), null))
            .flatMap(valido -> {
                nota.setCreatedAt(LocalDateTime.now());
                nota.setUpdatedAt(LocalDateTime.now());
                return notaRepository.save(nota);
            });
    }

    @Transactional
    public Mono<Nota> actualizar(Long id, Nota nota) {
        return notaRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nota no encontrada")))
            .flatMap(notaExistente -> 
                validarPorcentaje(notaExistente.getEstudianteMateriaId(), 
                                nota.getPorcentaje(), id)
                    .flatMap(valido -> {
                        notaExistente.setValor(nota.getValor());
                        notaExistente.setPorcentaje(nota.getPorcentaje());
                        notaExistente.setDescripcion(nota.getDescripcion());
                        notaExistente.setUpdatedAt(LocalDateTime.now());
                        return notaRepository.save(notaExistente);
                    })
            );
    }

    @Transactional
    public Mono<Void> eliminar(Long id) {
        return notaRepository.findById(id)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Nota no encontrada")))
            .flatMap(nota -> notaRepository.deleteById(id));
    }

    public Flux<NotaConDetallesDTO> listarPorMateria(Long materiaId) {
        return estudianteMateriaRepository.findByMateriaId(materiaId)
            .flatMap(em -> 
                notaRepository.findByEstudianteMateriaId(em.getId())
                    .flatMap(nota -> 
                        Mono.zip(
                            estudianteRepository.findById(em.getEstudianteId()),
                            materiaRepository.findById(em.getMateriaId())
                        ).map(tuple -> new NotaConDetallesDTO(
                            nota.getId(),
                            nota.getValor(),
                            nota.getPorcentaje(),
                            nota.getDescripcion(),
                            tuple.getT1().getNombre() + " " + tuple.getT1().getApellido(),
                            tuple.getT2().getNombre()
                        ))
                    )
            );
    }

    public Mono<NotaAcumuladaDTO> calcularNotaAcumulada(Long estudianteId, Long materiaId) {
        return estudianteMateriaRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId)
            .switchIfEmpty(Mono.error(new IllegalArgumentException(
                "El estudiante no está inscrito en esta materia")))
            .flatMap(em -> 
                notaRepository.findByEstudianteMateriaId(em.getId())
                    .flatMap(nota -> 
                        Mono.zip(
                            estudianteRepository.findById(em.getEstudianteId()),
                            materiaRepository.findById(em.getMateriaId())
                        ).map(tuple -> new NotaConDetallesDTO(
                            nota.getId(),
                            nota.getValor(),
                            nota.getPorcentaje(),
                            nota.getDescripcion(),
                            tuple.getT1().getNombre() + " " + tuple.getT1().getApellido(),
                            tuple.getT2().getNombre()
                        ))
                    )
                    .collectList()
                    .map(notas -> {
                        BigDecimal notaAcumulada = BigDecimal.ZERO;
                        int porcentajeAcumulado = 0;
                        
                        for (NotaConDetallesDTO nota : notas) {
                            BigDecimal valorPonderado = nota.getValor()
                                .multiply(new BigDecimal(nota.getPorcentaje()))
                                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            notaAcumulada = notaAcumulada.add(valorPonderado);
                            porcentajeAcumulado += nota.getPorcentaje();
                        }
                        
                        BigDecimal notaFinalProyectada = BigDecimal.ZERO;
                        if (porcentajeAcumulado > 0 && porcentajeAcumulado < 100) {
                            notaFinalProyectada = notaAcumulada
                                .multiply(new BigDecimal(100))
                                .divide(new BigDecimal(porcentajeAcumulado), 2, RoundingMode.HALF_UP);
                        } else if (porcentajeAcumulado == 100) {
                            notaFinalProyectada = notaAcumulada;
                        }
                        
                        return new NotaAcumuladaDTO(
                            notas,
                            notaAcumulada.setScale(2, RoundingMode.HALF_UP),
                            porcentajeAcumulado,
                            notaFinalProyectada.setScale(2, RoundingMode.HALF_UP)
                        );
                    })
            );
    }

    private Mono<Boolean> validarPorcentaje(Long estudianteMateriaId, Integer nuevoPorcentaje, Long notaIdExcluir) {
        Mono<Integer> sumaPorcentajes;
        
        if (notaIdExcluir != null) {
            sumaPorcentajes = notaRepository.sumPorcentajesExcludingNota(estudianteMateriaId, notaIdExcluir);
        } else {
            sumaPorcentajes = notaRepository.sumPorcentajesByEstudianteMateriaId(estudianteMateriaId);
        }
        
        return sumaPorcentajes
            .map(suma -> suma + nuevoPorcentaje)
            .flatMap(total -> {
                if (total > 100) {
                    return Mono.error(new IllegalArgumentException(
                        "La suma de los porcentajes no puede superar el 100%. " +
                        "Porcentaje actual: " + (total - nuevoPorcentaje) + "%, " +
                        "intentando agregar: " + nuevoPorcentaje + "%"));
                }
                return Mono.just(true);
            });
    }
}

