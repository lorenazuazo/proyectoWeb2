package com.web2.hotel.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name="caracteristicashabitacion")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CaracteristicasHabitacion implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	@Column
	private long id;
	
	@Column
	@NotBlank
	private String detalle;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	public enum Estado {
		VIGENTE,FUERA_DE_SERVICIO
	}
	
}
