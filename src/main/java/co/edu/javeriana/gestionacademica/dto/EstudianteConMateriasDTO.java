package co.edu.javeriana.gestionacademica.dto;

import co.edu.javeriana.gestionacademica.model.Materia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteConMateriasDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String codigo;
    private List<Materia> materias;
}