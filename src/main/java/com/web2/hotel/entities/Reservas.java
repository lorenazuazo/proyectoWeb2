package com.web2.hotel.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@SuppressWarnings("serial")
@Table(name="reservas")
@Data @AllArgsConstructor @NoArgsConstructor

public class Reservas  implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	@Column
	private long idReserva;
	
	@Column
	@NotNull
	private String nombre;
	
	@Column
	@NotNull
	private String apellido;
	
	@Column
	@NotNull
	private String dni;
	
	@Column
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaEntrada;
	
	@Column
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaSalida;
	
	@Column
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaReserva;
	
	@Column
	private int cantDias;
	
	@Column
	@NotNull
	private int cantHabitaciones;
	
	@Column
	@NotNull
	private int cantAdultos;
	
	@Column
	private int cantNinios;
	
	@Column
	private int tarifaReserva;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	@Transient
	private String username;
	
	
	/*union con usuario para saber quien hace la reserva*/
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name="idUsuario")
	 private Usuario usuario;
    
    /*union con huesped para el grupo del huesped que hace la reserva*/
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "reserva_grupohuesped", 
        joinColumns = { @JoinColumn(name = "reserva_id")}, 
        inverseJoinColumns = {@JoinColumn(name = "huesped_id")})
    private Set<Huesped>huespedGrupo;
    
    /*union con habitaciones*/
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToMany
    @JoinTable(
	        name = "reserva_habitacion", 
	        joinColumns = { @JoinColumn(name = "reserva_id")}, 
	        inverseJoinColumns = {@JoinColumn(name = "habitacion_id")})
    private Set<Habitacion>habitacion;
	
      
	public enum Estado {
		CONFIRMADA,CONCLUIDA,EXPIRADA,CANCELADA
	}

}
