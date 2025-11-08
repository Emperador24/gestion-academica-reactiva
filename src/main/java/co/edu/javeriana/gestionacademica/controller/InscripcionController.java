package co.edu.javeriana.gestionacademica.controller;

import co.edu.javeriana.gestionacademica.dto.InscripcionDTO;
import co.edu.javeriana.gestionacademica.model.EstudianteMateria;
import co.edu.javeriana.gestionacademica.service.InscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InscripcionController {
    private final InscripcionService inscripcionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EstudianteMateria> inscribir(@Valid @RequestBody InscripcionDTO inscripcion) {
        return inscripcionService.inscribir(
            inscripcion.getEstudianteId(), 
            inscripcion.getMateriaId()
        );
    }

    @GetMapping
    public Mono<EstudianteMateria> obtenerRelacion(
            @RequestParam Long estudianteId,
            @RequestParam Long materiaId) {
        return inscripcionService.obtenerRelacion(estudianteId, materiaId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> desinscribir(@Valid @RequestBody InscripcionDTO inscripcion) {
        return inscripcionService.desinscribir(
            inscripcion.getEstudianteId(), 
            inscripcion.getMateriaId()
        );
    }
}