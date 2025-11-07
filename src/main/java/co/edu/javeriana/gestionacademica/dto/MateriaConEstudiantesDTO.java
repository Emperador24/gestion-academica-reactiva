package co.edu.javeriana.gestionacademica.dto;

import co.edu.javeriana.gestionacademica.model.Estudiante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MateriaConEstudiantesDTO {
    private Long id;
    private String nombre;
    private Integer creditos;
    private List<Estudiante> estudiantes;
}