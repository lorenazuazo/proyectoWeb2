package com.web2.hotel.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SuppressWarnings("serial")
@Table(name="MesajesUsuarioAnonimo")
@Data
@NoArgsConstructor @AllArgsConstructor
public class MesajesUsuarioAnonimo implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	protected long id;
	
	@Column
	@NotBlank
	protected String nombre;
	
	@Column
	@NotBlank
	protected String apellido;
	
	@Column
	@NotBlank
	@Email
	protected String correo;
	
	@Column
	@NotBlank
	protected String mensaje;
	
	@PrePersist
	@PreUpdate
	protected void prepareData(){
		this.correo = correo == null ? null : correo.toLowerCase();
		this.mensaje= mensaje== null ? null : mensaje.toLowerCase();
		}
}
