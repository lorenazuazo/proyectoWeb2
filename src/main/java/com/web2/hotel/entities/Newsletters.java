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
@Table(name="newsletters")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Newsletters implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	protected long id;
	
	@Column
	@NotBlank
	@Email
	protected String correo;
	
	@PrePersist
	@PreUpdate
	protected void prepareData(){this.correo = correo == null ? null : correo.toLowerCase();}

}
