package com.web2.hotel.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SuppressWarnings("serial")
@Table(name="Tipohabitacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoHabitacion implements Serializable{
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	@Column
	private long id_tipo;
	
	@Column
	@NotBlank
	private String clase;
	
	@Column
	@NotBlank
	private String descripcion;

	@Column
	@NotBlank
	private String dimension;
	
	@Column
	@NotBlank
	private String camas;
	
	@Column
	@Enumerated(EnumType.STRING)
	private Estado estado;
	
	public enum Estado {
		VIGENTE,FUERA_DE_SERVICIO
	}

}
