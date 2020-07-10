package com.web2.hotel.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@SuppressWarnings("serial")
@Table(name="habitacion")
@Data @AllArgsConstructor @NoArgsConstructor

public class Habitacion implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	@Column
	private long id_habitacion;
	
	@Column
	@NotBlank
	private String numerohabitacion;
	
	@Column
	@NotNull
	private int tarifa;
	
	@Column
	@NotBlank
	private String descripcion;
	
	@Column
	@NotNull
	private int cantidadhuesped;
	
	@Column
	@Enumerated(EnumType.STRING)
	private EstadoHabitacion estado;
	
	public enum EstadoHabitacion {
		VIGENTE,FUERA_DE_SERVICIO
	}
	
	/*union con Tipos de habitacion*/
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JoinColumn(name="id_tipohabitacion")
	private TipoHabitacion tipoHabitacion;
    
    /*union con CaracteristicasHabitacion*/
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "habitacion_caracthabitacion", 
        joinColumns = { @JoinColumn(name = "habitacion_id")}, 
        inverseJoinColumns = {@JoinColumn(name = "caracthabitacion_id")})
    private Set<CaracteristicasHabitacion>caractehabitacion;


    /*union con Huesped*/
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
        name = "habitacion_huesped", 
        joinColumns = { @JoinColumn(name = "habitacion_id")}, 
        inverseJoinColumns = {@JoinColumn(name = "huesped_id")})
    private Set<Huesped>huespedHabitacion;
	
    /*union con Reservas*/
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@ManyToMany(mappedBy="habitacion")
    private Set<Reservas> reservas;
	
    
}
