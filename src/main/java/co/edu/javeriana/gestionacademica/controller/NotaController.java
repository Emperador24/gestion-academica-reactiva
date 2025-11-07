package co.edu.javeriana.gestionacademica.controller;

import co.edu.javeriana.gestionacademica.dto.NotaAcumuladaDTO;
import co.edu.javeriana.gestionacademica.dto.NotaConDetallesDTO;
import co.edu.javeriana.gestionacademica.model.Nota;
import co.edu.javeriana.gestionacademica.service.NotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotaController {
    private final NotaService notaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Nota> crear(@Valid @RequestBody Nota nota) {
        return notaService.crear(nota);
    }

    @PutMapping("/{id}")
    public Mono<Nota> actualizar(@PathVariable Long id, @Valid @RequestBody Nota nota) {
        return notaService.actualizar(id, nota);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminar(@PathVariable Long id) {
        return notaService.eliminar(id);
    }

    @GetMapping("/materia/{materiaId}")
    public Flux<NotaConDetallesDTO> listarPorMateria(@PathVariable Long materiaId) {
        return notaService.listarPorMateria(materiaId);
    }

    @GetMapping("/estudiante/{estudianteId}/materia/{materiaId}/acumulada")
    public Mono<NotaAcumuladaDTO> calcularNotaAcumulada(
            @PathVariable Long estudianteId,
            @PathVariable Long materiaId) {
        return notaService.calcularNotaAcumulada(estudianteId, materiaId);
    }
}