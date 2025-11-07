package co.edu.javeriana.gestionacademica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("materias")
public class Materia {
    @Id
    private Long id;
    
    @NotBlank(message = "El nombre de la materia es obligatorio")
    private String nombre;
    
    @NotNull(message = "Los créditos son obligatorios")
    @Positive(message = "Los créditos deben ser un número positivo")
    private Integer creditos;
    
    private LocalDateTime createdAt;
}