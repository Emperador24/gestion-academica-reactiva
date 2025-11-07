package co.edu.javeriana.gestionacademica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaConDetallesDTO {
    private Long id;
    private BigDecimal valor;
    private Integer porcentaje;
    private String descripcion;
    private String nombreEstudiante;
    private String nombreMateria;
}