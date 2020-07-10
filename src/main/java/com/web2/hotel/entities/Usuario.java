package com.web2.hotel.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="usuario")
@Data
@NoArgsConstructor @AllArgsConstructor

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	protected long id;
	
	@Column(length=50)
	@NotBlank(message="Nombre no puede estar vacio")
	protected String nombre;
	
	@Column(length=50)
	@NotBlank(message="Apellido no puede estar vacio")
	protected String apellido;		

	@Column
	@NotBlank(message="Correo no puede estar vacio")
	@Email
	protected String correo;
	
	@PrePersist
	@PreUpdate
	protected void prepareData(){this.correo = correo == null ? null : correo.toLowerCase();}

	@Column
	@NotBlank(message="Dni no puede estar vacio")
	private String dni;

	@Column
	@NotBlank(message="Telefono no puede estar vacio")
	private String telefono;
	
	@Column
	@NotBlank(message="Nombre de usuario no puede estar vacio")
	private String username;
	
	@Column
	@NotBlank(message="Clave no puede estar vacio")
	private String password;
	
	@Transient 
	private String confirmPassword;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="users_authorities",
	joinColumns=@JoinColumn(name="usuario_id"),
	inverseJoinColumns=@JoinColumn(name="authority_id"))
	private Set<Authority> authority;
	 
	
    @OneToMany(fetch=FetchType.LAZY,mappedBy="usuario")
    private Set<Reservas> reservas;
	
    @ToString.Exclude
	@EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL,mappedBy="usuario")
    private Set<Mensajes> mensaje=new HashSet<>();
}
