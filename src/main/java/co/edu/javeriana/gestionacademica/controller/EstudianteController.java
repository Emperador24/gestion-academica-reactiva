package co.edu.javeriana.gestionacademica.controller;

import co.edu.javeriana.gestionacademica.dto.EstudianteConMateriasDTO;
import co.edu.javeriana.gestionacademica.model.Estudiante;
import co.edu.javeriana.gestionacademica.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstudianteController {
    private final EstudianteService estudianteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Estudiante> crear(@Valid @RequestBody Estudiante estudiante) {
        return estudianteService.crear(estudiante);
    }

    @GetMapping
    public Flux<Estudiante> listarTodos() {
        return estudianteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Mono<Estudiante> obtenerPorId(@PathVariable Long id) {
        return estudianteService.obtenerPorId(id);
    }

    @GetMapping("/{id}/materias")
    public Mono<EstudianteConMateriasDTO> obtenerConMaterias(@PathVariable Long id) {
        return estudianteService.obtenerConMaterias(id);
    }

    @PutMapping("/{id}")
    public Mono<Estudiante> actualizar(@PathVariable Long id, @Valid @RequestBody Estudiante estudiante) {
        return estudianteService.actualizar(id, estudiante);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return estudianteService.eliminar(id);
    }
}