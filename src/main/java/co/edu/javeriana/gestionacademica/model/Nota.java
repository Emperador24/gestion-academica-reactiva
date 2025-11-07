package co.edu.javeriana.gestionacademica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("notas")
public class Nota {
    @Id
    private Long id;
    
    @NotNull(message = "El ID de estudiante-materia es obligatorio")
    private Long estudianteMateriaId;
    
    @NotNull(message = "El valor de la nota es obligatorio")
    @DecimalMin(value = "0.0", message = "La nota debe ser mayor o igual a 0.0")
    @DecimalMax(value = "5.0", message = "La nota debe ser menor o igual a 5.0")
    private BigDecimal valor;
    
    @NotNull(message = "El porcentaje es obligatorio")
    @Min(value = 1, message = "El porcentaje debe ser mayor a 0")
    @Max(value = 100, message = "El porcentaje debe ser menor o igual a 100")
    private Integer porcentaje;
    
    private String descripcion;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

