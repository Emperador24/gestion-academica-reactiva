package co.edu.javeriana.gestionacademica.controller;

import co.edu.javeriana.gestionacademica.dto.MateriaConEstudiantesDTO;
import co.edu.javeriana.gestionacademica.model.Materia;
import co.edu.javeriana.gestionacademica.service.MateriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MateriaController {
    private final MateriaService materiaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Materia> crear(@Valid @RequestBody Materia materia) {
        return materiaService.crear(materia);
    }

    @GetMapping
    public Flux<Materia> listarTodas() {
        return materiaService.listarTodas();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Materia> listarTodasStream() {
        return materiaService.listarTodas()
            .delayElements(Duration.ofMillis(500));
    }

    @GetMapping("/{id}")
    public Mono<Materia> obtenerPorId(@PathVariable Long id) {
        return materiaService.obtenerPorId(id);
    }

    @GetMapping("/{id}/estudiantes")
    public Mono<MateriaConEstudiantesDTO> obtenerConEstudiantes(@PathVariable Long id) {
        return materiaService.obtenerConEstudiantes(id);
    }

    @PutMapping("/{id}")
    public Mono<Materia> actualizar(@PathVariable Long id, @Valid @RequestBody Materia materia) {
        return materiaService.actualizar(id, materia);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return materiaService.eliminar(id);
    }
}