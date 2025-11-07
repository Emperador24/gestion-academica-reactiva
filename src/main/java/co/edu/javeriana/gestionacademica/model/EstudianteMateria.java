package co.edu.javeriana.gestionacademica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("estudiante_materia")
public class EstudianteMateria {
    @Id
    private Long id;
    
    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long estudianteId;
    
    @NotNull(message = "El ID de la materia es obligatorio")
    private Long materiaId;
    
    private LocalDateTime createdAt;
}
