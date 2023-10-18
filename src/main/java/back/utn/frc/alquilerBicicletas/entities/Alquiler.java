package back.utn.frc.alquilerBicicletas.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ALQUILERES")
public class Alquiler {
    @Id
    @GeneratedValue(generator = "ALQUILERES")
    @TableGenerator(name = "ALQUILERES", table = "sqlite_sequence",
            pkColumnName = "name", valueColumnName = "seq",
            pkColumnValue="id",
            initialValue=1, allocationSize=1)
    private long id;

    @Column(name = "ID_CLIENTE")
    private String idCliente;

    @Column(name = "ESTADO")
    private boolean estado;

    @Column(name = "ESTACION_RETIRO")
    private int estacionRetiro;

    @Column(name = "ESTACION_DEVOLUCION")
    private int estacionDevolucion;

    @Column(name = "ESTACION_DEVOLUCION")
    private LocalDateTime fechaHoraRetiro;

    @Column(name = "FECHA_HORA_DEVOLUCION")
    private LocalDateTime fechaHoraDevolucion;

    @Column(name = "MONTO")
    private double monto;

    @Column(name = "ID_TARIFA")
    private long idTarifa;
}
