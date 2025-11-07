package co.edu.javeriana.gestionacademica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaAcumuladaDTO {
    private List<NotaConDetallesDTO> notas;
    private BigDecimal notaAcumulada;
    private Integer porcentajeAcumulado;
    private BigDecimal notaFinalProyectada;
}