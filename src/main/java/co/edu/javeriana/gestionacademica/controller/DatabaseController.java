package co.edu.javeriana.gestionacademica.controller;

import co.edu.javeriana.gestionacademica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/database-viewer")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DatabaseViewController {
    
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;
    private final EstudianteMateriaRepository estudianteMateriaRepository;
    private final NotaRepository notaRepository;

    @GetMapping
    @ResponseBody
    public Mono<String> viewDatabase() {
        return Mono.zip(
            estudianteRepository.findAll().collectList(),
            materiaRepository.findAll().collectList(),
            estudianteMateriaRepository.findAll().collectList(),
            notaRepository.findAll().collectList()
        ).map(tuple -> {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>");
            html.append("<html><head>");
            html.append("<title>Visor de Base de Datos</title>");
            html.append("<style>");
            html.append("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
            html.append("h1 { color: #2563eb; }");
            html.append("h2 { color: #1e40af; margin-top: 30px; }");
            html.append("table { width: 100%; border-collapse: collapse; background: white; margin: 10px 0; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }");
            html.append("th { background: #2563eb; color: white; padding: 12px; text-align: left; }");
            html.append("td { padding: 10px; border-bottom: 1px solid #e5e7eb; }");
            html.append("tr:hover { background: #f9fafb; }");
            html.append(".container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            html.append(".count { color: #059669; font-weight: bold; }");
            html.append(".refresh { background: #2563eb; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; margin-bottom: 20px; }");
            html.append(".refresh:hover { background: #1e40af; }");
            html.append("</style>");
            html.append("</head><body>");
            html.append("<div class='container'>");
            html.append("<h1>🗄️ Visor de Base de Datos - Gestión Académica</h1>");
            html.append("<button class='refresh' onclick='location.reload()'>🔄 Actualizar</button>");
            
            // Estudiantes
            html.append("<h2>👨‍🎓 Estudiantes <span class='count'>(" + tuple.getT1().size() + ")</span></h2>");
            html.append("<table><tr><th>ID</th><th>Nombre</th><th>Apellido</th><th>Email</th><th>Código</th><th>Fecha Creación</th></tr>");
            tuple.getT1().forEach(e -> {
                html.append("<tr>");
                html.append("<td>").append(e.getId()).append("</td>");
                html.append("<td>").append(e.getNombre()).append("</td>");
                html.append("<td>").append(e.getApellido()).append("</td>");
                html.append("<td>").append(e.getEmail()).append("</td>");
                html.append("<td>").append(e.getCodigo()).append("</td>");
                html.append("<td>").append(e.getCreatedAt()).append("</td>");
                html.append("</tr>");
            });
            html.append("</table>");
            
            // Materias
            html.append("<h2>📚 Materias <span class='count'>(" + tuple.getT2().size() + ")</span></h2>");
            html.append("<table><tr><th>ID</th><th>Nombre</th><th>Créditos</th><th>Fecha Creación</th></tr>");
            tuple.getT2().forEach(m -> {
                html.append("<tr>");
                html.append("<td>").append(m.getId()).append("</td>");
                html.append("<td>").append(m.getNombre()).append("</td>");
                html.append("<td>").append(m.getCreditos()).append("</td>");
                html.append("<td>").append(m.getCreatedAt()).append("</td>");
                html.append("</tr>");
            });
            html.append("</table>");
            
            // Inscripciones
            html.append("<h2>📝 Inscripciones (Estudiante-Materia) <span class='count'>(" + tuple.getT3().size() + ")</span></h2>");
            html.append("<table><tr><th>ID</th><th>Estudiante ID</th><th>Materia ID</th><th>Fecha Creación</th></tr>");
            tuple.getT3().forEach(em -> {
                html.append("<tr>");
                html.append("<td>").append(em.getId()).append("</td>");
                html.append("<td>").append(em.getEstudianteId()).append("</td>");
                html.append("<td>").append(em.getMateriaId()).append("</td>");
                html.append("<td>").append(em.getCreatedAt()).append("</td>");
                html.append("</tr>");
            });
            html.append("</table>");
            
            // Notas
            html.append("<h2>📊 Notas <span class='count'>(" + tuple.getT4().size() + ")</span></h2>");
            html.append("<table><tr><th>ID</th><th>Estudiante-Materia ID</th><th>Valor</th><th>Porcentaje</th><th>Descripción</th><th>Fecha Creación</th></tr>");
            tuple.getT4().forEach(n -> {
                html.append("<tr>");
                html.append("<td>").append(n.getId()).append("</td>");
                html.append("<td>").append(n.getEstudianteMateriaId()).append("</td>");
                html.append("<td>").append(n.getValor()).append("</td>");
                html.append("<td>").append(n.getPorcentaje()).append("%</td>");
                html.append("<td>").append(n.getDescripcion() != null ? n.getDescripcion() : "").append("</td>");
                html.append("<td>").append(n.getCreatedAt()).append("</td>");
                html.append("</tr>");
            });
            html.append("</table>");
            
            html.append("</div></body></html>");
            return html.toString();
        });
    }
}