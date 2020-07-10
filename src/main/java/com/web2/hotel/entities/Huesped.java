package com.web2.hotel.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SuppressWarnings("serial")
@Table(name="huesped")
@AllArgsConstructor @NoArgsConstructor
@Data
public class Huesped implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	protected long id;
	
	@Column(length=50)
	@NotBlank
	protected String nombre;
	
	@Column(length=50)
	@NotBlank
	protected String apellido;
	
	@Column
	@NotBlank
	private String dni;
	
	@Transient
	private long resAux;
	
    
    /*union con reserva para el grupo del huesped que hace la reserva*/
    @ManyToMany(mappedBy="huespedGrupo",cascade = {CascadeType.PERSIST})
    private Set<Reservas> reserva;
    
}
